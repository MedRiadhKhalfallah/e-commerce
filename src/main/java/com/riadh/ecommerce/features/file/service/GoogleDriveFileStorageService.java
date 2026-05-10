package com.riadh.ecommerce.features.file.service;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.riadh.ecommerce.features.file.dto.FileRequest;
import com.riadh.ecommerce.features.file.dto.FileResponse;
import com.riadh.ecommerce.features.file.exception.FileStorageException;
import com.riadh.ecommerce.features.file.mapper.FileMapper;
import com.riadh.ecommerce.features.file.repository.FileResponseRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
/**
 * Implementacion de IFileStorageService utilisant Google Drive via OAuth2.
 */
@Service("googleDriveStorage")
public class GoogleDriveFileStorageService implements IFileStorageService {
    private final GoogleDriveCredentialManager credentialManager;
    @Value("${google.drive.root-folder-id:}")
    private String rootFolderId;
    private final FileResponseRepository fileResponseRepository;
    private final FileMapper fileMapper;

    public GoogleDriveFileStorageService(GoogleDriveCredentialManager credentialManager,
                                         FileResponseRepository fileResponseRepository,
                                         FileMapper fileMapper) {
        this.credentialManager = credentialManager;
        this.fileResponseRepository = fileResponseRepository;
        this.fileMapper = fileMapper;
    }

    @Override
    public String uploadFile(MultipartFile file, Long id, String pathType) {
        try {
            Drive drive = credentialManager.getDriveService();
            String folderId = getOrCreateFolder(drive, pathType + "/" + id, rootFolderId);
            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
            int dotIndex = originalFilename.lastIndexOf('.');
            String extension = dotIndex >= 0 ? originalFilename.substring(dotIndex) : "";
            String generatedName = UUID.randomUUID() + extension;
            File fileMetadata = new File();
            fileMetadata.setName(generatedName);
            fileMetadata.setParents(List.of(folderId));
            InputStreamContent mediaContent = new InputStreamContent(
                    file.getContentType(),
                    file.getInputStream()
            );
            File uploadedFile = drive.files()
                    .create(fileMetadata, mediaContent)
                    .setSupportsAllDrives(true)
                    .setFields("id, name")
                    .execute();
            return uploadedFile.getId();
        } catch (IllegalStateException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FileStorageException("uploadFile (Google Drive)", ex);
        }
    }

    @Override
    public byte[] downloadFile(Long fileId) {
        try {
            com.riadh.ecommerce.features.file.entity.File fileEntity= fileResponseRepository.getReferenceById(fileId);

            Drive drive = credentialManager.getDriveService();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            drive.files().get(fileEntity.getFileName()).executeMediaAndDownloadTo(outputStream);
            return outputStream.toByteArray();
        } catch (IllegalStateException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FileStorageException("downloadFile (Google Drive)", ex);
        }
    }

    @Override
    public void deleteFile(String fileId, Long id, String pathType) {
        try {
            Drive drive = credentialManager.getDriveService();
            drive.files().delete(fileId).execute();
        } catch (IllegalStateException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FileStorageException("deleteFile (Google Drive)", ex);
        }
    }

    @Override
    public FileResponse processUpload(FileRequest request, String baseUrl) {
        String fileId = uploadFile(
                request.file(),
                request.entityId(),
                request.pathType()
        );

        String fileDownloadUri = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/api/files/download")
                .queryParam("fileName", fileId)
                .queryParam("id", request.entityId())
                .queryParam("pathType", request.pathType())
                .toUriString();

        com.riadh.ecommerce.features.file.entity.File fileEntity = new com.riadh.ecommerce.features.file.entity.File();
        fileEntity.setFileName(fileId);
        fileEntity.setFileDownloadUri(fileDownloadUri);
        fileEntity.setFileType(request.file().getContentType());
        fileEntity.setSize(request.file().getSize());
        fileEntity.setPathType(request.pathType());
        fileEntity.setEntityId(request.entityId());

        com.riadh.ecommerce.features.file.entity.File saved = fileResponseRepository.save(fileEntity);

        return fileMapper.toResponse(saved);
    }

    @Override
    public void processDelete(Long id) {
        com.riadh.ecommerce.features.file.entity.File fileEntity = fileResponseRepository.getReferenceById(id);
        // fileName contient l'ID Google Drive pour ce service
        deleteFile(fileEntity.getFileName(), id, fileEntity.getPathType());
        fileResponseRepository.deleteById(id);
    }

    private String getOrCreateFolder(Drive drive, String folderPath, String parentId) throws Exception {
        String currentParentId = parentId;
        for (String part : folderPath.split("/")) {
            currentParentId = getOrCreateSingleFolder(drive, part, currentParentId);
        }
        return currentParentId;
    }

    private String getOrCreateSingleFolder(Drive drive, String folderName, String parentId) throws Exception {
        String query = String.format(
                "mimeType='application/vnd.google-apps.folder' and name='%s' and '%s' in parents and trashed=false",
                folderName.replace("'", "\\'"), parentId
        );
        FileList result = drive.files().list()
                .setQ(query)
                .setFields("files(id, name)")
                .execute();
        if (result.getFiles() != null && !result.getFiles().isEmpty()) {
            return result.getFiles().get(0).getId();
        }
        File folderMetadata = new File();
        folderMetadata.setName(folderName);
        folderMetadata.setMimeType("application/vnd.google-apps.folder");
        folderMetadata.setParents(List.of(parentId));
        File folder = drive.files().create(folderMetadata)
                .setSupportsAllDrives(true)
                .setFields("id")
                .execute();
        return folder.getId();
    }
}

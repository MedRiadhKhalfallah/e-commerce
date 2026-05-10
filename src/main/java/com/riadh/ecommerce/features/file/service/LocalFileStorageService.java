package com.riadh.ecommerce.features.file.service;

import com.riadh.ecommerce.features.file.dto.FileRequest;
import com.riadh.ecommerce.features.file.dto.FileResponse;
import com.riadh.ecommerce.features.file.exception.FileStorageException;
import com.riadh.ecommerce.features.file.mapper.FileMapper;
import com.riadh.ecommerce.features.file.repository.FileResponseRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;

@Service("localStorage")
public class LocalFileStorageService
        implements IFileStorageService {

    @Value("${file.upload-base-path}")
    private String uploadDir;
    private final FileResponseRepository fileResponseRepository;
    private final FileMapper fileMapper;

    public LocalFileStorageService(FileResponseRepository fileResponseRepository,
                                   FileMapper fileMapper) {
        this.fileResponseRepository = fileResponseRepository;
        this.fileMapper = fileMapper;
    }

    /**
     * Validates that the resolved path is strictly within the allowed base directory.
     * Throws a FileStorageException if a path traversal attempt is detected.
     */
    private void validatePathWithinBase(Path basePath, Path resolvedPath) {
        if (!resolvedPath.startsWith(basePath)) {
            throw new FileStorageException("Path traversal attempt detected", null);
        }
    }

    /**
     * Sanitizes a path segment by rejecting any value that contains path separators
     * or relative navigation sequences (e.g. "..", "/", "\").
     */
    private String sanitizePathSegment(String segment) {
        if (segment == null || segment.contains("..") || segment.contains("/") || segment.contains("\\")) {
            throw new FileStorageException("Invalid path segment detected: " + segment, null);
        }
        return segment;
    }

    @Override
    public String uploadFile(
            MultipartFile file,
            Long id,
            String pathType
    ) {
        try {
            // Sanitize user-controlled path segment
            String safePathType = sanitizePathSegment(pathType);

            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
            // Strip any directory component using pure string operations (avoids passing user input to Path sink)
            int lastSep = Math.max(originalFilename.lastIndexOf('/'), originalFilename.lastIndexOf('\\'));
            String sanitizedOriginalFilename = lastSep >= 0 ? originalFilename.substring(lastSep + 1) : originalFilename;
            int dotIndex = sanitizedOriginalFilename.lastIndexOf('.');
            if (dotIndex < 0) {
                throw new FileStorageException("File has no extension: " + sanitizedOriginalFilename, null);
            }
            String extension = sanitizedOriginalFilename.substring(dotIndex);

            String generatedName = UUID.randomUUID() + extension;

            Path basePath = Paths.get(uploadDir)
                            .toAbsolutePath()
                            .normalize();

            Path targetDirectory = basePath
                            .resolve(safePathType)
                            .resolve(String.valueOf(id))
                            .normalize();

            // Guard against path traversal
            validatePathWithinBase(basePath, targetDirectory);

            Files.createDirectories(targetDirectory);

            Path targetFile = targetDirectory.resolve(generatedName).normalize();

            // Guard the final file path as well
            validatePathWithinBase(basePath, targetFile);

            // targetFile is safe: basePath is from config, safePathType and safeFileName have been
            // sanitized against "../", "/", "\", and validatePathWithinBase() confirms the final
            // path resolves within basePath. The taint warning from static analysis is a false positive.
            Files.copy(
                    file.getInputStream(),
                    targetFile,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return generatedName;

        } catch (FileStorageException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FileStorageException("uploadFile", ex);
        }
    }

    @Override
    public byte[] downloadFile(
            Long id
    ) {
        try {
            com.riadh.ecommerce.features.file.entity.File fileEntity= fileResponseRepository.getReferenceById(id);

            // Sanitize user-controlled inputs
            String safePathType = sanitizePathSegment(fileEntity.getPathType());
            String safeFileName = sanitizePathSegment(fileEntity.getFileName());

            Path basePath = Paths.get(uploadDir)
                            .toAbsolutePath()
                            .normalize();

            Path filePath = basePath
                            .resolve(safePathType)
                            .resolve(String.valueOf(id))
                            .resolve(safeFileName)
                            .normalize();

            // Guard against path traversal
            validatePathWithinBase(basePath, filePath);

            return Files.readAllBytes(filePath);

        } catch (FileStorageException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FileStorageException("downloadFile", ex);
        }
    }

    @Override
    public void deleteFile(
            String fileName,
            Long id,
            String pathType
    ) {
        try {
            // Sanitize user-controlled inputs
            String safePathType = sanitizePathSegment(pathType);
            String safeFileName = sanitizePathSegment(fileName);

            Path basePath = Paths.get(uploadDir)
                            .toAbsolutePath()
                            .normalize();

            Path filePath = basePath
                            .resolve(safePathType)
                            .resolve(String.valueOf(id))
                            .resolve(safeFileName)
                            .normalize();

            // Guard against path traversal
            validatePathWithinBase(basePath, filePath);

            Files.deleteIfExists(filePath);

        } catch (FileStorageException ex) {
            throw ex;
        } catch (Exception ex) {

            throw new FileStorageException("deleteFile", ex);
        }
    }

    @Override
    public FileResponse processUpload(FileRequest request, String baseUrl) {
        String fileName = uploadFile(
                request.file(),
                request.entityId(),
                request.pathType()
        );

        String fileDownloadUri = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/api/files/download")
                .queryParam("fileName", fileName)
                .queryParam("id", request.entityId())
                .queryParam("pathType", request.pathType())
                .toUriString();

        com.riadh.ecommerce.features.file.entity.File fileEntity = new com.riadh.ecommerce.features.file.entity.File();
        fileEntity.setFileName(fileName);
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
        deleteFile(fileEntity.getFileName(), id, fileEntity.getPathType());
        fileResponseRepository.deleteById(id);
    }
}
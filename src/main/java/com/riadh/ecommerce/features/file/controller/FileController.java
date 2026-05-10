package com.riadh.ecommerce.features.file.controller;

import com.riadh.ecommerce.features.file.dto.FileRequest;
import com.riadh.ecommerce.features.file.dto.FileResponse;
import com.riadh.ecommerce.features.file.service.IFileStorageService;
import com.riadh.ecommerce.features.file.service.StorageFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final IFileStorageService fileStorageService;

    public FileController(StorageFactory storageFactory) {
        this.fileStorageService = storageFactory.getStorage();
    }

    @PostMapping("/upload")
    public FileResponse uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("id") Long id,
            @RequestParam String pathType
    ) {
        FileRequest request = new FileRequest(file, id, pathType);
        return processUpload(request);
    }

    @PostMapping("/uploadMultiple")
    public List<FileResponse> uploadMultipleFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("id") Long id,
            @RequestParam String pathType
    ) {
        return Arrays.stream(files)
                .map(file -> uploadFile(file, id, pathType))
                .collect(Collectors.toList());
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam Long id
    ) {
        byte[] fileData = fileStorageService.downloadFile(id);
        Resource resource = new ByteArrayResource(fileData);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\""
                )
                .contentLength(fileData.length)
                .body(resource);
    }

    @PostMapping("/cloud/file")
    public ResponseEntity<FileResponse> cloudUploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("id") Long id,
            @RequestParam String pathType
    ) {
        return ResponseEntity.ok(uploadFile(file, id, pathType));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileStorageService.processDelete(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private FileResponse processUpload(FileRequest request) {
        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .toUriString();

        return fileStorageService.processUpload(request, baseUrl);
    }
}
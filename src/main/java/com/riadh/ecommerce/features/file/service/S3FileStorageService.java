package com.riadh.ecommerce.features.file.service;

import com.riadh.ecommerce.features.file.dto.FileRequest;
import com.riadh.ecommerce.features.file.dto.FileResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("s3Storage")
public class S3FileStorageService
        implements IFileStorageService {

    @Override
    public String uploadFile(
            MultipartFile file,
            Long id,
            String pathType
    ) {

        /**
         * Upload AWS S3
         */

        return "s3-file-url";
    }

    @Override
    public byte[] downloadFile(
            Long id
    ) {

        return new byte[0];
    }

    @Override
    public void deleteFile(
            String fileName,
            Long id,
            String pathType
    ) {

    }

    @Override
    public FileResponse processUpload(FileRequest request, String baseUrl) {
        return null;
    }

    @Override
    public void processDelete(Long id) {

    }
}

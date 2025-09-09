package com.example.tnovel_backend.service.domain.post;

import com.example.tnovel_backend.exception.domain.PostException;
import com.example.tnovel_backend.exception.error.PostErrorCode;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageUtil {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String saveImageToLocal(MultipartFile file) {
        if (file.isEmpty()) {
            throw new PostException(PostErrorCode.EMPTY_IMAGE_FILE);
        }

        String extension = getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID().toString() + "." + extension;
        String fullPath = uploadDir + File.separator + filename;

        try {
            file.transferTo(new File(fullPath));
        } catch (IOException e) {
            throw new PostException(PostErrorCode.FILE_UPLOAD_FAILED);
        }

        return "/uploads/posts/" + filename;
    }


    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}

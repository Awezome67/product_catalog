package com.catalogia.productcatalog.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

	private final Path uploadDir;

	public FileStorageService(@Value("${app.upload.dir:uploads}") String uploadDir) {
		this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
		try {
			Files.createDirectories(this.uploadDir);
		} catch (IOException ex) {
			throw new IllegalStateException("Could not create upload directory", ex);
		}
	}

	public String store(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return null;
		}

		String originalName = StringUtils.cleanPath(file.getOriginalFilename());
		String extension = "";
		int dotIndex = originalName.lastIndexOf('.');
		if (dotIndex >= 0) {
			extension = originalName.substring(dotIndex).toLowerCase();
		}

		// Validate file type (extension and content type)
		String contentType = file.getContentType();
		boolean validExt = ".jpg".equals(extension) || ".jpeg".equals(extension) || ".png".equals(extension);
		boolean validType = "image/jpeg".equals(contentType) || "image/png".equals(contentType);
		if (!validExt || !validType) {
			throw new IllegalArgumentException("Only .jpg, .jpeg, and .png files are allowed.");
		}

		String filename = UUID.randomUUID() + extension;
		Path target = uploadDir.resolve(filename);

		try {
			Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			throw new IllegalArgumentException("Failed to store file", ex);
		}

		return "/uploads/" + filename;
	}

	public void deleteIfPresent(String imageUrl) {
		if (imageUrl == null || !imageUrl.startsWith("/uploads/")) {
			return;
		}

		Path filePath = uploadDir.resolve(imageUrl.substring("/uploads/".length()));
		try {
			Files.deleteIfExists(filePath);
		} catch (IOException ignored) {
			// Best-effort cleanup only.
		}
	}

}

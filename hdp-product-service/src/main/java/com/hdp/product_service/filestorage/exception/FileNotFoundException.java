package com.hdp.product_service.filestorage.exception;

public class FileNotFoundException extends FileStorageException {
    public FileNotFoundException(String fileKey) {
        super("File not found: " + fileKey);
    }
}
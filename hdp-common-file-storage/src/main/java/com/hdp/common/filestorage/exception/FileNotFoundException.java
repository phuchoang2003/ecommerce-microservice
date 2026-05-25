package com.hdp.common.filestorage.exception;

public class FileNotFoundException extends FileStorageException {
    public FileNotFoundException(String fileKey) {
        super("File not found: " + fileKey);
    }
}
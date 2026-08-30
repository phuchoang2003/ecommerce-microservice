package com.hdp.common.filestorage.exception;

public class FileTooLargeException extends FileStorageException {
    private final long actualSize;
    private final long maxSize;

    public FileTooLargeException(long actualSize, long maxSize) {
        super("File size " + actualSize + " exceeds maximum allowed size " + maxSize);
        this.actualSize = actualSize;
        this.maxSize = maxSize;
    }

    public long actualSize() {
        return actualSize;
    }

    public long maxSize() {
        return maxSize;
    }
}
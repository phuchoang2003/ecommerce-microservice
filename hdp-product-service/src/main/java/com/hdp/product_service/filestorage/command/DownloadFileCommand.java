package com.hdp.product_service.filestorage.command;

public record DownloadFileCommand(
        String fileKey
) {}
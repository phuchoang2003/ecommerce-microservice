package com.hdp.product_service.filestorage.result;

import java.io.InputStream;

public record DownloadFileResult(
        String fileKey,
        String contentType,
        long contentLength,
        InputStream data
) {}
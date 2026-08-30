package com.hdp.common.filestorage.result;

import java.io.InputStream;

public record DownloadFileResult(
        String fileKey,
        String contentType,
        long contentLength,
        InputStream data
) {}
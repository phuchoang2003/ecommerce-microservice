package com.hdp.product_service.filestorage.result;

import java.util.List;

public record DeleteFilesResult(
        List<String> deletedKeys,
        int successCount,
        int failureCount
) {}
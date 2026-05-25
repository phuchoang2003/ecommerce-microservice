package com.hdp.common.filestorage.result;

import java.util.List;

public record DeleteFilesResult(
        List<String> deletedKeys,
        int successCount,
        int failureCount
) {}
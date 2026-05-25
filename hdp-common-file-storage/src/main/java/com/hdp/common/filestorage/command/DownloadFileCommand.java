package com.hdp.common.filestorage.command;

import com.hdp.core.validation.Rule;
import com.hdp.core.validation.SelfValidator;
import com.hdp.core.validation.ValidationResult;

public record DownloadFileCommand(
        String fileKey
) implements SelfValidator<DownloadFileCommand> {
    @Override
    public ValidationResult validate(DownloadFileCommand input) {
        ValidationResult result = new ValidationResult();
        Rule.of(fileKey, "fileKey", result)
                .notBlank("fileKey is required")
                .must(this::isNotPathTraversal, "fileKey must not contain path traversal sequences")
                .must(this::isNotMaliciousPattern, "fileKey contains potentially malicious pattern");
        return result;
    }

    private boolean isNotPathTraversal(String fileKey) {
        if (fileKey == null) return true;
        return !fileKey.contains("..") && !fileKey.startsWith("/") && !fileKey.contains(":");
    }

    private boolean isNotMaliciousPattern(String fileKey) {
        if (fileKey == null) return true;
        String lower = fileKey.toLowerCase();
        return !lower.contains(".ssh") &&
               !lower.contains(".aws") &&
               !lower.contains("credentials") &&
               !lower.contains("config") &&
               !lower.contains(".git");
    }
}
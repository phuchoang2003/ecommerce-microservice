package com.hdp.common.filestorage.command;

import com.hdp.core.validation.Rule;
import com.hdp.core.validation.SelfValidator;
import com.hdp.core.validation.ValidationResult;

import java.util.Set;

public record GeneratePresignedUrlCommand(
        String fileName,
        String contentType,
        String category,
        Long maxSizeBytes
) implements SelfValidator<GeneratePresignedUrlCommand> {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp",
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "gif", "webp",
            "pdf", "doc", "docx"
    );

    private static final long DEFAULT_MAX_SIZE_BYTES = 10 * 1024 * 1024; // 10MB

    @Override
    public ValidationResult validate(GeneratePresignedUrlCommand input) {
        ValidationResult result = new ValidationResult();

        Rule.of(fileName, "fileName", result)
                .notBlank("fileName is required")
                .matches("^[a-zA-Z0-9._-]+$", "fileName must be alphanumeric with dots, underscores, or hyphens")
                .must(this::isNotPathTraversal, "fileName must not contain path traversal sequences")
                .must(this::hasAllowedExtension, "fileName extension not allowed");

        Rule.of(contentType, "contentType", result)
                .notBlank("contentType is required")
                .must(ALLOWED_CONTENT_TYPES::contains, "contentType not allowed");

        Rule.of(category, "category", result)
                .notBlank("category is required")
                .matches("^[a-zA-Z0-9_-]+$", "category must be alphanumeric with underscores or hyphens");

        return result;
    }

    private boolean isNotPathTraversal(String fileName) {
        if (fileName == null) return true;
        return !fileName.contains("..") && !fileName.startsWith("/") && !fileName.contains(":");
    }

    private boolean hasAllowedExtension(String fileName) {
        if (fileName == null) return true;
        String lower = fileName.toLowerCase();
        return ALLOWED_EXTENSIONS.stream().anyMatch(lower::endsWith);
    }
}
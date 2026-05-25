package com.hdp.product_service.application.port.in;

import com.hdp.core.usecase.Usecase;
import com.hdp.core.validation.Rule;
import com.hdp.core.validation.SelfValidator;
import com.hdp.core.validation.ValidationResult;

import java.util.Set;

public interface GeneratePresignedUrlUsecase extends Usecase<GeneratePresignedUrlUsecase.Command, GeneratePresignedUrlUsecase.Result> {

    Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp"
    );

    Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "gif", "webp"
    );

    record Command(
            String fileName,
            String contentType,
            String category
    ) implements SelfValidator<Command> {

        @Override
        public ValidationResult validate(Command input) {
            ValidationResult result = new ValidationResult();

            Rule.of(input.fileName(), "fileName", result)
                    .notBlank("fileName is required")
                    .matches("^[a-zA-Z0-9._-]+$", "fileName must be alphanumeric with dots, underscores, or hyphens")
                    .must(this::isNotPathTraversal, "fileName must not contain path traversal sequences")
                    .must(this::hasAllowedExtension, "fileName extension not allowed");

            Rule.of(input.contentType(), "contentType", result)
                    .notBlank("contentType is required")
                    .must(ALLOWED_CONTENT_TYPES::contains, "contentType not allowed. Allowed: jpeg, png, gif, webp");

            Rule.of(input.category(), "category", result)
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

    record Result(
            String uploadUrl,
            String fileKey,
            String fileUrl,
            java.time.Instant expiresAt
    ) {}
}

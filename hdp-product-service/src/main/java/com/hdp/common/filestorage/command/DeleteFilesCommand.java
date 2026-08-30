package com.hdp.common.filestorage.command;

import com.hdp.core.validation.Rule;
import com.hdp.core.validation.SelfValidator;
import com.hdp.core.validation.ValidationResult;

import java.util.List;

public record DeleteFilesCommand(
        List<String> fileKeys
) implements SelfValidator<DeleteFilesCommand> {
    @Override
    public ValidationResult validate(DeleteFilesCommand input) {
        ValidationResult result = new ValidationResult();
        Rule.of(fileKeys, "fileKeys", result)
                .notEmpty("fileKeys is required");
        if (fileKeys != null) {
            for (int i = 0; i < fileKeys.size(); i++) {
                String key = fileKeys.get(i);
                Rule.of(key, "fileKeys[" + i + "]", result)
                        .notBlank("fileKey at index " + i + " is required")
                        .must(this::isNotPathTraversal, "fileKey at index " + i + " contains path traversal");
            }
        }
        return result;
    }

    private boolean isNotPathTraversal(String fileKey) {
        if (fileKey == null) return true;
        return !fileKey.contains("..") && !fileKey.startsWith("/") && !fileKey.contains(":");
    }
}
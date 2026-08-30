package com.hdp.common.persistence.datasource;

import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlCommentBuilder {

    private static final String SQL_COMMENT_TEMPLATE = "/* %s */%n";
    private static final String COMMENT_SEPARATOR = " ";

    private final Map<String, String> comments = new LinkedHashMap<>();

    public static SqlCommentBuilder builder() {
        return new SqlCommentBuilder();
    }

    public SqlCommentBuilder add(String key, String value) {
        if (StringUtils.hasText(value)) {
            comments.put(key, value);
        }
        return this;
    }

    public String build() {

        if (comments.isEmpty()) {
            return "";
        }

        String content = comments.entrySet()
                .stream()
                .map(entry -> formatComment(entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(COMMENT_SEPARATOR));

        // /* trace_id=abc123 service=order-service */
        return String.format(SQL_COMMENT_TEMPLATE, content);

    }

    private String formatComment(String key, String value) {
        return key + "=" + value;
    }
}
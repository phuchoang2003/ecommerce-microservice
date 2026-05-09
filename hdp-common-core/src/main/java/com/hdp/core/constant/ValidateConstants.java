package com.hdp.core.constant;

public final class ValidateConstants {
    private ValidateConstants() {
    }

    // Size constraints
    public static final int DEFAULT_SIZE = 255;
    public static final int MAX_NAME_LENGTH = 255;
    public static final int MAX_DESCRIPTION_LENGTH = 5000;
    public static final int MAX_IMAGES_COUNT = 20;
    public static final int MAX_IMAGE_URL_LENGTH = 2048;
    public static final int MAX_PARENT_ID_LENGTH = 100;

    // UUID pattern for path validation
    public static final String UUID_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
}
package com.hdp.product_service.filestorage.command;

import java.util.List;

public record DeleteFilesCommand(
        List<String> fileKeys
) {}
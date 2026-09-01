package com.hdp.product_service.filestorage;

import com.hdp.product_service.application.port.in.generatepresignedurl.GeneratePresignedUrlCommand;
import com.hdp.product_service.application.port.in.generatepresignedurl.GeneratePresignedUrlResult;
import com.hdp.product_service.filestorage.command.DeleteFilesCommand;
import com.hdp.product_service.filestorage.command.DownloadFileCommand;
import com.hdp.product_service.filestorage.result.DeleteFilesResult;
import com.hdp.product_service.filestorage.result.DownloadFileResult;

public interface FileStorageService {

    GeneratePresignedUrlResult generatePresignedUploadUrl(GeneratePresignedUrlCommand command);

    String getFileUrl(String fileKey);

    DownloadFileResult downloadFile(DownloadFileCommand command);

    void deleteFile(String fileKey);

    DeleteFilesResult deleteFiles(DeleteFilesCommand command);
}
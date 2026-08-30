package com.hdp.common.filestorage;

import com.hdp.common.filestorage.command.DeleteFilesCommand;
import com.hdp.common.filestorage.command.DownloadFileCommand;
import com.hdp.common.filestorage.command.GeneratePresignedUrlCommand;
import com.hdp.common.filestorage.result.DeleteFilesResult;
import com.hdp.common.filestorage.result.DownloadFileResult;
import com.hdp.common.filestorage.result.GeneratePresignedUrlResult;

public interface FileStorageService {

    GeneratePresignedUrlResult generatePresignedUploadUrl(GeneratePresignedUrlCommand command);

    String getFileUrl(String fileKey);

    DownloadFileResult downloadFile(DownloadFileCommand command);

    void deleteFile(String fileKey);

    DeleteFilesResult deleteFiles(DeleteFilesCommand command);
}
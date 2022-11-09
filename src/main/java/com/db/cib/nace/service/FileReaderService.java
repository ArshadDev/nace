package com.db.cib.nace.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileReaderService<T> {
    List<T> readFileData(MultipartFile file, Class<T> type) throws Exception;

}

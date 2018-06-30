package main.model.service;

import main.model.entity.Catalog;
import main.model.entity.Cd;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface CatalogService {
    List<Cd> getAllCds() throws IOException;

    Catalog getCatalog() throws IOException;

    String getCatalogAsString() throws IOException;

    Resource getFileOnServerAsResource() throws MalformedURLException;

    String getCatalogAsPageResourceString(int page, int size) throws IOException;

    void addUploadedMultipartFileToServerFile(MultipartFile multipartFile) throws IOException;
}

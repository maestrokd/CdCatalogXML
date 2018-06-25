package main.model.service;

import main.model.entity.Catalog;
import main.model.entity.CatalogPageResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface CatalogService {
    Catalog getCatalog() throws IOException;
    String getCatalogAsXmlString() throws IOException;
    Resource getCatalogAsResource() throws MalformedURLException;
    String getCatalogAsPageResourceString(int page, int size) throws IOException;
    void addUploadedMultipartFileToServerFile(MultipartFile multipartFile) throws IOException;
}

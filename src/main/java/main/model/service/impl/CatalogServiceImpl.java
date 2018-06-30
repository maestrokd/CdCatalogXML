package main.model.service.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import main.model.entity.Catalog;
import main.model.entity.CatalogPageResource;
import main.model.entity.Cd;
import main.model.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class CatalogServiceImpl implements CatalogService {

    private File serverFile;

    private XmlMapper xmlMapper;


    @Autowired
    public CatalogServiceImpl(File serverFile, XmlMapper xmlMapper) {
        this.serverFile = serverFile;
        this.xmlMapper = xmlMapper;
    }


    @Override
    public String getCatalogAsString() throws IOException {
        return readFileFromServer();
    }


    @Override
    public Catalog getCatalog() throws IOException {
        String str = getCatalogAsString();
        return convertStringToCatalog(str);
    }


    @Override
    public List<Cd> getAllCds() throws IOException {
        return getCatalog().getCds();
    }


    @Override
    public Resource getFileOnServerAsResource() throws MalformedURLException {
        Path file = serverFile.toPath();
        return new UrlResource(file.toUri());
    }


    @Override
    public String getCatalogAsPageResourceString(int page, int size) throws IOException {
        if (size < 1 || page < 1) {
            throw new IllegalArgumentException();
        }

        Catalog catalog = getCatalog();
        int sizeDB = catalog.getCds().size();

        int totalPageNew = ((sizeDB % size) != 0) ? (sizeDB / size + 1) : (sizeDB / size);
        int pageNew = page <= totalPageNew ? page : totalPageNew;

        int skip = size * (pageNew - 1);

        Stream<Cd> cdStream = catalog.getCds().stream()
                                                .skip(skip)
                                                .limit(size);

        List<Cd> collectCdList = cdStream.collect(Collectors.toList());

        CatalogPageResource catalogPageResource = new CatalogPageResource(collectCdList, size, totalPageNew, pageNew);
        return xmlMapper.writeValueAsString(catalogPageResource);
    }


    @Override
    public void addUploadedMultipartFileToServerFile(MultipartFile multipartFile) throws IOException {
        String uploadXmlString = readMultipartFile(multipartFile);
        Catalog uploadCatalog = convertStringToCatalog(uploadXmlString);
        Catalog newCatalogOnServer = addCatalogForUploadedFileToCatalogForServerFile(uploadCatalog, getCatalog());
        writeToFileOnServer(newCatalogOnServer);
    }


    private Catalog addCatalogForUploadedFileToCatalogForServerFile(Catalog uploadCatalog, Catalog serverCatalog) {
        Map<String, Cd> serverCdsMap = convertCdListToMap(serverCatalog.getCds());
        for (Cd cd : uploadCatalog.getCds()) {
            serverCdsMap.put(cd.getTitle(), cd);
        }
        return new Catalog(new ArrayList<>(serverCdsMap.values()));
    }


    private void writeToFileOnServer(Catalog catalog) throws IOException {
        xmlMapper.writeValue(serverFile, catalog);
    }


    private String readFileFromServer() throws IOException {
        byte[] encoded = Files.readAllBytes(serverFile.toPath());
        return new String(encoded, Charset.defaultCharset());
    }


    private String readMultipartFile(MultipartFile multipartFile) throws IOException {
        return new BufferedReader(new InputStreamReader(multipartFile
                .getInputStream())).lines().collect(Collectors.joining("\n"));
    }


    private Map<String, Cd> convertCdListToMap(List<Cd> cdList) {
        Map<String, Cd> cdMap = new HashMap<>();
        for (Cd cd : cdList) {
            cdMap.put(cd.getTitle(), cd);
        }
        return cdMap;
    }


    private Catalog convertStringToCatalog(String xmlOfCatalog) throws IOException {
        return xmlMapper.readValue(xmlOfCatalog, Catalog.class);
    }


}

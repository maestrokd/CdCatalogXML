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

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private File serverFile;
    @Autowired
    private XmlMapper xmlMapper;


    @Override
    public Catalog getCatalog() throws IOException {
        return xmlMapper.readValue(getCatalogAsXmlString(), Catalog.class);
    }

    @Override
    public String getCatalogAsXmlString() throws IOException {
        return inputStreamToString(new FileInputStream(serverFile));
    }

    @Override
    public Resource getCatalogAsResource() throws MalformedURLException {
        Path file = serverFile.toPath();
        return new UrlResource(file.toUri());
    }


    @Override
    public String getCatalogAsPageResourceString(int page, int size) throws IOException {
        Catalog catalog = getCatalog();
        int sizeDB = catalog.getCds().size();

        int totalPageNew = ((sizeDB % size) != 0) ? (sizeDB / size + 1) : (sizeDB / size);
        int pageNew = page <= totalPageNew ? page : totalPageNew;

        int skip = size * (pageNew - 1);

        Stream<Cd> cdStream = catalog.getCds().stream().skip(skip).limit(size);
        List<Cd> collect = cdStream.collect(Collectors.toList());

        CatalogPageResource catalogPageResource = new CatalogPageResource();
        catalogPageResource.setCds(collect);
        catalogPageResource.setSize(size);
        catalogPageResource.setPage(pageNew);
        catalogPageResource.setTotalPage(totalPageNew);
        return xmlMapper.writeValueAsString(catalogPageResource);
    }


    @Override
    public void addUploadedMultipartFileToServerFile(MultipartFile multipartFile) throws IOException {
        String uploadXmlString = new BufferedReader(new InputStreamReader(multipartFile
                .getInputStream())).lines().collect(Collectors.joining("\n"));

        Catalog uploadCatalog = xmlMapper.readValue(uploadXmlString, Catalog.class);

        String serverXmlString = getCatalogAsXmlString();
        Catalog serverCatalog = xmlMapper.readValue(serverXmlString, Catalog.class);

        addCatalogForUploadedFileToCatalogForServerFile(uploadCatalog, serverCatalog);
        xmlMapper.writeValue(serverFile, serverCatalog);
    }

    private String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    private void addCatalogForUploadedFileToCatalogForServerFile(Catalog uploadCatalog, Catalog serverCatalog) {

        Map<String, Cd> cdMap = new HashMap<>();
        for (Cd cd : serverCatalog.getCds()) {
            cdMap.put(cd.getTitle(), cd);
        }

        for (Cd cd : uploadCatalog.getCds()) {
            cdMap.put(cd.getTitle(), cd);
        }
        serverCatalog.setCds(new ArrayList<>(cdMap.values()));
    }

}

package main.controller;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import main.model.entity.Cd;
import main.model.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.util.FileSystemUtils;
//import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
import java.util.List;


@RestController
public class XmlController {

    private String serverFilePathName = "first.xml";

    @Autowired
    private CatalogService catalogService;


    @Bean
    public XmlMapper getXmlMapper() {
        JacksonXmlModule xmlModule = new JacksonXmlModule();
        xmlModule.setDefaultUseWrapper(false);
        return new XmlMapper(xmlModule);
    }

    @Bean
    public File getServerFile() {
        return new File(serverFilePathName);
    }


    @GetMapping("/main")
    public ModelAndView listUploadedFiles() throws IOException {

        List<Cd> cds = catalogService.getCatalog().getCds();
        ModelAndView modelAndView= new ModelAndView();
        modelAndView.setViewName("main");
        modelAndView.addObject("files", cds);

        return modelAndView;
    }


    @RequestMapping(value = "/api/catalog", method = RequestMethod.GET)
    public ResponseEntity<String> getCatalog() throws IOException {
        String serverXmlString = catalogService.getCatalogAsXmlString();
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("application/xml"))
                .body(serverXmlString);
    }


    @RequestMapping(value = "/api/catalog/pageResource", method = RequestMethod.GET)
    public ResponseEntity<String> getCatalogPageResource(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size) throws IOException {
        String serverXmlString = catalogService.getCatalogAsPageResourceString(page, size);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("application/xml"))
                .body(serverXmlString);
    }


    @RequestMapping(value = "/api/catalog/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> doDownloadFile() throws IOException {
        Resource resource = catalogService.getCatalogAsResource();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @RequestMapping(value = "/api/catalog/upload")
    public ModelAndView doUpload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        catalogService.addUploadedMultipartFileToServerFile(multipartFile);
        return new ModelAndView("redirect:/main");
    }















    @RequestMapping(value = "/api/catalog/uploadOld")
    public ResponseEntity<String> doUploadOld(@RequestParam("file") MultipartFile multipartFile) throws IOException {

//        String uploadXmlString = new BufferedReader(new InputStreamReader(multipartFile.getInputStream())).lines().collect(Collectors.joining("\n"));
//        Catalog uploadCatalog = xmlMapper.readValue(uploadXmlString, Catalog.class);
//
//        String serverXmlString = inputStreamToString(new FileInputStream(serverFile));
//        Catalog serverCatalog = xmlMapper.readValue(serverXmlString, Catalog.class);
//
//        addCatalogForUploadedFileToCatalogForServerFile(uploadCatalog, serverCatalog);
//        xmlMapper.writeValue(serverFile, serverCatalog);
        catalogService.addUploadedMultipartFileToServerFile(multipartFile);
        String serverXmlString = catalogService.getCatalogAsXmlString();

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("application/xml"))
                .body(serverXmlString);
    }







//    public static String inputStreamToString(InputStream is) throws IOException {
//        StringBuilder sb = new StringBuilder();
//        String line;
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        while ((line = br.readLine()) != null) {
//            sb.append(line);
//        }
//        br.close();
//        return sb.toString();
//    }

//    public static void addCatalogForUploadedFileToCatalogForServerFile(Catalog uploadCatalog, Catalog serverCatalog) {
//
//        Map<String, Cd> cdMap = new HashMap<>();
//        for (Cd cd : serverCatalog.getCds()) {
//            cdMap.put(cd.getTitle(), cd);
//        }
//
//        for (Cd cd : uploadCatalog.getCds()) {
//            cdMap.put(cd.getTitle(), cd);
//        }
//        serverCatalog.setCds(new ArrayList<>(cdMap.values()));
//    }


//    @RequestMapping(value = "/")
//    public ResponseEntity<String> getCatalogDef(@RequestParam("file") MultipartFile multipartFile) throws IOException {
//
//        JacksonXmlModule xmlModule = new JacksonXmlModule();
//        xmlModule.setDefaultUseWrapper(false);
//        XmlMapper xmlMapper = new XmlMapper(xmlModule);
//
//
//        String uploadXmlString = new BufferedReader(new InputStreamReader(multipartFile.getInputStream())).lines().collect(Collectors.joining("\n"));
//        System.out.println("UpStr: " + uploadXmlString);
//        Catalog uploadCatalog = xmlMapper.readValue(uploadXmlString, Catalog.class);
//
//
//        File serverFile = new File("first.xml");
//        String serverXmlString = inputStreamToString(new FileInputStream(serverFile));
//        Catalog serverCatalog = xmlMapper.readValue(serverXmlString, Catalog.class);
//
//
//        Map<String, Cd> cdMap = new HashMap<>();
//        for (Cd cd : serverCatalog.getCds()) {
//            cdMap.put(cd.getTitle(), cd);
//        }
//
//        for (Cd cd : uploadCatalog.getCds()) {
//            cdMap.put(cd.getTitle(), cd);
//        }
//
//        System.out.println(cdMap.values());
//        serverCatalog.setCds(new ArrayList<>(cdMap.values()));
//        xmlMapper.writeValue(serverFile, serverCatalog);
//
//        serverXmlString = inputStreamToString(new FileInputStream(serverFile));
//
////        Path rootLocation = Paths.get("upload-dir");
////        Files.createDirectories(rootLocation);
////        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//
//
//
////        try (InputStream inputStream = multipartFile.getInputStream()) {
////            Files.copy(inputStream, rootLocation.resolve(filename),
////                    StandardCopyOption.REPLACE_EXISTING);
////        }
////
////        System.out.println("filename: " + filename);
////
////        File file22 = rootLocation.resolve(filename).toFile();
//
////        String uploadXmlString = xmlMapper.writeValueAsString(catalog);
////        String uploadXmlString = inputStreamToString(new FileInputStream(file22));
//
//
//
//
//
////        System.out.println(serverCatalog.getCds().size());
//
////        FileSystemUtils.deleteRecursively(rootLocation.toFile());
//
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.parseMediaType("application/xml"))
//                .body(serverXmlString);
//    }
//
//
//
//
//
//
//
//
//
//    @RequestMapping(value = "/m")
//    public ResponseEntity<String> getCatalogee() throws JsonProcessingException {
//        Cd cd1 = new Cd("Title 1", "Artist 1", "Country 1", "Company 1", "101", "2001");
//        Cd cd2 = new Cd("Title 2", "Artist 2", "Country 2", "Company 2", "102", "2002");
//
//        Catalog catalog = new Catalog();
//        catalog.getCds().add(cd1);
//        catalog.getCds().add(cd2);
//
//        JacksonXmlModule xmlModule = new JacksonXmlModule();
//        xmlModule.setDefaultUseWrapper(false);
//
//        XmlMapper xmlMapper = new XmlMapper(xmlModule);
//        String str;
//        str = xmlMapper.writeValueAsString(catalog);
//
//        System.out.println(str);
//
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.parseMediaType("application/xml"))
//                .body(str);
//    }


}

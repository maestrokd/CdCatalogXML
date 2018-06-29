package main.controller;

import main.model.entity.Cd;
import main.model.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


@RestController
public class XmlController {

    private CatalogService catalogService;


    @Autowired
    public XmlController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }


    @GetMapping("/main")
    public ModelAndView listUploadedFiles() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("uploadForm");
        List<Cd> cds;
        try {
            cds = catalogService.getAllCds();
        } catch (IOException e) {
            modelAndView.addObject("cds", new ArrayList<>());
            modelAndView.addObject("MainErrorMessage", "Uploading list of cd failed!");
            return modelAndView;
        }
        modelAndView.addObject("cds", cds);
        return modelAndView;
    }


    @RequestMapping(value = "/api/catalog", method = RequestMethod.GET)
    public ResponseEntity<String> getCatalog() {
        String serverXmlString;
        try {
            serverXmlString = catalogService.getCatalogAsString();
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("application/xml"))
                .body(serverXmlString);
    }


    @RequestMapping(value = "/api/catalog/pageResource", method = RequestMethod.GET)
    public ResponseEntity<String> getCatalogPageResource(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size) {
        String serverXmlString;
        try {
            serverXmlString = catalogService.getCatalogAsPageResourceString(page, size);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("application/xml"))
                .body(serverXmlString);
    }


    @RequestMapping(value = "/api/catalog/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> doDownloadFile() {
        Resource resource;
        try {
            resource = catalogService.getFileOnServerAsResource();
        } catch (MalformedURLException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @RequestMapping(value = "/api/catalog/upload")
    public ModelAndView doUpload(@RequestParam("file") MultipartFile multipartFile,
                                 RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/main");
        try {
            catalogService.addUploadedMultipartFileToServerFile(multipartFile);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("UploadFileErrorMessage", "File uploading fail!");
            return modelAndView;
        }
        redirectAttributes.addFlashAttribute("UploadFileInfoMessage", "File uploading success!");
        return modelAndView;
    }


}

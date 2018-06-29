package main.config;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.File;


@Configuration
public class CatalogServiceConfig {

    @Value("${value.serverFilePathName}")
    private String serverFilePathName;


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
}

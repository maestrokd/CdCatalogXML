package main.model.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "DATA")
public class CatalogPageResource {
    @JacksonXmlProperty(localName = "CD")
    private List<Cd> cds;
    @JacksonXmlProperty(localName = "SIZE")
    private int size;
    @JacksonXmlProperty(localName = "TOTALPAGE")
    private int totalPage;
    @JacksonXmlProperty(localName = "PAGE")
    private int page;


}

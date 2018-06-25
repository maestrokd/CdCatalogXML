package main.model.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@JacksonXmlRootElement(localName = "CATALOG")
public class Catalog {
    @JacksonXmlProperty(localName = "CD")
    private List<Cd> cds = new ArrayList<>();
}

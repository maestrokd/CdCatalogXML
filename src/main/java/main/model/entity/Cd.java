package main.model.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "CD")
public class Cd {
    @JacksonXmlProperty(localName = "TITLE")
    private String title;
    @JacksonXmlProperty(localName = "ARTIST")
    private String artist;
    @JacksonXmlProperty(localName = "COUNTRY")
    private String country;
    @JacksonXmlProperty(localName = "COMPANY")
    private String company;
    @JacksonXmlProperty(localName = "PRICE")
    private float price;
    @JacksonXmlProperty(localName = "YEAR")
    private int year;
}

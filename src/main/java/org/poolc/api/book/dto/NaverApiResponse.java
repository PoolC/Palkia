package org.poolc.api.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverApiResponse {

    @JacksonXmlProperty(localName = "channel")
    private Channel channel;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Channel {

        @JacksonXmlProperty(localName="lastBuildDate")
        private String lastBuildDate;

        @JacksonXmlProperty(localName="total")
        private Integer total;

        @JacksonXmlProperty(localName="start")
        private Integer start;

        @JacksonXmlProperty(localName="display")
        private Integer display;

        @JacksonXmlProperty(localName = "item")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<BookApiResponse> items;
    }
}

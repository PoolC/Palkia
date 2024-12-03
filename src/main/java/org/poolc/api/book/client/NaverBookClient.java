package org.poolc.api.book.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.poolc.api.book.dto.BookApiResponse;
import org.poolc.api.book.dto.NaverApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.management.modelmbean.XMLParseException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NaverBookClient implements BookClient{

    @Value("${book.api.url}")
    private String url;

    @Value("${book.api.secret}")
    private String clientSecret;

    @Value("${book.api.id}")
    private String clientId;

    private final RestTemplate restTemplate;

    private static final int PAGE_SIZE = 10;

    @Override
    public List<BookApiResponse> searchBooks(String query, int page) throws XMLParseException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = new StringBuilder(this.url)
                .append("?query=").append(query)
                .append("&display=").append(PAGE_SIZE)
                .append("&start=").append((page - 1) * PAGE_SIZE + 1)
                .toString();

        String xmlResponse = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        System.out.println(xmlResponse);


        try {
            NaverApiResponse naverApiResponse = parseBooks(xmlResponse);
            return naverApiResponse.getChannel().getItems();
        } catch (Exception e) {
            e.printStackTrace();
            throw new XMLParseException("Failed to parse XML response");
        }
    }

    private NaverApiResponse parseBooks(String xmlResponse) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(xmlResponse, NaverApiResponse.class);
    }
}

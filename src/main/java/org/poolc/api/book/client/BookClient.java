package org.poolc.api.book.client;

import org.poolc.api.book.dto.response.BookApiResponse;

import javax.management.modelmbean.XMLParseException;
import java.util.List;

public interface BookClient {

    List<BookApiResponse> searchBooks(String query, int page) throws XMLParseException;

}

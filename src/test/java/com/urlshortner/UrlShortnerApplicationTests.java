package com.urlshortner;

import com.urlshortner.exception.DomainBlacklistedException;
import com.urlshortner.exception.UrlNotFoundException;
import com.urlshortner.repository.UrlRepository;
import com.urlshortner.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UrlShortnerApplicationTests {

    private UrlService service;

    @BeforeEach
    void setup() {
        UrlRepository repository = new UrlRepository();
        service = new UrlService(repository);
    }

    @Test
    void sameUrlShouldReturnSameShortCode() throws DomainBlacklistedException {
        String url = "https://youtube.com/test";
        String code1 = service.shortenUrl(url);
        String code2 = service.shortenUrl(url);

        assertEquals(code1, code2);
    }

    @Test
    void shouldReturnOriginalUrl() throws DomainBlacklistedException {
        String url = "https://google.com";
        String code = service.shortenUrl(url);
        String result = service.getOriginalUrl(code);

        assertEquals(url, result);
    }

    @Test
    void shouldThrowExceptionWhenCodeNotFound() {
        assertThrows(UrlNotFoundException.class,
                    () -> service.getOriginalUrl("invalidCode"));
    }

    @Test
    void shouldReturnTopDomains() throws DomainBlacklistedException {
        service.shortenUrl("https://youtube.com/a");
        service.shortenUrl("https://youtube.com/b");
        service.shortenUrl("https://google.com");
        service.shortenUrl("https://google.com");

        Map<String, Long> result = service.getTopDomains();

        assertTrue(result.containsKey("youtube.com"));
        assertTrue(result.containsKey("google.com"));
        }
}

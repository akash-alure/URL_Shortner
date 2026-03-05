package com.urlshortner.controller;

import com.urlshortner.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/url")
public class UrlController {

    private final UrlService urlService;

    // this api shorten the provided url
    @PostMapping("/shorten")
    public String shorten(@RequestParam String url) {
        String code = urlService.shortenUrl(url);
        return "http://localhost:8080/" + code;
    }

    // this api gives original url parallel to provided shorted url
    @GetMapping("/{code}")
    public void redirect(@PathVariable String code, HttpServletResponse response) throws Exception {
        String originalUrl = urlService.getOriginalUrl(code);
        response.sendRedirect(originalUrl);
    }

    // this api will give most shortened top 3 domains
    @GetMapping("/top-domains")
    public Map<String, Long> getTopDomains() {
        return urlService.getTopDomains();
    }
}

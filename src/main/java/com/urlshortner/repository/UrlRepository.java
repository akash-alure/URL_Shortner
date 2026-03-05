package com.urlshortner.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UrlRepository {
    private final Map<String, String> urlToCode = new ConcurrentHashMap<>();
    private final Map<String, String> codeToUrl = new ConcurrentHashMap<>();

    public void save(String url, String code) {
        urlToCode.put(url, code);
        codeToUrl.put(code, url);
    }

    public String getCode(String url) {
        return urlToCode.get(url);
    }
    public String getUrl(String code) {
        return codeToUrl.get(code);
    }
    public Map<String,String> getAllUrls(){
        return urlToCode;
    }
}

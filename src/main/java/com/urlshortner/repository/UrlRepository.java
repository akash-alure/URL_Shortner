package com.urlshortner.repository;

import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UrlRepository {
    private final Map<String, String> urlToCode = new TreeMap<>();
    private final Map<String, String> codeToUrl = new TreeMap<>();
    private final Set<String> blackListedDomains = new HashSet<>();

    public void save(String url, String code) {
        urlToCode.put(url, code);
        codeToUrl.put(code, url);
    }

    public boolean addToBlackList(String domain){
        return blackListedDomains.add(domain);
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
    public Set<String> getBlackListedDomains(){ return blackListedDomains; }
}

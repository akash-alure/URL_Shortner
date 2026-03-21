package com.urlshortner.service;

import com.urlshortner.exception.DomainBlacklistedException;
import com.urlshortner.exception.UrlNotFoundException;
import com.urlshortner.repository.UrlRepository;
import com.urlshortner.util.EncodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrlService {

    private final UrlRepository repository;
    private final AtomicLong counter = new AtomicLong(1000);

    // this method shorten the user given url
    public String shortenUrl(String url) throws DomainBlacklistedException {

        log.info("Received URL to shorten: {}",url);
        String domainName = extractDomain(url);
        if(repository.getBlackListedDomains().contains(domainName)){
            throw new DomainBlacklistedException("Domain "+domainName+" is black listed!");
        }

        String existing = repository.getCode(url);

        if(existing != null){
            log.info("URL already shortened: {}",existing);
            return existing;
        }

        long id = counter.incrementAndGet();
        String code = EncodeUtil.encode(id);
        repository.save(url,code);

        log.info("Generated short code {} for URL {}",code,url);

        return code;
    }

    public String blackListURL(String url){
        String msgResponse = "Domain "+ url +" is blacklisted!";

        if(!repository.addToBlackList(url)){
            msgResponse = "Failed to add domain blacklisted!";
        }
        return msgResponse;
    }

    // this method gives original url from provided shorten url
    public String getOriginalUrl(String code){
        String url = repository.getUrl(code);

        if(url == null){
            throw new UrlNotFoundException("Short URL not found");
        }

        log.info("Redirecting code {} to {}",code,url);

        return url;
    }

    // this method gives top domain names which was shortened highly
    public Map<String, Long> getTopDomains() {
        Map<String, Long> domainCount = repository.getAllUrls().keySet().stream()
                .map(this::extractDomain)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return domainCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // this method gives domain names extracted from input URL
    private String extractDomain(String url){
        try{
            URI uri = new URI(url);
            return uri.getHost().replace("www.","");
        }
        catch(Exception e){
            return "unknown";
        }
    }
}
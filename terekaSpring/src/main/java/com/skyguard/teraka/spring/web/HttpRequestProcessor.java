package com.skyguard.teraka.spring.web;

import com.google.common.base.Strings;
import com.skyguard.teraka.client.TerakaClient;
import com.skyguard.teraka.config.ServiceConfig;
import com.skyguard.teraka.spring.util.SpringUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class HttpRequestProcessor {


      public static HttpRequest processHttpRequest(HttpRequest httpRequest){

          TerakaClient terakaClient = SpringUtil.getBean("terakaClient",TerakaClient.class);
          URI uri = httpRequest.getURI();
          ServiceConfig serviceConfig = terakaClient.getServer("");
           URI  uri1 = reconstructURIWithServer(serviceConfig, uri);
          HttpRequest httpRequest1 = new HttpRequest() {
              @Override
              public HttpMethod getMethod() {
                  return httpRequest.getMethod();
              }

              @Override
              public URI getURI() {
                  return uri1;
              }

              @Override
              public HttpHeaders getHeaders() {
                  return httpRequest.getHeaders();
              }
          };

          return httpRequest1;
      }


    public static URI reconstructURIWithServer(ServiceConfig serviceConfig, URI original) {
        String host = serviceConfig.getIp();
        int port = serviceConfig.getPort();
        if (host.equals(original.getHost()) && port == original.getPort()) {
            return original;
        } else {
            String scheme = original.getScheme();
            if (scheme == null) {
                scheme = deriveSchemeAndPortFromPartialUri(original);
            }

            try {
                StringBuilder sb = new StringBuilder();
                sb.append(scheme).append("://");
                if (!Strings.isNullOrEmpty(original.getRawUserInfo())) {
                    sb.append(original.getRawUserInfo()).append("@");
                }

                sb.append(host);
                if (port >= 0) {
                    sb.append(":").append(port);
                }

                sb.append(original.getRawPath());
                if (!Strings.isNullOrEmpty(original.getRawQuery())) {
                    sb.append("?").append(original.getRawQuery());
                }

                if (!Strings.isNullOrEmpty(original.getRawFragment())) {
                    sb.append("#").append(original.getRawFragment());
                }

                URI newURI = new URI(sb.toString());
                return newURI;
            } catch (URISyntaxException var8) {
                throw new RuntimeException(var8);
            }
        }
    }

    protected static String deriveSchemeAndPortFromPartialUri(URI uri) {
        boolean isSecure = false;
        String scheme = uri.getScheme();
        if (scheme != null) {
            isSecure = scheme.equalsIgnoreCase("https");
        }

        if (scheme == null) {
            if (isSecure) {
                scheme = "https";
            } else {
                scheme = "http";
            }
        }

        return scheme;
    }




}

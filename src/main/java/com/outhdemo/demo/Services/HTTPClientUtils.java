package com.outhdemo.demo.Services;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HTTPClientUtils {

    public static String executeGetWithAuthorization(String targetURL, String accessToken) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(targetURL);
        httpGet.addHeader("Authorization", "Bearer "+ accessToken);
        ResponseHandler<String> handler = new BasicResponseHandler();
        HttpResponse response = client.execute(httpGet);
        return handler.handleResponse(response);
    }
}

package com.wikia.api.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

public class JsonClientImpl implements JsonClient {
    private static Logger logger = LoggerFactory.getLogger(JsonClientImpl.class);
    private ClientConnectionManager clientConnectionManager;

    public JsonClientImpl(ClientConnectionManager clientConnectionManager) {
        this.clientConnectionManager = clientConnectionManager;
    }

    public JsonElement getJsonElement(URI url) throws IOException {
        logger.info("GET: " + url);
        HttpClient httpClient = getHttpClient();
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");
            String response = httpClient.execute(httpGet, new BasicResponseHandler());
            return parseJson(response);
        } finally {
            if( httpGet != null ) {
                httpGet.releaseConnection();
            }
        }
    }

    protected HttpClient getHttpClient() {
        return new DefaultHttpClient(clientConnectionManager);
    }

    @Override
    public <T> T get(URI url, Class<T> tClass) throws IOException {
        return new Gson().fromJson(
                getJsonElement(url),
                tClass
        );
    }

    protected JsonElement parseJson( String json ) {
        if( json == null ) {
            throw new IllegalArgumentException("json parameter cannot be null.");
        }
        return new JsonParser().parse(json);
    }
}

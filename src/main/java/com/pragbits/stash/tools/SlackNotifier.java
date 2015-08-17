package com.pragbits.stash.tools;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class SlackNotifier {

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private static final Logger log = LoggerFactory.getLogger(SlackNotifier.class);

    public  SlackNotifier() {

    }

    public void SendSlackNotification(String targetUrl, String jsonString) {
        try {
            /*
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(15000)
                    .setConnectTimeout(15000)
                    .setSocketTimeout(15000)
                    .build();
            */
            HttpPost httpPost = new HttpPost(targetUrl);
            //httpPost.setConfig(requestConfig);

            httpPost.setEntity(new StringEntity(jsonString, ContentType.APPLICATION_JSON));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    String responseString = EntityUtils.toString(entity, "UTF-8");
                    log.error("#error during httpPost in SlackNotifier: " + responseString);
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            log.error("#error during http request execution in SlackNotifier: ", e);
        }
    }
}

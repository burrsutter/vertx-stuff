package com.stuff;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;

public class AchievementReset extends AbstractVerticle {
  public void start() throws Exception {
    HttpClient client = vertx.createHttpClient(new HttpClientOptions()
      .setDefaultHost("192.168.1.9")
      .setMaxPoolSize(20)
      .setDefaultPort(9090)
      );

    client.delete(9090,"192.168.1.9","/api/reset", response -> {
        System.out.println("statusCode: " + response.statusCode());
        System.out.println("statusMessage: " + response.statusMessage());
        if (response.statusCode() == 200 ) {

        }
    }).setTimeout(3000)            
       .putHeader("Content-Type", "application/json")
       .end();
  }
}
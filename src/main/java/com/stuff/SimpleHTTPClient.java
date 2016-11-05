package com.stuff;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;

/*
  Interacts with SimpleWebRouter
*/
public class SimpleHTTPClient extends AbstractVerticle {
  public void start() throws Exception {
    HttpClient client = vertx.createHttpClient(new HttpClientOptions()
      .setDefaultHost("localhost")
      .setMaxPoolSize(2)
      .setDefaultPort(8888)
      );

    // HTTP GET
    client.get("/", response -> {
        System.out.println("* GET");
        System.out.println("statusCode: " + response.statusCode());
        System.out.println("statusMessage: " + response.statusMessage());
        response.bodyHandler( body -> { 
          System.out.println("Received: " + body);
        });
    }).setTimeout(3000)            
       .putHeader("Content-Type", "application/json")
       .putHeader("Accept", "application/json")
       .end();


    // HTTP GET with param
    client.get("/hi", response -> {
        System.out.println("* GET with Param");
        System.out.println("statusCode: " + response.statusCode());
        System.out.println("statusMessage: " + response.statusMessage());
        response.bodyHandler( body -> { 
          System.out.println("Received: " + body);
        });        
    }).setTimeout(3000)            
       .putHeader("Content-Type", "application/json")
       .putHeader("Accept", "application/json")
       .end();

    // HTTP POST
    String toPost = "{ \"hello\" : \"world\" }";

    client.post("/postme", response -> {
        System.out.println("* POST");
        System.out.println("statusCode: " + response.statusCode());
        System.out.println("statusMessage: " + response.statusMessage());
        response.bodyHandler( body -> { 
          System.out.println("Received: " + body);
        });        
    }).setTimeout(3000)            
       .putHeader("Content-Type", "application/json")
       .putHeader("Accept", "application/json")
       .putHeader("Content-Length", String.valueOf(toPost.length()))
       .write(toPost)
       .end();

   // HTTP PUT JSON
   String toPut = 
       "[{" +
	      "\"type\": \"pops3\" ," +
	      "\"description\": \"Splash Apprentice!3 in a row!\"" +
       "}]";

    client.put("/putme", response -> {
        System.out.println("* PUT");
        System.out.println("statusCode: " + response.statusCode());
        System.out.println("statusMessage: " + response.statusMessage());
    }).setTimeout(3000)            
       .putHeader("Content-Type", "application/json")
       .putHeader("Accept", "application/json")
       .putHeader("Content-Length", String.valueOf(toPut.length()))
       .write(toPut)
       .end();

   // HTTP PUT 
   String toPut2 = "some stuff"; 

    client.put("/putme", response -> {
        System.out.println("* PUT2");
        System.out.println("statusCode: " + response.statusCode());
        System.out.println("statusMessage: " + response.statusMessage());
    }).setTimeout(3000)            
       .end(toPut2);


  } // start
}
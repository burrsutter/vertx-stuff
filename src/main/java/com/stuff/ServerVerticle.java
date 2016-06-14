package com.stuff;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.http.HttpClient; 
import io.vertx.core.http.HttpClientOptions;
import java.util.Base64;
import java.util.Iterator;

public class ServerVerticle extends AbstractVerticle {
  private static final String USERID = "user";
  private static final String PASSWORD = "super@secret";
  private static final String defaultHost = "somehost";
                                            
  @Override
  public void start() throws Exception {
    StringBuilder userpassword = new StringBuilder(USERID+":"+PASSWORD);

    String basicAuth = "Basic " + Base64.getEncoder().encodeToString(userpassword.toString().getBytes());
    
    System.out.println("basicAuth: " + basicAuth);
    
    JsonObject input = new JsonObject();
    
    JsonObject player = new JsonObject();
    player.put("uuid","p1");
    player.put("username", "John Doe");
    player.put("team", 1);
    player.put("score", 120);
    player.put("consecutivePops",20);
    // System.out.println(player);
 
    JsonObject playerClass = new JsonObject();
    playerClass.put("com.redhatkeynote.score.Player",player);

    JsonObject playerObject = new JsonObject();
    playerObject.put("object",playerClass);
    playerObject.put("out-identifier","player");
    playerObject.put("return-object", true);

    JsonObject insertCommand = new JsonObject();
    insertCommand.put("insert",playerObject);
    
    JsonObject fireAllRules = new JsonObject();
    JsonObject emptyObject = new JsonObject();
    fireAllRules.put("fire-all-rules",emptyObject);

    JsonArray commandsArray = new JsonArray();
    commandsArray.add(insertCommand);
    commandsArray.add(fireAllRules);

    input.put("lookup", "ScoreSession");
    input.put("commands", commandsArray);
    System.out.println(input);
    

    HttpClient client = vertx.createHttpClient(new HttpClientOptions()
      .setDefaultHost(defaultHost)
      .setDefaultPort(8080));

    
    client.post("/kie-server/services/rest/server/containers/instances/score", response -> {
      response.exceptionHandler(t -> {
          System.err.println("RESPONSE: " + t);  // print exception
          
      }).bodyHandler(output -> {
        JsonObject json = output.toJsonObject();
        System.out.println("OUTPUT: " + output);
        // TODO, parse the output
       JsonArray resultsArray = json.getJsonObject("result")
         .getJsonObject("execution-results")
         .getJsonArray("results");
         
       System.out.println("\nresultsArray: " + resultsArray);

       JsonObject x = resultsArray.getJsonObject(0);

       System.out.println("x: " + x);

       JsonObject playerObjectResults = resultsArray.getJsonObject(0)
          .getJsonObject("value")
          .getJsonObject("com.redhatkeynote.score.Player");

       System.out.println("\nplayerObjectResults: " + playerObjectResults);

       JsonArray playerAchievements = playerObjectResults.getJsonArray("achievements");
       System.out.println("\nplayerAchievements: " + playerAchievements);

       Iterator i = playerAchievements.iterator() ; 
        while( i.hasNext() ) { 
         JsonObject achievement = (JsonObject) i.next() ;
         System.out.println(achievement.getString("desc"));  
       } // while 

      });  // bodyHandler
    })
        .setTimeout(3000)
        .putHeader("Authorization", basicAuth)
        .putHeader("Accept","application/json")
        .putHeader("Content-Type","application/json")
        .exceptionHandler(t -> {
          System.err.println("REQUEST: " + t);  // print exception
          
        })
        .end(input.encode());

    
  } // start()


}
package com.stuff;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;  
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpClientOptions;
import java.util.Base64;
import java.util.Iterator;

public class POSTVerticleSummary2 extends AbstractVerticle {
  private static final String USERID = "kiewb";
  private static final String PASSWORD = "kiewb";
  private static final String defaultHost = "localhost";
                                            
  @Override
  public void start() throws Exception {
    StringBuilder userpassword = new StringBuilder(USERID+":"+PASSWORD);

    String basicAuth = "Basic " + Base64.getEncoder().encodeToString(userpassword.toString().getBytes());
    
    System.out.println("basicAuth: " + basicAuth);

    final String SUMMARY_REQUEST_PREFIX = "{" +
      "\"lookup\"   : \"SummarySession\"," +
      "\"commands\" : [" +
      "  { \"insert\" : {" +
      "       \"object\" : {\"com.redhatkeynote.score.ScoreSummary\":{" +
      "         \"topPlayers\"     : ";

    final String SUMMARY_REQUEST_SUFFIX = "       }}," +
      "       \"out-identifier\" : \"scoreSummary\"," +
      "       \"return-object\" : true" +
      "    }" +
      "  }," +
      "  {" +
      "      \"fire-all-rules\" : {}" +
      "  } ]" +
      "}";

    int numTopPlayers = 10;
    /*
    JsonObject input = new JsonObject();
    
    JsonObject topPlayers = new JsonObject();
    topPlayers.put("topPlayers",10);
    // System.out.println(player);
 
    JsonObject scoreSummaryClass = new JsonObject();
    scoreSummaryClass.put("com.redhatkeynote.score.ScoreSummary",topPlayers);

    JsonObject playerObject = new JsonObject();
    playerObject.put("object",scoreSummaryClass);
    playerObject.put("out-identifier","scoreSummary");
    playerObject.put("return-object", true);

    JsonObject insertCommand = new JsonObject();
    insertCommand.put("insert",playerObject);
    
    JsonObject fireAllRules = new JsonObject();
    JsonObject emptyObject = new JsonObject();
    fireAllRules.put("fire-all-rules",emptyObject);

    JsonArray commandsArray = new JsonArray();
    commandsArray.add(insertCommand);
    commandsArray.add(fireAllRules);

    input.put("lookup", "SummarySession");
    input.put("commands", commandsArray);
    System.out.println(input);
    */
     
    String input = SUMMARY_REQUEST_PREFIX + numTopPlayers + SUMMARY_REQUEST_SUFFIX;

    HttpClient client = vertx.createHttpClient(new HttpClientOptions()
      .setDefaultHost(defaultHost)
      .setMaxPoolSize(20)
      .setDefaultPort(8080));

    
    final HttpClientRequest summaryRequest = client.post("/kie-server/services/rest/server/containers/instances/score", response -> {
      response.exceptionHandler(t -> {
          t.printStackTrace();
          client.close();          
      });
      System.out.println("! resp.statusCode(): " + response.statusCode());

      if (response.statusCode() == 200 ) {
        response.bodyHandler(body -> {
          final JsonObject bodyJson = body.toJsonObject();
          handleResponse(bodyJson);
        });      
      } // if 200    
    })  
    .putHeader("Accept", "application/json")
    .putHeader("Authorization", basicAuth)
    .putHeader("Content-Type", "application/json")
    .setTimeout(3000)
    .exceptionHandler(t -> {
      t.printStackTrace();
      client.close();      
    });
    // summaryRequest.end(input.encode());
    summaryRequest.end(input);
  } // start()

  private void handleResponse(JsonObject output) {
        
        // JsonObject json = output.toJsonObject();
        System.out.println("OUTPUT: " + output);
        
       JsonArray resultsArray = output.getJsonObject("result")
         .getJsonObject("execution-results")
         .getJsonArray("results");
         
       System.out.println("\nresultsArray: " + resultsArray);

       JsonObject x = resultsArray.getJsonObject(0);

       System.out.println("x: " + x);

       JsonObject summaryResults = resultsArray.getJsonObject(0)
          .getJsonObject("value")
          .getJsonObject("com.redhatkeynote.score.ScoreSummary");

       System.out.println("\nsummaryResults: " + summaryResults);
       
       
       JsonArray teamScores = summaryResults.getJsonArray("teamScores");
       System.out.println("\nteamScores: " + teamScores);

       Iterator i = teamScores.iterator() ; 
        while( i.hasNext() ) { 
         JsonObject achievement = (JsonObject) i.next() ;
         System.out.println(achievement.getInteger("score"));  
       } // while 

  }
}
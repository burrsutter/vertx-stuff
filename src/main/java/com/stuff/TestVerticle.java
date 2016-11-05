package com.stuff;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class TestVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
      System.out.println("START");
      

      vertx.setPeriodic(3000, id -> {
        // long seed = System.nanoTime();
        // Collections.shuffle(alist, new Random(seed));
        ArrayList<JsonObject> alist = generateJsonObjects();
        for (JsonObject a : alist) {
            System.out.print("name: " + a.getString("name") + " ");
            System.out.println("score: " + a.getInteger("score"));
        }        
      }); // setPeriodic

  } // start
  private ArrayList<JsonObject> generateJsonObjects() {
      ArrayList<JsonObject> alist = new ArrayList<JsonObject>();
      
      for (int i = 1; i <= 10; i++) {
       int random = (int )(Math.random() * 10 + 1);
       JsonObject player = new JsonObject();
       player.put("name","Mercury Slice" + i);
       player.put("score",random);
       JsonObject player_cheeves = new JsonObject();
       player_cheeves.put("streak5", true);
       player_cheeves.put("streak10", true);
       player_cheeves.put("streak15", false);
       player_cheeves.put("points50", true);
       player_cheeves.put("points100", true);
       player_cheeves.put("points300", true);
       player_cheeves.put("snitch", false);
       player.put("cheeves", player_cheeves);
       alist.add(player);
    } // for
    return alist;
  }  // generateJsonObjects
}
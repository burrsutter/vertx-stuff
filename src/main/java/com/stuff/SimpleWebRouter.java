package com.stuff;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

/*
  Interacts with SimpleHTTPClient
*/
public class SimpleWebRouter extends AbstractVerticle {
   public void start() {
      Router router = Router.router(vertx);

      // GET      
      router.get("/").handler(rc -> {
        System.out.println("* GET");
        System.out.println(rc.getClass().getName());
        rc.response().end("GET response");        
      }); // get

      // GET with param      
      router.get("/:myparam").handler(rc -> {
        System.out.println("* GET with param");
        System.out.println(rc.getClass().getName());
        String myParam = rc.pathParam("myparam");
        System.out.println("myparam: " + myParam);
        rc.response().end("GET response with param: " + myParam);        
      }); // get

      // POST
      router.post("/postme").handler(rc -> {
        System.out.println("* POST");
        String input = rc.getBodyAsString();
        // JsonObject input = rc.getBodyAsJson();
        System.out.println("body: " + input);
        System.out.println(rc.getClass().getName());
        rc.response().putHeader("content-type", "application/json")
          .end("{ \"stuff\": \"happens\"}");        
      }); // post

      router.put("/putme").handler(rc -> {
        System.out.println("* PUT");          
        System.out.println(rc.getClass().getName());
        // String myparam = rc.pathParam("myparam");
        // System.out.println("myparam: " + myparam);
        System.out.println("body: " + rc.getBodyAsJson());
        rc.response().putHeader("content-type", "application/json")
          .end();
      }); // put

      // setup server and listen
      vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8888);

   } // start

}
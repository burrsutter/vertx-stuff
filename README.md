This is a playground to try different pieces of API/functionality
mostly around HTTP clients & services

vertx run src/main/java/com/stuff/ServerVerticle.java

or for interactive development

vertx run src/main/java/com/stuff/ServerVerticle.java --redeploy="**/*.java" --launcher-class=io.vertx.core.Launcher -cp .:src/main/java/

Note: requires download of Vert.x, unzip and added to your PATH

or

mvn package

java -jar /target/stuff-1.0.0-fat.jar

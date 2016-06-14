vertx run src/main/java/com/stuff/ServerVerticle.java

or for interactive development
vertx run src/main/java/com/stuff/ServerVerticle.java --redeploy="**/*.java" --launcher-class=io.vertx.core.Launcher -cp .:src/main/java/

or

mvn package

java -jar /target/stuff-1.0.0-fat.jar

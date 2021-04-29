package main;

import io.vertx.core.Vertx;
import server.ServerVerticle;

public class Main {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ServerVerticle());
        System.out.println(">>> Verticle iniciado e rodando na porta 9000");
    }

}

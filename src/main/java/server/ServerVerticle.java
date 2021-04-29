package server;

import entities.Contato;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ServerVerticle extends AbstractVerticle {

    private final Random random = new Random();
    private final Map<Integer, Contato> contatos = new HashMap<>();

    @Override
    public void start(Future<Void> future) {

        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/html")
                    .end("<h1>Meu Primeiro Server com Vertx Funcionando</h1>");
        });

        router.route("/api/contatos*").handler(BodyHandler.create());
        router.post("/api/contatos").handler(this::addContato);
        router.get("/api/contatos").handler(this::getAll);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(config().getInteger("http.port", 9000), result -> {
                    if (result.succeeded()) {
                        future.complete();
                    } else {
                        future.fail(result.cause());
                    }
                });
    }

    private void addContato(RoutingContext routingContext) {
        final Contato contato = Json.decodeValue(routingContext.getBodyAsString(),
                Contato.class);

        contato.setId(random.nextInt(100000));
        contatos.put(contato.getId(), contato);

        routingContext.response()
                .setStatusCode(201)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(contatos.get(contato.getId())));
    }

    private void getAll(RoutingContext routingContext) {
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(contatos.values()));
    }

}

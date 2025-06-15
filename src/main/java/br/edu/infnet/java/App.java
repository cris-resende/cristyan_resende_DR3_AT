package br.edu.infnet.java;

import io.javalin.Javalin;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create()
                .start(7000); // Inicia o Javalin na porta 7000

        // Endpoint /hello
        app.get("/hello", ctx -> ctx.result("Hello, Javalin!"));

        // Endpoint /status
        app.get("/status", ctx -> {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            Map<String, String> status = Map.of("status", "ok", "timestamp", timestamp);
            ctx.json(status);
        });

        // Endpoint /echo
        app.post("/echo", ctx -> {
            Map<String, String> requestBody = ctx.bodyAsClass(Map.class);
            ctx.json(requestBody);
        });

        // Endpoint /saudacao/{nome}
        app.get("/saudacao/{nome}", ctx -> {
            String nome = ctx.pathParam("nome");
            Map<String, String> response = Map.of("mensagem", "Olá, " + nome + "!");
            ctx.json(response);
        });
    }
}
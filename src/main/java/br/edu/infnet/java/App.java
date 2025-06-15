package br.edu.infnet.java;

import io.javalin.Javalin;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import br.edu.infnet.java.model.Mensalista;
import br.edu.infnet.java.service.MensalistaService;

public class App {

    public Javalin app;
    private MensalistaService mensalistaService;

    public App() {
        this.app = Javalin.create();
        this.mensalistaService = new MensalistaService();
        configureRoutes();
    }

    public static void main(String[] args) {
        new App().app.start(7000);
    }

    private void configureRoutes() {
        app.get("/hello", ctx -> ctx.result("Hello, Javalin!"));

        app.get("/status", ctx -> {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            Map<String, String> status = Map.of("status", "ok", "timestamp", timestamp);
            ctx.json(status);
        });

        app.post("/echo", ctx -> {
            Map<String, String> requestBody = ctx.bodyAsClass(Map.class);
            ctx.json(requestBody);
        });

        app.get("/saudacao/{nome}", ctx -> {
            String nome = ctx.pathParam("nome");
            Map<String, String> response = Map.of("mensagem", "Olá, " + nome + "!");
            ctx.json(response);
        });

        // Endpoints para Mensalista

        app.get("/mensalistas", ctx -> {
            ctx.json(mensalistaService.listarTodosMensalistas());
        });

        app.get("/mensalistas/{matricula}", ctx -> {
            String matricula = ctx.pathParam("matricula");
            mensalistaService.buscarMensalistaPorMatricula(matricula)
                    .ifPresentOrElse(
                            ctx::json,
                            () -> ctx.status(404).result("Mensalista não encontrado")
                    );
        });

        app.post("/mensalistas", ctx -> {
            Mensalista novoMensalista = ctx.bodyAsClass(Mensalista.class);

            if (novoMensalista.getMatricula() == null || novoMensalista.getMatricula().trim().isEmpty() ||
                    novoMensalista.getNome() == null || novoMensalista.getNome().trim().isEmpty()) {
                ctx.status(400).result("Matrícula e Nome são obrigatórios.");
                return;
            }

            if (mensalistaService.buscarMensalistaPorMatricula(novoMensalista.getMatricula()).isPresent()) {
                ctx.status(409).result("Mensalista com esta matrícula já existe.");
                return;
            }

            mensalistaService.adicionarMensalista(novoMensalista);
            ctx.status(201).json(novoMensalista);
        });
    }
}

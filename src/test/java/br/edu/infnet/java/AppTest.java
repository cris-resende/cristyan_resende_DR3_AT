package br.edu.infnet.java;

import br.edu.infnet.java.model.Mensalista;
import io.javalin.Javalin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AppTest {

    private static Javalin app;
    private static final int PORT = 7001;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setup() {
        app = new App().app.start(PORT);
    }

    @AfterAll
    public static void teardown() {
        if (app != null) {
            app.stop();
        }
    }

    @Test
    public void testEndpointHello() throws Exception {
        URL url = new URL("http://localhost:" + PORT + "/hello");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        Assertions.assertEquals(200, responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();

        Assertions.assertEquals("Hello, Javalin!", content.toString());
    }

    @Test
    public void testCriacaoMensalistaEndpoint() throws Exception {
        Mensalista novoMensalista = new Mensalista("M005", "Fernanda Santos", "Coordenadora", 7000.0);
        String jsonInputString = objectMapper.writeValueAsString(novoMensalista);

        URL url = new URL("http://localhost:" + PORT + "/mensalistas");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        Assertions.assertEquals(201, responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = in.readLine()) != null) {
            response.append(responseLine.trim());
        }
        in.close();
        connection.disconnect();

        Mensalista mensalistaCriado = objectMapper.readValue(response.toString(), Mensalista.class);
        Assertions.assertEquals("M005", mensalistaCriado.getMatricula());
        Assertions.assertEquals("Fernanda Santos", mensalistaCriado.getNome());
    }

    @Test
    public void testBuscarMensalistaPorMatriculaEndpoint() throws Exception {

        String matriculaExistente = "M001";
        URL url = new URL("http://localhost:" + PORT + "/mensalistas/" + matriculaExistente);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        Assertions.assertEquals(200, responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();

        Mensalista mensalistaEncontrado = objectMapper.readValue(content.toString(), Mensalista.class);
        Assertions.assertEquals(matriculaExistente, mensalistaEncontrado.getMatricula());
        Assertions.assertEquals("Alice Silva", mensalistaEncontrado.getNome());
    }

    @Test
    public void testListarTodosMensalistasEndpoint() throws Exception {

        URL url = new URL("http://localhost:" + PORT + "/mensalistas");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        Assertions.assertEquals(200, responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();

        Mensalista[] mensalistasArray = objectMapper.readValue(content.toString(), Mensalista[].class);
        Assertions.assertNotNull(mensalistasArray);
        Assertions.assertTrue(mensalistasArray.length >= 2);
    }
}
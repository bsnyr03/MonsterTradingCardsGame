package at.fhtw.MTCG.controller;

import at.fhtw.MTCG.service.UserService;
import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class SessionsController implements RestController {

    private final UserService userService;

    public SessionsController() {
        this.userService = new UserService();
    }

    @Override
    public Response handleRequest(Request request) {
        try {
            if (request.getMethod() == Method.POST) {
                return handleLogin(request);
            }

            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Invalid request method\"}");

        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private Response handleLogin(Request request) {
        try {
            // Lese die JSON-Daten aus der Anfrage
            Map<String, String> requestBody = new ObjectMapper().readValue(request.getBody(), Map.class);
            String username = requestBody.get("username");
            String password = requestBody.get("password");

            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                return new Response(
                        HttpStatus.BAD_REQUEST,
                        ContentType.JSON,
                        "{\"error\": \"Username or password cannot be empty\"}"
                );
            }

            // Versuche, den Benutzer einzuloggen
            String token = userService.loginUser(username, password);

            // Erfolgreicher Login
            String jsonResponse = new ObjectMapper().writeValueAsString(Map.of(
                    "message", "Login successful",
                    "username", username,
                    "token", token
            ));

            return new Response(HttpStatus.OK, ContentType.JSON, jsonResponse);

        } catch (IllegalArgumentException e) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"error\": \"Invalid username or password\"}");
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
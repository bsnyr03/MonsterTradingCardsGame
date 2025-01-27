package at.fhtw.MTCG.controller;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.service.PackageService;
import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PackageController implements RestController {
    private final PackageService packageService;

    public PackageController() {
        this.packageService = new PackageService();
    }

    @Override
    public Response handleRequest(Request request) {
        try {
            if (request.getMethod() == Method.POST) {
                return handlePostPackage(request); // Package erstellen
            } else if (request.getMethod() == Method.GET) {
                return handleGetPackages(request); // Alle Packages abrufen
            }

            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Invalid request method or path\"}");
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private Response handlePostPackage(Request request) throws JsonProcessingException, SQLException {
        Map<String, Object> requestBody = new ObjectMapper().readValue(request.getBody(), Map.class);

        String packageName = (String) requestBody.get("name");
        List<Card> cards = new ObjectMapper().convertValue(requestBody.get("cards"), new TypeReference<List<Card>>() {});

        if (packageName == null || packageName.isEmpty()) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Package name cannot be null or empty\"}");
        }

        if (cards.size() != 5) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"A package must contain exactly 5 cards.\"}");
        }

        if (packageService.createPackage(packageName, cards)) {
            // Erfolgreiche Antwort mit dem Paketnamen
            String successMessage = String.format("{\"message\": \"Package '%s' was successfully created.\"}", packageName);
            return new Response(HttpStatus.CREATED, ContentType.JSON, successMessage);
        } else {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Failed to create package.\"}");
        }
    }

    private Response handleGetPackages(Request request) throws SQLException, JsonProcessingException {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"error\": \"Missing or invalid token\"}");
        }
        token = token.replace("Bearer ", "").trim();

        var packages = packageService.getPackagesByToken(token);

        if (packages.isEmpty()) {
            return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{\"error\": \"No packages found\"}");
        }

        String jsonResponse = new ObjectMapper().writeValueAsString(packages);

        return new Response(HttpStatus.OK, ContentType.JSON, jsonResponse);
    }
}
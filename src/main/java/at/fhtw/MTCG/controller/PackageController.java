package at.fhtw.MTCG.controller;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.service.CardService;
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

public class PackageController implements RestController {
    private final PackageService packageService;

    public PackageController() {
        this.packageService = new PackageService();
    }

    @Override
    public Response handleRequest(Request request) {
        try {
            if (request.getMethod() == Method.POST) {
                return handleCreatePackage(request);
            } else if (request.getMethod() == Method.GET) {
                return handleGetPackages(request);
            }

            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Invalid request method\"}");
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private Response handleCreatePackage(Request request) throws JsonProcessingException, SQLException {

        List<Card> cards = new ObjectMapper().readValue(request.getBody(), new TypeReference<List<Card>>() {});
        if (packageService.createPackage(cards)) {
            return new Response(HttpStatus.CREATED, ContentType.JSON, "{\"message\": \"Package created successfully\"}");
        } else {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Failed to create package\"}");
        }
    }

    private Response handleGetPackages(Request request) throws SQLException, JsonProcessingException {

        String token = request.getHeader("Authorization").replace("Bearer ", "");
        if (token == null || token.isEmpty()) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"error\": \"Missing or invalid token\"}");
        }

        var packages = packageService.getPackagesByToken(token);
        if (packages.isEmpty()) {
            return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{\"error\": \"No packages found\"}");
        }

        String jsonResponse = new ObjectMapper().writeValueAsString(packages);
        return new Response(HttpStatus.OK, ContentType.JSON, jsonResponse);
    }
}
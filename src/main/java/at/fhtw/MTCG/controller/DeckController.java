package at.fhtw.MTCG.controller;

import at.fhtw.MTCG.model.Deck;
import at.fhtw.MTCG.service.DeckService;
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

public class DeckController implements RestController {
    private final DeckService deckService;

    public DeckController() {
        this.deckService = new DeckService();
    }

    @Override
    public Response handleRequest(Request request) {
        try {
            if (request.getMethod() == Method.GET) {
                return handleGetRequest(request);
            } else if (request.getMethod() == Method.PUT) {
                return handlePutRequest(request);
            }

            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Invalid request method\"}");
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private Response handleGetRequest(Request request) throws SQLException, JsonProcessingException {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        int userId = deckService.getUserIdFromToken(token);

        Deck deck = deckService.getDeckByUserId(userId);

        String jsonResponse = new ObjectMapper().writeValueAsString(deck);
        return new Response(HttpStatus.OK, ContentType.JSON, jsonResponse);
    }

    private Response handlePutRequest(Request request) throws SQLException, JsonProcessingException {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        int userId = deckService.getUserIdFromToken(token);

        List<Integer> cardIds = new ObjectMapper().readValue(request.getBody(), new TypeReference<List<Integer>>() {});

        Deck deck = new Deck(0, userId, deckService.getCardsByIds(cardIds));

        boolean isUpdated = deckService.updateDeck(userId, deck);

        if (isUpdated) {
            return new Response(HttpStatus.OK, ContentType.JSON, "{\"message\": \"Deck updated successfully\"}");
        } else {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Failed to update deck\"}");
        }
    }
}
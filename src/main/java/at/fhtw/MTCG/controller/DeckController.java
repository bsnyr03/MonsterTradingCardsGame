package at.fhtw.MTCG.controller;

import at.fhtw.MTCG.model.Card;
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
import java.util.Map;

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

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(
                Map.of(
                        "userId", deck.getUserId(),
                        "cards", deck.getCards()
                )
        );

        return new Response(HttpStatus.OK, ContentType.JSON, jsonResponse);
    }

    private Response handlePutRequest(Request request) throws SQLException, JsonProcessingException {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"error\": \"Missing or invalid Authorization header\"}");
        }
        token = token.replace("Bearer ", "");

        int userId = deckService.getUserIdFromToken(token);
        if (userId <= 0) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"error\": \"Invalid user token\"}");
        }

        List<Integer> cardIds;
        try {
            cardIds = new ObjectMapper().readValue(request.getBody(), new TypeReference<>() {});
            if (cardIds == null || cardIds.size() != 4) {
                return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"You must provide exactly 4 card IDs\"}");
            }
        } catch (JsonProcessingException e) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Invalid request body format\"}");
        }

        List<Card> cards = deckService.getCardsByIds(cardIds, userId);
        if (cards.size() != 4) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Some cards are invalid or do not belong to the user\"}");
        }

        Deck existingDeck = null;
        try {
            existingDeck = deckService.getDeckByUserId(userId);
        } catch (IllegalArgumentException e) {
            System.out.println("No deck found for user. Creating a new one.");
        }

        if (existingDeck == null) {
            Deck newDeck = new Deck(userId, cards);
            boolean isCreated = deckService.createDeck(userId, newDeck);
            if (!isCreated) {
                return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"error\": \"Failed to create deck\"}");
            }
            return new Response(HttpStatus.CREATED, ContentType.JSON, "{\"message\": \"Deck created successfully\"}");
        } else {
            existingDeck.setCards(cards);
            boolean isUpdated = deckService.updateDeck(userId, existingDeck);
            if (!isUpdated) {
                return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"error\": \"Failed to update deck\"}");
            }
            return new Response(HttpStatus.OK, ContentType.JSON, "{\"message\": \"Deck updated successfully\"}");
        }
    }
}
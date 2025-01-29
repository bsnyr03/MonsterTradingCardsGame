package at.fhtw.MTCG.controller;

import at.fhtw.MTCG.model.Card;
import at.fhtw.MTCG.service.CardService;
import at.fhtw.MTCG.service.UserService;
import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

public class CardController implements RestController {
    private final CardService cardService;
    private final UserService userService;

    public CardController() {
        this.cardService = new CardService();
        this.userService = new UserService();
    }

    @Override
    public Response handleRequest(Request request) {
        try {
            if (request.getMethod() == Method.GET) {
                return handleGetRequest(request);
            } else if (request.getMethod() == Method.POST) {
                return handlePostRequest(request);
            } else if (request.getMethod() == Method.DELETE) {
                return handleDeleteRequest(request);
            }
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Invalid request method\"}");
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private Response handleGetRequest(Request request) throws SQLException, JsonProcessingException {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"error\": \"Missing or invalid Authorization header\"}");
        }
        token = token.replace("Bearer ", "");

        int userId = userService.getUserIdFromToken(token);
        if (userId <= 0) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"error\": \"Invalid user token\"}");
        }

        Collection<Card> userCards = cardService.getCardsByToken(token);

        if (userCards.isEmpty()) {
            return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{\"error\": \"List is empty\"}");
        }

        String json = new ObjectMapper().writeValueAsString(Map.of("userId", userId, "cards", userCards));
        return new Response(HttpStatus.OK, ContentType.JSON, json);
    }

    private Response handlePostRequest(Request request) throws JsonProcessingException, SQLException {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"error\": \"Missing or invalid Authorization header\"}");
        }
        token = token.replace("Bearer ", "");

        int userId = userService.getUserIdFromToken(token);
        if (userId <= 0) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"error\": \"Invalid user token\"}");
        }

        if (request.getPathParts().size() > 1 && "assign".equals(request.getPathParts().get(1))) {
            Card card = new ObjectMapper().readValue(request.getBody(), Card.class);
            if (cardService.assignCardToUser(String.valueOf(card.getId()), token)) {
                return new Response(HttpStatus.CREATED, ContentType.JSON, "{\"message\": \"Card successfully assigned to user\"}");
            } else {
                return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Failed to assign card to user\"}");
            }
        }

        Card card = new ObjectMapper().readValue(request.getBody(), Card.class);
        if (cardService.createCard(card)) {
            return new Response(HttpStatus.CREATED, ContentType.JSON, "{\"message\": \"Card created successfully\"}");
        } else {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Failed to create card\"}");
        }
    }

    private Response handleDeleteRequest(Request request) throws SQLException {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"error\": \"Missing or invalid Authorization header\"}");
        }
        token = token.replace("Bearer ", "");

        int userId = userService.getUserIdFromToken(token);
        if (userId <= 0) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"error\": \"Invalid user token\"}");
        }

        if (request.getPathParts().size() > 1) {
            int cardId = Integer.parseInt(request.getPathParts().get(1));

            if (cardService.deleteCard(cardId)) {
                return new Response(HttpStatus.OK, ContentType.JSON, "{\"message\": \"Card deleted successfully\"}");
            } else {
                return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{\"error\": \"Card not found\"}");
            }
        } else {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Card ID required for deletion\"}");
        }
    }
}
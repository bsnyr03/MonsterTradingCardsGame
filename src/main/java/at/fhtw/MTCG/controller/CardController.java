package at.fhtw.MTCG.controller;

import at.fhtw.MTCG.model.User;
import at.fhtw.MTCG.service.CardService;
import at.fhtw.MTCG.service.UserService;
import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.RestController;
import at.fhtw.MTCG.model.Card;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.Collection;

public class CardController implements RestController {
    private final CardService cardService;
    private final UserService userService;

    public CardController() {
        this.cardService = new CardService();
        this.userService = new UserService();
    }


    @Override
    public Response handleRequest(Request request) {
        try{
            if(request.getMethod() == Method.GET){
                return handleGetRequest(request);
            }else if (request.getMethod() == Method.POST){
                return handlePostRequest(request);
            }else if(request.getMethod() == Method.DELETE){
                return handleDeleteRequest(request);
            }
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Invalid request method\"}");
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private Response handleGetRequest(Request request) throws SQLException, JsonProcessingException {
        try{
            validateToken(request);

            if (request.getPathParts().size() > 1) {
            int cardId = Integer.parseInt(request.getPathParts().get(1));
            Card card = cardService.getCardById(cardId);

            if (card != null) {
                String json = new ObjectMapper().writeValueAsString(card);
                return new Response(HttpStatus.OK, ContentType.JSON, json);
            } else {
                return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{\"error\": \"Card not found\"}");
            }
        } else {
            Collection<Card> cards = cardService.getAllCards();
            String json = new ObjectMapper().writeValueAsString(cards);
            return new Response(HttpStatus.OK, ContentType.JSON, json);
        }
        }catch (IllegalArgumentException e){
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private Response handlePostRequest(Request request) throws JsonProcessingException, SQLException {
        try{
            validateToken(request);
        Card card = new ObjectMapper().readValue(request.getBody(), Card.class);

        if (cardService.createCard(card)) {
            return new Response(HttpStatus.CREATED, ContentType.JSON, "{\"message\": \"Card created successfully\"}");
        } else {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Failed to create card\"}");
        }
        }catch(IllegalArgumentException e){
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private Response handleDeleteRequest(Request request) throws SQLException {
        try{
            validateToken(request);
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
        }catch(IllegalArgumentException e){
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void validateToken(Request request) throws IllegalArgumentException, SQLException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.substring(7); // "Bearer " abschneiden

        boolean isValid = userService.validateToken(token);

        if (!isValid) {
            throw new IllegalArgumentException("Invalid or expired token");
        }
    }
}

package at.fhtw.MTCG.controller;

import at.fhtw.MTCG.persistence.UnitOfWork;
import at.fhtw.MTCG.persistence.repository.CardRepository;
import at.fhtw.MTCG.persistence.repository.CardRepositoryImpl;
import at.fhtw.MTCG.service.CardService;
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

    public CardController() {
        this.cardService = new CardService();
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
    }

    private Response handlePostRequest(Request request) throws JsonProcessingException, SQLException {
        Card card = new ObjectMapper().readValue(request.getBody(), Card.class);

        if (cardService.createCard(card)) {
            return new Response(HttpStatus.CREATED, ContentType.JSON, "{\"message\": \"Card created successfully\"}");
        } else {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Failed to create card\"}");
        }
    }

    private Response handleDeleteRequest(Request request) throws SQLException {
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

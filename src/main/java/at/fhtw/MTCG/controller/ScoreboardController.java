package at.fhtw.MTCG.controller;

import at.fhtw.MTCG.model.User;
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
import java.util.List;

public class ScoreboardController implements RestController {
    private final UserService userService;

    public ScoreboardController() {
        this.userService = new UserService();
    }

    @Override
    public Response handleRequest(Request request) {
        try {
            if (request.getMethod() == Method.GET && request.getPathname().equals("/scoreboard")) {
                return getScoreboard();
            }

            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Invalid request method\"}");
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private Response getScoreboard() throws SQLException, JsonProcessingException {
        List<User> users = userService.getScoreboard();
        String jsonResponse = new ObjectMapper().writeValueAsString(users);
        return new Response(HttpStatus.OK, ContentType.JSON, jsonResponse);
    }
}
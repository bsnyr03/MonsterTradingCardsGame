package at.fhtw.MTCG.controller;

import at.fhtw.MTCG.model.User;
import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.RestController;
import at.fhtw.MTCG.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;


public class UserController implements RestController{
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Response handleRequest(Request request) {
        try {
            // Login-Endpunkt: POST /users/login
            if (request.getMethod() == Method.POST && request.getPathParts().size() > 1 && "login".equals(request.getPathParts().get(1))) {
                return handleLogin(request);
            }
            // Registrierung-Endpunkt: POST /users
            else if (request.getMethod() == Method.POST) {
                return handleRegister(request);
            }

            // Ungültige Anfrage
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.JSON,
                    "{\"error\": \"Invalid request\"}"
            );

        } catch (Exception e) {
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{\"error\": \"" + e.getMessage() + "\"}"
            );
        }
    }

    private Response handleRegister(Request request) {
        try {
            User user = new ObjectMapper().readValue(request.getBody(), User.class);

            userService.registerUser(user);

            return new Response(
                    HttpStatus.CREATED,
                    ContentType.JSON,
                    "{\"message\": \"User registered successfully\"}"
            );

        } catch (IllegalStateException e) {
            return new Response(
                    HttpStatus.CONFLICT,
                    ContentType.JSON,
                    "{\"error\": \"" + e.getMessage() + "\"}"
            );

        } catch (SQLException | RuntimeException e) {
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{\"error\": \"" + e.getMessage() + "\"}"
            );
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Response handleLogin(Request request) {
        try {
            User user = new ObjectMapper().readValue(request.getBody(), User.class);

            String token = userService.loginUser(user.getName(), user.getPassword());

            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    "{\"token\": \"" + token + "\"}"
            );

        } catch (IllegalArgumentException e) {
            return new Response(
                    HttpStatus.UNAUTHORIZED,
                    ContentType.JSON,
                    "{\"error\": \"" + e.getMessage() + "\"}"
            );

        } catch (SQLException | RuntimeException e) {
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{\"error\": \"" + e.getMessage() + "\"}"
            );
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

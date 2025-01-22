package at.fhtw.MTCG.controller;

import at.fhtw.MTCG.model.User;
import at.fhtw.MTCG.persistence.repository.UserRepository;
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
import java.util.Collection;
import java.util.Map;


public class UserController implements RestController {
    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    @Override
    public Response handleRequest(Request request) {
        try {
            if (request.getMethod() == Method.POST) {
                return handleUserRequestPOST(request);
            } else if (request.getMethod() == Method.GET) {
                return handleUserRequestGET(request);
            }

            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Invalid request method\"}");

        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private Response handleUserRequestPOST(Request request) {
        try {
            User user = new ObjectMapper().readValue(request.getBody(), User.class);

            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                return new Response(
                        HttpStatus.BAD_REQUEST,
                        ContentType.JSON,
                        "{\"error\": \"Username cannot be empty\"}"
                );
            }

            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                return new Response(
                        HttpStatus.BAD_REQUEST,
                        ContentType.JSON,
                        "{\"error\": \"Password cannot be empty\"}"
                );
            }

            try {
                User existingUser = userService.findUserByUsername(user.getUsername());

                if (existingUser != null) {
                    String token = userService.loginUser(user.getUsername(), user.getPassword());
                    existingUser.setToken(token);

                    // Log für Terminal
                    System.out.println("User logged in: " + existingUser.getUsername() + " (Token: " + existingUser.getToken() + ")");

                    String jsonResponse = new ObjectMapper().writeValueAsString(
                            Map.of(
                                    "message", "User successfully logged in",
                                    "username", existingUser.getUsername(),
                                    "token", existingUser.getToken()
                            )
                    );
                    return new Response(
                            HttpStatus.OK,
                            ContentType.JSON,
                            jsonResponse
                    );
                }

            } catch (IllegalArgumentException e) {
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        "{\"success\": \"Invalid username or password\"}"
                );
            }

            userService.registerUser(user);

            // Log für Terminal
            System.out.println("User registered successfully: " + user.getUsername());

            return new Response(HttpStatus.CREATED, ContentType.JSON, "{\"message\": \"User registered successfully.\"}");

        } catch (IllegalArgumentException e) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        } catch (SQLException e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        } catch (JsonProcessingException e) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private Response handleUserRequestGET(Request request) {
        try {


            Collection<User> users = userService.findAllUsers();
            String json = new ObjectMapper().writeValueAsString(users);

            return new Response(HttpStatus.OK, ContentType.JSON, json);
        } catch (IllegalArgumentException e) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        } catch (SQLException e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        } catch (JsonProcessingException e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"error\": \"Error serializing data to JSON\"}");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateToken(Request request) throws IllegalArgumentException, SQLException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.substring(7);

        // Überprüfe den Token in der Datenbank
        boolean isValid = userService.validateToken(token);

        if (!isValid) {
            throw new IllegalArgumentException("Invalid or expired token");
        }
    }
}
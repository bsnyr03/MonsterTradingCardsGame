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
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
                    return new Response(
                            HttpStatus.CONFLICT,
                            ContentType.JSON,
                            "{\"error\": \"Username already exists\"}"
                    );
                }
            } catch (IllegalArgumentException ignored) {
            }

            userService.registerUser(user);

            System.out.println("User registered successfully: " + user.getUsername());

            return new Response(
                    HttpStatus.CREATED,
                    ContentType.JSON,
                    "{\"message\": \"User registered successfully.\"}"
            );

        } catch (JsonProcessingException e) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Invalid JSON format\"}");
        } catch (SQLException e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private Response handleUserRequestGET(Request request) {
        try {
            Collection<User> users = userService.findAllUsers();

            @NotNull List<Map<@NotNull String, ? extends Serializable>> filteredUsers = users.stream()
                    .map(user -> Map.of(
                            "id", user.getId(),
                            "username", user.getUsername(),
                            "password", user.getPassword(),
                            "token", user.getToken()
                    ))
                    .collect(Collectors.toList());

            String json = new ObjectMapper().writeValueAsString(filteredUsers);

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
}
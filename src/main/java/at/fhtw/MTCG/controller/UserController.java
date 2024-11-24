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


public class UserController implements RestController{
    private final UserService userService;


    public UserController() {
        this.userService = new UserService();
    }

    @Override
    public Response handleRequest(Request request) {
        try {
            if (request.getMethod() == Method.POST) {
                return handleUserRequestPOST(request);
            }else if (request.getMethod() == Method.GET) {
                return handleUserRequestGET(request);
            }

            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.JSON,
                    "{\"error\": \"Invalid request method\"}"
            );

        } catch (Exception e) {
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{\"error\": \"" + e.getMessage() + "\"}"
            );
        }
    }

    private Response handleUserRequestPOST(Request request) {
        try {
            User user = new ObjectMapper().readValue(request.getBody(), User.class);

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                userService.registerUser(user);
                return new Response(
                        HttpStatus.CREATED,
                        ContentType.JSON,
                        "{\"message\": \"User registered successfully\"}"
                );
            }

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
        } catch (SQLException e) {
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
        private Response handleUserRequestGET(Request request){
            try {
                //Collection<User> users = ;

                String json = new ObjectMapper().writeValueAsString(users);

                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        json
                );
            } catch (SQLException e) {
                return new Response(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ContentType.JSON,
                        "{\"error\": \"" + e.getMessage() + "\"}"
                );
            } catch (JsonProcessingException e) {
                return new Response(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ContentType.JSON,
                        "{\"error\": \"Error serializing data to JSON\"}"
                );
            }
        }
}

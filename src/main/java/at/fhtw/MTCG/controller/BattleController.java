package at.fhtw.MTCG.controller;

import at.fhtw.MTCG.model.Battle;
import at.fhtw.MTCG.service.UserService;
import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class BattleController implements RestController {
    private final UserService userService;

    public BattleController() {
        this.userService = new UserService();
    }

    @Override
    public Response handleRequest(Request request) {
        try {
            if (request.getMethod() == Method.POST) {
                return handleBattleRequest(request);
            }

            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Invalid request method\"}");
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private Response handleBattleRequest(Request request) {
        try {
            // Spieler-Token aus dem Header extrahieren
            String token1 = request.getHeader("Authorization");
            if (token1 == null || !token1.startsWith("Bearer ")) {
                return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"error\": \"Missing or invalid Authorization header\"}");
            }
            token1 = token1.replace("Bearer ", "");

            // Spieler 2 ID aus dem Request-Body extrahieren
            Map<String, Object> requestBody = new ObjectMapper().readValue(request.getBody(), Map.class);
            if (!requestBody.containsKey("opponentId")) {
                return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Opponent ID is required\"}");
            }

            int player2Id = (int) requestBody.get("opponentId");

            // Spieler-IDs abrufen
            int player1Id = userService.getUserIdFromToken(token1);
            if (player1Id <= 0 || player2Id <= 0) {
                return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"error\": \"Invalid player IDs\"}");
            }

            // Battle-Instanz erstellen und starten
            Battle battle = new Battle(player1Id, player2Id);
            String battleResult = battle.startBattle();

            // Battle-Log zurückgeben
            String jsonResponse = new ObjectMapper().writeValueAsString(Map.of(
                    "message", "Battle completed successfully",
                    "result", battleResult,
                    "log", battle.getBattleLog()
            ));
            return new Response(HttpStatus.OK, ContentType.JSON, jsonResponse);

        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
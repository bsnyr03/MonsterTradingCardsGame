package at.fhtw;

import at.fhtw.MTCG.controller.*;
import at.fhtw.httpserver.server.Server;
import at.fhtw.httpserver.utils.Router;


import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(10001, configureRouter());
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Router configureRouter()
    {
        Router router = new Router();
        router.addService("/users", new UserController());
        router.addService("/cards", new CardController());
        router.addService("/packages", new PackageController());
        router.addService("/transactions", new TransactionsController());
        router.addService("/sessions", new SessionsController());
        router.addService("/decks", new DeckController());
        router.addService("/battles", new BattleController());
        router.addService("/scoreboard", new ScoreboardController());
        router.addService("/stats", new StatsController());
        return router;
    }
}

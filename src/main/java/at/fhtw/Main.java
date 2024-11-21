package at.fhtw;

import at.fhtw.MTCG.controller.UserController;
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
        return router;
    }
}

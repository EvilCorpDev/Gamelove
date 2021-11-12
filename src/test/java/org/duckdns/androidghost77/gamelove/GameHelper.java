package org.duckdns.androidghost77.gamelove;

import org.duckdns.androidghost77.gamelove.repository.model.Game;

import java.util.UUID;

public class GameHelper {

    public static String DEFAULT_GAME_ID = UUID.randomUUID().toString();
    public static Game DEFAULT_GAME = createGame(DEFAULT_GAME_ID, "Dark Souls", "From Software");

    public static Game createGame(String id, String name, String company) {
        Game game = new Game();
        game.setId(id);
        game.setName(name);
        game.setCompany(company);

        return game;
    }

}

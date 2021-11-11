package org.duckdns.androidghost77.gamelove.rest;

import lombok.RequiredArgsConstructor;
import org.duckdns.androidghost77.gamelove.dto.GameRequest;
import org.duckdns.androidghost77.gamelove.dto.GameResponse;
import org.duckdns.androidghost77.gamelove.service.GameService;
import org.duckdns.androidghost77.gamelove.validation.annotation.ValidUuid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponse addNewGame(@RequestBody GameRequest newGame) {
        return gameService.addGame(newGame);
    }

    @GetMapping("/{gameId}")
    public GameResponse findGameById(@PathVariable("gameId") @ValidUuid String gameId) {
        return gameService.findGameById(gameId);
    }

    @GetMapping
    public List<GameResponse> findGamesByName(@RequestParam("name") String name) {
        return gameService.findGamesByName(name);
    }

    @DeleteMapping("/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGameById(@PathVariable("gameId") @ValidUuid String gameId) {
        gameService.deleteGame(gameId);
    }
}

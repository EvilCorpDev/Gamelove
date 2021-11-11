package org.duckdns.androidghost77.gamelove.service.impl;

import lombok.RequiredArgsConstructor;
import org.duckdns.androidghost77.gamelove.dto.GameRequest;
import org.duckdns.androidghost77.gamelove.dto.GameResponse;
import org.duckdns.androidghost77.gamelove.exception.GameNotFoundException;
import org.duckdns.androidghost77.gamelove.mapper.GameMapper;
import org.duckdns.androidghost77.gamelove.repository.GameRepository;
import org.duckdns.androidghost77.gamelove.repository.model.Game;
import org.duckdns.androidghost77.gamelove.service.GameService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @Override
    public GameResponse addGame(GameRequest gameRequest) {
        Game savedGame = gameRepository.save(gameMapper.gameRequestToGame(gameRequest));
        return gameMapper.gameToGameResponse(savedGame);
    }

    @Override
    public GameResponse findGameById(String id) {
        return gameRepository.findById(id)
                .map(gameMapper::gameToGameResponse)
                .orElseThrow(() -> new GameNotFoundException(String.format("Game with id %s was not found", id)));

    }

    @Override
    public List<GameResponse> findGamesByName(String name) {
        return gameRepository.findAllByName(String.format("%%%s%%", name))
                .stream()
                .map(gameMapper::gameToGameResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteGame(String id) {
        gameRepository.deleteById(id);
    }
}

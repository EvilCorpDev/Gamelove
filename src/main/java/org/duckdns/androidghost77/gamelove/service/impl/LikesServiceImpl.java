package org.duckdns.androidghost77.gamelove.service.impl;

import lombok.RequiredArgsConstructor;
import org.duckdns.androidghost77.gamelove.dto.GameResponse;
import org.duckdns.androidghost77.gamelove.exception.GameNotFoundException;
import org.duckdns.androidghost77.gamelove.exception.LikeNotFoundException;
import org.duckdns.androidghost77.gamelove.exception.UserNotFoundException;
import org.duckdns.androidghost77.gamelove.mapper.GameMapper;
import org.duckdns.androidghost77.gamelove.repository.GameRepository;
import org.duckdns.androidghost77.gamelove.repository.LikesRepository;
import org.duckdns.androidghost77.gamelove.repository.UserRepository;
import org.duckdns.androidghost77.gamelove.repository.model.Game;
import org.duckdns.androidghost77.gamelove.repository.model.Likes;
import org.duckdns.androidghost77.gamelove.repository.model.User;
import org.duckdns.androidghost77.gamelove.service.LikesService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

    private final LikesRepository likesRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameMapper gameMapper;

    @Override
    public void likeGame(String gameId, String userId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(String.format("Game with id %s was not found", gameId)));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %s was not found", userId),
                        userId));

        Likes like = new Likes();
        like.setGame(game);
        like.setUser(user);

        likesRepository.save(like);
    }

    @Override
    public void unlikeGame(String gameId, String userId) {
        Likes like = likesRepository.findLikeByGameAndUser(gameId, userId)
                .orElseThrow(() -> new LikeNotFoundException(
                        String.format("Game %s wasn't liked by user %s", gameId, userId)));
        likesRepository.delete(like);
    }

    @Override
    public List<GameResponse> getGamesLikedByUser(String userId) {
        List<String> gameIds = likesRepository.getUserLikes(userId)
                .stream()
                .map(it -> it.getGame().getId())
                .collect(Collectors.toList());

        if(!gameIds.isEmpty()) {
            return gameRepository.findAllById(gameIds)
                    .stream()
                    .map(gameMapper::gameToGameResponse)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

}

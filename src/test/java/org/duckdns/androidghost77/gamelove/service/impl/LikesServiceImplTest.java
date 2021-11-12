package org.duckdns.androidghost77.gamelove.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.duckdns.androidghost77.gamelove.GameHelper.DEFAULT_GAME;
import static org.duckdns.androidghost77.gamelove.GameHelper.createGame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.duckdns.androidghost77.gamelove.dto.GameResponse;
import org.duckdns.androidghost77.gamelove.exception.GameNotFoundException;
import org.duckdns.androidghost77.gamelove.exception.UserNotFoundException;
import org.duckdns.androidghost77.gamelove.mapper.GameMapper;
import org.duckdns.androidghost77.gamelove.repository.GameRepository;
import org.duckdns.androidghost77.gamelove.repository.LikesRepository;
import org.duckdns.androidghost77.gamelove.repository.UserRepository;
import org.duckdns.androidghost77.gamelove.repository.model.Game;
import org.duckdns.androidghost77.gamelove.repository.model.GameLikes;
import org.duckdns.androidghost77.gamelove.repository.model.Likes;
import org.duckdns.androidghost77.gamelove.repository.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class LikesServiceImplTest {

    @Mock
    private LikesRepository likesRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GameMapper gameMapper;

    @Captor
    private ArgumentCaptor<Likes> likesCaptor;

    @InjectMocks
    private LikesServiceImpl likesService;

    @Test
    void gameNotFoundWhenLikeGame() {
        //given
        String gameId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        String excMessage = String.format("Game with id %s was not found", gameId);

        //when
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> likesService.likeGame(gameId, userId))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage(excMessage);
    }

    @Test
    void userNotFoundWhenLikeGame() {
        //given
        String gameId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        String excMessage = String.format("User with id %s was not found", userId);

        //when
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(DEFAULT_GAME));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> likesService.likeGame(gameId, userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(excMessage);
    }

    @Test
    void successfullyLikeGame() {
        //given
        String gameId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        User user = new User();
        user.setId(userId);
        user.setPasswordHash(UUID.randomUUID().toString());
        user.setEmail("user@gmail.com");
        user.setUsername("coolUser");

        //when
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(DEFAULT_GAME));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //then
        likesService.likeGame(gameId, userId);
        verify(likesRepository).save(likesCaptor.capture());
        Likes capturedValue = likesCaptor.getValue();
        assertThat(capturedValue.getUser()).isEqualTo(user);
        assertThat(capturedValue.getGame()).isEqualTo(DEFAULT_GAME);
    }

    @Test
    void unlikeGameWhenGameNotFound() {
        //given
        String gameId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        //when
        when(likesRepository.findLikeByGameAndUser(gameId, userId)).thenReturn(Optional.empty());

        //then
        likesService.unlikeGame(gameId, userId);
        verify(likesRepository).findLikeByGameAndUser(gameId, userId);
    }

    @Test
    void unlikeGameWhenGameIsFound() {
        //given
        String id = UUID.randomUUID().toString();
        String gameId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        User user = new User();
        user.setId(userId);
        user.setPasswordHash(UUID.randomUUID().toString());
        user.setEmail("user@gmail.com");
        user.setUsername("coolUser");

        Likes like = createLike(DEFAULT_GAME, user, id);

        //when
        when(likesRepository.findLikeByGameAndUser(gameId, userId)).thenReturn(Optional.of(like));

        //then
        likesService.unlikeGame(gameId, userId);
        verify(likesRepository).findLikeByGameAndUser(gameId, userId);
        verify(likesRepository).delete(like);
    }

    @Test
    void getGamesLikedByUser_NoGames() {
        //given
        String userId = UUID.randomUUID().toString();

        //when
        when(likesRepository.getUserLikes(userId)).thenReturn(Collections.emptyList());

        //then
        List<GameResponse> actualResult = likesService.getGamesLikedByUser(userId);
        assertThat(actualResult).isEmpty();
        verifyNoInteractions(gameRepository);
    }

    @Test
    void getGamesLikedByUser_GamesFound() {
        //given
        String userId = UUID.randomUUID().toString();
        String firstLikeId = UUID.randomUUID().toString();
        String secondLikeId = UUID.randomUUID().toString();
        String name = "Metro 2033";

        String firstId = UUID.randomUUID().toString();
        String secondId = UUID.randomUUID().toString();
        String secondName = "Metro 2033 Last Light";
        String company = "4A Games";

        Game firstGame = createGame(firstId, name, company);
        Game secondGame = createGame(secondId, secondName, company);

        GameResponse firstResponse = new GameResponse(firstId, name, company);
        GameResponse secondResponse = new GameResponse(secondId, secondName, company);

        User user = new User();
        user.setId(userId);
        user.setPasswordHash(UUID.randomUUID().toString());
        user.setEmail("user@gmail.com");
        user.setUsername("coolUser");

        Likes firstLike = createLike(firstGame, user, firstLikeId);
        Likes secondLike = createLike(secondGame, user, secondLikeId);

        //when
        when(likesRepository.getUserLikes(userId)).thenReturn(Arrays.asList(firstLike, secondLike));
        when(gameRepository.findAllById(Arrays.asList(firstId, secondId)))
                .thenReturn(Arrays.asList(firstGame, secondGame));
        when(gameMapper.gameToGameResponse(firstGame)).thenReturn(firstResponse);
        when(gameMapper.gameToGameResponse(secondGame)).thenReturn(secondResponse);

        //then
        List<GameResponse> actualResult = likesService.getGamesLikedByUser(userId);
        assertThat(actualResult).isEqualTo(Arrays.asList(firstResponse, secondResponse));
    }

    @Test
    void getMostLikedGames() {
        //given
        int maxSize = 2;
        String firstId = UUID.randomUUID().toString();
        String secondId = UUID.randomUUID().toString();

        GameLikes firstGameLike = new GameLikes(firstId, 5);
        GameLikes secondGameLike = new GameLikes(secondId, 2);

        //when
        when(likesRepository.getMostLikedGames(PageRequest.of(0, maxSize)))
                .thenReturn(new PageImpl<>(Arrays.asList(firstGameLike, secondGameLike)));

        //then
        List<GameLikes> actualResponse = likesService.getMostLikedGames(maxSize);
        assertThat(actualResponse).isEqualTo(Arrays.asList(firstGameLike, secondGameLike));
    }

    private Likes createLike(Game game, User user, String id) {
        Likes like = new Likes();
        like.setGame(game);
        like.setUser(user);
        like.setId(id);

        return like;
    }
}

package org.duckdns.androidghost77.gamelove.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.duckdns.androidghost77.gamelove.GameHelper.createGame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.duckdns.androidghost77.gamelove.dto.GameRequest;
import org.duckdns.androidghost77.gamelove.dto.GameResponse;
import org.duckdns.androidghost77.gamelove.exception.GameNotFoundException;
import org.duckdns.androidghost77.gamelove.mapper.GameMapper;
import org.duckdns.androidghost77.gamelove.repository.GameRepository;
import org.duckdns.androidghost77.gamelove.repository.model.Game;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private GameMapper gameMapper;

    @Captor
    private ArgumentCaptor<Game> gameCaptor;
    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    void successfullyAddGame() {
        //given
        String id = UUID.randomUUID().toString();
        String company = "From Software";
        String name = "Dark Souls";
        GameRequest input = new GameRequest(name, company);

        Game game = createGame(id, name, company);

        GameResponse expectedResult = new GameResponse(id, name, company);

        //when
        when(gameMapper.gameRequestToGame(input)).thenReturn(game);
        when(gameRepository.save(game)).thenReturn(game);
        when(gameMapper.gameToGameResponse(game)).thenReturn(expectedResult);

        //then
        GameResponse actualResponse = gameService.addGame(input);

        verify(gameRepository).save(gameCaptor.capture());
        assertThat(gameCaptor.getValue()).isEqualTo(game);
        assertThat(actualResponse).isEqualTo(expectedResult);
    }

    @Test
    void gameNotFoundById() {
        //given
        String id = UUID.randomUUID().toString();
        String excMessage = String.format("Game with id %s was not found", id);

        //when
        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> gameService.findGameById(id))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage(excMessage);
    }

    @Test
    void successfullyFindGameById() {
        //given
        String id = UUID.randomUUID().toString();
        String name = "Assassin's Creed";
        String company = "Ubisoft";

        Game game = createGame(id, name, company);

        GameResponse expectedResult = new GameResponse(id, name, company);

        //when
        when(gameRepository.findById(id)).thenReturn(Optional.of(game));
        when((gameMapper.gameToGameResponse(game))).thenReturn(expectedResult);

        //then
        GameResponse actualResult = gameService.findGameById(id);
        verify(gameMapper).gameToGameResponse(gameCaptor.capture());
        assertThat(gameCaptor.getValue()).isEqualTo(game);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void gameNotFoundByName() {
        //given
        String name = "Mario Brothers";
        String query = String.format("%%%s%%", name);

        //when
        when(gameRepository.findAllByName(query)).thenReturn(Collections.emptyList());

        //then
        List<GameResponse> actualResult = gameService.findGamesByName(name);
        assertThat(actualResult).isEmpty();
    }

    @Test
    void foundGamesByName() {
        //given
        String name = "Metro 2033";
        String query = String.format("%%%s%%", name);

        String firstId = UUID.randomUUID().toString();
        String secondId = UUID.randomUUID().toString();
        String secondName = "Metro 2033 Last Light";
        String company = "4A Games";

        Game firstGame = createGame(firstId, name, company);
        Game secondGame = createGame(secondId, secondName, company);

        GameResponse firstResponse = new GameResponse(firstId, name, company);
        GameResponse secondResponse = new GameResponse(secondId, secondName, company);

        //when
        when(gameRepository.findAllByName(query)).thenReturn(Arrays.asList(firstGame, secondGame));
        when(gameMapper.gameToGameResponse(firstGame)).thenReturn(firstResponse);
        when(gameMapper.gameToGameResponse(secondGame)).thenReturn(secondResponse);

        //then
        List<GameResponse> actualResponse = gameService.findGamesByName(name);
        assertThat(actualResponse).isEqualTo(Arrays.asList(firstResponse, secondResponse));
    }

    @Test
    void deleteGameById() {
        //given
        String id = UUID.randomUUID().toString();

        //then
        gameService.deleteGame(id);
        verify(gameRepository).deleteById(stringCaptor.capture());
        assertThat(stringCaptor.getValue()).isEqualTo(id);
    }
}

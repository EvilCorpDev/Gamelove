package org.duckdns.androidghost77.gamelove.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.duckdns.androidghost77.gamelove.GameHelper.DEFAULT_GAME;
import static org.duckdns.androidghost77.gamelove.GameHelper.DEFAULT_GAME_ID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.duckdns.androidghost77.gamelove.GameloveApplication;
import org.duckdns.androidghost77.gamelove.dto.GameRequest;
import org.duckdns.androidghost77.gamelove.dto.GameResponse;
import org.duckdns.androidghost77.gamelove.exception.handler.dto.ExceptionMessageDto;
import org.duckdns.androidghost77.gamelove.repository.GameRepository;
import org.duckdns.androidghost77.gamelove.repository.model.Game;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GameloveApplication.class)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class GameControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GameRepository gameRepository;

    @Captor
    private ArgumentCaptor<Game> gameCaptor;

    @BeforeAll
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser(value = "test", password = "pass", authorities = "ADMIN")
    void addNewGame() throws Exception {
        //given
        GameRequest gameRequest = new GameRequest("Counter Strike 1.6", "Valve");

        //then
        mvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameRequest)))
                .andExpect(status().isCreated());

        verify(gameRepository).save(gameCaptor.capture());
        Game capturedGame = gameCaptor.getValue();
        assertThat(capturedGame.getCompany()).isEqualTo(gameRequest.getCompany());
        assertThat(capturedGame.getName()).isEqualTo(gameRequest.getName());
    }

    @Test
    @WithMockUser(value = "test", password = "pass", authorities = "USER")
    void findGameById() throws Exception {
        //given
        GameResponse expectedResponse = new GameResponse(DEFAULT_GAME_ID, DEFAULT_GAME.getName(),
                DEFAULT_GAME.getCompany());

        //when
        when(gameRepository.findById(DEFAULT_GAME_ID)).thenReturn(Optional.of(DEFAULT_GAME));

        //then
        MvcResult actualResult = mvc.perform(get("/games/{gameId}", DEFAULT_GAME_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = actualResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(expectedResponse));
    }

    @Test
    @WithMockUser(value = "test", password = "pass", authorities = "USER")
    void findGamesByName() throws Exception {
        //given
        GameResponse expectedResponse = new GameResponse(DEFAULT_GAME_ID, DEFAULT_GAME.getName(),
                DEFAULT_GAME.getCompany());

        //when
        when(gameRepository.findAllByName((String.format("%%%s%%", DEFAULT_GAME.getName()))))
                .thenReturn(List.of(DEFAULT_GAME));

        //then
        MvcResult actualResult = mvc.perform(get("/games")
                        .param("name", DEFAULT_GAME.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = actualResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(List.of(expectedResponse)));
    }

    @Test
    @WithMockUser(value = "test", password = "pass", authorities = "ADMIN")
    void deleteGameById() throws Exception {
        //given
        String gameId = UUID.randomUUID().toString();

        //when
        when(gameRepository.findAllByName((String.format("%%%s%%", DEFAULT_GAME.getName()))))
                .thenReturn(List.of(DEFAULT_GAME));

        //then
        mvc.perform(delete("/games/{gameId}", gameId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        verify(gameRepository).deleteById(gameId);
    }

    @Test
    @WithMockUser(value = "test", password = "pass", authorities = "USER")
    void deleteGameById_Forbidden() throws Exception {
        //given
        String gameId = UUID.randomUUID().toString();
        String shortMessage = "You are not allowed to access this resource";
        String message = "Access is denied";

        //then
        MvcResult actualResult = mvc.perform(delete("/games/{gameId}", gameId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        ExceptionMessageDto response = objectMapper.readValue(actualResult.getResponse().getContentAsString(),
                ExceptionMessageDto.class);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getShortMessage()).isEqualTo(shortMessage);
        assertThat(response.getTimestamp()).isNotNull();

        verifyNoInteractions(gameRepository);
    }

    @Test
    @WithMockUser(value = "test", password = "pass", authorities = "USER")
    void addGame_Forbidden() throws Exception {
        //given
        String shortMessage = "You are not allowed to access this resource";
        String message = "Access is denied";
        GameRequest gameRequest = new GameRequest("Counter Strike 1.6", "Valve");

        //then
        MvcResult actualResult = mvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameRequest)))
                .andExpect(status().isForbidden())
                .andReturn();

        ExceptionMessageDto response = objectMapper.readValue(actualResult.getResponse().getContentAsString(),
                ExceptionMessageDto.class);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getShortMessage()).isEqualTo(shortMessage);
        assertThat(response.getTimestamp()).isNotNull();

        verifyNoInteractions(gameRepository);
    }

}

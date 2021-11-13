package org.duckdns.androidghost77.gamelove.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.duckdns.androidghost77.gamelove.GameHelper.DEFAULT_GAME;
import static org.duckdns.androidghost77.gamelove.GameHelper.DEFAULT_GAME_ID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.duckdns.androidghost77.gamelove.GameloveApplication;
import org.duckdns.androidghost77.gamelove.dto.GameResponse;
import org.duckdns.androidghost77.gamelove.repository.GameRepository;
import org.duckdns.androidghost77.gamelove.repository.LikesRepository;
import org.duckdns.androidghost77.gamelove.repository.UserRepository;
import org.duckdns.androidghost77.gamelove.repository.model.GameLikes;
import org.duckdns.androidghost77.gamelove.repository.model.Likes;
import org.duckdns.androidghost77.gamelove.repository.model.User;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
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
class LikesControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GameRepository gameRepository;
    @MockBean
    private LikesRepository likesRepository;
    @MockBean
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<Likes> likesCaptor;

    @BeforeAll
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        when(userRepository.count()).thenReturn(0L);
    }

    @Test
    @WithUserDetails("admin")
    void likeGame() throws Exception {
        //given
        String userId = "default_id";
        User user = new User();
        user.setId(userId);
        user.setPasswordHash(UUID.randomUUID().toString());
        user.setEmail("user@gmail.com");
        user.setUsername("coolUser");

        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findById(DEFAULT_GAME_ID)).thenReturn(Optional.of(DEFAULT_GAME));

        //then
        mvc.perform(post("/likes/{gameId}", DEFAULT_GAME_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(likesRepository).save(likesCaptor.capture());
        Likes captured = likesCaptor.getValue();
        assertThat(captured.getGame()).isEqualTo(DEFAULT_GAME);
        assertThat(captured.getUser()).isEqualTo(user);
    }

    @Test
    @WithUserDetails("admin")
    void unlikeGame() throws Exception {
        //given
        String userId = "default_id";
        Likes like = new Likes();
        like.setId(UUID.randomUUID().toString());

        //when
        when(likesRepository.findLikeByGameAndUser(DEFAULT_GAME_ID, userId))
                .thenReturn(Optional.of(like));

        //then
        mvc.perform(delete("/likes/{gameId}", DEFAULT_GAME_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(likesRepository).delete(like);
    }

    @Test
    @WithUserDetails("admin")
    void getMyLikes() throws Exception {
        //given
        String userId = "default_id";
        Likes like = new Likes();
        like.setId(UUID.randomUUID().toString());
        like.setGame(DEFAULT_GAME);
        GameResponse gameResponse = new GameResponse(DEFAULT_GAME_ID, DEFAULT_GAME.getName(),
                DEFAULT_GAME.getCompany());

        //when
        when(likesRepository.getUserLikes(userId)).thenReturn(List.of(like));
        when(gameRepository.findAllById(List.of(DEFAULT_GAME_ID))).thenReturn(List.of(DEFAULT_GAME));

        //then
        MvcResult actualResult = mvc.perform(get("/likes/my-games", DEFAULT_GAME_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(actualResult.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(List.of(gameResponse)));
    }

    @Test
    @WithMockUser(value = "test", password = "pass", authorities = "USER")
    void getMostLikedGames() throws Exception {
        //given
        GameLikes gameLikes = new GameLikes(DEFAULT_GAME_ID, 3);

        //when
        when(likesRepository.getMostLikedGames(PageRequest.of(0, 1)))
                .thenReturn(new PageImpl<>(List.of(gameLikes)));

        //then
        MvcResult actualResult = mvc.perform(get("/likes/popular")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(actualResult.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(List.of(gameLikes)));
    }
}

package org.duckdns.androidghost77.gamelove.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.duckdns.androidghost77.gamelove.enums.UserRoleType.ADMIN;
import static org.duckdns.androidghost77.gamelove.enums.UserRoleType.USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.duckdns.androidghost77.gamelove.GameloveApplication;
import org.duckdns.androidghost77.gamelove.dto.UserRequest;
import org.duckdns.androidghost77.gamelove.dto.UserResponse;
import org.duckdns.androidghost77.gamelove.exception.handler.dto.ExceptionMessageDto;
import org.duckdns.androidghost77.gamelove.repository.UserRepository;
import org.duckdns.androidghost77.gamelove.repository.UserRoleRepository;
import org.duckdns.androidghost77.gamelove.repository.model.User;
import org.duckdns.androidghost77.gamelove.repository.model.UserRole;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GameloveApplication.class)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserRoleRepository userRoleRepository;

    @Captor
    private ArgumentCaptor<User> userCaptor;
    @Captor
    private ArgumentCaptor<UserRole> userRoleCaptor;

    @BeforeAll
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser(value = "test", password = "pass", authorities = "ADMIN")
    void createAdmin() throws Exception {
        //given
        UserRequest userRequest = new UserRequest();
        userRequest.setPassword(UUID.randomUUID().toString());
        userRequest.setEmail("user@gmail.com");
        userRequest.setUsername("coolUser");
        userRequest.setRoleType(ADMIN.toString());

        User user = new User();
        user.setId(UUID.randomUUID().toString());

        //when
        when(userRepository.save(any())).thenReturn(user);

        //then
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated());

        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getEmail()).isEqualTo(userRequest.getEmail());
        assertThat(capturedUser.getUsername()).isEqualTo(userRequest.getUsername());
        verify(userRoleRepository).save(userRoleCaptor.capture());
        UserRole capturedUserRole = userRoleCaptor.getValue();
        assertThat(capturedUserRole.getUserRoleType()).isEqualTo(ADMIN);
        assertThat(capturedUserRole.getUser()).isEqualTo(user);
    }

    @Test
    @WithMockUser(value = "test", password = "pass", authorities = "USER")
    void createUser() throws Exception {
        //given
        UserRequest userRequest = new UserRequest();
        userRequest.setPassword(UUID.randomUUID().toString());
        userRequest.setEmail("user@gmail.com");
        userRequest.setUsername("coolUser");

        User user = new User();
        user.setId(UUID.randomUUID().toString());

        //when
        when(userRepository.save(any())).thenReturn(user);

        //then
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated());

        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getEmail()).isEqualTo(userRequest.getEmail());
        assertThat(capturedUser.getUsername()).isEqualTo(userRequest.getUsername());
        verify(userRoleRepository).save(userRoleCaptor.capture());
        UserRole capturedUserRole = userRoleCaptor.getValue();
        assertThat(capturedUserRole.getUserRoleType()).isEqualTo(USER);
        assertThat(capturedUserRole.getUser()).isEqualTo(user);
    }

    @Test
    @WithUserDetails("admin")
    void getCurrentUser() throws Exception {
        //given
        String userId = "default_id";
        User user = new User();
        user.setUsername("admin");
        user.setEmail("default_email@gmail.com");

        UserResponse userResponse = new UserResponse(user.getUsername(), user.getEmail());

        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //then
        MvcResult actualResult = mvc.perform(get("/users/current"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(actualResult.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(userResponse));
    }

    @Test
    @WithUserDetails("admin")
    void getUserById() throws Exception {
        //given
        String userId = UUID.randomUUID().toString();
        User user = new User();
        user.setUsername("test");
        user.setEmail("test_email@gmail.com");

        UserResponse userResponse = new UserResponse(user.getUsername(), user.getEmail());

        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //then
        MvcResult actualResult = mvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(actualResult.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(userResponse));
    }

    @Test
    @WithMockUser(value = "test", password = "pass", authorities = "USER")
    void createAdmin_Forbidden() throws Exception {
        //given
        UserRequest userRequest = new UserRequest();
        userRequest.setPassword(UUID.randomUUID().toString());
        userRequest.setEmail("user@gmail.com");
        userRequest.setUsername("coolUser");
        userRequest.setRoleType(ADMIN.toString());
        String shortMessage = "You are not allowed to access this resource";
        String message = "Access is denied";

        //then
        MvcResult actualResult = mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isForbidden())
                .andReturn();

        ExceptionMessageDto response = objectMapper.readValue(actualResult.getResponse().getContentAsString(),
                ExceptionMessageDto.class);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getShortMessage()).isEqualTo(shortMessage);
        assertThat(response.getTimestamp()).isNotNull();

        verifyNoInteractions(userRepository);
    }
}

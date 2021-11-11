package org.duckdns.androidghost77.gamelove.rest;

import lombok.RequiredArgsConstructor;
import org.duckdns.androidghost77.gamelove.dto.GameResponse;
import org.duckdns.androidghost77.gamelove.security.dto.UserPrincipal;
import org.duckdns.androidghost77.gamelove.service.LikesService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likeGame(@PathVariable("gameId") String gameId) {
        likesService.likeGame(gameId, getUserId());
    }

    @DeleteMapping("/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlikeGame(@PathVariable("gameId") String gameId) {
        likesService.unlikeGame(gameId, getUserId());
    }

    @GetMapping("/my-games")
    public List<GameResponse> getMyLikes() {
        return likesService.getGamesLikedByUser(getUserId());
    }

    private String getUserId() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userPrincipal.getId();
    }
}

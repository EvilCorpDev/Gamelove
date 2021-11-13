
# Game Love Service

In this assignment, we look forward to test your skills in software development. Please, be pragmatic and avoid over engineering your solution.

## Problem Statement:

We want you to implement a game love service that keeps track of the games that the player loves.

* Design a REST API with the following:
    * It should be possible to create a new entry by feeding it with the following:
        * The player that loved the game.
        * The game it loved.
    * It should be possible to unlove games.
    * It should be possible to fetch all games a player have loved.
    * It should be possible to fetch the most loved games.
        * The list should contain the x top loved games, where x should be possible to define in every request.
        * Each item in this list should contain:
            * The game identifier.
            * Number of loves the game has.

## How to
App contains several controllers. GameController, UserController, LikesController 
and AuthController. At first start when there is no users, default admin:nimda user is available.
First you need to create admin user and then with the help of new user you can create other users, games etc.
To make operations you need to log in through AuthController and use provided jwtToken with Bearer auth.
Api description is available under `localhost/gamelove/swagger-ui.html` and search for `/gamelove/api-docs`.
Some operations can be done only through admin user: 
- creating admin user
- adding games
- delete games
- etc.

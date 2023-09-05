package com.game.manacala.controller;


import com.game.manacala.model.MancalaGame;
import com.game.manacala.service.ManacalaPlayService;
import com.game.manacala.service.MancalaGameService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashMap;

import static com.game.manacala.constants.ValidationMessages.INVALID_REQUEST;
import static com.game.manacala.constants.ValidationMessages.NO_GAME_FOUND_FOR_SPECIFIED_ID;

@RestController
@EnableSwagger2
@RequestMapping("/mancala")
public class MancalaController {

    private MancalaGameService mancalaGameService ;
    private ManacalaPlayService manacalaPlayService;

    MancalaController(MancalaGameService mancalaGameService, ManacalaPlayService manacalaPlayService){
        this.mancalaGameService = mancalaGameService;
        this.manacalaPlayService = manacalaPlayService;
    }

    @GetMapping("/board/numberOfPits/{numberOfPits}/numberOfStones/{numberOfStones}")
    @ApiOperation(value = "Endpoint for creating new Mancala game instance",
            produces = "Application/JSON", response = MancalaGame.class, httpMethod = "GET")
    public ResponseEntity<MancalaGame> getMancalaBoard(@PathVariable int numberOfPits , @PathVariable int numberOfStones){
        if(numberOfPits == 0 || numberOfStones == 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MancalaGame(INVALID_REQUEST));
        }
        MancalaGame mancalaGame = mancalaGameService.setupGame(numberOfPits,numberOfStones);
        return ResponseEntity.status(HttpStatus.OK).body(mancalaGame);
    }


    @PostMapping("/play")
    @ApiOperation(value = "Endpoint for sowing the game. ",
            produces = "Application/JSON", response = MancalaGame.class, httpMethod = "POST")
    public ResponseEntity<MancalaGame> playManacala(@RequestParam String gameId, @RequestParam int choosenPit){

        HashMap<String, MancalaGame> mancalaGamesStore = mancalaGameService.getMancalaGamesStore();
        if(mancalaGamesStore.containsKey(gameId)) {
            MancalaGame mancalaGame = mancalaGamesStore.get(gameId);
            manacalaPlayService.setMancalaMancalaPitBoard(mancalaGame.getMancalaPitBoard());
            mancalaGame.setChoosenPit(choosenPit);
            return ResponseEntity.status(HttpStatus.OK).body(manacalaPlayService.playAround(mancalaGame));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MancalaGame(NO_GAME_FOUND_FOR_SPECIFIED_ID));
        }
    }

}

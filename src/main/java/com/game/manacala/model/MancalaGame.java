package com.game.manacala.model;

import com.game.manacala.enums.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MancalaGame {

    private boolean isGameOver=false;
    private Player currentPlayer;
    private String winner;
    private int choosenPit;
    private MancalaPitBoard mancalaPitBoard;
    private String gameId;
    private String turnMessage;

    public MancalaGame(MancalaPitBoard mancalaPitBoard, String gameId){
        this.mancalaPitBoard=mancalaPitBoard;
        this.gameId = gameId;
    }

    public MancalaGame(String turnMessage) {
        this.turnMessage = turnMessage;
    }
}

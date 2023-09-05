package com.game.manacala.model;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class MancalaPitBoard {

    private int numberOfPits;
    private int numberOfStones;
    private  int boardLength;
    private int[] pitBoard;
    private int leftPlayerLargePit;
    private int rightPlayerLargePit;
}

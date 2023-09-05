package com.game.manacala.service;

import com.game.manacala.enums.GameStatus;
import com.game.manacala.enums.Player;
import com.game.manacala.model.MancalaGame;
import com.game.manacala.model.MancalaPitBoard;
import com.game.manacala.validator.ValidateInputs;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static com.game.manacala.constants.ValidationMessages.GAME_OVER;
import static com.game.manacala.constants.ValidationMessages.ONE_ROUND_DONE;

@Service
@Setter
@Getter
public class ManacalaPlayService {

    Logger logger = Logger.getLogger(ManacalaPlayService.class.getName());


    private MancalaPitBoard mancalaMancalaPitBoard;
    private MancalaGame mancalaGame;
    private ValidateInputs validateInputs=new ValidateInputs();

    /**
     *
     * return the GameResult with the information of next player turn
     * if game is over and who is the winner
     */
    public MancalaGame playAround(MancalaGame mancalaGame){

        if(!validateInputs.validateRequest(mancalaGame))
            return mancalaGame;

        this.mancalaGame=mancalaGame;

        int pitToStartFrom = mancalaGame.getChoosenPit();
        Player playerTurn = mancalaGame.getCurrentPlayer();

        logger.info("Starting game round: " + "chosenPit: "+ pitToStartFrom + " for Player: "+ playerTurn);
        if(!isGameOver()){
            sowsStones(pitToStartFrom,playerTurn);
            mancalaGame.setTurnMessage(ONE_ROUND_DONE + mancalaGame.getCurrentPlayer());
            logger.info(ONE_ROUND_DONE + mancalaGame.getCurrentPlayer());
        }

        if(isGameOver()){
            mancalaGame.setWinner(findWinner());
            mancalaGame.setGameOver(true);
            mancalaGame.setTurnMessage(GAME_OVER);
            logger.info( GAME_OVER + " and winner is " + mancalaGame.getWinner());
        }

        return mancalaGame;
    }

    /**
     *
     * player will pick up all stone from the chosen pit and adds each stone to each pit on its right
     * except the big pit of the opponent
     */
    private void sowsStones(int pitToStartFrom, Player playerTurn) {

        int pickedPitStones = mancalaMancalaPitBoard.getPitBoard()[pitToStartFrom];
        mancalaMancalaPitBoard.getPitBoard()[pitToStartFrom]=0;

        int sowIndex = pitToStartFrom;
        while(pickedPitStones!=0){
            sowIndex = (sowIndex+1) % mancalaMancalaPitBoard.getBoardLength();
            if(!opponentBigPit(playerTurn, sowIndex)){
                mancalaMancalaPitBoard.getPitBoard()[sowIndex]= mancalaMancalaPitBoard.getPitBoard()[sowIndex]+1;
                pickedPitStones--;

                if(eligibleToCapture(sowIndex,playerTurn))
                    captureStones(sowIndex,playerTurn);
            }
        }

        setNextTurn(sowIndex,playerTurn);
    }

    /**
     *
     * if stone lands in the players own big pit then player gets the next turn
     */
    private void setNextTurn(int sowIndex, Player playerTurn) {
        if(playerTurn==Player.PLAYER1 && sowIndex== mancalaMancalaPitBoard.getLeftPlayerLargePit())
            mancalaGame.setCurrentPlayer(playerTurn);
        else if(playerTurn==Player.PLAYER2 && sowIndex== mancalaMancalaPitBoard.getRightPlayerLargePit())
            mancalaGame.setCurrentPlayer(playerTurn);
        else if(playerTurn==Player.PLAYER1)
            mancalaGame.setCurrentPlayer(Player.PLAYER2);
        else
            mancalaGame.setCurrentPlayer(Player.PLAYER1);

    }

    /**
     *
     * if last stone lands in players own empty pit except the large pits then return true
     */
    private boolean eligibleToCapture(int sowIndex, Player playerTurn) {
        if(mancalaMancalaPitBoard.getPitBoard()[sowIndex]==1){
            if((playerTurn==Player.PLAYER1 && sowIndex> mancalaMancalaPitBoard.getLeftPlayerLargePit() && sowIndex< mancalaMancalaPitBoard.getRightPlayerLargePit())
               || (playerTurn==Player.PLAYER2 && sowIndex> mancalaMancalaPitBoard.getRightPlayerLargePit() && sowIndex< mancalaMancalaPitBoard.getBoardLength())
            )
                return true;
        }


        return false;
    }

    /**
     *
     * pick the stone from the own pit and all stones from opponents corresponding pit and
     * put them in own large pit
     */
    private void captureStones(int sowIndex, Player playerTurn) {

        logger.info("Capturing Stones for Player "+ playerTurn);
        int captureIndex = mancalaMancalaPitBoard.getBoardLength()-sowIndex;
        int largePitStones=0;
        if(playerTurn == Player.PLAYER1){
           largePitStones= mancalaMancalaPitBoard.getPitBoard()[mancalaMancalaPitBoard.getLeftPlayerLargePit()];
           mancalaMancalaPitBoard.getPitBoard()[mancalaMancalaPitBoard.getLeftPlayerLargePit()]=
                   largePitStones+ mancalaMancalaPitBoard.getPitBoard()[captureIndex]+1;
        }else if(playerTurn == Player.PLAYER2){
            largePitStones= mancalaMancalaPitBoard.getPitBoard()[mancalaMancalaPitBoard.getRightPlayerLargePit()];
            mancalaMancalaPitBoard.getPitBoard()[mancalaMancalaPitBoard.getRightPlayerLargePit()]=
                    largePitStones+ mancalaMancalaPitBoard.getPitBoard()[captureIndex]+1;
        }
        mancalaMancalaPitBoard.getPitBoard()[captureIndex]=0;
        mancalaMancalaPitBoard.getPitBoard()[sowIndex]=0;
    }

    /**
     *
     * Considering Zero Index as first Player big pit and center pit as an second
     * Player big pit
     */
    private boolean opponentBigPit(Player playerTurn, int sowIndex) {

        if(playerTurn==Player.PLAYER1 && sowIndex== mancalaMancalaPitBoard.getRightPlayerLargePit())
            return true;
        else if(playerTurn==Player.PLAYER2 && sowIndex== mancalaMancalaPitBoard.getLeftPlayerLargePit())
            return true;
        else
            return false;
    }

    /**
     *
     * the player having the more stones in their large pit, wins
     */
    private String findWinner() {
        if(mancalaMancalaPitBoard.getPitBoard()[mancalaMancalaPitBoard.getLeftPlayerLargePit()]> mancalaMancalaPitBoard.getPitBoard()[mancalaMancalaPitBoard.getRightPlayerLargePit()])
            return Player.PLAYER1.toString();
        else if(mancalaMancalaPitBoard.getPitBoard()[mancalaMancalaPitBoard.getLeftPlayerLargePit()]< mancalaMancalaPitBoard.getPitBoard()[mancalaMancalaPitBoard.getRightPlayerLargePit()])
            return Player.PLAYER2.toString();
        else
            return GameStatus.DRAW.toString();

    }

    /**
     *
     * checks if any of the players side pit is empty except the largePit then return true else false
     */
    boolean isGameOver(){

        int[] pitBoard = mancalaMancalaPitBoard.getPitBoard();
        boolean isRightPlayerBoardEmpty=true;
        boolean isLeftPlayerBoardEmpty=true;
        for(int i = 1; i<= mancalaMancalaPitBoard.getNumberOfPits(); i++){
            if(pitBoard[i]>0)
                isRightPlayerBoardEmpty=false;
        }

        for(int i = mancalaMancalaPitBoard.getNumberOfPits()+2; i< mancalaMancalaPitBoard.getBoardLength(); i++){
            if(pitBoard[i]>0)
                isLeftPlayerBoardEmpty=false;
        }

        return isLeftPlayerBoardEmpty || isRightPlayerBoardEmpty;
    }

}

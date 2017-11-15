package by.bsu.mmf.badthrowgame.server.master.handle.command.impl;

import by.bsu.mmf.badthrowgame.dice.DicePack;
import by.bsu.mmf.badthrowgame.enums.ResponsePlayerAction;
import by.bsu.mmf.badthrowgame.server.master.MasterServer;
import by.bsu.mmf.badthrowgame.server.master.handle.command.MasterCommand;
import by.bsu.mmf.badthrowgame.server.send.Sender;
import by.bsu.mmf.badthrowgame.transferobject.MasterTransfer;

import java.io.IOException;

import static by.bsu.mmf.badthrowgame.server.ServerVariable.*;
import static by.bsu.mmf.badthrowgame.server.ServerVariable.gameBet;
import static by.bsu.mmf.badthrowgame.server.ServerVariable.winCash;

public class SetReadyCommand implements MasterCommand {
    @Override
    public void execute(MasterTransfer multiplayerTransfer) throws IOException {
        setPlayerReady(multiplayerTransfer.getIdPlayer());

        if (allPlayersReady()) {
            gameActive = true;
            takeCashAndPrepareForGame();
        } else {
            winCash = 0;
        }

        System.out.println(winCash);
        Sender.broadcastPlayers(multiplayerTransfer, ResponsePlayerAction.SET_READY);
    }

    private void setPlayerReady(String id){
        synchronized (players) {
            for (MasterServer player : players.keySet()) {
                if (players.get(player).getIdPlayer().equals(id)) {
                    players.get(player).setReady(true);
                }
            }
        }
    }

    private boolean allPlayersReady(){
        return players.entrySet().stream().allMatch((p) -> p.getValue().isReady());
    }

    private void takeCashAndPrepareForGame(){
        players.entrySet().stream().forEach((p) -> {
            serverDAO.takeBetCash(p.getValue().getIdPlayer(), gameBet);
            serverDAO.addGamePlayed(p.getValue().getIdPlayer());
            p.getValue().setDicePack(new DicePack());
            winCash += gameBet;
        });
    }
}

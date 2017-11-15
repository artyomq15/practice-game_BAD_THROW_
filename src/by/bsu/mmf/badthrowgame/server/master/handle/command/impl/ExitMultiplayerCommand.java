package by.bsu.mmf.badthrowgame.server.master.handle.command.impl;

import by.bsu.mmf.badthrowgame.enums.ResponsePlayerAction;
import by.bsu.mmf.badthrowgame.server.master.handle.command.MasterCommand;
import by.bsu.mmf.badthrowgame.server.master.handle.command.help.HandlerFinder;
import by.bsu.mmf.badthrowgame.server.master.handle.command.help.WinnerFinder;
import by.bsu.mmf.badthrowgame.server.send.Sender;
import by.bsu.mmf.badthrowgame.transferobject.MasterTransfer;

import java.io.IOException;

import static by.bsu.mmf.badthrowgame.server.ServerVariable.gameActive;
import static by.bsu.mmf.badthrowgame.server.ServerVariable.players;
import static by.bsu.mmf.badthrowgame.server.ServerVariable.spectators;

public class ExitMultiplayerCommand implements MasterCommand {
    @Override
    public void execute(MasterTransfer multiplayerTransfer) throws IOException {

        String idPlayer = multiplayerTransfer.getIdPlayer();
        serverDAO.setPlaying(idPlayer, false);
        if (players.containsKey(HandlerFinder.getHandlerById(idPlayer))) {
            synchronized (players) {
                players.remove(HandlerFinder.getHandlerById(idPlayer));
            }
            WinnerFinder.findWinner();
        } else {
            synchronized (spectators) {
                spectators.remove(HandlerFinder.getHandlerById(idPlayer));
            }
        }
        if (players.size() == 0) gameActive = false;
        Sender.broadcastPlayers(multiplayerTransfer, ResponsePlayerAction.EXIT_MULTIPLAYER);
    }
}

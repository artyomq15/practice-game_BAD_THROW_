package by.bsu.mmf.badthrowgame.server.master.handle.command.impl;

import by.bsu.mmf.badthrowgame.enums.ResponsePlayerAction;
import by.bsu.mmf.badthrowgame.player.Player;
import by.bsu.mmf.badthrowgame.server.master.handle.command.MasterCommand;
import by.bsu.mmf.badthrowgame.server.master.handle.command.help.HandlerFinder;
import by.bsu.mmf.badthrowgame.server.send.Sender;
import by.bsu.mmf.badthrowgame.transferobject.MasterTransfer;

import java.io.IOException;

import static by.bsu.mmf.badthrowgame.server.ServerVariable.*;


public class EnterMultiplayerCommand implements MasterCommand{
    @Override
    public void execute(MasterTransfer multiplayerTransfer) throws IOException {

        serverDAO.setPlaying(multiplayerTransfer.getIdPlayer(), true);

        Player player = serverDAO.findPlayerById(multiplayerTransfer.getIdPlayer());
        if (gameActive) {
            synchronized (spectators) {
                spectators.put(HandlerFinder.getHandlerById(multiplayerTransfer.getIdPlayer()), player);
            }
            Sender.broadcastPlayers(multiplayerTransfer,ResponsePlayerAction.ENTER_SPECTATORS);
        } else {
            synchronized (players) {
                players.put(HandlerFinder.getHandlerById(multiplayerTransfer.getIdPlayer()), player);
            }
            gameActive = false;
            Sender.broadcastPlayers(multiplayerTransfer, ResponsePlayerAction.ENTER_PLAYERS);
        }
    }
}

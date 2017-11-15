package by.bsu.mmf.badthrowgame.server.master.handle.command.impl;

import by.bsu.mmf.badthrowgame.dice.DicePack;
import by.bsu.mmf.badthrowgame.enums.ResponsePlayerAction;
import by.bsu.mmf.badthrowgame.server.master.MasterServer;
import by.bsu.mmf.badthrowgame.server.master.handle.command.MasterCommand;
import by.bsu.mmf.badthrowgame.server.master.handle.command.help.WinnerFinder;
import by.bsu.mmf.badthrowgame.server.send.Sender;
import by.bsu.mmf.badthrowgame.transferobject.MasterTransfer;

import java.io.IOException;

import static by.bsu.mmf.badthrowgame.server.ServerVariable.players;

public class MakeThrowCommand implements MasterCommand {
    @Override
    public void execute(MasterTransfer multiplayerTransfer) throws IOException {

        for (MasterServer player :players.keySet()){
            if (players.get(player).getIdPlayer().equals(multiplayerTransfer.getIdPlayer())){
                DicePack dicePack = players.get(player).getDicePack();
                dicePack.countTotal();
                if (!dicePack.isEmpty()) {
                    dicePack.makeThrow();
                }
                break;
            }
        }
        WinnerFinder.findWinner();
        Sender.broadcastPlayers(multiplayerTransfer, ResponsePlayerAction.MAKE_THROW);
    }
}

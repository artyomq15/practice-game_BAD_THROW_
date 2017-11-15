package by.bsu.mmf.badthrowgame.server.master.handle.command.impl;

import by.bsu.mmf.badthrowgame.enums.ResponsePlayerAction;
import by.bsu.mmf.badthrowgame.server.master.handle.command.MasterCommand;
import by.bsu.mmf.badthrowgame.server.send.Sender;
import by.bsu.mmf.badthrowgame.transferobject.MasterTransfer;

import java.io.IOException;

import static by.bsu.mmf.badthrowgame.server.ServerVariable.gameBet;

public class ChangeBetCommand implements MasterCommand {
    @Override
    public void execute(MasterTransfer multiplayerTransfer) throws IOException {

        gameBet = Integer.parseInt(multiplayerTransfer.getBet());
        Sender.broadcastBet(multiplayerTransfer, ResponsePlayerAction.CHANGE_BET);
    }
}

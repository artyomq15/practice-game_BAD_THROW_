package by.bsu.mmf.badthrowgame.server.master.handle.command.impl;

import by.bsu.mmf.badthrowgame.enums.ResponsePlayerAction;
import by.bsu.mmf.badthrowgame.player.Player;
import by.bsu.mmf.badthrowgame.server.master.handle.command.MasterCommand;
import by.bsu.mmf.badthrowgame.server.send.Sender;
import by.bsu.mmf.badthrowgame.transferobject.MasterTransfer;

import java.io.IOException;


public class SendMessageCommand implements MasterCommand {
    @Override
    public void execute(MasterTransfer messageTransfer) throws IOException {
        String sender = messageTransfer.getIdSender();

        Player player = serverDAO.findPlayerById(sender);
        if (player.isPlaying()) {
            String senderName = player.getNamePlayer();
            messageTransfer.setNameSender(senderName);
            messageTransfer.setResponse(ResponsePlayerAction.SEND_MESSAGE);
        }
        Sender.sendToPlayersAndSpectators(messageTransfer);
    }
}

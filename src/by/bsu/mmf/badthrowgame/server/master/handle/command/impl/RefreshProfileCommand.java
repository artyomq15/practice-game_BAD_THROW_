package by.bsu.mmf.badthrowgame.server.master.handle.command.impl;

import by.bsu.mmf.badthrowgame.player.Player;
import by.bsu.mmf.badthrowgame.server.master.handle.command.MasterCommand;
import by.bsu.mmf.badthrowgame.server.send.Sender;
import by.bsu.mmf.badthrowgame.transferobject.MasterTransfer;

import java.io.IOException;

public class RefreshProfileCommand implements MasterCommand {
    @Override
    public void execute(MasterTransfer masterTransfer) throws IOException {

        Player player = serverDAO.findPlayerById(masterTransfer.getIdClient());
        Sender.sendProfileInfo(masterTransfer, player);
    }
}

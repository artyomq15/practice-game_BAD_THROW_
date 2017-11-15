package by.bsu.mmf.badthrowgame.server.master.handle;

import by.bsu.mmf.badthrowgame.dao.DAOFactory;
import by.bsu.mmf.badthrowgame.dao.ServerDAO;
import by.bsu.mmf.badthrowgame.server.master.handle.command.CommandDirector;
import by.bsu.mmf.badthrowgame.server.master.handle.command.MasterCommand;
import by.bsu.mmf.badthrowgame.transferobject.MasterTransfer;

import java.io.IOException;

public class MasterHandler {
    public static void handle(MasterTransfer masterTransfer) throws IOException{
        CommandDirector director = new CommandDirector();
        MasterCommand command = director.getCommand(masterTransfer.getRequest());
        command.execute(masterTransfer);
    }
}

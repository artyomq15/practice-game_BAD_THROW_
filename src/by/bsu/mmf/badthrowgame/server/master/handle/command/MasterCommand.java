package by.bsu.mmf.badthrowgame.server.master.handle.command;


import by.bsu.mmf.badthrowgame.dao.DAOFactory;
import by.bsu.mmf.badthrowgame.dao.ServerDAO;
import by.bsu.mmf.badthrowgame.transferobject.MasterTransfer;

import java.io.IOException;

public interface MasterCommand {
    ServerDAO serverDAO = DAOFactory.getInstance().getServerDAO();
    void execute(MasterTransfer objectTransfer) throws IOException;
}

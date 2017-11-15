package by.bsu.mmf.badthrowgame.server.master.handle.auth;

import by.bsu.mmf.badthrowgame.dao.DAOFactory;
import by.bsu.mmf.badthrowgame.dao.ServerDAO;
import by.bsu.mmf.badthrowgame.enums.AccountAction;
import by.bsu.mmf.badthrowgame.enums.AccountSuccess;
import by.bsu.mmf.badthrowgame.server.Server;
import by.bsu.mmf.badthrowgame.transferobject.AuthorizationTransfer;

import java.io.IOException;

public class Authorizer {
    private static ServerDAO serverDAO = DAOFactory.getInstance().getServerDAO();

    public static String authorization(Server server) throws IOException, ClassNotFoundException{
        String idClient = "";
        boolean entered = false;
        while (!entered) {
            AuthorizationTransfer authTransfer = (AuthorizationTransfer) server.getInStream().readObject();
            if (authTransfer.getAction() == AccountAction.SIGNING_IN) {

                idClient = serverDAO.enterAccount(authTransfer.getLogin(), authTransfer.getPassword());

                if (!idClient.equals("")) {
                    authTransfer.setId(idClient);
                    authTransfer.setSuccess(AccountSuccess.SUCCESSFUL_ENTERING);
                    entered = true;
                } else {
                    authTransfer.setId("");
                    authTransfer.setSuccess(AccountSuccess.UNSUCCESSFUL_ENTERING);
                }


            } else if (authTransfer.getAction() == AccountAction.SIGNING_UP) {

                AccountSuccess accountSuccess = serverDAO.createAccount(authTransfer.getName(), authTransfer.getLogin(), authTransfer.getPassword());
                authTransfer.setId("");
                authTransfer.setSuccess(accountSuccess);

            }

            server.getOutStream().writeObject(authTransfer);
            server.getOutStream().flush();

        }
        return idClient;
    }
}

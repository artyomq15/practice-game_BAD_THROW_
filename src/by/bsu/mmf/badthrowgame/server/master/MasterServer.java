package by.bsu.mmf.badthrowgame.server.master;

import by.bsu.mmf.badthrowgame.server.dao.DAOFactory;
import by.bsu.mmf.badthrowgame.enums.ResponsePlayerAction;
import by.bsu.mmf.badthrowgame.server.Server;
import by.bsu.mmf.badthrowgame.server.master.handle.MasterHandler;
import by.bsu.mmf.badthrowgame.server.master.handle.auth.Authorizer;
import by.bsu.mmf.badthrowgame.server.send.Sender;
import by.bsu.mmf.badthrowgame.transferobject.MasterTransfer;
import java.io.IOException;
import java.net.Socket;

import static by.bsu.mmf.badthrowgame.server.ServerVariable.*;

public class MasterServer extends Thread{
    private Server server;

    public MasterServer(Socket socket) throws IOException{
        server = new Server(socket);
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void run(){
        try {
            String id = Authorizer.authorization(server);
            users.put(this,DAOFactory.getInstance().getServerDAO().findPlayerById(id));
            MasterTransfer masterTransfer;
            while (true) {
                masterTransfer = (MasterTransfer) server.getInStream().readObject();
                MasterHandler.handle(masterTransfer);
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try{
                DAOFactory.getInstance().getServerDAO().setPlaying(players.get(this).getIdPlayer(), false);
                synchronized (players) {
                    players.remove(this);
                }
                synchronized (spectators) {
                    spectators.remove(this);
                }
                if (players.size() == 0) {
                    gameActive = false;
                    winCash = 0;
                }
                users.remove(this);
                Sender.broadcastPlayers(ResponsePlayerAction.EXIT_MULTIPLAYER);
            } catch (IOException e){
                e.printStackTrace();
            }
            try {
                server.getInStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.getOutStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.getClientSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}

package by.bsu.mmf.badthrowgame.server.send;

import by.bsu.mmf.badthrowgame.enums.ResponsePlayerAction;
import by.bsu.mmf.badthrowgame.player.Player;
import by.bsu.mmf.badthrowgame.server.master.MasterServer;
import by.bsu.mmf.badthrowgame.transferobject.MasterTransfer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import static by.bsu.mmf.badthrowgame.server.ServerVariable.*;

public class Sender {

    public static void sendProfileInfo(MasterTransfer masterTransfer, Player player) throws IOException{
        masterTransfer.setPlayer(player);
        masterTransfer.setResponse(ResponsePlayerAction.REFRESH_PROFILE);
        sendToClient(getOutputStreamById(masterTransfer.getIdClient()), masterTransfer);
    }

    public static void sendStatistics(MasterTransfer masterTransfer, List<Player> playerList) throws IOException{
        masterTransfer.setPlayerList(playerList);
        masterTransfer.setResponse(ResponsePlayerAction.REFRESH_STATISTICS);
        sendToClient(getOutputStreamById(masterTransfer.getIdClient()), masterTransfer);
    }

    private static ObjectOutputStream getOutputStreamById(String id){
        for (MasterServer player :users.keySet()){
            if (users.get(player).getIdPlayer().equals(id)){
                return player.getServer().getOutStream();
            }
        }
        return null;
    }



    public static void broadcastPlayers(ResponsePlayerAction responsePlayerAction)throws IOException{
        broadcastPlayers(new MasterTransfer(), responsePlayerAction);
    }

    public static void broadcastPlayers(MasterTransfer multiplayerTransfer, ResponsePlayerAction responsePlayerAction) throws IOException{
        players.entrySet().stream().forEach((player) -> {
            multiplayerTransfer.addPlayer(player.getValue());
        });
        spectators.entrySet().stream().forEach((spectator) -> {
            multiplayerTransfer.addSpectator(spectator.getValue());
        });
        multiplayerTransfer.setResponse(responsePlayerAction);
        multiplayerTransfer.setBet(String.valueOf(gameBet));
        sendToPlayersAndSpectators(multiplayerTransfer);
    }



    public static void broadcastBet(MasterTransfer multiplayerTransfer, ResponsePlayerAction responsePlayerAction) throws IOException{
        multiplayerTransfer.setResponse(responsePlayerAction);
        multiplayerTransfer.setBet(String.valueOf(gameBet));
        sendToPlayersAndSpectators(multiplayerTransfer);
    }

    public static void sendToPlayersAndSpectators(MasterTransfer transferObject) throws IOException{
        for (MasterServer player :players.keySet()){
            sendToClient(player.getServer().getOutStream(), transferObject);
        }
        for (MasterServer player :spectators.keySet()){
            sendToClient(player.getServer().getOutStream(), transferObject);
        }
    }

    private static void sendToClient(ObjectOutputStream stream, MasterTransfer transferObject) throws IOException{
        stream.reset();
        stream.writeObject(transferObject);
        stream.flush();
    }
}

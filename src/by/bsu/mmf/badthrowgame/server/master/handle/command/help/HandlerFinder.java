package by.bsu.mmf.badthrowgame.server.master.handle.command.help;

import by.bsu.mmf.badthrowgame.server.master.MasterServer;

import static by.bsu.mmf.badthrowgame.server.ServerVariable.users;

public class HandlerFinder {
    public static MasterServer getHandlerById(String id){
        for (MasterServer player : users.keySet()){
            if (users.get(player).getIdPlayer().equals(id)){
                return player;
            }
        }
        return null;
    }
}

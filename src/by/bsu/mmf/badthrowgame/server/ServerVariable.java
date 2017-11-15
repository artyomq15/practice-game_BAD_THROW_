package by.bsu.mmf.badthrowgame.server;

import by.bsu.mmf.badthrowgame.player.Player;
import by.bsu.mmf.badthrowgame.server.master.MasterServer;

import java.util.*;

public class ServerVariable {
    public static final Map<MasterServer, Player> users = Collections.synchronizedMap(new HashMap<>());

    public static final Map<MasterServer, Player> players = Collections.synchronizedMap(new HashMap<>());
    public static final Map<MasterServer, Player> spectators = Collections.synchronizedMap(new HashMap<>());

    public static Boolean gameActive = false;
    public static Integer gameBet = 100;
    public static Integer winCash = 0;
}

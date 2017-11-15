package by.bsu.mmf.badthrowgame.server.master.handle.command.help;

import by.bsu.mmf.badthrowgame.dao.DAOFactory;
import by.bsu.mmf.badthrowgame.dao.ServerDAO;
import by.bsu.mmf.badthrowgame.player.Player;

import java.util.ArrayList;

import static by.bsu.mmf.badthrowgame.server.ServerVariable.players;
import static by.bsu.mmf.badthrowgame.server.ServerVariable.winCash;


public class WinnerFinder {
    private static ServerDAO serverDAO = DAOFactory.getInstance().getServerDAO();
    public static void findWinner() {
        if (players.entrySet().stream().allMatch(player -> player.getValue().getDicePack().isEmpty()) && winCash != 0) {
            ArrayList<String> winnersId = new ArrayList<>();
            int maxTotal = 0;
            for (Player player : players.values()) {
                if (maxTotal < player.getDicePack().getTotal()) {
                    maxTotal = player.getDicePack().getTotal();
                }
            }
            for (Player player : players.values()) {
                if (maxTotal == player.getDicePack().getTotal()) {
                    winnersId.add(player.getIdPlayer());
                }
            }
            for (String id : winnersId) {
                serverDAO.putBetCash(id, winCash / winnersId.size());
                serverDAO.addGameWon(id);
            }

            winCash = 0;
            winnersId.clear();
        }
    }
}

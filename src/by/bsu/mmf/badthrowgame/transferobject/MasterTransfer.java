package by.bsu.mmf.badthrowgame.transferobject;


import by.bsu.mmf.badthrowgame.enums.RequestPlayerAction;
import by.bsu.mmf.badthrowgame.enums.ResponsePlayerAction;
import by.bsu.mmf.badthrowgame.player.Player;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class MasterTransfer implements Serializable {
    protected RequestPlayerAction request;
    protected ResponsePlayerAction response;

    private String idClient;

    private String idSender;
    private String nameSender;
    private String textMessage;

    private String idPlayer;
    private String bet;

    private Player player;
    private List<Player> playerList = new LinkedList<>();
    private List<Player> spectatorList = new LinkedList<>();


    public String getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(String idPlayer) {
        this.idPlayer = idPlayer;
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public void addPlayer(Player player) {
        playerList.add(player);
    }

    public List<Player> getSpectatorList() {
        return spectatorList;
    }

    public void setSpectatorList(List<Player> spectatorList) {
        this.spectatorList = spectatorList;
    }

    public void addSpectator(Player spectator) {
        spectatorList.add(spectator);
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getNameSender() {
        return nameSender;
    }

    public void setNameSender(String nameSender) {
        this.nameSender = nameSender;
    }

    public String getIdClient() {
        return idClient;
    }

    public RequestPlayerAction getRequest() {
        return request;
    }

    public void setRequest(RequestPlayerAction request) {
        this.request = request;
    }

    public ResponsePlayerAction getResponse() {
        return response;
    }

    public void setResponse(ResponsePlayerAction response) {
        this.response = response;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }
}

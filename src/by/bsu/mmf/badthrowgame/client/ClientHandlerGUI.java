package by.bsu.mmf.badthrowgame.client;

import by.bsu.mmf.badthrowgame.dice.DicePack;
import by.bsu.mmf.badthrowgame.enums.AccountAction;
import by.bsu.mmf.badthrowgame.enums.RequestPlayerAction;
import by.bsu.mmf.badthrowgame.enums.ResponsePlayerAction;
import by.bsu.mmf.badthrowgame.enums.ViewType;
import by.bsu.mmf.badthrowgame.player.Player;
import by.bsu.mmf.badthrowgame.transferobject.*;

import static by.bsu.mmf.badthrowgame.validation.RegisterValidation.*;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ClientHandlerGUI extends Thread {
    private Socket clientSocket;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;


    // private ClientHandler clientHandler;


    private JPanel jpanel;
    private JPanel signInPanel;
    private JTextArea playersList;
    private JTextArea chatList;
    private JTextField messageField;
    private JButton sendMessageButton;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JPanel signUpPanel;
    private JTextField nameRegisterField;
    private JTextField loginRegisterField;
    private JPasswordField passwordRegisterField;
    private JPasswordField passwordConfirmField;
    private JButton enterAccount;
    private JButton registerNewAccountButton;
    private JButton registerButton;
    private JPanel gamePanel;
    private JTabbedPane accountMainPanel;
    private JButton backToSigningInButton;
    private JButton enterTheGameButton;
    private JButton exitFromTheGameButton;
    private JButton readyButton;
    private JLabel betLabel;
    private JButton throwButton;
    private JLabel totalField;
    private JButton plusBet;
    private JButton minusBet;
    private JLabel betLabel1;
    private JTextArea spectatorsList;
    private JButton changeNameButton;
    private JLabel nameProfile;
    private JButton addCashButton;
    private JButton refreshButton;
    private JLabel cashProfileField;
    private JLabel gamesProfileField;
    private JLabel gamesWonProfileField;
    private JLabel winRateProfileField;
    private JTextArea dicesValues;
    private JTextArea statisticsArea;
    private JButton refreshStatisticsButton;
    private JTextArea playerDicesArea;
    private JTextArea computerDicesArea;
    private JButton throwButtonVsComputer;
    private JLabel playerTotal;
    private JLabel computerTotal;
    private JLabel resultMessage;
    private JButton startGameVsComputerButton;
    private JLabel playerLabel;
    private JLabel computerLabel;
    private JLabel dicesLabel;

    private String idClient;

    private final Map<ResponsePlayerAction, Consumer<MasterTransfer>> dispatch = new HashMap<>();

    DicePack playerDicePack;
    DicePack computerDicePack;

    public ClientHandlerGUI(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        outStream = new ObjectOutputStream(clientSocket.getOutputStream());
        inStream = new ObjectInputStream(clientSocket.getInputStream());

        chatList.setLineWrap(true);
        chatList.setWrapStyleWord(true);

        enterTheGameButton.setVisible(true);
        exitFromTheGameButton.setVisible(false);

        readyButton.setVisible(false);
        plusBet.setVisible(false);
        minusBet.setVisible(false);
        throwButton.setVisible(false);
        totalField.setVisible(false);

        betLabel.setVisible(false);
        betLabel1.setVisible(false);

        throwButtonVsComputer.setVisible(false);

        enterAccount.addActionListener(e -> {
            String login = loginField.getText();
            char[] password = passwordField.getPassword();
            enterAccount(login, password);
        });

        sendMessageButton.addActionListener(e -> {
            sendMessage();

        });

        registerNewAccountButton.addActionListener(e -> {
            setView(ViewType.REGISTER_VIEW);

        });

        backToSigningInButton.addActionListener(e -> {

            setView(ViewType.LOGIN_VIEW);

        });

        registerButton.addActionListener(e -> {

            if (passwordConfirmValidation(passwordRegisterField.getPassword(), passwordConfirmField.getPassword()) && loginRegisterValidation(loginRegisterField.getText())) {
                registerAccount(nameRegisterField.getText(), loginRegisterField.getText(), passwordRegisterField.getPassword());
            } else {
                loginRegisterField.setText("");
                passwordRegisterField.setText("");
                passwordConfirmField.setText("");
            }

        });

        enterTheGameButton.addActionListener(e -> {

            enterMultiplayer();

            dicesValues.setText("");

        });

        exitFromTheGameButton.addActionListener(e -> {
            exitMultiplayer();

            enterTheGameButton.setVisible(true);
            exitFromTheGameButton.setVisible(false);

            playersList.setText("");
            spectatorsList.setText("");

            readyButton.setVisible(false);
            plusBet.setVisible(false);
            minusBet.setVisible(false);
            throwButton.setVisible(false);
            totalField.setVisible(false);

            betLabel.setVisible(false);
            betLabel1.setVisible(false);

        });

        plusBet.addActionListener(e -> {

            changeBet(String.valueOf(Integer.parseInt(betLabel.getText()) + 50));

        });

        minusBet.addActionListener(e -> {

            if (Integer.parseInt(betLabel.getText()) > 50)
                changeBet(String.valueOf(Integer.parseInt(betLabel.getText()) - 50));

        });

        readyButton.addActionListener(e -> {

            setReady();

        });

        refreshButton.addActionListener(e -> {

            refreshProfileInfo();

        });

        throwButton.addActionListener(e -> {

            makeThrow();

        });

        refreshStatisticsButton.addActionListener(e -> {

            refreshStatistics();

        });


        throwButtonVsComputer.addActionListener(e -> {

            makeThrowVsComputer(playerDicePack, computerDicePack);

        });

        startGameVsComputerButton.addActionListener(e -> {

            playerDicePack = new DicePack();
            computerDicePack = new DicePack();
            throwButtonVsComputer.setVisible(true);
            startGameVsComputerButton.setVisible(false);
            playerDicesArea.setText("");
            computerDicesArea.setText("");
            resultMessage.setText("");
            playerTotal.setText("0");
            computerTotal.setText("0");

        });

        initGUI();

        startClient();


    }

    public void initGUI() {
        JFrame jframe = new JFrame("Bad Throw Game");
        jframe.setContentPane(jpanel);
        jframe.setSize(500, 500);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.pack();
        jframe.setVisible(true);
    }

    public void startClient() {
        this.start();
    }

    public void enterAccount(String login, char[] password) {
        AuthorizationTransfer authorizationTransfer = new AuthorizationTransfer();
        authorizationTransfer.setAction(AccountAction.SIGNING_IN);
        authorizationTransfer.setLogin(login);
        authorizationTransfer.setPassword(password);
        try {
            outStream.writeObject(authorizationTransfer);
            outStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void registerAccount(String name, String login, char[] password) {
        AuthorizationTransfer authorizationTransfer = new AuthorizationTransfer();
        authorizationTransfer.setAction(AccountAction.SIGNING_UP);
        authorizationTransfer.setLogin(login);
        authorizationTransfer.setPassword(password);
        authorizationTransfer.setName(name);
        try {
            outStream.writeObject(authorizationTransfer);
            outStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMessage() {
        MasterTransfer messageTransfer = new MasterTransfer();
        messageTransfer.setRequest(RequestPlayerAction.SEND_MESSAGE);
        if (!messageField.getText().equals("")) {
            System.out.println(idClient + " " + messageField.getText());
            messageTransfer.setTextMessage(messageField.getText());
            messageTransfer.setIdSender(idClient);
            messageField.setText("");
        }
        sendToServer(messageTransfer);
    }

    public void enterMultiplayer() {
        MasterTransfer multiplayerTransfer = new MasterTransfer();
        multiplayerTransfer.setRequest(RequestPlayerAction.ENTER_MULTIPLAYER);
        multiplayerTransfer.setIdPlayer(idClient);
        sendToServer(multiplayerTransfer);
    }

    public void exitMultiplayer() {
        MasterTransfer masterTransfer = new MasterTransfer();
        masterTransfer.setRequest(RequestPlayerAction.EXIT_MULTIPLAYER);
        masterTransfer.setIdPlayer(idClient);
        sendToServer(masterTransfer);
    }

    public void setReady() {
        MasterTransfer masterTransfer = new MasterTransfer();
        masterTransfer.setRequest(RequestPlayerAction.SET_READY);
        masterTransfer.setIdPlayer(idClient);
        sendToServer(masterTransfer);
    }

    public void changeBet(String bet) {
        MasterTransfer masterTransfer = new MasterTransfer();
        masterTransfer.setRequest(RequestPlayerAction.CHANGE_BET);
        masterTransfer.setIdPlayer(idClient);
        masterTransfer.setBet(bet);
        sendToServer(masterTransfer);
    }

    public void refreshProfileInfo() {
        MasterTransfer masterTransfer = new MasterTransfer();
        masterTransfer.setRequest(RequestPlayerAction.REFRESH_PROFILE);
        masterTransfer.setIdClient(idClient);
        sendToServer(masterTransfer);
    }

    public void refreshStatistics() {
        MasterTransfer masterTransfer = new MasterTransfer();
        masterTransfer.setRequest(RequestPlayerAction.REFRESH_STATISTICS);
        masterTransfer.setIdClient(idClient);
        sendToServer(masterTransfer);
    }

    public void makeThrow() {
        MasterTransfer masterTransfer = new MasterTransfer();
        masterTransfer.setRequest(RequestPlayerAction.MAKE_THROW);
        masterTransfer.setIdPlayer(idClient);
        sendToServer(masterTransfer);
    }

    public void makeThrowVsComputer(DicePack player, DicePack computer) {
        if (!player.isEmpty()) {
            player.makeThrow();
            player.getDices().stream().forEach(dice -> playerDicesArea.append(dice.getValue() + " . "));
            player.countTotal();
            playerTotal.setText(String.valueOf(player.getTotal()));
            playerDicesArea.append("\n");
        } else {
            while (!computer.isEmpty()) {
                computer.makeThrow();
                computer.getDices().stream().forEach(dice -> computerDicesArea.append(dice.getValue() + " . "));
                computer.countTotal();
                computerTotal.setText(String.valueOf(computer.getTotal()));
                computerDicesArea.append("\n");

            }

            throwButtonVsComputer.setVisible(false);
            startGameVsComputerButton.setVisible(true);
            if (player.getTotal() > computer.getTotal()) {
                resultMessage.setText("WIN");
            } else if (player.getTotal() < computer.getTotal()) {
                resultMessage.setText("LOSS");
            } else resultMessage.setText("DRAW");

        }
    }

    public void sendToServer(MasterTransfer masterTransfer) {
        try {
            outStream.reset();
            outStream.writeObject(masterTransfer);
            outStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setView(ViewType viewType) {
        switch (viewType) {
            case LOGIN_VIEW: {
                signInPanel.setVisible(true);
                signUpPanel.setVisible(false);
                gamePanel.setVisible(false);
                break;
            }
            case REGISTER_VIEW: {
                signInPanel.setVisible(false);
                signUpPanel.setVisible(true);
                gamePanel.setVisible(false);
                break;
            }
            case GAME_VIEW: {
                signInPanel.setVisible(false);
                signUpPanel.setVisible(false);
                gamePanel.setVisible(true);
            }
        }
    }

    public Consumer<MasterTransfer> sendMessageResponse() {
        return sct -> {
            chatList.append(sct.getNameSender() + " : " + sct.getTextMessage() + "\n");
        };
    }

    public Consumer<MasterTransfer> enterPlayersResponse() {
        return sct -> {
            refreshLists(sct);
            betLabel.setText(sct.getBet());

            if (idClient.equals(sct.getIdPlayer())) {
                enterTheGameButton.setVisible(false);
                exitFromTheGameButton.setVisible(true);

                readyButton.setVisible(true);
                plusBet.setVisible(true);
                minusBet.setVisible(true);

                betLabel.setVisible(true);
                betLabel1.setVisible(true);
            }

        };
    }

    public Consumer<MasterTransfer> enterSpectatorsResponse() {
        return sct -> {
            refreshLists(sct);
            betLabel.setText(sct.getBet());

            if (idClient.equals(sct.getIdPlayer())) {

                enterTheGameButton.setVisible(false);
                exitFromTheGameButton.setVisible(true);

                readyButton.setVisible(false);
                plusBet.setVisible(false);
                minusBet.setVisible(false);

                betLabel.setVisible(true);
                betLabel1.setVisible(true);
            }

        };
    }

    public Consumer<MasterTransfer> exitMultiplayerResponse() {
        return sct -> {
            refreshLists(sct);
        };
    }

    public Consumer<MasterTransfer> setReadyResponse() {
        return sct -> {

            refreshLists(sct);
            plusBet.setVisible(false);
            minusBet.setVisible(false);

            if (idClient.equals(sct.getIdPlayer())) {

                readyButton.setVisible(false);
                plusBet.setVisible(false);
                minusBet.setVisible(false);
                throwButton.setVisible(true);
                totalField.setVisible(true);
            }
        };
    }

    public Consumer<MasterTransfer> changeBetResponse() {
        return sct -> {

            betLabel.setText(sct.getBet());
        };
    }

    public Consumer<MasterTransfer> refreshProfileResponse() {
        return sct -> {

            Player player = sct.getPlayer();
            nameProfile.setText(player.getNamePlayer());
            cashProfileField.setText(String.valueOf(player.getCashPlayer()));
            gamesProfileField.setText(String.valueOf(player.getGamesPlayed()));
            gamesWonProfileField.setText(String.valueOf(player.getGamesWon()));
            if (player.getGamesPlayed() != 0) {
                winRateProfileField.setText(String.valueOf(100 * player.getGamesWon() / player.getGamesPlayed()) + " %");
            }
        };
    }

    public Consumer<MasterTransfer> makeThrowResponse() {
        return sct -> {

            if (idClient.equals(sct.getIdPlayer())) {
                Player player = sct.getPlayerList().stream().filter(pl -> pl.getIdPlayer().equals(idClient)).findFirst().orElse(null);
                DicePack dicePack = player.getDicePack();
                if (!dicePack.isEmpty()) {
                    totalField.setText(String.valueOf(dicePack.getTotal()));
                    dicePack.getDices().stream().forEach(dice -> dicesValues.append(dice.getValue() + " . "));
                    dicesValues.append("\n");
                } else {
                    throwButton.setVisible(false);
                }
            }

            refreshLists(sct);
        };
    }

    public Consumer<MasterTransfer> refreshStatisticsResponse() {
        return sct -> {

            statisticsArea.setText("");
            List<Player> players = sct.getPlayerList();
            players.stream().forEach(player -> {
                int percent = 0;
                if (player.getGamesPlayed() != 0) percent = 100 * player.getGamesWon() / player.getGamesPlayed();
                statisticsArea.append(player.getNamePlayer() + "  |  " + player.getGamesPlayed() + "/" + player.getGamesWon() + "  |  " + percent + "%  |  " + player.getCashPlayer() + "$ \n");
            });
        };
    }

    public void load(ResponsePlayerAction type, Consumer<MasterTransfer> handle) {
        dispatch.put(type, handle);
    }

    public void initDispatcher() {
        load(ResponsePlayerAction.SEND_MESSAGE, sendMessageResponse());
        load(ResponsePlayerAction.ENTER_PLAYERS, enterPlayersResponse());
        load(ResponsePlayerAction.ENTER_SPECTATORS, enterSpectatorsResponse());
        load(ResponsePlayerAction.EXIT_MULTIPLAYER, exitMultiplayerResponse());
        load(ResponsePlayerAction.SET_READY, setReadyResponse());
        load(ResponsePlayerAction.CHANGE_BET, changeBetResponse());
        load(ResponsePlayerAction.REFRESH_PROFILE, refreshProfileResponse());
        load(ResponsePlayerAction.MAKE_THROW, makeThrowResponse());
        load(ResponsePlayerAction.REFRESH_STATISTICS, refreshStatisticsResponse());
    }

    public void handle(MasterTransfer sct) {
        dispatch.get(sct.getResponse()).accept(sct);
    }

    public void refreshLists(MasterTransfer sct) {
        playersList.setText("");
        sct.getPlayerList().stream().forEach((p) -> {
            if (p.isReady()) playersList.append(" + ");
            playersList.append(p.getNamePlayer() + " / $" + p.getCashPlayer());
            if (p.getDicePack() != null) {
                playersList.append(" [" + p.getDicePack().getTotal() + "] ");
            }
            playersList.append("\n");
        });
        spectatorsList.setText("");
        sct.getSpectatorList().stream().forEach((p) -> {
            spectatorsList.append(p.getNamePlayer() + "\n");
        });
    }

    public void run() {
        try {

            ViewType viewType = ViewType.LOGIN_VIEW;
            setView(viewType);

            boolean entered = false;
            while (!entered) {
                AuthorizationTransfer scea = (AuthorizationTransfer) inStream.readObject();

                switch (scea.getSuccess()) {
                    case SUCCESSFUL_ENTERING: {
                        entered = true;
                        idClient = scea.getId();
                        viewType = ViewType.GAME_VIEW;
                        break;
                    }
                    case UNSUCCESSFUL_ENTERING: {
                        passwordField.setText("");
                        break;
                    }
                    case SUCCESSFUL_REGISTER: {
                        viewType = ViewType.LOGIN_VIEW;
                        setView(viewType);
                        break;
                    }
                    case UNSUCCESSFUL_REGISTER: {
                        loginRegisterField.setText("");
                        passwordRegisterField.setText("");
                        passwordConfirmField.setText("");
                        break;
                    }

                }


            }

            setView(viewType);
            refreshProfileInfo();
            refreshStatistics();


            initDispatcher();

            MasterTransfer masterTransfer;
            while (true) {
                masterTransfer = (MasterTransfer) inStream.readObject();
                handle(masterTransfer);
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket(InetAddress.getLocalHost(), 8080);
        new ClientHandlerGUI(clientSocket);

    }
}

package by.bsu.mmf.badthrowgame.server.master.handle.command;

import by.bsu.mmf.badthrowgame.enums.RequestPlayerAction;
import by.bsu.mmf.badthrowgame.server.master.handle.command.impl.*;

import java.util.HashMap;
import java.util.Map;

public class CommandDirector {
    private Map<RequestPlayerAction, MasterCommand> dispatcherMap = new HashMap<>();

    {
        dispatcherMap.put(RequestPlayerAction.REFRESH_STATISTICS, new ResreshStatisticsCommand());
        dispatcherMap.put(RequestPlayerAction.REFRESH_PROFILE, new RefreshProfileCommand());
        dispatcherMap.put(RequestPlayerAction.CHANGE_BET, new ChangeBetCommand());
        dispatcherMap.put(RequestPlayerAction.SET_READY, new SetReadyCommand());
        dispatcherMap.put(RequestPlayerAction.MAKE_THROW, new MakeThrowCommand());
        dispatcherMap.put(RequestPlayerAction.ENTER_MULTIPLAYER, new EnterMultiplayerCommand());
        dispatcherMap.put(RequestPlayerAction.EXIT_MULTIPLAYER, new ExitMultiplayerCommand());
        dispatcherMap.put(RequestPlayerAction.SEND_MESSAGE, new SendMessageCommand());
    }

    public CommandDirector(){}

    public MasterCommand getCommand(RequestPlayerAction commandType){
        return dispatcherMap.get(commandType);
    }
}

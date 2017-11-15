package by.bsu.mmf.badthrowgame.transferobject;


import by.bsu.mmf.badthrowgame.enums.AccountAction;
import by.bsu.mmf.badthrowgame.enums.AccountSuccess;

import java.io.Serializable;

public class AuthorizationTransfer implements Serializable{
    private AccountAction action;
    private String name;
    private String login;
    private char[] password;

    private AccountSuccess success;
    private String id;

    public AccountAction getAction() {
        return action;
    }

    public void setAction(AccountAction action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public AccountSuccess getSuccess() {
        return success;
    }

    public void setSuccess(AccountSuccess success) {
        this.success = success;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

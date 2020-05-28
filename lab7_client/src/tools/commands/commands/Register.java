package tools.commands.commands;

import tools.DataBase.DataBaseConnector;
import tools.commands.Command;

import java.io.Serializable;

public class Register extends Command implements Serializable {
    public Register() {
        super("Register", "Register");
        hasData = false;
    }

    @Override
    public void execute() {
        try {
            this.res = "";
            res += DataBaseConnector.register(login, password) ? "Successfully registered ":"NOT_REG";
        }catch (Exception e){
            res += "NOT_REG";
            //System.out.println("Troubles finding user in BD");
        }
    }

    @Override
    public String getAnswer() {
        return res;
    }
}
package tools.commands.commands;

import data.LabworksStorage;
import tools.DataBase.DataBaseConnector;
import tools.commands.Command;

import java.io.Serializable;

public class Login extends Command implements Serializable {
    public Login() {
        super("Login", "Logs in");
        hasData = false;
    }

    @Override
    public void execute() {
        try {
            this.res = "";
            res += DataBaseConnector.login(login, password) ? "Successfully logged in ":"NOT_LOGGED";
        }catch (Exception e){
            res += "NOT_LOGGED";
            //System.out.println("Troubles finding user in BD");
        }
    }

    @Override
    public String getAnswer() {
        return res;
    }
}
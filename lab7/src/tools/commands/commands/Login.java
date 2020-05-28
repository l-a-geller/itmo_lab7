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
            System.out.println("SHIT1");
        }catch (Exception e){
            res += "NOT_LOGGED";
            e.printStackTrace();
            System.out.println("SHIT2");
        }
    }

    @Override
    public String getAnswer() {
        return res;
    }
}
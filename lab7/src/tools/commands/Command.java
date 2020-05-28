package tools.commands;

import data.LabWork;

import java.io.Serializable;

public abstract class Command extends AbstractCommand implements Serializable {

    public boolean needsScanner = false;
    public boolean needsExecutor = false;
    public String data;
    public boolean hasData;
    public String lab;
    public String res;
    public String login;
    public String password;

    protected Command(String name, String description){
        super(name, description);
        res = "Answer from from server " + name.toUpperCase() + " >>> ";
    }

    public void execute() throws ArrayIndexOutOfBoundsException{
        throw new ArrayIndexOutOfBoundsException();
        //return "";
    }
    public void execute(String data) throws ArrayIndexOutOfBoundsException {
        throw new ArrayIndexOutOfBoundsException();
    }

    public String getAnswer() {
        String res = "";
        return res;
    }

    public void addUserInfo(String login, String password){
        this.login = login;
        this.password = password;
    }
}

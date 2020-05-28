package tools.commands.commands;

import data.LabworksStorage;
import tools.commands.Command;

import java.io.Serializable;

public class Quit extends Command implements Serializable {
    public Quit(){
        super("Quit","Exits");
        hasData = false;
    }
    @Override
    public void execute() {
        this.res = "QUIT";
        //res += LabworksStorage.printInfo();
    }

    @Override
    public String getAnswer(){
        return res;
    }
}
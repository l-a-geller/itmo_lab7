package tools.commands.commands;

import data.LabworksStorage;
import tools.DataBase.DataBaseConnector;
import tools.commands.Command;

import java.io.Serializable;

public class Clear extends Command implements Serializable {
    public Clear(){
        super("Clear","Clears a collection");
        hasData = false;
    }
    @Override
    public void execute(){
        this.res = "";
        LabworksStorage.getData().forEach(l -> DataBaseConnector.removeLab(l, login));
        LabworksStorage.clear();
        res += "Collection of your Labworks cleared.\n";
    }

    @Override
    public String getAnswer(){
        return res;
    }
}
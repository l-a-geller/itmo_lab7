package tools.commands.commands;

import data.LabworksStorage;
import tools.commands.Command;

import java.io.Serializable;

public class Clear extends Command implements Serializable {
    public Clear(){
        super("Clear","Clears a collection");
        hasData = false;
    }
    @Override
    public void execute(){
        LabworksStorage.clear();
        res += "Collection cleared.\n";
    }

    @Override
    public String getAnswer(){
        return res;
    }
}
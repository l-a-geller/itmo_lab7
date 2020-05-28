package tools.commands.commands;

import data.LabworksStorage;
import tools.commands.Command;

public class Show extends Command {
    public Show(){
        super("Show","Shows elements of collection");
        hasData = false;
    }
    @Override
    public void execute(){
        String oldRes = res;
        LabworksStorage.getData().forEach(w -> res += w.print());
        if (oldRes.equals(res)){
            res += "Collection empty";
        }
        //res += LabworksStorage.print();
    }

    @Override
    public String getAnswer(){
        return res;
    }
}
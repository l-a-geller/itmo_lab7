package tools.commands.commands;

import data.LabworksStorage;
import data.LabWork;
import tools.commands.Command;

import java.util.ArrayList;

public class Remove_Any_By_Minimal_Point extends Command {
    public Remove_Any_By_Minimal_Point(){
        super("Remove_Any_By_Minimal_Point", "Removes any element with minimalPoint provided.");
        hasData = true;
    }
    @Override
    public void execute(String data){
        //res = "";
        try {
            Float minElement = Float.parseFloat(data);
            ArrayList<LabWork> lab = LabworksStorage.getByMin(minElement);
            if (lab.size() > 0){
                res += LabworksStorage.remove(lab.get((int) (Math.random() * lab.size())));
            }else {
                throw new NumberFormatException();
            }
        }catch (NumberFormatException e){
            res += "No such elements.";
        }
    }

    @Override
    public String getAnswer(){
        return res;
    }
}
package tools.commands.commands;

import data.LabworksStorage;
import data.LabWork;
import tools.commands.Command;

public class Remove_by_id extends Command {
    public Remove_by_id(){
        super("Remove_by_id", "Removes element of collection with ID provided.");
        hasData = true;
    }

    @Override
    public void execute(String data){
        //res = "";
        try {
            int id = Integer.parseInt(data);
            LabWork lab = LabworksStorage.searchById(id);
            res += LabworksStorage.remove(lab);
        }catch (NumberFormatException e){
            res += "Please provide valid id";
        }catch (NullPointerException e){
            res += "No element with such id.";
        }
    }

    @Override
    public String getAnswer(){
        return res;
    }
}
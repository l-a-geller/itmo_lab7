package tools.commands.commands;

import data.LabworksStorage;
import data.LabWork;
import tools.commands.Command;

public class Update extends Command{
    public Update(){
        super("Update", "Updates element of collection with IP provided");
        hasData = true;
    }
    @Override
    public void execute(String data){
        try {
            int id = Integer.parseInt(data);
            LabWork lab = LabworksStorage.searchById(id);
            if (lab == null){
                throw new NumberFormatException();
            }
            res += lab.update();
        }catch (NumberFormatException e){
            res += "Please provide valid id";
        }
    }

    @Override
    public String getAnswer(){
        return res;
    }
}
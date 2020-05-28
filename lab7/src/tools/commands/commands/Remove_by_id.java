package tools.commands.commands;

import data.LabworksStorage;
import data.LabWork;
import tools.DataBase.DataBaseConnector;
import tools.commands.Command;

public class Remove_by_id extends Command {
    public Remove_by_id(){
        super("Remove_by_id", "Removes element of collection with ID provided.");
        hasData = true;
    }

    @Override
    public void execute(String data){
        res = "";
        try {
            int id = Integer.parseInt(data);
            //System.out.println(id);
            LabWork lab = LabworksStorage.searchById(id);
            System.out.println(lab.getUser());
            System.out.println(this.login);
            if (lab.getUser().equals(this.login)) {
                res += LabworksStorage.remove(lab, login);
                DataBaseConnector.removeLab(lab, login);
            }else {
                res += "Labwork does not belong to you, sorry";
            }
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
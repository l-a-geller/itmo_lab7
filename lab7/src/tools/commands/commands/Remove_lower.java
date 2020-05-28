package tools.commands.commands;

import data.LabworksStorage;
import data.LabWork;
import tools.commands.Command;

import java.util.stream.Stream;

public class Remove_lower extends Command {

    //private boolean hasData = true;
    public Remove_lower(){
        super("Remove_lower", "Removes all elements lower than provided.");
        hasData = true;
    }
    @Override
    public void execute(String data){
        this.res = "";
        try {
            int id = Integer.parseInt(data);
            LabWork labWork = LabworksStorage.searchById(id);
            if (labWork == null){
                throw new NumberFormatException();
            }else {
                String oldres = res;
                LabworksStorage.getData().stream().filter(w -> labWork.compareTo(w) < 0).forEach(w -> res += LabworksStorage.remove((LabWork) w, login));
                if (oldres.equals(res)) {
                    //LabworksStorage.getData().stream().forEach(w -> res += LabworksStorage.remove((LabWork) w));
                    res += "No elements lower found";
                }
            }
        }catch (NumberFormatException e){
            res += "No such id";
        }
    }

    @Override
    public String getAnswer(){
        return res;
    }
}
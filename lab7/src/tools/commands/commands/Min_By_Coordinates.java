package tools.commands.commands;

import data.LabworksStorage;
import data.LabWork;
import tools.commands.Command;

import java.util.NoSuchElementException;

public class Min_By_Coordinates extends Command {
    public Min_By_Coordinates(){
        super("Min_By_Coordinates", "Prints any element with minimal coordinates.");
        hasData = false;
    }
    @Override
    public void execute(){
        this.res = "";
        try {
            LabWork lab = LabworksStorage.getMinByCoordinates();
            res += lab.print();
        }catch (NoSuchElementException e){
            res += "No elements in collection";
        }
    }

    @Override
    public String getAnswer(){
        return res;
    }
}
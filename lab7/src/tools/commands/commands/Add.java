package tools.commands.commands;

import data.LabWork;
import tools.commands.Command;

import java.util.Scanner;

public class Add extends Command {
    //boolean needsScanner;
    public Add(){
        super("Add","Adds an element");
        hasData = false;
        needsScanner = true;
    }

    public void execute(){
        res = "LabWork added";
        //System.out.println("EEEEXXXEEEECCC");
        //Loader loader = new Loader();
        //LabWork lab = loader.search(sc);
        //loader.load(lab);
    }

    @Override
    public String getAnswer(){
        return res;
    }
}
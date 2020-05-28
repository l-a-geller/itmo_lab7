package tools.commands.commands;

import tools.commands.Command;
import tools.commands.CommandInvoker;
//import tools.ParametrizedCommand;

import java.io.Serializable;
//import src.server.ParametrizedCommandInvoker;

public class Help extends Command implements Serializable {
    public Help(){
        super("Help","Prints information about available commands.");
        hasData = false;
    }
    @Override
    public void execute(){
        //CommandInvoker.getCommands().values().forEach(w -> res += "\n" + w.getName() + ": " + w.getDescription());                //// ??????????



        /*for (Command comm : CommandInvoker.getCommands().values()){
            System.out.println(comm.getName() + ": " + comm.getDescription());
        }*/
        /*for (ParametrizedCommand comm : CommandInvoker.getParametrizedCommands().values()){
            System.out.println(comm.getName() + ": " + comm.getDescription());
        }*/
    }

    @Override
    public String getAnswer(){
        return res;
    }
}
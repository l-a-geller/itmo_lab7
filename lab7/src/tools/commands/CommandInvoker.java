package tools.commands;

//import src.server.commands.*;
import tools.commands.*;
import tools.commands.commands.*;
import tools.commands.commands.Execute_Script;
import tools.commands.commands.Add;
import tools.commands.commands.Info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CommandInvoker{
    private static HashMap<String, Command> commands;
    //private static HashMap<String, ParametrizedCommand> parametrizedCommands;
    private static List<Command> lastCommands = new ArrayList<>();

    static {
        commands = new HashMap<>();
        Command info = commands.put("INFO", new Info());
        Command help = commands.put("HELP", new Help());
        Command show = commands.put("SHOW", new Show());
        Command clear = commands.put("CLEAR", new Clear());
        Command history = commands.put("HISTORY", new History());
        Command update = commands.put("UPDATE", new Update());
        Command remove_by_id = commands.put("REMOVE_BY_ID", new Remove_by_id());
        //Command save = commands.put("SAVE", new Save());
        Command add = commands.put("ADD", new Add());
        Command remove_lower = commands.put("REMOVE_LOWER", new Remove_lower());
        Command remove_any_by_minimal_point = commands.put("REMOVE_ANY_BY_MINIMAL_POINT", new Remove_Any_By_Minimal_Point());
        Command min_by_coordinates = commands.put("MIN_BY_COORDINATES", new Min_By_Coordinates());
        Command filter_by_discipline = commands.put("FILTER_BY_DISCIPLINE", new Filter_By_Discipline());
        Command execute_script = commands.put("EXECUTE_SCRIPT", new Execute_Script());
        Command quit = commands.put("QUIT", new Quit());

        //for logging in
        Command login = commands.put("LOGIN", new Login());
        Command register = commands.put("REGISTER", new Register());
    }

    public static void registerCommand(String commmandName, Command c){
        commands.put(commmandName, c);
    }

    public static Command getCommand(String name){
        Optional<Command> command = Optional.ofNullable(commands.get(name.toUpperCase()));
        return command.orElse(null);
    }

    /*public static ParametrizedCommand getParametrizedCommand(String name){
        Optional<ParametrizedCommand> command = Optional.ofNullable(parametrizedCommands.get(name.toUpperCase()));
        return command.orElse(null);
    }*/

    public static HashMap<String, Command> getCommands(){
        return commands;
    }

    /*public static HashMap<String, ParametrizedCommand> getParametrizedCommands(){
        return parametrizedCommands;
    }*/

    public static void loadToSavedCommands(Command comm){
        if (lastCommands.size() >= 7){ lastCommands = lastCommands.subList(0, 6); }
        lastCommands.add(comm);
    }

    public static String printSavedCommands(){
       // if (lastCommands.size() >= 7){
            String res = "Last commands:\n";
            for (Command comm: lastCommands){
                res += comm.getName() + "\n";
            }
            return res;
        //}else{ return "7 commands not entered yet. Perform more commands to enable this command"; }
    }
}

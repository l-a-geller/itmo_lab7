package tools.commands;

import data.LabWork;
import tools.Loader;
import tools.io.QuitException;
import tools.io.StreamRead;
import tools.io.StreamReadWriter;
import tools.io.Transport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class CommandInvoker {
    private static HashMap<String, Command> commands;
    private Thread hook;
    private StreamReadWriter ioServer;
    private boolean inScriptExecution;
    private StreamReadWriter io;
    private Scanner sc;
    private String login;
    private String password;
    private static boolean logged = true;

    public CommandInvoker(HashMap<String, Command> commandList, StreamReadWriter ioServer) {
        this.commands = commandList;
        this.ioServer = ioServer;
        //this.password = password;
        //this.login = login;
    }

    public void setData(String login, String password){
        this.login = login;
        this.password = password;
    }
    public static boolean getLogged(){
        return logged;
    }

    public static Command getCommand(String name){
        Optional<Command> command = Optional.ofNullable(commands.get(name.toUpperCase()));
        return command.orElse(null);
    }

    public static String printSavedCommands(){
        return "";
    }

    public boolean run(String str, StreamReadWriter ioi, Scanner scanner) throws QuitException, IOException {
        io = ioi;
        sc = scanner;

        try {
            if (!str.trim().equals("")) {

                String[] s = str.trim().toUpperCase().split(" ");
                Command command = commands.get(s[0]);

                if (isCommand(str.trim().toUpperCase(), scanner)) {
                    command.addUserInfo(login, password);                  // putting userParameters into Command object

                    //shitcode, change to sql needed
                    //command.lab = null;

                    Transport trans = new Transport(command);
                    System.out.println(command.getName());

                    this.ioServer.writeObj(trans);                        // writing to server
                    long start = System.currentTimeMillis();

                    System.out.println(" > > > Sent data to server............");

                    while (!this.ioServer.ready()) {
                        long finish = System.currentTimeMillis();
                        if (finish - start > 2000L) {
                            throw new QuitException();
                        }
                    }

                    System.out.println("> > > Second part...........");

                    boolean i = true;
                    while (this.ioServer.ready()) {
                        if (i){
                            i = false;
                            io.writeln(" >>> Answer from server >>> ");
                        }

                        String answer = this.ioServer.readLine();
                        if (answer.equals("NOT_LOGGED")){
                            return false;
                        }
                        if (answer.equals("NOT_REG")){
                            return false;
                        }
                        io.writeln(answer);              //reading from server
                    }
                } else {
                    //System.out.println("Not a correct command");
                }
            }
            return true;
            } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Unknown Command");
            return true;
        } catch (IllegalArgumentException e) {
            io.writeln("Скрипт составлен неверно");
            return true;
        }
    }






    public boolean isCommand(String line, Scanner sc)  {
       // try {
            String[] parts = line.split(" ", 2);
            String COMMAND = parts[0];
            Command command = CommandInvoker.getCommand(COMMAND);

            if (command == null){
                return false;
            }

            if (command.needsScanner){
                Loader loader = new Loader();
                LabWork lab = null;
                lab = loader.search(sc);
                String sqlString =  lab.getName() + "," +
                                    lab.getCoordinates().getX() + "," + lab.getCoordinates().getY() + "," +
                                    lab.getMin() + "," + lab.getMax() + "," +
                                    lab.getDiff().ordinal() + "," +
                                    lab.getDisc().toString() + "," + lab.getDisc().getLecture() + "," + lab.getDisc().getPractice() + "," + lab.getDisc().getSelfStudy();

                command.lab = sqlString;
            }

            try {
                if (command.hasData){
                    command.data = parts[1];
                }
            }catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Command must have an argument");
                return false;
            }


            if (command.needsExecutor){
                try {
                    if (inScriptExecution){
                        return false;
                    }

                    System.out.println("Script executing started");
                    command.data = parts[1];
                    System.out.println(command.data);
                    File file = new File(command.data.toLowerCase());
                    if (file.exists()){
                        try {
                            Scanner scanner1 = new Scanner(file);
                            System.out.println("scanning started");
                            //inScriptExecution = true;
                            while (scanner1.hasNext()) {

                                String l = scanner1.nextLine();
                                if (!(l.trim().toUpperCase().equals("EXIT") | l.trim().toUpperCase().equals("QUIT") )) {
                                    run(l, io, scanner1);
                                    }
                                }
                            } catch (QuitException ex) {
                            ex.printStackTrace();
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    catch (StackOverflowError e){ System.out.println("Invalid script. Please remove self calls."); }
                    }else { System.out.println("No such file"); }
                    return false;
                }catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("Command must have an argument");
                    return false;
                }
            }
            return true;
    }
}


















/*
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
    }

    public static void registerCommand(String commmandName, Command c){
        commands.put(commmandName, c);
    }

    public static Command getCommand(String name){
        Optional<Command> command = Optional.ofNullable(commands.get(name.toUpperCase()));
        return command.orElse(null);
    }



    public static HashMap<String, Command> getCommands(){
        return commands;
    }


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
}*/

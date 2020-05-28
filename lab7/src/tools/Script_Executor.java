package tools;

import tools.commands.Command;
import tools.commands.CommandInvoker;
//import tools.ParametrizedCommand;
//import src.server.ParametrizedCommandInvoker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Script_Executor {
    private File file;
    public Script_Executor(File f){
        this.file = f;
    }
    public void exec(){
        try {
            Scanner scanner = new Scanner(this.file);
            while (scanner.hasNext()){
                String line = scanner.nextLine();
                if (!line.trim().toUpperCase().equals("EXIT")) {

                    String[] parts = line.split(" ", 2);
                    String COMMAND = parts[0];

                    //ParametrizedCommand pc = CommandInvoker.getParametrizedCommand(line.trim().toUpperCase());
                    //if (pc != null){
                    //    pc.execute(scanner);
                    //}else {
                        Command comm = CommandInvoker.getCommand(COMMAND);
                        if(comm != null){
                            CommandInvoker.loadToSavedCommands(comm);

                            boolean hasArgument = false;
                            String ARG = null;
                            try {
                                ARG = parts[1];
                                hasArgument = true;
                            }catch (ArrayIndexOutOfBoundsException e){
                                //
                            }

                            if (hasArgument){
                                try {
                                    comm.execute(ARG);
                                }catch (ArrayIndexOutOfBoundsException e){
                                    System.out.println("No parameters expected for this command.");
                                }
                            }else {
                                try{
                                    comm.execute();
                                }catch (ArrayIndexOutOfBoundsException e){
                                    System.out.println("Parameter expected for this command.");
                                }
                            }
                        //}
                    }
                }else {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        }catch (NullPointerException e){
            System.out.println("invalid file");
        }
    }
}
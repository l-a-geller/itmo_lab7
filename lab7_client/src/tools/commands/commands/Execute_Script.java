package tools.commands.commands;

import tools.commands.Command;
import tools.Script_Executor;

import java.io.File;

public class Execute_Script extends Command {
    public Execute_Script(){
        super("Execute_Script", "Executes a script");
        hasData = true;
        needsExecutor = true;
    }
    @Override
    public void execute(String data){
        File file = new File(data);
        if (file.exists()){
            Script_Executor se = new Script_Executor(file);
            try {
                se.exec();
            }catch (StackOverflowError e){ res += "Invalid script. Please remove self calls."; }
        }else {
            res += "No such file";
        }
    }

    @Override
    public String getAnswer(){
        return res;
    }
}
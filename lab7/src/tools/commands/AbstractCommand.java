package tools.commands;

import java.io.Serializable;

public abstract class AbstractCommand implements Serializable {
    private String name;
    private String description;
    protected AbstractCommand(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getName(){return name;}
    public String getDescription(){return description;}
    public void execute() throws ArrayIndexOutOfBoundsException{
        throw new ArrayIndexOutOfBoundsException();
    }
    public void execute(String data) throws ArrayIndexOutOfBoundsException {
        throw new ArrayIndexOutOfBoundsException();
    }
}
package IO;

import tools.commands.Command;
import tools.io.Transport;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ReadTask implements Runnable {

    private SelectionKey selKey;
    private IOinterface ioClient;
    private Transport transport;
    private Command command;

    public ReadTask(SelectionKey selkey, IOinterface ioClient, Transport transport, Command command){
        this.selKey = selkey;
        this.ioClient = ioClient;
        this.transport = transport;
        this.command = command;
    }
    public void run(){
        try {
            ioClient = new IOClient((SocketChannel)selKey.channel(), true);
            transport = (Transport)ioClient.readObj();                                             // reading a command
            command = (Command) transport.getObject();
            System.out.println("Command received " + command.getName());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

import IO.IOClient;
import IO.IOinterface;
import data.*;
import tools.*;
import tools.DataBase.DataBaseConnector;
import tools.commands.Command;
import tools.commands.CommandInvoker;
import tools.connector.ClientHandler;
import tools.io.Transport;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.BindException;
import java.net.ConnectException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {
   public static boolean sent;
   private static ExecutorService ftp = Executors.newFixedThreadPool(2);
   private static ForkJoinPool fjp = new ForkJoinPool();
   private static ReadWriteLock rrl = new ReentrantReadWriteLock();

    /**
     * Is a loop handling incoming requests with help of Selector
     */
   public static void main(String[] args) throws IOException, StreamCorruptedException, ClassNotFoundException {

      loadLabs();
      try {
         ClientHandler clientHandler = new ClientHandler(Integer.parseInt(args[0]));
         IOinterface ioClient = null;

         Transport collectionSender = new Transport("map");
         HashMap<String, Command> l = CommandInvoker.getCommands();
         collectionSender.putObject(l);
         Command comm = null;

         while(true) {
            clientHandler.getSelector().select();
            Iterator iter = clientHandler.getSelector().selectedKeys().iterator();

            while(iter.hasNext()) {
               SelectionKey selKey = (SelectionKey)iter.next();
               iter.remove();

               try {
                  if (selKey.isValid()) {
                     if (selKey.isAcceptable()) { clientHandler.acceptConnect(); sent = false; }

                     if (selKey.isWritable()) {
                        if (!sent) {
                           ioClient = new IOClient((SocketChannel)selKey.channel(), true);
                           ioClient.writeObj(collectionSender); sent = true;                                    //sent a collection
                        } else {
                           Command finalComm = comm;
                           IOinterface finalIoClient = ioClient;
                           ftp.submit(() -> {
                              try {
                                 rrl.writeLock().lock();
                                 processCommand(finalComm, finalIoClient);                                               // processing a command
                              } catch (IOException e) {} finally { rrl.writeLock().unlock(); }
                           });
                        }
                        selKey.interestOps(1);
                     }

                     if (selKey.isReadable()) {
                         ioClient = new IOClient((SocketChannel)selKey.channel(), true);
                         IOinterface finalIoClient1 = ioClient;
                         Future<Command> future = ftp.submit(() -> (Command)((Transport) finalIoClient1.readObj()).getObject());

                         comm = future.get();
                         System.out.println("Command received " + comm.getName());
                         selKey.interestOps(4);
                     }
                  }
               } catch (ConnectException e) { System.out.println(e.getMessage());sent = false;
               } catch (InterruptedException e) { e.printStackTrace();
               } catch (ExecutionException e) { e.printStackTrace(); }
            }
         }
      } catch (IndexOutOfBoundsException ex) {
         System.out.println("Please provide PORT");
         System.exit(0);
      }catch (BindException e){ System.out.println("Address & port already in use"); }
   }

    /**
     * Executes a command, puts it into saved commands and sends result to client
     * @param comm A command to be executed
     * @param ioClient Interface for communicating with Client
     * @throws IOException
     */
   private static void processCommand(Command comm, IOinterface ioClient) throws IOException {
      String res = "";
      if (!comm.getName().equals("QUIT")){
         if (comm.needsScanner){
            String labWork = comm.lab;
            DataBaseConnector.writeLab(labWork, comm.login);
         }
         CommandInvoker.loadToSavedCommands(comm);
         if (comm.hasData) comm.execute(comm.data);
         else comm.execute();
         res = comm.getAnswer();
      }
      ioClient.writeln(res);                                            // writing to client
   }

    /**
     * Loads labs from DB
     */
   private static void loadLabs(){
      TreeSet<LabWork> labs = new TreeSet<>();
      ArrayList<LabWork> labWorks = DataBaseConnector.readLab();
      for (LabWork labWork: labWorks){ labs.add(labWork); }
      LabworksStorage.setData(labs);
      LabworksStorage.setData(LabworksStorage.getSortedByCoordinates());
   }
}

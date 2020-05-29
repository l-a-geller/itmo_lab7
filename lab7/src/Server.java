//java -cp lib/postgresql-42.2.12.jar; Server 9876

import IO.IOClient;
import IO.IOinterface;
import IO.ReadTask;
import data.*;
import tools.*;
import tools.DataBase.DataBaseConnector;
import tools.commands.Command;
import tools.commands.CommandInvoker;
import tools.connector.ClientHandler;
import tools.io.Transport;


import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
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

   //public static boolean logged = false;
  // private static DataBaseConnector dsc;

   public static void main(String[] args) throws IOException, ClassNotFoundException {

      //dsc = new DataBaseConnector();

      loadLabs();
      ClientHandler clientHandler = null;

      try {
         clientHandler = new ClientHandler(Integer.parseInt(args[0]));
         IOinterface ioClient = null;

         // receiving login & password
            //no, in a loop
         //

         Transport collectionSender = new Transport("map");
         HashMap<String, Command> l = CommandInvoker.getCommands();
         collectionSender.putObject(l);

         Transport trans = null;
         Command comm = null;

         while(true) {
            clientHandler.getSelector().select();
            Iterator iter = clientHandler.getSelector().selectedKeys().iterator();

            while(iter.hasNext()) {
               SelectionKey selKey = (SelectionKey)iter.next();
               iter.remove();

               try {
                  if (selKey.isValid()) {
                     if (selKey.isAcceptable()) {
                        clientHandler.acceptConnect();
                        sent = false;
                     }

                     if (selKey.isWritable()) {
                        if (!sent) {
                           ioClient = new IOClient((SocketChannel)selKey.channel(), true);
                           ioClient.writeObj(collectionSender);                                          //sent a collection
                           sent = true;
                        } else {
                           Command finalComm = comm;
                           IOinterface finalIoClient = ioClient;
                           ftp.submit(() -> {
                              try {
                                 rrl.writeLock().lock();
                                 processCommand(finalComm, finalIoClient);                                               // processing a command
                              } catch (IOException e) {
                                 //e.printStackTrace();
                              }finally {
                                 rrl.writeLock().unlock();
                              }
                           });
                        }

                        selKey.interestOps(1);
                     }

                     if (selKey.isReadable()) {
                        //ftp.execute(() -> {
                           ioClient = new IOClient((SocketChannel)selKey.channel(), true);
                           //trans = (Transport)ioClient.readObj();                                             // reading a command
                           //comm = (Command) trans.getObject();


                        IOinterface finalIoClient1 = ioClient;
                        Future<Command> future = ftp.submit(() -> (Command)((Transport) finalIoClient1.readObj()).getObject());

                        comm = future.get();
                        System.out.println("Command received " + comm.getName());
                        selKey.interestOps(4);
                        //ioClient = new IOClient((SocketChannel)selKey.channel(), true);
                        //IOinterface finalIoClient1 = ioClient;
                        //ftp.execute(() -> {
                          // try {

                              //trans = (Transport) finalIoClient1.readObj();                                             // reading a command
                              //Command com = (Command) trans.getObject();
                              //System.out.println("Command received " + com.getName());

                           //} catch (IOException | ClassNotFoundException e) {
                           //   e.printStackTrace();
                           //}

                        //});

                        //ReadTask task = new ReadTask(selKey, ioClient, trans, comm);
                        //Thread thread = new Thread(task);
                        //thread.start();
                        //ftp.execute(task);
                        //selKey.interestOps(4);
                        //});
                     }
                  }
               } catch (ConnectException e) {
                  System.out.println(e.getMessage());
                  sent = false;
               } catch (InterruptedException e) {
                  e.printStackTrace();
               } catch (ExecutionException e) {
                  e.printStackTrace();
               }
            }
         }
      } catch (IndexOutOfBoundsException ex) {
         System.out.println("Please provide PORT");
         System.exit(0);
      }catch (BindException e){
         System.out.println("Address & port already in use");
      }
   }

   private static void processCommand(Command comm, IOinterface ioClient) throws IOException {

      System.out.println(comm.getName());
      String res = "";
      if (comm.getName().equals("QUIT")){
         Java2DB j2 = new Java2DB();
         j2.writeXML();
	 System.out.println("Collection saved");
      }else {
         if (comm.needsScanner){
            String labWork = comm.lab;
            System.out.println(labWork);
            DataBaseConnector.writeLab(labWork, comm.login);

            //comm.res += LabworksStorage.put(labWork);
         }
         CommandInvoker.loadToSavedCommands(comm);
         if (comm.hasData){
            comm.execute(comm.data);
         }else {
            comm.execute();
         }
         res = comm.getAnswer();
      }
      ioClient.writeln(res);                                            // writing to client
   }

   private static void loadLabs(){
      //File file = new File("data.xml");
      //XMLScanner xsc = new XMLScanner();
      TreeSet<LabWork> labs = new TreeSet<>();
      ArrayList<LabWork> labWorks = DataBaseConnector.readLab();
      for (LabWork labWork: labWorks){
         labs.add(labWork);
      }
      LabworksStorage.setData(labs);
      LabworksStorage.setData(LabworksStorage.getSortedByCoordinates());
   }
}

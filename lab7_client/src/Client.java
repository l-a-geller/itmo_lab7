import tools.commands.Command;
import tools.commands.CommandInvoker;
import tools.connector.ServerConnector;
import tools.io.StreamReadWriter;
import tools.io.Transport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Client {
   private static boolean logged = false;
   public static Scanner scanner = new Scanner(System.in);
   public static void main(String[] args) throws IOException, ClassNotFoundException {
      StreamReadWriter iOclient = new StreamReadWriter(System.in, System.out, true);

      try {
         ServerConnector serverConnector = new ServerConnector();
         if (args.length >= 2) {
            try {
               serverConnector.connect(args[0], Integer.parseInt(args[1])); // connecting to server
            } catch (NumberFormatException e) {
               System.out.println("PORT is incorrect");
               System.exit(0);
            }
         } else {
            System.out.println("Please provide host and port");
            System.exit(0);
         }

         StreamReadWriter ioServer = new StreamReadWriter(serverConnector.getInputStream(), serverConnector.getOutputStream(), true); //provides Input / Output Streams

         while(!ioServer.ready()) {
         }

         Transport fromServer = (Transport)ioServer.readObj(); // reading from ObjectInputStream
         HashMap<String, Command> commandMap = (HashMap)fromServer.getObject();    // reading collection from server



         CommandInvoker commandInvoker = new CommandInvoker(commandMap, ioServer);
         String logComand = "LOGIN";
         while(true) {
            String commName = logged ? scanner.nextLine().trim() : logComand ;
            //System.out.println(commName);
            try {
               if (commName.equals("LOGIN")){
                  System.out.println("Enter login & password");
                  System.out.print(" >>> login: ");
                  String login = scanner.nextLine().trim();
                  System.out.print(" >>> password: ");
                  String password = scanner.nextLine().trim();
                  commandInvoker.setData(login, password);
               }
               //System.out.println("Logging... " + commName);
               logged = commandInvoker.run(commName, iOclient, scanner);
               //System.out.println("logged: "+logged);
            } catch (StackOverflowError e) {
               System.out.println("StackOverflowError threat");
            }
            if (!logged){
               System.out.println("Login failed. Enter R to register, A to try again, ANY OTHER KEY to quit");
               Scanner sc1 = new Scanner(System.in);
               String flag = sc1.nextLine().trim();
               if (flag.equals("R")){

                  System.out.println("Enter login & password for registration");
                  System.out.print(" >>> login: ");
                  String login = sc1.nextLine().trim();
                  System.out.print(" >>> password: ");
                  String password = sc1.nextLine().trim();
                  commandInvoker.setData(login, password);

                  if (commandInvoker.run("REGISTER", iOclient, scanner)){
                      System.out.println("User successfully registered " + login);
                  }
                  //System.out.println("Logging... " + "REGISTER " + " Logged: " + logged);
               }else if (flag.equals("A")){
                  continue;
               }else break;
            }
            //System.out.println("LOGGED" + commandInvoker.getLogged());
            //if (commandInvoker.getLogged()){
            //   logged = true;
            //}
        }
      } catch (NullPointerException | IOException e) {
         e.printStackTrace();
         System.out.println("App stopped working, data saved");
      }catch (Exception ex){
         ex.printStackTrace();
         System.out.println("quiting");
      }
   }
}

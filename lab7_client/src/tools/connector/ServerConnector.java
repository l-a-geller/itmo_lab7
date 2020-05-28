package tools.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerConnector {
   Socket soc;
   //IOconsole ioclient;

   public Socket connect(String host, Integer port) throws IOException {
      try {
         this.soc = new Socket(host, port);
         System.out.println("Connection successful");
         return this.soc;
      } catch (UnknownHostException | ConnectException var4) {
         System.out.println("Troubles while connecting to server, host or port may be incorrect");
         System.exit(0);
         return null;
      }
   }

   public InputStream getInputStream() throws IOException {
      return this.soc.getInputStream();
   }

   public OutputStream getOutputStream() throws IOException {
      return this.soc.getOutputStream();
   }

   public void close() throws IOException {
      this.soc.close();
   }
}

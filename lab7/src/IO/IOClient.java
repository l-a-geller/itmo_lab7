package IO;

import tools.Java2DB;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class IOClient implements IOinterface {
   private boolean interactive;
   SocketChannel socCh;

   public IOClient(SocketChannel socCh, boolean interactive) throws IOException {
      this.interactive = interactive;
      this.socCh = socCh;
   }

   public void write(String str) throws IOException {
      ByteBuffer bb = ByteBuffer.wrap(str.getBytes());
      this.socCh.write(bb);
      System.out.println(str);
   }

   public void writeln(String str) throws IOException {
      this.write(str + "\n");
   }

   public String readLine() throws IOException {
      return null;
   }

   public boolean hasNextLine() throws IOException {
      return false;
   }

   public boolean ready() {
      return false;
   }

   public boolean isInteractive() {
      return false;
   }

   public void writeObj(Object obj) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(obj);
      oos.flush();
      this.socCh.write(ByteBuffer.wrap(baos.toByteArray()));
   }

   public Object readObj() throws IOException, ClassNotFoundException {
      ByteBuffer bb = ByteBuffer.allocate(5120);

      try {
         this.socCh.read(bb);
         return (new ObjectInputStream(new ByteArrayInputStream(bb.array()))).readObject();
      } catch (IOException var3) {
         this.socCh.close();

         Java2DB j2 = new Java2DB();
         j2.writeXML();

         throw new ConnectException("Connection aborted");
      }
   }

   public Object read() {
      return null;
   }

   public void close() throws IOException {
   }
}

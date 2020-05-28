package IO;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Scanner;

public class IOconsole implements IOinterface {
   private Writer output;
   private Reader input;
   private Scanner scan;
   private InputStream in;
   private OutputStream out;
   private boolean interactive;

   public IOconsole(InputStream in, OutputStream out, boolean interactive) throws IOException {
      this.interactive = interactive;
      this.in = in;
      this.out = out;
      this.output = new OutputStreamWriter(out);
      this.scan = new Scanner(in);
   }

   public void write(String str) {
      try {
         this.output.write(str);
         this.output.flush();
      } catch (IOException var3) {
         this.write(var3.getMessage());
      }

   }

   public void writeln(String str) {
      this.write(str + "\n");
   }

   public String readLine() {
      return this.scan.nextLine();
   }

   public boolean hasNextLine() {
      return this.scan.hasNextLine();
   }

   public boolean ready() {
      return this.scan.hasNext();
   }

   public boolean isInteractive() {
      return this.interactive;
   }

   public void writeObj(Object obj) throws IOException {
      (new ObjectOutputStream(this.out)).writeObject(obj);
   }

   public Object readObj() throws IOException, ClassNotFoundException {
      return (new ObjectInputStream(this.in)).readObject();
   }

   public Object read() {
      return this.scan.next();
   }

   public void close() throws IOException {
      this.output.close();
   }
}

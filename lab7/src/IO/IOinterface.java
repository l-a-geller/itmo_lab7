package IO;

import java.io.IOException;

public interface IOinterface {
   void write(String var1) throws IOException;

   void writeln(String var1) throws IOException;

   String readLine() throws IOException;

   boolean hasNextLine() throws IOException;

   boolean ready();

   boolean isInteractive();

   void writeObj(Object var1) throws IOException;

   Object readObj() throws IOException, ClassNotFoundException;

   Object read();

   void close() throws IOException;
}

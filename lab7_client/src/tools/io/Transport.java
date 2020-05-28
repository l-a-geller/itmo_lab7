package tools.io;

import java.io.Serializable;

public class Transport implements Serializable {
    Object obj;
    String request;

    public Transport(String requestString) {
        this.request = requestString;
    }
    public Transport(Object obj) {
        this.obj = obj;
    }

    public String getRequest() {
        return this.request;
    }

    public Object getObject() {
        return this.obj;
    }

    public void putObject(Object obj) {
        this.obj = obj;
    }
}

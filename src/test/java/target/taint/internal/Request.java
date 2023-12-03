package target.taint.internal;

// Custom Source
public class Request {

    private String req;

    public Request(String req) {
        this.req = req;
    }

    public String get(){
        return req;
    }
}

package target.taint;
import target.taint.internal.Request;
import target.taint.internal.QueryExecuter;

import java.util.Objects;

// CODE ADAPTED FROM DR. KARIM ALI
public class CustomServlet {
    public static void main(String[] args){

        String user;
        Request request = new Request("$");
        String userRaw = request.get();

        if (!Objects.equals(userRaw, "")) {
            user = userRaw;
        } else {
            user = "defaultuser0";
        }

        QueryExecuter queryExecuter = new QueryExecuter(); 
        try {
            queryExecuter.validateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

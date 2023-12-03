package target.taint;
import target.taint.internal.Request;
import target.taint.internal.QueryExecuter;

import java.util.Objects;

// CODE ADAPTED FROM DR. KARIM ALI
public class CustomServlet {
    public static void main(String[] args){

        String user;
        Request request = new Request("$");
        String userRaw = request.get(); // Using custom Request class

        if (!Objects.equals(userRaw, "")) {
            user = userRaw;
        } else {
            user = "defaultuser0";
        }

        QueryExecuter queryExecuter = new QueryExecuter(); // Using custom QueryExecuter
        try {
            queryExecuter.validateUser(user); // Execute the query using custom sink
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

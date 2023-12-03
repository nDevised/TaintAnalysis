package target.taint;
import target.taint.internal.Request;
import target.taint.internal.QueryExecuter;

// CODE ADAPTED FROM DR. KARIM ALI
public class CustomServlet {
    public static void main(String[] args){

        Request request = new Request("$");
        String studentId = request.get(); // Using custom Request class
        StringBuilder sb = new StringBuilder("SELECT * FROM Students WHERE studId='");

        boolean var1 = true; // Replace this with your actual condition

        if (var1) {
            sb.append(studentId);
        } else {
            sb.append("123456");
        }

        sb.append("'");
        String query = sb.toString();

        QueryExecuter queryExecuter = new QueryExecuter(); // Using custom QueryExecuter
        try {
            queryExecuter.executeQuery(query); // Execute the query using custom sink
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

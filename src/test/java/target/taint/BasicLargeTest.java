package target.taint;
import target.taint.internal.InputHandler;
import target.taint.internal.Request;
import target.taint.internal.QueryExecuter;
import java.util.Objects;
public class BasicLargeTest {
    public static void main(String[] args) {
        String userInput;
        Request request = new Request("$");
        String rawInput = request.get();
        InputHandler inputHandler = new InputHandler();
        if (!Objects.equals(rawInput, "")) {
            userInput = rawInput;
        } else {
            userInput = "defaultuser0";
        }
        QueryExecuter queryExecuter = new QueryExecuter();
        try {
            queryExecuter.validateUser(userInput);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userInput = userInput.trim().toLowerCase();
        String reverse = new StringBuilder(userInput).reverse().toString();
        boolean isPalindrome = userInput.equalsIgnoreCase(reverse);
        if (isPalindrome) {
            System.out.println("Input is a palindrome");
        } else {
            System.out.println("Input is not a palindrome");
        }
        String processedInput;
        if (userInput.length() > 100) {
            processedInput = "Input is too long";
        } else if (userInput.isEmpty()) {
            processedInput = "Input is empty";
        } else if (userInput.contains("$")) {
            processedInput = "Invalid Input";
        } else if (userInput.matches("[0-9]+")) {
            processedInput = "Input should not be numeric only";
        } else if (!userInput.matches("[A-Za-z0-9 ]+")) {
            processedInput = "Input contains special characters";
        } else {
            processedInput = userInput;
        }
        String message;
        if (processedInput.isEmpty()) {
            message = "No input provided";
        } else if (processedInput.matches(".*[<>{}].*")) {
            message = "Input contains invalid characters";
        } else {
            if (processedInput.length() < 5) {
                message = "Input is too short";
            } else if (processedInput.equalsIgnoreCase("admin")) {
                message = "Admin access is not allowed";
            } else {
                message = "Input handling completed";
            }
        }
        System.out.println(message);
        inputHandler.passInput(processedInput); // Pass the input only if it passes initial checks
    }
}

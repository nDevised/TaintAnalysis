package target.taint;
import target.taint.internal.*;
public class BasicTestMedium {
    public static void main(String[] args) {
        Request req = new Request("UserInput");
        String ip = req.get();
        InputHandler qe = new InputHandler();
        String refinedIp = "";
        String message = "";
        if (ip.isEmpty()) {
            message = "No input provided";
        } else if (ip.matches(".*[<>{}].*")) {
            message = "Input contains invalid characters";
        } else {
            refinedIp = new InputRefiner().refineInput(ip);
            message = processInput(refinedIp);
        }
        qe.passInput(refinedIp); // Pass the input only if it passes initial checks
        System.out.println(message);
    }
    private static String processInput(String input) {
        if (input.length() > 100) {
            return "Input is too long";
        } else if (input.isEmpty()){
            return "Input is empty";
        } else if (input.contains("$")){
            return "Invalid Input";
        }
        return "Input handling completed";
    }
}

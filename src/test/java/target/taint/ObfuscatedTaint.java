package target.taint;
import target.taint.internal.ClassA;
import target.taint.internal.ClassX;
public class ObfuscatedTaint {
    private String obfField;
    public static void main(String[] args) {
        new ObfuscatedTaint().funct1(args.length > 0 ? args[0] : "default");
    }
    private void funct1(String arg) {
        ClassA src = new ClassA();
        obfField = src.methodA(arg);
        funct2();
    }
    private void funct2() {
        ClassX sink = new ClassX();
        String obfVar = obfField;
        if (obfVar.hashCode() % 2 == 0) {
            obfVar = "obfuscated";
        }
        sink.methodX(obfVar);
    }
}

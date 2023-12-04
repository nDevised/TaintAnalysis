package target.taint;
import target.taint.internal.ClassA;
import target.taint.internal.ClassX;
import target.taint.internal.SampleFields;
public class ObfuscatedTaint {
    public static void main(String[] args) {
        ClassA alpha = new ClassA();
        ClassX omega = new ClassX();
        SampleFields fields = new SampleFields();
        String data1 = "info1";
        fields.x = alpha.methodA();
        String intermediateVar;
        if (fields.x.length() > 5) {
            intermediateVar = fields.x;
        } else {
            intermediateVar = data1;
        }
        fields.y = intermediateVar;
        fields.z = "info2";
        omega.methodX(fields.y);
    }
}

package target.taint;
import target.taint.internal.SourceClass;
import target.taint.internal.SinkClass;
public class OverallTestLarge {
    private static String staticString;
    private String x;
    private String y;
    public static void main(String[] args) {
        OverallTestLarge combined = new OverallTestLarge();
        SourceClass sc1 = new SourceClass();
        staticString = sc1.anInstanceSource();
        String b1 = staticString;
        String a2 = "";
        if (args[0].isEmpty()) {
            a2 = sc1.anInstanceSource();
        } else {
            a2 = sc1.anInstanceSource();
        }
        String b2 = a2;
        String a3 = sc1.anInstanceSource();
        if (args[0].isEmpty()) {
            a3 = "";
        } else {
            a3 = "";
        }

        String b3 = a3;
        assignStaticSource();
        combined.x = sc1.anInstanceSource();
        String a5 = combined.x;
        String a6 = "";
        for(int i = 0; i < 10; i++) {
            a6 = SourceClass.aStaticSource();
        }

        String a7 = sourceInside();
        String a8 = a7;

        if (args.length > 0){
            combined.y = a7;
        } else if (args.length == 1) {
            combined.x = combined.y;
        } else{
            b3 = a6;
            a6 = a3;
        }
        SinkClass sink = new SinkClass();
        sink.anInstanceSink(b1); // Using instance sink for the first example
        SinkClass.aStaticSink(b2); // Using static sink for the second example
    }
    static void assignStaticSource() {
        staticString = SourceClass.aStaticSource();
    }
    static String sourceInside() {
        SourceClass sc = new SourceClass();
        return sc.anInstanceSource();
    }
}

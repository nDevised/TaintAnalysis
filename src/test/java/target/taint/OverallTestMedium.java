package target.taint;
import target.taint.internal.SourceClass;
import target.taint.internal.SinkClass;
public class OverallTestMedium {
    // This main method combines various scenarios from the provided snippets
    public static void main(String[] args) {
        SourceClass sc = new SourceClass();

        // Assignment tests
        String assignmentInstance = sc.anInstanceSource();
        String assignmentStatic = SourceClass.aStaticSource();

        // Branching tests
        String branchingIf = "";
        if (args.length > 0 && args[0].equals("")) {
            branchingIf = sc.anInstanceSource();
        }

        String branchingElse = branchingIf;

        // Field tests
        FieldTest fieldTest = new FieldTest();
        fieldTest.x = sc.anInstanceSource();
        fieldTest.y = SourceClass.aStaticSource();

        // Loop test
        String loopVar = "";
        for (int i = 0; i < 5; ++i) {
            loopVar = SourceClass.aStaticSource();
        }

        // Sink calls
        SinkClass sink = new SinkClass();
        sink.anInstanceSink(assignmentInstance);
        SinkClass.aStaticSink(assignmentStatic);
        sink.anInstanceSink(branchingIf);
        sink.anInstanceSink(branchingElse);
        sink.anInstanceSink(fieldTest.x);
        sink.anInstanceSink(fieldTest.y);
        sink.anInstanceSink(loopVar);
    }

    private static class FieldTest {
        String x;
        String y;
    }
}

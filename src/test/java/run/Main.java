package run;

import analysis.IFDSTaintAnalysisProblem;
import analysis.data.DFF;
import boomerang.scene.jimple.BoomerangPretransformer;
import heros.InterproceduralCFG;
import org.junit.Test;
import soot.*;
import soot.jimple.toolkits.ide.JimpleIFDSSolver;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;
import soot.options.Options;
import test.base.IFDSTestSetUp;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
public class Main {
    // The IFDSTestSetupClass is being re-used for the purp
    public static void main(String[] args) {
        // Must input args otherwise send message to user
        List<SootMethodRef> sources = new ArrayList<>();
        List<SootMethodRef> sinks = new ArrayList<>();
        SootClass sourceClass = new SootClass("target.taint.internal.SourceClass");
        SootMethodRef source1 = new SootMethodRefImpl(sourceClass, "anInstanceSource", Collections.emptyList(), RefType.v("java.lang.String"), false);
        SootMethodRef source2 = new SootMethodRefImpl(sourceClass, "aStaticSource", Collections.emptyList(), RefType.v("java.lang.String"), true);

        SootClass sinkClass = new SootClass("target.taint.internal.SinkClass");
        SootMethodRef sink1 = new SootMethodRefImpl(sinkClass, "anInstanceSink", Collections.emptyList(), RefType.v("java.lang.String"), false);
        SootMethodRef sink2 = new SootMethodRefImpl(sinkClass, "aStaticSink", Collections.emptyList(), RefType.v("java.lang.String"), true);

        sources.add(source1);
        sources.add(source2);
        sinks.add(sink1);
        sinks.add(sink2);

        // Create the Scene Transformer

    }

}



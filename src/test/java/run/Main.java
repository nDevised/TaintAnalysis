package run;

import analysis.IFDSTaintAnalysisProblem;
import analysis.data.DFF;
import boomerang.scene.jimple.BoomerangPretransformer;
import heros.InterproceduralCFG;
import soot.*;
import soot.jimple.toolkits.ide.JimpleIFDSSolver;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;
import soot.options.Options;

import java.io.File;
import java.util.*;

public class Main {
    /**
     * The main entry point of the program.
     * NOTE: This analysis is designed so that only Strings are tainted
     * @param args Command line arguments, expects a single argument: the fully qualified name of the class to analyze.
     */
    protected static JimpleIFDSSolver<?, ?> solver = null;

    // CLASS_NAME MUST BE SET
    // ONLY ENTRY POINT POSSIBLE IS MAIN
    // BEFORE PROCEEDING SET SOURCE1, SOURCE2, SINK1, SINK2, or set DEFAULT = TRUE for default config.
    // ALSO SET THE STATIC STATUS OF
    // YOU CAN LEAVE SOURCE2 AND SINK2 as "" IF YOU AREN'T PLANNING ON USING A STATIC SOURCE FROM THE SAME CLASS
    // SOURCES AND SINKS ARE LOCATED IN "target.taint.internal"

    private static final String CLASS_NAME = "target.taint.Loop2";
    private final boolean DEFAULT = true;
    private final String  SOURCE_CLASS = "target.taint.internal.ClassA";
    private final String  SOURCE = "methodA";
    private final String STATIC_SOURCE = "";
    private final String  SINK_CLASS = "target.taint.internal.ClassX";
    private final String  SINK = "methodX";
    private final String STATIC_SINK = "";
    public static void main(String[] args) {

        String targetClass = CLASS_NAME;
        Main main = new Main();
        main.run(targetClass);

    }

    /**
     * Executes the taint analysis on the specified class.
     *
     * @param targetClass The fully qualified name of the class to analyze.
     */
    public void run(String targetClass){
        JimpleIFDSSolver<?, ? extends InterproceduralCFG<Unit, SootMethod>> analysis = executeStaticAnalysis(targetClass);
        Set<String> defaultIDEResult = getResult(analysis);
        displayResults(defaultIDEResult);

    }

    /**
     * Sets up the Soot framework for static analysis.
     *
     * @param targetClass The fully qualified name of the class to set up for analysis.
     */
    private void setupSoot(String targetClass) {

        G.reset();
        String userdir = System.getProperty("user.dir");
        String javaHome = System.getProperty("java.home");

        String sootCp = userdir + File.separator + "target" + File.separator + "test-classes"+ File.pathSeparator + javaHome + File.separator +"lib"+File.separator+"rt.jar";
        Options.v().set_soot_classpath(sootCp);

        // We want to perform a whole program, i.e. an interprocedural analysis.
        // We construct a basic CHA call graph for the program
        Options.v().set_whole_program(true);
        Options.v().setPhaseOption("cg.cha", "on");
        Options.v().setPhaseOption("cg", "all-reachable:true");

        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().setPhaseOption("jb", "use-original-names:true");
        Options.v().setPhaseOption("jb.ls", "enabled:false");
        Options.v().set_prepend_classpath(false);

        Scene.v().addBasicClass("java.lang.StringBuilder");
        SootClass c = Scene.v().forceResolve(targetClass, SootClass.BODIES);
        if (c != null) {
            c.setApplicationClass();
        }
        Scene.v().loadNecessaryClasses();

    }

    /**
     * Retrieves the entry point method for the analysis.
     *
     * @return The SootMethod that serves as the entry point of the analysis.
     */
    protected SootMethod getEntryPointMethod() {
        for (SootClass c : Scene.v().getApplicationClasses()) {
            for (SootMethod m : c.getMethods()) {
                if (!m.hasActiveBody()) {
                    continue;
                }
                if (m.getName().equals("entryPoint") || m.toString().contains("void main(java.lang.String[])")) {
                    return m;
                }
            }
        }
        throw new IllegalArgumentException("Method does not exist in scene!");
    }

    /**
     * Registers the transformers required for the Soot analysis.
     * Transformers are useful to perform the analysis in SOOT
     */
    private void registerSootTransformers() {
        Transform transform = new Transform("wjtp.ifds", createAnalysisTransformer());
        PackManager.v().getPack("wjtp").add(transform);
    }

    /**
     * Creates a SceneTransformer for the purpose of taint analysis.
     *
     * @return A SceneTransformer configured for taint analysis.
     */
    protected Transformer createAnalysisTransformer() {
        List<SootMethodRef> sources = new ArrayList<>();
        List<SootMethodRef> sinks = new ArrayList<>();
        SootClass sourceClass;
        SootMethodRef source1;
        SootMethodRef source2;
        SootClass sinkClass;
        SootMethodRef sink1;
        SootMethodRef sink2;

        if (DEFAULT){
            sourceClass = new SootClass("target.taint.internal.SourceClass");
            source1 = new SootMethodRefImpl(sourceClass, "anInstanceSource", Collections.emptyList(), RefType.v("java.lang.String"), false);
            source2 = new SootMethodRefImpl(sourceClass, "aStaticSource", Collections.emptyList(), RefType.v("java.lang.String"), true);

            sinkClass = new SootClass("target.taint.internal.SinkClass");
            sink1 = new SootMethodRefImpl(sinkClass, "anInstanceSink", Collections.emptyList(), RefType.v("java.lang.String"), false);
            sink2 = new SootMethodRefImpl(sinkClass, "aStaticSink", Collections.emptyList(), RefType.v("java.lang.String"), true);
            sources.add(source1);
            sources.add(source2);
            sinks.add(sink1);
            sinks.add(sink2);
        } else{
            sourceClass = new SootClass(SOURCE_CLASS);
            source1 = new SootMethodRefImpl(sourceClass, SOURCE, Collections.emptyList(), RefType.v("java.lang.String"), false);

            if (STATIC_SOURCE != ""){
                source2 = new SootMethodRefImpl(sourceClass, STATIC_SOURCE, Collections.emptyList(), RefType.v("java.lang.String"), true);
                sources.add(source2);
            }
            sinkClass = new SootClass(SINK_CLASS);
            sink1 = new SootMethodRefImpl(sinkClass, SINK, Collections.emptyList(), RefType.v("java.lang.String"), false);

            if (STATIC_SINK != ""){
                sink2 = new SootMethodRefImpl(sinkClass, STATIC_SINK, Collections.emptyList(), RefType.v("java.lang.String"), true);
                sinks.add(sink2);
            }
            sources.add(source1);
            sinks.add(sink1);
        }


        return new SceneTransformer() {
            @Override
            protected void internalTransform(String phaseName, Map<String, String> options) {
                JimpleBasedInterproceduralCFG icfg = new JimpleBasedInterproceduralCFG(false);
                IFDSTaintAnalysisProblem problem = new IFDSTaintAnalysisProblem(icfg, sources, sinks);
                @SuppressWarnings({"rawtypes", "unchecked"})
                JimpleIFDSSolver<?, ?> solver = new JimpleIFDSSolver<>(problem);
                solver.solve();
                Main.solver = solver; // check, this might be problematic
            }
        };
    }

    /**
     * Extracts the taint analysis results from the analysis object.
     *
     * @param analysis The analysis object from which to extract results.
     * @return A set of strings representing the taint analysis results.
     */
    private Set<String> getResult(Object analysis) {
        SootMethod m = getEntryPointMethod();
        Map<DFF, Integer> res = null;
        Set<String> result = new HashSet<>();
        if (analysis instanceof JimpleIFDSSolver) {
            JimpleIFDSSolver solver = (JimpleIFDSSolver) analysis;
            res = (Map<DFF, Integer>) solver.resultsAt(m.getActiveBody().getUnits().getLast());
        }
        for (Map.Entry<DFF, Integer> e : res.entrySet()) {
            result.add(e.getKey().toString());
        }
        return result;
    }

    /**
     * Displays the taint analysis results.
     *
     * @param defaultIDEResult A set of strings representing the taint analysis results.
     */
    private void displayResults(Set<String> defaultIDEResult) {
        if (defaultIDEResult.isEmpty()) {
            System.out.println("No tainted elements found.");
        } else {
            System.out.println("Tainted elements:");
            for (String element : defaultIDEResult) {
                System.out.println(element);
            }
        }
    }

    /**
     * Executes the static analysis using Soot.
     *
     * @param targetTestClassName The fully qualified name of the test class to analyze.
     * @return An instance of JimpleIFDSSolver after completing the analysis.
     */
    protected JimpleIFDSSolver<?, ?> executeStaticAnalysis(String targetTestClassName) {
        setupSoot(targetTestClassName);
        registerSootTransformers();
        executeSootTransformers();
        if (solver == null) {
            throw new NullPointerException("Something went wrong solving the IFDS problem!");
        }
        return solver;
    }

    /**
     * Executes the necessary Soot transformers as part of the analysis process.
     */
    private void executeSootTransformers() {
        //Apply all necessary packs of soot. This will execute the respective Transformer
        PackManager.v().getPack("cg").apply();
        // Must have for Boomerang
        BoomerangPretransformer.v().reset();
        BoomerangPretransformer.v().apply();
        PackManager.v().getPack("wjtp").apply();
    }

}



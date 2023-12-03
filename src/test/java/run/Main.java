package run;

import analysis.IFDSTaintAnalysisProblem;
import analysis.data.DFF;
import boomerang.scene.jimple.BoomerangPretransformer;
import heros.InterproceduralCFG;
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
    protected static JimpleIFDSSolver<?, ?> solver = null;
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: Main <class-to-analyze>");
            return;
        }

        String targetClass = args[0];
        Main main = new Main();
        main.run(targetClass);

    }

    public void run(String targetClass){
        JimpleIFDSSolver<?, ? extends InterproceduralCFG<Unit, SootMethod>> analysis = executeStaticAnalysis(targetClass);
        Set<String> defaultIDEResult = getResult(analysis);
        displayResults(defaultIDEResult);

    }

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

    private void registerSootTransformers() {
        Transform transform = new Transform("wjtp.ifds", createAnalysisTransformer());
        PackManager.v().getPack("wjtp").add(transform);
    }

    protected Transformer createAnalysisTransformer() {
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

    protected JimpleIFDSSolver<?, ?> executeStaticAnalysis(String targetTestClassName) {
        setupSoot(targetTestClassName);
        registerSootTransformers();
        executeSootTransformers();
        if (solver == null) {
            throw new NullPointerException("Something went wrong solving the IFDS problem!");
        }
        return solver;
    }

    private void executeSootTransformers() {
        //Apply all necessary packs of soot. This will execute the respective Transformer
        PackManager.v().getPack("cg").apply();
        // Must have for Boomerang
        BoomerangPretransformer.v().reset();
        BoomerangPretransformer.v().apply();
        PackManager.v().getPack("wjtp").apply();
    }



}



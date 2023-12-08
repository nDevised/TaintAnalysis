# TaintAnalysis

TaintAnalysis is a static analysis tool developed using the Soot framework. It is designed to identify and analyze potentially tainted data sources and their propagation through Java programs.

## Getting Started

### Prerequisites

Ensure you have the following installed:
- [Git](https://git-scm.com/)
- [Java JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)

### Cloning the Repository

Use the GitHub desktop app or the command line to clone the repository:

```bash
git clone https://github.com/nDevised/TaintAnalysis.git
```

### Setting Up Dependencies
   - These need manual installation as Maven imports are no longer functional due to being depreciated. Our project requires the following dependencies:

1. **Heros**: [https://github.com/Sable/heros](https://github.com/Sable/heros)
2. **BoomerangPDS**: [https://github.com/CodeShield-Security/SPDS](https://github.com/CodeShield-Security/SPDS)
3. **WPDS**: [Google Drive Link](https://drive.google.com/drive/folders/1Rikse-1NYmkVyzCEMveY8NsXX2fWGsBE?usp=sharing)
   - Note: Original download not available, so hosted on Google Drive.
4. **PDS**: [https://mvnrepository.com/artifact/de.fraunhofer.iem/synchronizedPDS/2.5.1](https://mvnrepository.com/artifact/de.fraunhofer.iem/synchronizedPDS/2.5.1)
5. **Path Expression**: [https://mvnrepository.com/artifact/de.fraunhofer.iem/pathexpression](https://mvnrepository.com/artifact/de.fraunhofer.iem/pathexpression)
links.
6. Google Drive Archive (backup): [Google Drive Link](https://drive.google.com/drive/folders/1wPsFTwMdf0AIYBiDTeZbnXPk3idQja-h?usp=sharing)
7. COMEX: https://pypi.org/project/comex/ (For visual CFGS)
8. GraphViz: https://graphviz.org/download/ (For compiling COMEX CFGs)
9. Python: 3.8 https://www.python.org/downloads/release/python-380/
### Configuring IntelliJ IDEA

1. Open the cloned repository in IntelliJ IDEA.
2. Navigate to `File > Project Structure > Libraries`.
3. Manually install all the downloaded jar files.

### Verification

If you see a specific message in IntelliJ IDEA (not specified in the document), it means the repository is correctly set up on your computer.

## Taint Analysis Using SOOT

The TaintAnalysis tool is based on a fork of a repository by kadirayk and utilizes the SOOT framework for taint analysis. It requires the specification of source and sinks manually by the analysis designer.

### Main Features

- Analysis of field sensitivity, branching, assignment statements, context sensitivity, and loops.
- Utilizes SOOT’s IFDS functionality and Hero’s Jimple IFDS solver.
- Capability to handle different types of data flows in Java programs, focusing on string objects.

## Usage of SOOT

The visual usage of our SOOT library can be seen in this Youtube video:
 https://www.youtube.com/watch?v=Ji4r_-sXus0
- To access this "Main" file go to `TaintAnalysis > src > test > java > run > Main.java`
- Follow how the video sets the parameters for each test performed in the paper

## Usage of COMEX + GPT-4:
Video of COMEX: https://youtu.be/BBkz_Agj4Mg
- To access all tests used in the video's Java code go to `TaintAnalysis > src > test > java > target.taint`
1. Install all dependencies for COMEX ([COMEX](https://pypi.org/project/comex/), [GraphViz](https://graphviz.org/download/), [Python 3.8](https://www.python.org/downloads/release/python-380/))
2. Execute command `comex --lang "java" --code-file <File director for comex outputy> --graphs "cfg,dfg"`
3. Execute command `dot -T png -o <File directory for output cfg png> <File directory for comex output>`
4. Use query: Generate a taint analysis propagation for this provided control flow graph in this image. Determine what is the taint from the provided control flow graph. Get ALL possible taint (perform a may-taint analysis).



## Contributing

Contributions to the TaintAnalysis project are welcome. Please read our contribution guidelines for more information.

## Acknowledgments

- Kadirayk for the original repository.
- Contributors and maintainers of the SOOT framework.
- Authors of the COMEX paper [arXiv:2307.04693]. 



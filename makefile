JCC = javac
# Define the classes that are dependant on the sources
CLASSES = Tester.class SubsetNode.class Optimize.class QueryOptimization.class
# Define the source code that will be compiled
SOURCE = Tester.java SubsetNode.java Optimize.java QueryOptimization.java

# Set classes as the default dependanceis
default: CLASSES

# Set the classes dependancies to source
CLASSES: $(SOURCE)
	$(JCC) *.java

# Simple command to clean the directory of .class files.
clean: 
	$(RM) *.class
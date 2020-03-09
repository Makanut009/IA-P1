default:
	javac Estado.java;
	javac Main.java;
	javac MySuccessorFunction.java
	javac MyHeuristicFunction.java
	javac MyGoalTest.java

run:
	java Main
	
clean:
	rm -f *.class;

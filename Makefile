default:
	javac Estado.java;
	javac MySuccessorFunction.java
	javac MyHeuristicFunction.java
	javac MyGoalTest.java
	javac MyProblem.java

run:
	java MyProblem
	
clean:
	rm -f *.class;

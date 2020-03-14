default:
	javac Estado.java;
	javac MySuccessorFunction.java
	javac MyHeuristicFunction1.java
	javac MyHeuristicFunction2.java
	javac MyGoalTest.java
	javac MyProblem.java

run:
	java MyProblem
	
clean:
	rm -f *.class;

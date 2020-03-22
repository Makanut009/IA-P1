default:
	javac Estado.java;
	javac MySuccessorFunction1.java
	javac MySuccessorFunction2.java
	javac MySuccessorFunction3.java
	javac MySuccessorFunctionSA1.java
	javac MyHeuristicFunction1.java
	javac MyHeuristicFunction2.java
	javac MyGoalTest.java
	javac MyProblem.java

run:
	java MyProblem
	
clean:
	rm -f *.class;

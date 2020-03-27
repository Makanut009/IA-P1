import aima.search.framework.GoalTest;

public class MyGoalTest implements GoalTest {
	public boolean isGoalState(Object aState) {
		Estado estado = (Estado) aState;
		return (estado.isGoalState());
	}
}

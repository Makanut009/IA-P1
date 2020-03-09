import aima.search.framework.GoalTest;

public class MyGoalTest implements GoalTest {
	public boolean isGoalState(Object aState) {
		Estado estat = (Estado) aState;
		return (estat.isGoalState());
	}
}

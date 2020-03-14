import aima.search.framework.HeuristicFunction;

public class MyHeuristicFunction1 implements HeuristicFunction {

	public double getHeuristicValue(Object state) {
            Estado estado = (Estado) state;
            return estado.getHeuristicValue1();
	}
} 

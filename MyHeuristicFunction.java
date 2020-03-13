import aima.search.framework.HeuristicFunction;

public class MyHeuristicFunction implements HeuristicFunction {

	public double getHeuristicValue(Object state) {
            Estado estado = (Estado) state;
            return estado.getHeuristicValue();
	}
} 

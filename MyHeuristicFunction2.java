import aima.search.framework.HeuristicFunction;

public class MyHeuristicFunction2 implements HeuristicFunction {

	public double getHeuristicValue(Object state) {
            Estado estado = (Estado) state;
            return estado.getHeuristicValue2();
	}
} 

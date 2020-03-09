import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;

import java.util.*;

public class MyProblem {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Random random = new Random();
        int ops = 0;
        while (ops != 3) {
            mostrar_opciones();
            ops = in.nextInt();
            if (ops == 1 || ops == 2) {
            
                System.out.println("Introduce una seed o '-1' si deseas usar una seed random:");
                int seed1 = in.nextInt();
                if (seed1 < 0) seed1 = random.nextInt();
                else seed1 = random.nextInt(10000);
                
                System.out.println("Introduce una seed o '-1' si deseas usar una seed random:");
                int seed2 = in.nextInt();
                if (seed2 < 0) seed2 = random.nextInt();
                else seed2 = random.nextInt(10000);
                
                System.out.println("Introduce el número de usuarios:");
                int nusu = in.nextInt();
                System.out.println("Introduce el número de peticiones:");
                int npet = in.nextInt();
                System.out.println("Introduce el número de servidores:");
                int nserv = in.nextInt();
                System.out.println("Introduce el número mínimo de réplicas:");
                int nrep = in.nextInt();
                
                Estado estado = new Estado(nusu, npet, nserv, nrep, seed1, seed2);
                
//                 System.out.println("Si deseas usar el generador de soluciones iniciales 1, introduce '1'; si deseas usar el generador 2, introduce cualquier otro número:");
//                 if (in.nextInt() == 1) estat.generaSolInicial1();
//                 else estat.generaSolInicial2();
                
                estat.generaSolInicial();                
                
//                 System.out.println("Si deseas usar la función heurística 1, introduce '1'; si deseas usar la función heurística 2, introduce cualquier otro número:");
//                 if (in.nextInt() == 1) felicidad = false;
//                 else felicidad = true;

                if (ops == 1) MyHillClimbingSearch(estado);
                else MySimulatedAnnealingSearch(estado);

            }
        }
    }

    public static void mostrar_opciones() {
        System.out.println("Hill Climbing: 1");
        System.out.println("Simulated Annealing: 2");
        System.out.println("Salir: 3");
    }

    private static void MyHillClimbingSearch(Estado estat) {
        try {
            Problem problem;
            if (felicitat && succ1) problem = new Problem(estat, new AzamonSuccessorFunction(), new AzamonGoalTest(), new AzamonHeuristicFunction2());
            else if (felicitat) problem = new Problem(estat, new AzamonSuccessorFunction2(), new AzamonGoalTest(), new AzamonHeuristicFunction2());
            else if (succ1) problem = new Problem(estat, new AzamonSuccessorFunction(), new AzamonGoalTest(), new AzamonHeuristicFunction1());
            else problem = new Problem(estat, new AzamonSuccessorFunction2(), new AzamonGoalTest(), new AzamonHeuristicFunction1());
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem, search);

            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
            System.out.print(((AzamonEstado) search.getGoalState()).toString());
            System.out.println("\n" + ((AzamonEstado) search.getGoalState()).correspondenciasToString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void MySimulatedAnnealingSearch(Estado estat) {
        try {
            Problem problem;
            if (felicitat && succ1) problem = new Problem(estat, new AzamonSuccessorFunction(), new AzamonGoalTest(), new AzamonHeuristicFunction2());
            else if (felicitat) problem = new Problem(estat, new AzamonSuccessorFunction2(), new AzamonGoalTest(), new AzamonHeuristicFunction2());
            else if (succ1) problem = new Problem(estat, new AzamonSuccessorFunction(), new AzamonGoalTest(), new AzamonHeuristicFunction1());
            else problem = new Problem(estat, new AzamonSuccessorFunction2(), new AzamonGoalTest(), new AzamonHeuristicFunction1());
            Search search = new SimulatedAnnealingSearch(10000, 100, 5, 0.001);
            SearchAgent agent = new SearchAgent(problem, search);

            System.out.println("\n" + ((AzamonEstado) search.getGoalState()).toString());
            System.out.println("\n" + ((AzamonEstado) search.getGoalState()).correspondenciasToString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

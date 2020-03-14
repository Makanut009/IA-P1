import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import IA.DistFS.Servers;

import java.util.*;

public class MyProblem {

    public static void main(String[] args) throws Servers.WrongParametersException {
        Scanner in = new Scanner(System.in);
        Random random = new Random();
        int ops = 0;
        while (ops != 3) {
            mostrar_opciones();
            ops = in.nextInt();
            if (ops == 1 || ops == 2) {
            
                System.out.println("Introduce una seed o '-1' si deseas usar una seed random:");
                int seed1 = -1; //in.nextInt();
                if (seed1 < 0) seed1 = random.nextInt();
                else seed1 = random.nextInt(10000);
                
                System.out.println("Introduce una seed o '-1' si deseas usar una seed random:");
                int seed2 = -1; //in.nextInt();
                if (seed2 < 0) seed2 = random.nextInt();
                else seed2 = random.nextInt(10000);
                
                System.out.println("Introduce el número de usuarios:");
                int nusu = 5; //in.nextInt();
                System.out.println("Introduce el número de peticiones:");
                int npet = 10; //in.nextInt();
                System.out.println("Introduce el número de servidores:");
                int nserv = 5; //in.nextInt();
                System.out.println("Introduce el número mínimo de réplicas:");
                int nrep = 2; //in.nextInt();
                
                
                Estado estado = new Estado(nusu, npet, nserv, nrep, seed1, seed2);
                
                // System.out.println("Si deseas usar el generador de soluciones iniciales 1, introduce '1'; si deseas usar el generador 2, introduce cualquier otro número:");
                /* if (in.nextInt() == 1)*/ estado.generaSolInicial1();
                // else estado.generaSolInicial2();            
                
                // int heuristico = 0;
                // System.out.println("Si deseas usar la función heurística 1, introduce '1'; si deseas usar la función heurística 2, introduce cualquier otro número:");
                // if (in.nextInt() == 1) heuristico = 1;
                // else heuristico = 2;

                System.out.println("Hill Climbing");

                MyHillClimbingSearch(estado, 1);

                //Estado estado2 = new Estado(nusu, npet, nserv, nrep, seed1, seed2);
                //estado2.generaSolInicial1();
                MyHillClimbingSearch(estado, 2);
                

                System.out.println("Annealing");

                MySimulatedAnnealingSearch(estado, 1);

                MySimulatedAnnealingSearch(estado, 2);
                // if (ops == 1) MyHillClimbingSearch(estado, heuristico);
                // else MySimulatedAnnealingSearch(estado, heuristico);

            }
        }
    }

    public static void mostrar_opciones() {
        System.out.println("Hill Climbing: 1");
        System.out.println("Simulated Annealing: 2");
        System.out.println("Salir: 3");
    }

    private static void MyHillClimbingSearch(Estado estado, int heuristico) {
        try {

            Problem problema;
            if (heuristico == 1) problema = new Problem(estado, new MySuccessorFunction(), new MyGoalTest(), new MyHeuristicFunction1());
            else problema = new Problem(estado, new MySuccessorFunction(), new MyGoalTest(), new MyHeuristicFunction2());

            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problema, search);

            //System.out.println(agent.getInstrumentation());
            //System.out.println(agent.getActions());
            //agent.getActions();
            
            Estado estado_final = (Estado) search.getGoalState();
            estado_final.imprimir_asignaciones();
            estado_final.imprimir_tiempos();
            //System.out.println((estat_final).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void MySimulatedAnnealingSearch(Estado estado, int heuristico) {
        try {

            Problem problema;
            if (heuristico == 1) problema = new Problem(estado, new MySuccessorFunction(), new MyGoalTest(), new MyHeuristicFunction1());
            else problema = new Problem(estado, new MySuccessorFunction(), new MyGoalTest(), new MyHeuristicFunction2());
            
            Search search = new SimulatedAnnealingSearch(10000, 100, 5, 0.001);
            SearchAgent agent = new SearchAgent(problema, search);

            //System.out.println("\n" + ((AzamonEstado) search.getGoalState()).toString());
            //System.out.println("\n" + ((AzamonEstado) search.getGoalState()).correspondenciasToString());
            Estado estado_final = (Estado) search.getGoalState();
            estado_final.imprimir_asignaciones();
            estado_final.imprimir_tiempos();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

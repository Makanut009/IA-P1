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
        int alg = 0;
        while (alg != 3) {

            System.out.println("Hill Climbing: 1");
            System.out.println("Simulated Annealing: 2");
            System.out.println("Salir: 3");

            alg = in.nextInt();
            if (alg == 1 || alg == 2) {
                
                float lambda = 0;
                int stiter = 0;
                int steps = 0;
                int k = 0;
                if(alg==2){
                    System.out.println("Has escogido SA, elige el parametro steps: ");
                    steps = in.nextInt();
                    System.out.println("Has escogido SA, elige el parametro stiter: ");
                    stiter = in.nextInt();
                    System.out.println("Has escogido SA, elige el parametro k: ");
                    k = in.nextInt();
                    System.out.println("Has escogido SA, elige el parametro lambda: ");
                    lambda = in.nextFloat();
                }
                
                System.out.println("Introduce el número de usuarios: ");
                int nusu = in.nextInt();
                System.out.println("Introduce el número máximo de peticiones por usuario: ");
                int npet = in.nextInt();
                System.out.println("Introduce el número de servidores: ");
                int nserv = in.nextInt();
                System.out.println("Introduce el número mínimo de réplicas por fichero: ");
                int nrep = in.nextInt();
                
                System.out.println("Introduce el generador de solucion inicial, 1 o 2: ");
                int sol = in.nextInt();
                
                System.out.println("Introduce el heuristico, 1 o 2: ");
                int heu = in.nextInt();
                
                System.out.println("Introduce el generador de sucesores, 1, 2 o 3: ");
                int succ = in.nextInt();
                
                System.out.println("Iteraciones que hacer sobre el problema: ");
                int ite = in.nextInt();

                for(int i = 0; i < ite; ++i){

                    System.out.println("Introduce una seed para las peticiones o '-1' si deseas usar una seed random: ");
                    int seed1 = in.nextInt();
                    if (seed1 < 0) seed1 = random.nextInt();
                    
                    System.out.println("Introduce una seed para los servidores o '-1' si deseas usar una seed random: ");
                    int seed2 = in.nextInt();
                    if (seed2 < 0) seed2 = random.nextInt();

                    System.out.println("--- Iteración " + i + " ---");

                    // int seed1, seed2;
                    // if (i == 0) { seed1 = 1771455837; seed2 = 2003815554;}
                    // else if (i == 1) { seed1 = 891091758; seed2 = -202376362;}
                    // else if (i == 2) { seed1 = -804509587; seed2 = -658628491;}
                    // else if (i == 3) { seed1 = 1238774172; seed2 = -711265905;}
                    // else if (i == 4) { seed1 = 7009249; seed2 = 809791697;}
                    // else if (i == 5) { seed1 = -573801870; seed2 = 571569998;}
                    // else if (i == 6) { seed1 = 1969224113; seed2 = 1840351992;}
                    // else if (i == 7) { seed1 = -1869634747; seed2 = -542952602;}
                    // else if (i == 8) { seed1 = 1131403503; seed2 = -2007456259;}
                    // else { seed1 = -489086994; seed2 = 354937147;}
                    //System.out.println("Semillas: " + seed1 + " " + seed2);

                    Estado estado;
                    estado = new Estado(nusu, npet, nserv, nrep, seed1, seed2);
                                
                    if(sol==1) estado.generaSolInicial1();
                    else estado.generaSolInicial2();
                    
                    long tini = System.currentTimeMillis();
                    
                    if(alg==1)
                        MyHillClimbingSearch(estado, heu, succ);
                    else
                        MySimulatedAnnealingSearch(estado, heu, succ, steps, stiter, k, lambda);
                    
                    long elapsedTime = System.currentTimeMillis() - tini;

                    System.out.println("Tiempo de ejecución: " + elapsedTime + " ms\n");
                }
                System.out.println("-----------------------------------");
            }
        }
    }

    private static void MyHillClimbingSearch(Estado estado, int heuristico,int succ) {
        try {

            Problem problema;
            if (heuristico == 1){
                if(succ==1)
                    problema = new Problem(estado, new MySuccessorFunction1(), new MyGoalTest(), new MyHeuristicFunction1());
                else if (succ==2)
                    problema = new Problem(estado, new MySuccessorFunction2(), new MyGoalTest(), new MyHeuristicFunction1());
                else
                    problema = new Problem(estado, new MySuccessorFunction3(), new MyGoalTest(), new MyHeuristicFunction1());
            }
            else {
                if(succ==1)
                    problema = new Problem(estado, new MySuccessorFunction1(), new MyGoalTest(), new MyHeuristicFunction2());
                else if(succ==2)
                    problema = new Problem(estado, new MySuccessorFunction2(), new MyGoalTest(), new MyHeuristicFunction2());
                else
                    problema = new Problem(estado, new MySuccessorFunction3(), new MyGoalTest(), new MyHeuristicFunction2());
            }

            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problema, search);

            //System.out.println(agent.getInstrumentation());
            //System.out.println(agent.getActions());
            
            Estado estado_final = (Estado) search.getGoalState();
            //estado_final.imprimir_asignaciones();
            estado_final.imprimir_tiempo(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void MySimulatedAnnealingSearch(Estado estado, int heuristico, int succ, int steps, int stiter, int k, float lambda) {
        try {

            Problem problema;
            if (heuristico == 1){
                if(succ==1)
                    problema = new Problem(estado, new MySuccessorFunctionSA1(), new MyGoalTest(), new MyHeuristicFunction1());
                else if(succ==2)
                    problema = new Problem(estado, new MySuccessorFunction2(), new MyGoalTest(), new MyHeuristicFunction1());
                else 
                    problema = new Problem(estado, new MySuccessorFunction3(), new MyGoalTest(), new MyHeuristicFunction1());
            }
            else {
                if(succ==1)
                    problema = new Problem(estado, new MySuccessorFunctionSA1(), new MyGoalTest(), new MyHeuristicFunction2());
                else if(succ==2)
                    problema = new Problem(estado, new MySuccessorFunction2(), new MyGoalTest(), new MyHeuristicFunction2());
                else 
                    problema = new Problem(estado, new MySuccessorFunction3(), new MyGoalTest(), new MyHeuristicFunction2());
            }
            
            Search search = new SimulatedAnnealingSearch(steps, stiter, k, lambda);
            SearchAgent agent = new SearchAgent(problema, search);
            
            //System.out.println(agent.getInstrumentation());
            //System.out.println(agent.getActions());
            
            Estado estado_final = (Estado) search.getGoalState();
            //estado_final.imprimir_asignaciones();
            estado_final.imprimir_tiempo(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

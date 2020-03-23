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
                
                float lambda = 0;
                int stiter = 0;
                int steps = 0;
                int k = 0;
                if(ops==2){
                    //System.out.print("Has escogido SimmulatedAnnealing, elige el parametro steps: ");
                    steps = 10000; //in.nextInt();
                    //System.out.print("Has escogido SimmulatedAnnealing, elige el parametro stiter: ");
                    stiter = 100; //in.nextInt();
                    System.out.print("Has escogido SimmulatedAnnealing, elige el parametro K: ");
                    k = 5;//in.nextInt();
                    System.out.print("Has escogido SimmulatedAnnealing, elige el parametro lambda: ");
                    lambda = in.nextFloat();
                }
                
                System.out.println("Introduce el número de usuarios: 200");
                int nusu = 200; //in.nextInt();
                System.out.println("Introduce el número máximo de peticiones por usuario: 5");
                int npet = 5; //in.nextInt();
                System.out.println("Introduce el número de servidores: 50");
                int nserv = 50; //in.nextInt();
                System.out.println("Introduce el número mínimo de réplicas por fichero: 5");
                int nrep = 5; //in.nextInt();
                
                int sol = 2;
                while(sol!= 1 && sol!=2){
                    System.out.println("Introduce el generador de solucion inicial, 1 o 2: 2");
                    sol = in.nextInt();
                }
                
                System.out.println("Introduce tipo de heuristico, 1 o 2 o 3: 1");
                int heu = 1; //in.nextInt();
                
                System.out.println("Introduce tipo de successor, 1, 2 o 3: 3");
                int succ = 3; //in.nextInt();
                
                System.out.println("Iteraciones que hacer sobre el problema: 10 \n");
                int ite = 10; //in.nextInt();

                for(int i = 0; i < ite;++i){

                    System.out.println("--- Iteración " + i + " ---");

                    // System.out.println("Introduce una seed o '-1' si deseas usar una seed random:");
                    int seed1;//in.nextInt();
                    //if (seed1 < 0) seed1 = random.nextInt();
                    
                    //System.out.println("Introduce una seed o '-1' si deseas usar una seed random:");
                    int seed2;//in.nextInt();
                    //if (seed2 < 0) seed2 = random.nextInt();

                    seed1 = 1234;
                    seed2 = 1234;

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

                    System.out.println("Semillas: " + seed1 + " " + seed2);

                    Estado estado;
                    
                    estado = new Estado(nusu, npet, nserv, nrep, seed1, seed2);
                    // System.out.println("Si deseas usar el generador de soluciones iniciales 1, introduce '1'; si deseas usar el generador 2, introduce cualquier otro número:");
                    // if (in.nextInt() == 1) estado.generaSolInicial2();
                    // else estado.generaSolInicial2();            
                    if(sol==1) estado.generaSolInicial1();
                    else estado.generaSolInicial2();
                    // int heuristico = 0;
                    // System.out.println("Si deseas usar la función heurística 1, introduce '1'; si deseas usar la función heurística 2, introduce cualquier otro número:");
                    // if (in.nextInt() == 1) heuristico = 1;
                    // else heuristico = 2;
                    
                    long tini = System.currentTimeMillis();
                    
                    if(ops==1)
                    MyHillClimbingSearch(estado, heu, succ);
                    else
                    MySimulatedAnnealingSearch(estado, heu, succ, steps, stiter, k, lambda);
                    
                    long elapsedTime = System.currentTimeMillis() - tini;
                    System.out.println("Tiempo de ejecución: " + elapsedTime +" ms\n");
                    
                    nusu = nusu +100;
                }
            }
        }
    }

    public static void mostrar_opciones() {
        System.out.println("Hill Climbing: 1");
        System.out.println("Simulated Annealing: 2");
        System.out.println("Salir: 3");
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

            System.out.println(agent.getInstrumentation());
            //System.out.println(agent.getActions());
            //agent.getActions();
            
            Estado estado_final = (Estado) search.getGoalState();            
            estado_final.imprimir_asignaciones();
            estado_final.imprimir_tiempos();

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
                else{
                    problema = new Problem(estado, new MySuccessorFunctionSA1(), new MyGoalTest(), new MyHeuristicFunction1());

                }
            }
            else {
                if(succ==1)
                    problema = new Problem(estado, new MySuccessorFunctionSA1(), new MyGoalTest(), new MyHeuristicFunction2());
                else{
                    problema = new Problem(estado, new MySuccessorFunctionSA1(), new MyGoalTest(), new MyHeuristicFunction2());

                }
            }
            
            Search search = new SimulatedAnnealingSearch(steps, stiter, k, lambda);
            SearchAgent agent = new SearchAgent(problema, search);
            
            System.out.println(agent.getInstrumentation());
            // System.out.println(agent.getActions());
            
            Estado estado_final = (Estado) search.getGoalState();
            estado_final.imprimir_asignaciones();
            estado_final.imprimir_tiempos();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

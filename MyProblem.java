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

                if(ops==2){
                    System.out.println("Has escogido SimmulatedAnnealing, elige el parametro lambda");
                    lambda = in.nextFloat();
                }

                System.out.println("Introduce una seed o '-1' si deseas usar una seed random:");
                int seed1 = 1234; //in.nextInt();
                // if (seed1 < 0) seed1 = random.nextInt();
                // else seed1 = random.nextInt(10000);
                
                System.out.println("Introduce una seed o '-1' si deseas usar una seed random:");
                int seed2 = 1234; //in.nextInt();
                // if (seed2 < 0) seed2 = random.nextInt();
                // else seed2 = random.nextInt(10000);
                
                System.out.println("Introduce el número de usuarios: 200");
                int nusu = 200; //in.nextInt();
                System.out.println("Introduce el número de peticiones: 5");
                int npet = 5; //in.nextInt();
                System.out.println("Introduce el número de servidores: 50");
                int nserv = 50; //in.nextInt();
                System.out.println("Introduce el número mínimo de réplicas: 5");
                int nrep = 5; //in.nextInt();
                
                int sol = 1; //0
                
                while(sol!= 1 && sol!=2){
                    System.out.println("Introduce el generador de solucion inicial, 1 o 2: ");
                    sol = in.nextInt();
                }
                
                System.out.println("Introduce tipo de heuristico, 1 o 2: 1");
                int heu = 1; //in.nextInt();
                
                System.out.println("Introduce tipo de successor, 1 o 2: ");
                int succ = in.nextInt();
                

                System.out.println("Iteraciones que hacer sobre el problema: ");
                int ite = in.nextInt();

                long tini = System.currentTimeMillis();

                for(int i = 0; i < ite;++i){
                    Estado estado;
                    if(seed1 == -1 && seed2 == -1)
                        estado = new Estado(nusu, npet, nserv, nrep, random.nextInt(10000), random.nextInt(10000));
                    
                    else{
                        estado = new Estado(nusu, npet, nserv, nrep, seed1, seed2);

                    }
                    // System.out.println("Si deseas usar el generador de soluciones iniciales 1, introduce '1'; si deseas usar el generador 2, introduce cualquier otro número:");
                    /* if (in.nextInt() == 1)*/// estado.generaSolInicial2();
                    // else estado.generaSolInicial2();            
                    if(sol==1) estado.generaSolInicial1();
                    else estado.generaSolInicial2();
                    // int heuristico = 0;
                    // System.out.println("Si deseas usar la función heurística 1, introduce '1'; si deseas usar la función heurística 2, introduce cualquier otro número:");
                    // if (in.nextInt() == 1) heuristico = 1;
                    // else heuristico = 2;

                    //System.out.println("Hill Climbing");
                    if(ops==1)
                    MyHillClimbingSearch(estado, heu, succ);

                    //Estado estado2 = new Estado(nusu, npet, nserv, nrep, seed1, seed2);
                    //estado2.generaSolInicial1();
                    

                    else
                    MySimulatedAnnealingSearch(estado, heu, succ, lambda);
                    // if (ops == 1) MyHillClimbingSearch(estado, heuristico);
                    // else MySimulatedAnnealingSearch(estado, heuristico);
                }

                long tfin = System.currentTimeMillis();
                long elapsedTime = tfin -tini;
                //System.out.println(elapsedTime);
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
                else
                    problema = new Problem(estado, new MySuccessorFunction2(), new MyGoalTest(), new MyHeuristicFunction1());
            }
            else {
                if(succ==1)
                    problema = new Problem(estado, new MySuccessorFunction1(), new MyGoalTest(), new MyHeuristicFunction2());
                else
                    problema = new Problem(estado, new MySuccessorFunction2(), new MyGoalTest(), new MyHeuristicFunction2());
            }
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problema, search);

            //System.out.println(agent.getInstrumentation());
            //System.out.println(agent.getActions());
            //agent.getActions();
            
            Estado estado_final = (Estado) search.getGoalState();
            estado_final.imprimir_asignaciones();
            System.out.println();
            // estado_final.imprimir_tiempos();
            //System.out.println((estat_final).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void MySimulatedAnnealingSearch(Estado estado, int heuristico, int succ, float lambda) {
        try {

            Problem problema;
            if (heuristico == 1){
                if(succ==1)
                    problema = new Problem(estado, new MySuccessorFunction1(), new MyGoalTest(), new MyHeuristicFunction1());
                else{
                    problema = new Problem(estado, new MySuccessorFunction2(), new MyGoalTest(), new MyHeuristicFunction1());

                }
            }
            else {
                if(succ==1)
                    problema = new Problem(estado, new MySuccessorFunction1(), new MyGoalTest(), new MyHeuristicFunction2());
                else{
                    problema = new Problem(estado, new MySuccessorFunction2(), new MyGoalTest(), new MyHeuristicFunction2());

                }
            }
            
            Search search = new SimulatedAnnealingSearch(10000, 100, 5, lambda);
            SearchAgent agent = new SearchAgent(problema, search);
            
            // System.out.println(agent.getInstrumentation());
            // System.out.println(agent.getActions());
            
            //System.out.println("\n" + ((AzamonEstado) search.getGoalState()).toString());
            //System.out.println("\n" + ((AzamonEstado) search.getGoalState()).correspondenciasToString());
            
            Estado estado_final = (Estado) search.getGoalState();
            // // estado_final.imprimir_asignaciones();
            // // System.out.println();
            //estado_final.imprimir_tiempos();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

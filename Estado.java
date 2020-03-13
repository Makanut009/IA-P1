import java.util.*;
import IA.DistFS.Servers;
import IA.DistFS.Requests;

public class Estado {

    //Atributos    
    static int npet;
    static int nserv;
    static Servers servidores;
    static Requests peticiones;
    static int[]  tiempo_servidores;
    ArrayList<Integer> asignaciones;

    public Estado(int nusu, int npet, int nserv, int nrep, int seed1, int seed2) throws Servers.WrongParametersException {
        this.npet = npet;
        this.nserv = nserv;
        peticiones = new Requests(nusu, npet, seed1);  //usuarios, peticiones, seed
        servidores = new Servers(nserv, nrep, seed2); //servidores, replicas, seed
        asignaciones = new ArrayList<Integer>(npet);
        tiempo_servidores = new int[nserv];
        
        for (int i = 0; i < npet; ++i) {
            int user = peticiones.getRequest(i)[0];
            int file = peticiones.getRequest(i)[1];

            Iterator<Integer> it = servidores.fileLocations(file).iterator();

            System.out.println("La peticion " + i + " tiene como usuario "+user+" y fichero " +file); 
            System.out.println("El fichero mencionado se encuentra en los servidores :");
            while(it.hasNext()){
                int serv = it.next();
                System.out.println("  " + serv + " que tardaria "+ servidores.tranmissionTime(serv,user) + "ms");
            }
            //System.out.println(asignaciones.get(i));
            System.out.println();
        }
    }
    
    public Estado(Estado estat){
        asignaciones = new ArrayList<Integer>(estat.asignaciones);
    }

	//Métodos
    public boolean isGoalState() {
        return false;
    }

    public void generaSolInicial() {
        
        for (int i=0; i<npet; ++i) {
            int fileID = peticiones.getRequest(i)[1];
            Iterator<Integer> it = servidores.fileLocations(fileID).iterator(); 
            asignaciones.add(i, it.next());
        }
        imprimir_asignaciones();
    }

    public void imprimir_asignaciones() {
        int tiempo_total = 0;
        for (int i=0; i<npet; ++i){
            int serv = asignaciones.get(i);
            System.out.print(i + " -> " + serv + "   ");
            tiempo_total += servidores.tranmissionTime(serv,peticiones.getRequest(i)[0]);
        }
        System.out.print("Tiempo: " + tiempo_total);
        System.out.println("\n");
    }
    
    private void calcular_tiempo_servidores(){
        for (int i=0; i<npet; ++i) {
            int userID = peticiones.getRequest(i)[0];
            int serverID = asignaciones.get(i);
            int tiempo = servidores.tranmissionTime(serverID, userID);
            tiempo_servidores[serverID] += tiempo;
        }
    }
    
    public double getHeuristicValue() {
        calcular_tiempo_servidores();
        //System.out.println();
        //for (int j = 0; j < tiempo_servidores.length; j++){ System.out.println(tiempo_servidores[j]);}
        int hv = 0;
        for(int i=0; i<nserv; ++i) {
            hv = Math.max(hv, tiempo_servidores[i]);
        } 
        return (double) hv;
    }
    
    public boolean moverPeticion(int pet, int serv) {
        if (asignaciones.get(pet) == serv) return false;
        else {
            asignaciones.add(pet, serv);
            return true;
        }
    }

    // public boolean intercambiarPeticiones(int pet1, int pet2) {
    //     if (asignaciones.get(pet1) == asignaciones.get(pet2)) return false;
    //     //if (pet1 no es pot assignar al servidor de pet2) return false;
    //     //if (pet2 no es pot assignar al servidor de pet1) return false;
    //     else {
    //         asignaciones.add(pet1, /*servidor de pet2*/);
    //         asignaciones.add(pet2, /*servidor de pet1*/);
    //         return true;
    //     }
    // }
}

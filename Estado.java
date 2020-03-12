import java.util.*;
import IA.DistFS.Servers;
import IA.DistFS.Requests;

public class Estado {

    //Atributos    
    static int npet;
    static Servers servidores;
    static Requests peticiones;
    ArrayList<Integer> asignaciones; 

    public Estado(int nusu, int npet, int nserv, int nrep, int seed1, int seed2) throws Servers.WrongParametersException {
        this.npet = npet;
        peticiones = new Requests(nusu, npet, seed1);  //usuarios, peticiones, seed
        servidores = new Servers(nserv, nrep, seed2); //servidores, replicas, seed
        asignaciones = new ArrayList<Integer>(npet);
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
            Iterator<Integer> it = servidores.fileLocations(i).iterator(); 
            asignaciones.add(i, it.next());
            System.out.println(asignaciones.get(i));
        }
    }
    
    public double getHeuristicValue() {
        return 3;
    }
    
    public boolean moverPeticion(int pet, int serv) {
        if (asignaciones.get(pet) == serv) return false;
        else {
            asignaciones.add(pet, serv);
            return true;
        }
    }
}

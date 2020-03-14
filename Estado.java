import java.util.*;
import IA.DistFS.Servers;
import IA.DistFS.Requests;

public class Estado {

    //Atributos    
    static int npet;
    static int nserv;
    static Servers servidores;
    static Requests peticiones;
    int[] tiempo_servidores;
    int[] asignaciones;

    public Estado(int nusu, int npet, int nserv, int nrep, int seed1, int seed2) throws Servers.WrongParametersException {
        this.npet = npet;
        this.nserv = nserv;
        peticiones = new Requests(nusu, npet, seed1);  //usuarios, peticiones, seed
        servidores = new Servers(nserv, nrep, seed2); //servidores, replicas, seed
        asignaciones = new int[npet];

        for (int i = 0; i < npet; ++i) {
            int UserID = peticiones.getRequest(i)[0];
            int FileID = peticiones.getRequest(i)[1];
            Iterator<Integer> it = servidores.fileLocations(FileID).iterator();

            System.out.println("La peticion " + i + " tiene como usuario "+ UserID +" y fichero " + FileID); 
            System.out.println("El fichero mencionado se encuentra en los servidores:");
            while(it.hasNext()){
                int serv = it.next();
                System.out.println("  " + serv + " que tardaria "+ servidores.tranmissionTime(serv, UserID) + "ms");
            }
            System.out.println();
        }
    }
    
    public Estado(Estado estat){
        asignaciones = estat.asignaciones.clone();
    }

	//Métodos
    public boolean isGoalState() {
        return false;
    }

    public void generaSolInicial1() {
        
        for (int i=0; i<npet; ++i) {
            int fileID = peticiones.getRequest(i)[1];
            Iterator<Integer> it = servidores.fileLocations(fileID).iterator(); 
            asignaciones[i] = it.next();
        }
        imprimir_asignaciones();
        imprimir_tiempos();
    }

    public void generaSolInicial2() {
        //Segona opció de generació inicial de solució
    }

    public void imprimir_asignaciones() {
        int tiempo_total = 0;
        for (int i=0; i<asignaciones.length; ++i){
            int serv = asignaciones[i];
            System.out.print(i + " -> " + serv + "   ");
            tiempo_total += servidores.tranmissionTime(serv,peticiones.getRequest(i)[0]);
        }
        System.out.print("Tiempo: "+ calcular_tiempo_servidores()[2]);
        System.out.println("\n");
    }

    public void imprimir_tiempos() {
        for (int i=0; i<nserv; ++i){
            System.out.print(i + " -> " + tiempo_servidores[i] + "   ");
        }
        System.out.println("\n");
    }
    
    public int[] calcular_tiempo_servidores(){

        tiempo_servidores = new int[nserv];
        int max = 0;
        int min = 999999;
        int suma = 0;
        int[] retVal = new int[3];

        for (int i=0; i<npet; ++i) {
            int userID = peticiones.getRequest(i)[0];
            int serverID = asignaciones[i];
            int tiempo = servidores.tranmissionTime(serverID, userID);
            tiempo_servidores[serverID] = tiempo_servidores[serverID] + tiempo;
            if (tiempo_servidores[serverID] > max)
                max = tiempo_servidores[serverID];
            suma += tiempo;
        }
        for (int i=0; i<nserv; ++i) {
            if (tiempo_servidores[i] < min)
                min = tiempo_servidores[i];
        }

        retVal[0] = max;
        retVal[1] = min;
        retVal[2] = suma;
        //System.out.println(" max min suma " + max + " " +min+ " " +  suma);
        return retVal;
    }
    
    public double getHeuristicValue1() { //max
        return (double) calcular_tiempo_servidores()[0];
    }

    public double getHeuristicValue2() { //max - min
        
        int[] ret = calcular_tiempo_servidores();
        int max = ret[0];
        int min = ret[1];
        int suma = ret[2];

        int heu = max - min;
        
        double total = (double)suma*0.2 + (double)heu*0.8;

        return (double) total;
    }

    public double getHeuristicValue3() { //entropia
        int[] maxMin = calcular_tiempo_servidores();
        int max =maxMin[0];
        int min = maxMin[1];
        boolean minZero = min==0;
        double entropia = 0.0;
        
        for (int i=0; i<nserv; ++i) {
            double aux;
            if (minZero)
            aux = (double)tiempo_servidores[i]/(double) (0.65);
            else{
                aux = (double)tiempo_servidores[i]/(double) (min);
            }
            if (aux != 0) entropia = entropia + aux*Math.log(aux);
        }
        // int suma = maxMin[2];
        // double total = (double)suma*0.5 + (double)entropia*0.5;
        return entropia;
    }
    
    public boolean moverPeticion(int pet, int serv) {
        if (asignaciones[pet] == serv) return false;
        else {
            asignaciones[pet]= serv;
            return true;
        }
    }

    public boolean intercambiarPeticiones(int pet1, int pet2) {
        if (asignaciones[pet1] == asignaciones[pet2]) return false; //Servidor compartido
        
        int file1 = peticiones.getRequest(pet1)[1];
        int file2 = peticiones.getRequest(pet2)[1];

        Set<Integer> lista1 = servidores.fileLocations(file1);
        Set<Integer> lista2 = servidores.fileLocations(file2);

        if (lista1.contains(asignaciones[pet2]) && lista2.contains(asignaciones[pet1])) {
            int aux = asignaciones[pet1];
            asignaciones[pet1] = asignaciones[pet2];
            asignaciones[pet2] = aux;
            return true;
        }
        return false;
    }
    //     }
    // }
}

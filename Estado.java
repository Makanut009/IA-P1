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
        this.nserv = nserv;
        peticiones = new Requests(nusu, npet, seed1);  //usuarios, peticiones max por usuario, seed
        this.npet = peticiones.size();
        servidores = new Servers(nserv, nrep, seed2); //servidores, replicas, seed
        asignaciones = new int[this.npet];

        // for (int i = 0; i < npet; ++i) {
        //     int UserID = peticiones.getRequest(i)[0];
        //     int FileID = peticiones.getRequest(i)[1];
        //     Iterator<Integer> it = servidores.fileLocations(FileID).iterator();

        //     System.out.println("La peticion " + i + " tiene como usuario "+ UserID +" y fichero " + FileID); 
        //     System.out.println("El fichero mencionado se encuentra en los servidores:");
        //     while(it.hasNext()){
        //         int serv = it.next();
        //         System.out.println("  " + serv + " que tardaria "+ servidores.tranmissionTime(serv, UserID) + "ms");
        //     }
        //     System.out.println();
        // }
    }
    
    public Estado(Estado estat){
        asignaciones = estat.asignaciones.clone();
    }

	//Métodos
    public boolean isGoalState() {
        return false;
    }

    public void generaSolInicial1() {
        
        for (int i = 0; i < npet; ++i) {
            int fileID = peticiones.getRequest(i)[1];
            Iterator<Integer> it = servidores.fileLocations(fileID).iterator(); 
            asignaciones[i] = it.next();
        }
        imprimir_asignaciones();
        // imprimir_tiempos();
    }

    public void generaSolInicial2() {
        //Segona opció de generació inicial de solució
        Random random = new Random();

        int[] servidores_usados = new int[nserv];
        for (int i = 0; i < npet; i++){
            int fileID = peticiones.getRequest(i)[1];
            Iterator<Integer> it = servidores.fileLocations(fileID).iterator(); 
            boolean found = false;
            for (int j = 0; j < servidores.fileLocations(fileID).size(); j++){
                int serv = it.next();
                if(servidores_usados[serv] == 0){
                    asignaciones[i] = serv;
                    servidores_usados[serv]++;
                    found = true;
                }
            }
            if (!found) {
                asignaciones[i] = random.nextInt(nserv);
            }
        }
        imprimir_asignaciones();
        // imprimir_tiempos();
    }

    public void imprimir_asignaciones() {
        //int tiempo_total = 0;
        // for (int i=0; i<asignaciones.length; ++i){
        //     int serv = asignaciones[i];
        //     System.out.print(i + " -> " + serv + "   ");
        //     tiempo_total += servidores.tranmissionTime(serv,peticiones.getRequest(i)[0]);
        // }
        System.out.println("Tiempo: " + calcular_tiempo_servidores()[2]);
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

        return retVal;
    }
    
    public double getHeuristicValue1() { //max
        return (double) calcular_tiempo_servidores()[0];
    }

    public double getHeuristicValue2() { //variancia
        
        double suma = (double)calcular_tiempo_servidores()[2];
        double m = suma/(double)nserv;
        double heu = 0;

        for (int i = 0; i < nserv; i++){
            double aux = tiempo_servidores[i] - m;
            heu = heu + aux*aux;
        }

        heu = heu/nserv;
        
        double total = suma + heu*0.01;

        return total;
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
}

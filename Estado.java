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
    int max;
    int suma;

    public Estado(int nusu, int npet, int nserv, int nrep, int seed1, int seed2) throws Servers.WrongParametersException {
        this.nserv = nserv;
        peticiones = new Requests(nusu, npet, seed1);  //usuarios, peticiones max por usuario, seed
        this.npet = peticiones.size();
        servidores = new Servers(nserv, nrep, seed2); //servidores, replicas, seed
        asignaciones = new int[this.npet];
        tiempo_servidores = new int[nserv];
        max = 0;
        suma = 0;

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
        tiempo_servidores = estat.tiempo_servidores.clone();
        max = estat.max;
        suma = estat.suma;
    }

	//Métodos
    public boolean isGoalState() {
        return false;
    }

    public void generaSolInicial1() {
        for (int i = 0; i < npet; ++i) {
            int fileID = peticiones.getRequest(i)[1];
            Iterator<Integer> it = servidores.fileLocations(fileID).iterator(); 
            int serverID = it.next();
            asignaciones[i] = serverID;
            int userID = peticiones.getRequest(i)[0];
            int tiempo = servidores.tranmissionTime(serverID, userID);
            tiempo_servidores[serverID] += tiempo;
            suma += tiempo;
        }
        for (int i=0; i<nserv; ++i) {
            if (tiempo_servidores[i] > max)
                max = tiempo_servidores[i];
        }
        imprimir_asignaciones();
        imprimir_tiempos();
    }

    public void generaSolInicial2() {
        //Segona opció de generació inicial de solució
        Random random = new Random();

        int[] servidores_usados = new int[nserv];
        for (int i = 0; i < npet; i++){
            int fileID = peticiones.getRequest(i)[1];
            Iterator<Integer> it = servidores.fileLocations(fileID).iterator(); 
            boolean found = false;
            int size = servidores.fileLocations(fileID).size();
            for (int j = 0; j < size && !found; j++){
                int serv = it.next();
                if(servidores_usados[serv] == 0){
                    asignaciones[i] = serv;
                    servidores_usados[serv]++;
                    found = true;
                }
            }
            if (!found) {
                it = servidores.fileLocations(fileID).iterator(); 
                Random rand = new Random();
                int elem = rand.nextInt(size);
                int servToMove = 0;
                for(int k = 0; k < elem; ++k){
                    servToMove = it.next();
                }
                asignaciones[i] = servToMove;
            }
        }
        imprimir_asignaciones();
        imprimir_tiempos();
    }

    public void imprimir_asignaciones() {
        //int tiempo_total = 0;
        // for (int i=0; i<asignaciones.length; ++i){
        //     int serv = asignaciones[i];
        //     System.out.print(i + " -> " + serv + "   ");
        //     tiempo_total += servidores.tranmissionTime(serv,peticiones.getRequest(i)[0]);
        // }
        System.out.println("Tiempo: " + suma + " " + max);
    }

    public void imprimir_tiempos() {
        for (int i=0; i<nserv; ++i){
            System.out.print(i + " -> " + tiempo_servidores[i] + "   ");
        }
        System.out.println();
    }
    
    // public int[] calcular_tiempo_servidores(){

    //     int suma = 0;
    //     int[] retVal = new int[3];

    //     for (int i=0; i<npet; ++i) {
    //         int userID = peticiones.getRequest(i)[0];
    //         int serverID = asignaciones[i];
    //         int tiempo = servidores.tranmissionTime(serverID, userID);
    //         tiempo_servidores[serverID] = tiempo_servidores[serverID] + tiempo;
    //         if (tiempo_servidores[serverID] > max)
    //             max = tiempo_servidores[serverID];
    //         suma += tiempo;
    //     }

    //     for (int i=0; i<nserv; ++i) {
    //         if (tiempo_servidores[i] < min)
    //             min = tiempo_servidores[i];
    //     }

    //     retVal[0] = max;
    //     retVal[1] = min;
    //     retVal[2] = suma;

    //     return retVal;
    // }
    
    public double getHeuristicValue1() { //max
        System.out.println(max);
        return (double) max;
    }

    public double getHeuristicValue2() { //variancia
        
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
        int serv_ant = asignaciones[pet];
        if (serv_ant == serv) return false;
        else {
            int tiempo_anterior = tiempo_servidores[serv_ant];
            int userID = peticiones.getRequest(pet)[0];
            int temps_anterior = servidores.tranmissionTime(serv_ant, userID);
            suma -= temps_anterior;
            tiempo_servidores[serv_ant] -= temps_anterior;
            int temps_nou = servidores.tranmissionTime(serv, userID);
            suma += temps_nou;
            tiempo_servidores[serv] += temps_nou;

            asignaciones[pet] = serv;

            if (tiempo_anterior == max) recalcular_max();
            if (temps_nou > max) max = temps_nou;
            return true;
        }
    }

    public void recalcular_max(){
        max = 0;
        for (int i = 0; i < nserv; i++) {
            if (tiempo_servidores[i] > max)
                max = tiempo_servidores[i];
        }
    }

    public boolean se_puede_intercambiar(int pet1, int pet2) {
        if (asignaciones[pet1] == asignaciones[pet2]) return false; //Servidor compartido
        int file1 = peticiones.getRequest(pet1)[1];
        int file2 = peticiones.getRequest(pet2)[1];
        Set<Integer> lista1 = servidores.fileLocations(file1);
        Set<Integer> lista2 = servidores.fileLocations(file2);
        return  (lista1.contains(asignaciones[pet2]) && lista2.contains(asignaciones[pet1]));
    }

    public void intercambiarPeticiones(int pet1, int pet2) {

        int serv1 = asignaciones[pet1];
        int serv2 = asignaciones[pet2];
        asignaciones[pet1] = serv2;
        asignaciones[pet2] = serv1;

        int userID1 = peticiones.getRequest(pet1)[0];
        int userID2 = peticiones.getRequest(pet2)[0];
        
        int temps1 = servidores.tranmissionTime(serv1, userID1);
        int temps2 = servidores.tranmissionTime(serv2, userID2);

        int temps3 = servidores.tranmissionTime(serv2, userID1);
        int temps4 = servidores.tranmissionTime(serv1, userID2);

        suma = suma - temps1 - temps2;
        suma = suma + temps3 + temps4;

        tiempo_servidores[serv1] = tiempo_servidores[serv1] - temps1 + temps4;
        tiempo_servidores[serv2] = tiempo_servidores[serv2] - temps2 + temps3;

        if (temps1 == max || temps2 == max) recalcular_max();
        else {
            if (temps3 > max) max = temps3;
            if (temps4 > max) max = temps4;
        }
    }
}

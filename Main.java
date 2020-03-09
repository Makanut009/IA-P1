import IA.DistFS.Servers;
import IA.DistFS.Requests;

public class Main {
	
	public static void main(String[] args) throws Servers.WrongParametersException {
        Requests peticiones = new Requests(5, 10, 1510);  //usuarios, peticiones, seed
        Servers servidores = new Servers(10, 2, 8437587); //servidores, replicas, seed
		Estado estat = new Estado(10, servidores, peticiones);
		estat.generaSolInicial();
        //estat.generaSolInicial2();
		System.out.println(estat.toString());
	}

} 

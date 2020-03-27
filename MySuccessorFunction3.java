import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class MySuccessorFunction3 implements SuccessorFunction {

	public List getSuccessors(Object aState) {
		ArrayList<Successor> retVal = new ArrayList<>();
		Estado estado = (Estado) aState;
		
		for(int pet = 0; pet < estado.npet; ++pet) {

			int FileID = estado.peticiones.getRequest(pet)[1];
            int size = estado.servidores.fileLocations(FileID).size();
            Iterator<Integer> it = estado.servidores.fileLocations(FileID).iterator(); 
			for(int j = 0; j < size; ++j) {
                Estado nuevoEstado = new Estado(estado);
                int serv = it.next();
				if(nuevoEstado.moverPeticion(pet, serv)){
					String s = "Peticion " + pet + " asignada al servidor " + serv;
					retVal.add(new Successor(s, nuevoEstado));
				}
			}

			for(int pet2 = 0; pet2 < estado.npet; ++pet2) {
				if (pet!=pet2){
					if(estado.se_puede_intercambiar(pet, pet2)){
						String s = "Peticion " + pet + " intercambiada con peticion " + pet2;
						Estado nuevoEstado = new Estado(estado);
						if (nuevoEstado.intercambiarPeticiones(pet, pet2))
							retVal.add(new Successor(s, nuevoEstado));
					}
				}
			}
		}
		return retVal;
	}
}

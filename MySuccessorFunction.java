import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class MySuccessorFunction implements SuccessorFunction {

	public List getSuccessors(Object aState) {
		ArrayList<Successor> retVal = new ArrayList<>();
		Estado estado = (Estado) aState;
		
		for(int pet = 0; pet < estado.npet; ++pet) {
            int size = estado.servidores.fileLocations(pet).size();
            Iterator<Integer> it = estado.servidores.fileLocations(pet).iterator(); 
			for(int j = 0; j < size; ++j) {
                Estado nuevoEstado = new Estado(estado);
                int serv = it.next();
				if(nuevoEstado.moverPeticion(pet, serv)){  //mover peticion pet a servidor serv
					String s = "Peticion " + pet + " asignada al servidor " + serv;
					retVal.add(new Successor(s, nuevoEstado));
				}
			}
		}
		return retVal;
	}

}

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class MySuccessorFunctionSA1 implements SuccessorFunction {

	public List getSuccessors(Object aState) {
		ArrayList<Successor> retVal = new ArrayList<>();
        
        Estado estado = (Estado) aState;
        Estado nuevoEstado = new Estado(estado);
        
        Random rand = new Random();

		int pet = rand.nextInt(estado.asignaciones.length);
        int FileID = estado.peticiones.getRequest(pet)[1];
        int servsDeFile = estado.servidores.fileLocations(FileID).size();

        Iterator<Integer> it = estado.servidores.fileLocations(FileID).iterator();
        it.next();

        int elem = rand.nextInt(servsDeFile);
        int servToMove = 0;

        do{
            for(int i = 0; i < elem;++i){
                servToMove = it.next();
            }

            it = estado.servidores.fileLocations(FileID).iterator();
            elem = rand.nextInt(servsDeFile);
            it.next();

        } while(!nuevoEstado.moverPeticion(pet, servToMove));
        
        String s = "Peticion " + pet + " asignada al servidor " + servToMove;
        retVal.add(new Successor(s, nuevoEstado));
        
        return retVal;
	}
}

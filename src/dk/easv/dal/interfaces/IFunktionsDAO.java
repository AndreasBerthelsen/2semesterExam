package dk.easv.dal.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

public interface IFunktionsDAO {
    HashMap<Integer,String> getFunktionsTilstande();

    HashMap<Integer,ArrayList<String>> getFunktionsVandskligheder();


}

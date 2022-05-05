package dk.easv.dal.interfaces;

import dk.easv.be.Section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IFunktionsDAO {
    HashMap<Integer,String> getFunktionsTilstande();

    HashMap<Integer,ArrayList<String>> getFunktionsVandskligheder();

    List<Section> getFunkSections();
}

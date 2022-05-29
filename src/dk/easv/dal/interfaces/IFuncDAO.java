package dk.easv.dal.interfaces;

import dk.easv.be.Section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IFuncDAO {
    /**
     * @return en liste af alle funktionsevnetilstande Sections
     */
    List<Section> getFunkSections();
}

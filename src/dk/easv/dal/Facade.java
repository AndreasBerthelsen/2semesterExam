package dk.easv.dal;

import dk.easv.dal.interfaces.ICitizienDAO;
import dk.easv.dal.interfaces.ILoginDAO;
import dk.easv.dal.interfaces.ITeacherDAO;

public class Facade {

    private ILoginDAO iLoginDAO;
    private ITeacherDAO iTeacherDAO;
    private ICitizienDAO iCitizienDAO;

    public Facade(ILoginDAO iLoginDAO, ICitizienDAO iCitizienDAO, ITeacherDAO iTeacherDAO){
    }


}

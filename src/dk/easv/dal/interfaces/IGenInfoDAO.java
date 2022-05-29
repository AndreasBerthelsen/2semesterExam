package dk.easv.dal.interfaces;

import java.util.ArrayList;

public interface IGenInfoDAO {
    /**
     * finder alle overskriften til inputes der skal bruges under generel info
     * @return list af strings
     */
    ArrayList<String> getGeneralinfoFields();
}

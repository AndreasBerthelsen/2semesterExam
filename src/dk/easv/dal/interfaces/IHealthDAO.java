package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Category;
import dk.easv.be.Section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IHealthDAO {
    List<Section> getHealthSections();
}

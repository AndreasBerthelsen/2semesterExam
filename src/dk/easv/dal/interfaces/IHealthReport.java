package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Category;

import java.util.List;

public interface IHealthReport {

    List<Category> getAllTitle() throws SQLServerException;

    List<String> getSubTitles(Category category) throws SQLServerException;
}

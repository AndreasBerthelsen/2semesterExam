package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.util.List;

public interface IHealthReport {

    List<String> getAllTitle() throws SQLServerException;

    List<String> getSubTitles();
}

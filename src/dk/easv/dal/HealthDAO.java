package dk.easv.dal;

import dk.easv.be.Section;
import dk.easv.dal.interfaces.IHealthDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HealthDAO implements IHealthDAO {

    DBConnector dc;
    public HealthDAO() throws IOException {
        dc = new DBConnector();
    }

    @Override
    public List<Section> getHealthSections() {
        List<Section> sectionList = new ArrayList<>();

        try (Connection connection = dc.getConnection()) {
            String sql = "SELECT problemid, HelbredsVanskligheder.tilstandsID, guititel, titel\n" +
                    "from HelbredsVanskligheder\n" +
                    "INNER join HelbredsTilstande on HelbredsVanskligheder.tilstandsID = HelbredsTilstande.tilstandsId\n" +
                    "ORDER by tilstandsID asc";

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            HashMap<Integer, String> idTitelMap = new HashMap<>();
            int currentSection = 0;
            String sectionTitle = null;
            while (resultSet.next()) {
                int problemId = resultSet.getInt("problemid");
                String guiTitel = resultSet.getString("guititel");
                int ownerID = resultSet.getInt("tilstandsID");
                if (currentSection != ownerID) {
                    if (!idTitelMap.isEmpty()) {
                        sectionList.add(new Section(currentSection,sectionTitle,idTitelMap));
                        idTitelMap = new HashMap<>();
                        idTitelMap.put(problemId, guiTitel);
                    }
                    currentSection = ownerID;
                }
                sectionTitle = resultSet.getString("titel");
                idTitelMap.put(problemId, guiTitel);
            }
            //sidste add for at få den sidste sektion med
            sectionList.add(new Section(currentSection,sectionTitle,idTitelMap));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sectionList;
    }
}

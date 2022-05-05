package dk.easv.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.User;
import dk.easv.dal.interfaces.ICitizienDAO;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CitizienDAO implements ICitizienDAO {
    private DatabaseConnector dc;

    public CitizienDAO() throws IOException {
        dc = new DatabaseConnector();
    }

    @Override
    public List<Citizen> getAllCitizens() {
        ArrayList<Citizen> allCitizen = new ArrayList<>();
        try (Connection connection = dc.getConnection()) {
            String sql = "SELECT * FROM Borger";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("borgerID");
                String fname = resultSet.getString("fName");
                String lName = resultSet.getString("lName");
                Date date = resultSet.getDate("dato");
                allCitizen.add(new Citizen(id, fname, lName, date));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allCitizen;
    }

    @Override

    public void createCitizen(String fName, String lName, Date birthDate) {
        try (Connection connection = dc.getConnection()) {
            String sql = "INSERT INTO Borger (fname, lname, dato) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, fName);
            preparedStatement.setString(2, lName);
            preparedStatement.setDate(3, birthDate);
            ;
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void addUserToCitizen(User user, Citizen citizen) {
        try (Connection connection = dc.getConnection()) {
            String sql = "INSERT INTO CitUser (citFK, userFK) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, citizen.getId());
            preparedStatement.setInt(2, user.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void createTemplate(Citizen citizen) throws SQLServerException {
        //geninfo
        HashMap<String, String> genInfoMap = citizen.getGenInfoText();

        //funk
        HashMap<Integer, Integer> funkCurrentCombo = citizen.getCurrentCombo();
        HashMap<Integer, Integer> funkTargetCombo = citizen.getTargetCombo();
        HashMap<Integer, String> funkInfo = citizen.getFunkInfo();

        //helbred
        HashMap<Integer,String> healthInfoMap= citizen.getHelbredInfo();
        HashMap<Integer,Integer> healthRelevansMap = citizen.getRelevansMap();
        try (Connection connection = dc.getConnection()) {
            //skab borger -> udfyld journaler
            String sql = "INSERT INTO Borger (fname, lname, dato) VALUES (?,?,?)";
            PreparedStatement psB = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psB.setString(1, citizen.getfName());
            psB.setString(2, citizen.getlName());
            psB.setDate(3, citizen.getbDate());
            psB.execute();

            ResultSet idKey = psB.getGeneratedKeys();
            idKey.next();
            int id = idKey.getInt(1);


            //gen info
            String geninfoFields = genInfoMap.keySet().toString().replace(" ", "").replace("[", "").replace("]", "");
            StringBuilder sb = new StringBuilder();
            for (String s : geninfoFields.split(",")) {
                sb.append("?,");
            }
            String genMarks = sb.deleteCharAt(sb.length() - 1).toString();

            String genSql = "Insert into Generelinfo (borgerId," + geninfoFields + ") values(?," + genMarks + ")";
            PreparedStatement psGen = connection.prepareStatement(genSql);
            int index = 2;
            psGen.setInt(1, id);
            for (String s : genInfoMap.values()) {
                psGen.setString(index++, s);
            }
            psGen.addBatch();

            //funktion
            List<Integer> funkMasterKeys = Stream.of(funkCurrentCombo.keySet(), funkTargetCombo.keySet(), funkInfo.keySet())
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList());

            String sqlF= "insert into FunktionsJournal (borgerid, problemID, nuVurdering, målvurdering, note) values(?,?,?,?,?)";
            PreparedStatement psF = connection.prepareStatement(sqlF);
            for (int key :funkMasterKeys) {
                psF.setInt(1,id);
                psF.setInt(2,key);
                psF.setInt(3,funkCurrentCombo.get(key));
                psF.setInt(4,funkTargetCombo.get(key));
                psF.setString(5,funkInfo.get(key));
                psF.addBatch();
            }
            psF.executeBatch();

            //helbred
            List<Integer> healthMasterKeys = Stream.of(healthInfoMap.keySet(),healthRelevansMap.keySet())
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList());
            String sqlH= "insert into Helbredsjournal(borgerID,problemID,value,relevans) values(?,?,?,?)";
            PreparedStatement psH = connection.prepareStatement(sqlH);
            for (int key :healthMasterKeys) {
                psH.setInt(1,id);
                psH.setInt(2,key);
                psH.setString(3,healthInfoMap.get(key));
                psH.setInt(4,healthRelevansMap.get(key));
                psH.addBatch();
            }
            psH.executeBatch();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}

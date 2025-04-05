package com.thau.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.thau.DataModel.DataRecord;
import com.thau.DataModel.Product;

public class DbQueries {

    private Connection connection;

    public DbQueries(Connection connection) {
        this.connection = connection;
    }

    public void insertProduct(Product product) {
        String insertSql = "INSERT INTO Product (name, companyName, machineId, dateTime) VALUES (?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getCompanyName());
            preparedStatement.setString(3, product.getMachineId());
            preparedStatement.setString(4, product.getDateTime());

            preparedStatement.executeUpdate();
            System.out.println("Product inserted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void insertDataRecords(ArrayList<DataRecord> records, int productId) {
        String insertSql = "INSERT INTO DataRecord (" +
                "date, " +
                "time, " +
                "milliseconds, " +
                "airTemp, " +
                "coreTemp, " +
                "humidity, " +
                "stepType, " +
                "setAirTemp, " +
                "setCoreTemp, " +
                "setHumidity, " +
                "productId" +
                ") " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

            for (DataRecord record : records) {
                preparedStatement.setInt(1, record.getDate());
                preparedStatement.setDouble(2, record.getTime());
                preparedStatement.setInt(3, record.getMilliseconds());
                preparedStatement.setInt(4, record.getAirTemp());
                preparedStatement.setInt(5, record.getCoreTemp());
                preparedStatement.setInt(6, record.getHumidity());
                preparedStatement.setInt(7, record.getStepType());
                preparedStatement.setInt(8, record.getSetAirTemp());
                preparedStatement.setInt(9, record.getSetCoreTemp());
                preparedStatement.setInt(10, record.getSetHumidity());
                preparedStatement.setInt(11, productId);

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            System.out.println("All data records inserted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertSingleDataRecord(DataRecord record, int productId) {
        String insertSql = "INSERT INTO DataRecord (" +
                "date, " +
                "time, " +
                "milliseconds, " +
                "airTemp, " +
                "coreTemp, " +
                "humidity, " +
                "stepType, " +
                "setAirTemp, " +
                "setCoreTemp, " +
                "setHumidity, " +
                "productId" +
                ") " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

            preparedStatement.setInt(1, record.getDate());
            preparedStatement.setDouble(2, record.getTime());
            preparedStatement.setInt(3, record.getMilliseconds());
            preparedStatement.setInt(4, record.getAirTemp());
            preparedStatement.setInt(5, record.getCoreTemp());
            preparedStatement.setInt(6, record.getHumidity());
            preparedStatement.setInt(7, record.getStepType());
            preparedStatement.setInt(8, record.getSetAirTemp());
            preparedStatement.setInt(9, record.getSetCoreTemp());
            preparedStatement.setInt(10, record.getSetHumidity());
            preparedStatement.setInt(11, productId); 

            preparedStatement.executeUpdate();
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getProductId(String name, String companyName, String machineId, String dateTime) {
        String querySql = "SELECT id FROM Product WHERE name = ? AND companyName = ? AND machineId = ? AND dateTime = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(querySql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, companyName);
            preparedStatement.setString(3, machineId);
            preparedStatement.setString(4, dateTime);

            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                System.out.println("No product found with the given details.");
                return -1; // Return -1 if no product is found
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Return -1 in case of an error
        }
    }

    
    public ArrayList<DataRecord> getDataRecordsByProductId(int productId) {
        String querySql = "SELECT * FROM DataRecord WHERE productId = ? ORDER BY date, time;";
        ArrayList<DataRecord> records = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(querySql)) {
            preparedStatement.setInt(1, productId);

            var resultSet = preparedStatement.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                DataRecord record = new DataRecord(i, resultSet.getInt( "date"), resultSet.getDouble("time"),
                        0, 0, 0, 0, 0, resultSet.getInt("airTemp"),
                        resultSet.getInt("coreTemp"), resultSet.getInt("humidity"),
                        resultSet.getInt("stepType"), resultSet.getInt("setAirTemp"),
                        resultSet.getInt("setCoreTemp"), resultSet.getInt("setHumidity"));
                records.add(record);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }

    public ArrayList<String> getAllCompanyNames() {
        String querySql = "SELECT DISTINCT companyName FROM Product;";
        ArrayList<String> companyNames = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(querySql)) {
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                companyNames.add(resultSet.getString("companyName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return companyNames;
    }
    

    public ArrayList<String> getMachineIdsByCompanyName(String companyName) {
        String querySql = "SELECT DISTINCT machineId FROM Product WHERE companyName = ?;";
        ArrayList<String> machineIds = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(querySql)) {
            preparedStatement.setString(1, companyName);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                machineIds.add(resultSet.getString("machineId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return machineIds;
    }

    public ArrayList<String> getNamesByCompanyAndMachine(String companyName, String machineId) {
        String querySql = "SELECT DISTINCT name FROM Product WHERE companyName = ? AND machineId = ?;";
        ArrayList<String> names = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(querySql)) {
            preparedStatement.setString(1, companyName);
            preparedStatement.setString(2, machineId);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                names.add(resultSet.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return names;
    }

    public ArrayList<String> getDateTimesByCompanyMachineAndName(String companyName, String machineId, String name) {
        String querySql = "SELECT dateTime FROM Product WHERE companyName = ? AND machineId = ? AND name = ?;";
        ArrayList<String> dateTimes = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(querySql)) {
            preparedStatement.setString(1, companyName);
            preparedStatement.setString(2, machineId);
            preparedStatement.setString(3, name);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                dateTimes.add(resultSet.getString("dateTime"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateTimes;
    }

    public ArrayList<Integer> getProcessStepsByProductId(int productId) {
        String querySql = "SELECT stepType FROM DataRecord WHERE productId = ?;";
        ArrayList<Integer> processSteps = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(querySql)) {
            preparedStatement.setInt(1, productId);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                processSteps.add(resultSet.getInt("stepType"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return processSteps;
    }


}

package org.example.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.domain.Car;
import org.example.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarsDBRepository implements CarRepository{

    private JdbcUtils dbUtils;



    private static final Logger logger= LogManager.getLogger();

    public CarsDBRepository(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);


    }

    @Override
    public List<Car> findByManufacturer(String manufacturerN) {
        logger.traceEntry("finding task {}", manufacturerN);
        Connection connection=dbUtils.getConnection();
        List<Car> cars=new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement("SELECT * FROM cars WHERE manufacturer = ?")){
            ps.setString(1, manufacturerN);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                String manufacturer=rs.getString("manufacturer");
                String model = rs.getString("model");
                int year=rs.getInt("year");
                cars.add(new Car(manufacturer, model, year));
            }
        }
        catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB " + e);
        }

        logger.traceExit(cars);
        return cars;
    }

    @Override
    public List<Car> findBetweenYears(int min, int max) {
        logger.traceEntry("finding task between {} and {}", min, max);
        Connection connection=dbUtils.getConnection();
        List<Car> cars=new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement("SELECT * FROM cars WHERE year between ? and ?")){
            ps.setInt(1, min);
            ps.setInt(2, max);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                String manufacturer=rs.getString("manufacturer");
                String model = rs.getString("model");
                int year=rs.getInt("year");
                cars.add(new Car(manufacturer, model, year));
            }
            return cars;
        }
        catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(cars);
        return cars;
    }

    @Override
    public void add(Car elem) {
        logger.traceEntry("saving task {}", elem);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("INSERT INTO cars (manufacturer,model,year) VALUES (?,?,?)")) {
            ps.setString(1, elem.getManufacturer());
            ps.setString(2, elem.getModel());
            ps.setInt(3, elem.getYear());
            int res = ps.executeUpdate();
            logger.trace("Saved {} instances", res);
        }
        catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Car elem) {
        logger.traceEntry("updating task {}, {}", integer, elem);
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("UPDATE cars SET manufacturer = ?, model = ?, year = ? WHERE id = ?")) {
            ps.setString(1, elem.getManufacturer());
            ps.setString(2, elem.getModel());
            ps.setInt(3, elem.getYear());
            ps.setInt(4, integer);
            int res = ps.executeUpdate();
            logger.trace("Updated {} instances", res);
        }
        catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
    }

    @Override
    public Iterable<Car> findAll() {
        logger.traceEntry();
        Connection connection=dbUtils.getConnection();
        List<Car> cars=new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement("SELECT * FROM cars")){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String manufacturer=rs.getString("manufacturer");
                String model = rs.getString("model");
                int year=rs.getInt("year");
                cars.add(new Car(manufacturer, model, year));
            }
        }
        catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(cars);
        return cars;

    }
}

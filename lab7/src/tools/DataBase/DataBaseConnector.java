package tools.DataBase;

import data.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;

public class DataBaseConnector {
    private static String tablename;
    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            tablename = "clients";
            connection = DriverManager.getConnection("jdbc:postgresql://pg:5432/studs", "s282518", "pbk389");
        } catch (SQLException | ClassNotFoundException e) {}
    }

    public static boolean register(String username, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select * from " + tablename + " where clientname = ?");
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        if (rs.next()){ return false; }

        PreparedStatement ps = connection.prepareStatement("INSERT INTO " + tablename + " VALUES(?, ?, ?) ");
        String salt = getSalt();
        ps.setString(1, username);
        ps.setString(2, hash(password, salt));
        ps.setString(3, salt);
        ps.executeUpdate();
        return true;
    }

    public static boolean login(String username, String password) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("select * from " + tablename + " where clientname = ?");
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        return rs.next() && hash(password, rs.getString("salt")).equals(rs.getString("pass"));
    }

    public static boolean writeLab(String l, String login){
        String[] labpts = l.split(",");
        Coordinates coordinates = new Coordinates(Integer.parseInt(labpts[1]), Float.parseFloat(labpts[2]));
        Difficulty diff = Difficulty.values()[Integer.parseInt(labpts[5])];
        Discipline diss = new Discipline(labpts[6], Integer.parseInt(labpts[7]), Integer.parseInt(labpts[8]), Long.parseLong(labpts[9]));
        LabWork labWork = new LabWork(labpts[0], coordinates, Float.parseFloat(labpts[3]), Long.parseLong(labpts[4]), diff, diss);

        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO labworks" + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
            ps.setString(1, labWork.getName());
            ps.setInt(2, labWork.getCoordinates().getX());
            ps.setFloat(3, labWork.getCoordinates().getY());
            ps.setFloat(4, labWork.getMin());
            ps.setLong(5, labWork.getMax());
            ps.setInt(6, labWork.getDiff().ordinal());
            ps.setString(7, labWork.getDisc().toString());
            ps.setInt(8, labWork.getDisc().getLecture());
            ps.setInt(9, labWork.getDisc().getPractice());
            ps.setLong(10, labWork.getDisc().getSelfStudy());
            ps.setString(11, login);
            ps.executeUpdate();

            PreparedStatement pps = connection.prepareStatement("SELECT * from labworks where name = ?" );
            pps.setString(1, labWork.getName());
            ResultSet r = pps.executeQuery();
            if (r.next()){
                int id = r.getInt("id");
                LabWork flab = new LabWork(id, labpts[0], coordinates, Float.parseFloat(labpts[3]), Long.parseLong(labpts[4]), diff, diss, login);
                flab.setUser(login);
                LabworksStorage.put(flab);
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static ArrayList<LabWork> readLab(){
        try {
            ArrayList<LabWork> labs = new ArrayList<LabWork>();
            PreparedStatement ps = connection.prepareStatement("SELECT * from labworks");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String name = rs.getString(1);
                Coordinates coordinates = new Coordinates(rs.getInt(2), rs.getFloat(3));
                Float minimalPoint = rs.getFloat(4);
                long maximumPoint = rs.getLong(5);
                Difficulty difficulty = Difficulty.values()[rs.getInt(6)];
                Discipline discipline = new Discipline(rs.getString(7), rs.getInt(8), rs.getInt(9), rs.getLong(10));
                int id = rs.getInt("id");
                LabWork labWork = new LabWork(id, name, coordinates, minimalPoint, maximumPoint, difficulty, discipline, rs.getString("author"));
                labs.add(labWork);
            }
            return labs;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void removeLab(LabWork labWork, String currentUser){
        try {
            if (currentUser.equals(labWork.getUser())){
                PreparedStatement ps = connection.prepareStatement("DELETE FROM labworks where name = ?");
                ps.setString(1, labWork.getName());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateLab(LabWork labWork){
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE labworks SET name = ? where name = ?");
            ps.setString(1, labWork.getName());
            ps.setString(2, labWork.getName() + "1");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getSalt() {
        byte[] salt = new byte[16];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(salt);
        return new String(salt, StandardCharsets.UTF_8);
    }

    private static String hash(String password, String salt) {
        try {
            String pepper = "22&3CdsFgh2cL97#3";
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] data = (pepper + password + salt).getBytes(StandardCharsets.UTF_8);
            byte[] hashbytes = md.digest(data);
            String s = Base64.getEncoder().encodeToString(hashbytes);
            System.out.println(s);
            return s;
        } catch (NoSuchAlgorithmException e) {
            return password;
        }
    }
}

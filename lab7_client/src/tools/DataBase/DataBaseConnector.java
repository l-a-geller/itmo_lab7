package tools.DataBase;

import java.sql.*;

public class DataBaseConnector {
    private static String tablename;
    private static Connection connection;
    static {
        //create a BD
        try {
            tablename = "users";
            connection = DriverManager.getConnection("jdbc:postgresql://pg:5432/studs", "s282518", "pbk389");
        } catch (SQLException e) {
            System.out.println("Wrong database congiguration");
        }
    }

    public static boolean register(String username, String password) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT username from" + tablename);
        ResultSet rs = ps.executeQuery();
        return true;
    }

    public static boolean login(String username, String password) throws SQLException{
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + tablename + "WHERE username = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}
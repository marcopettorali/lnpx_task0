import java.sql.*;
import java.util.*;

public class DBManager {
    private static Connection DBConnection;
    private static final String DBMSAddress = "";
    private static final String DBMSPort = "";
    private static final String DBName = "";
    private static final String DBMSUsername = "";
    private static final String DBMSPassword = "";
    
    private static final String checkLoginQuery = "";
    private static final String loadUserReservationsQuery = "";
    
    static{
        try{
            DBConnection = DriverManager.getConnection(DBMSAddress + ":" + DBMSPort + "/" + DBName, DBMSUsername, DBMSPassword); 
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
    }
    
    public static boolean checkLogin(String username, String password){
    
    }
    
    public static List<Reservation> loadUserReservations(String username){
    
    }
}

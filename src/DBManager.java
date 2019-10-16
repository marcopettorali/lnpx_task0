
import java.sql.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

/**
 * DBManager collects all the functions for inserting, removing and updating data in the database
 *
 */
public class DBManager {

    private static Connection DBConnection;
    private static final String DBMSFormat = "jdbc:mysql";
    private static final String DBMSAddress = "localhost";//"127.0.0.1";
    private static final String DBMSPort = "3306";
    private static final String DBName = "pc_booking";
    private static final String DBMSUsername = "root";
    private static final String DBMSPassword = "";

    private static final String checkLoginQuery = "SELECT * FROM user WHERE Username = ? AND Password = ?";

    private static final String loadUserReservationsQuery = "SELECT * FROM booking WHERE Username = ? AND Date >= ?";

    private static final String queryControlReservations = ""
            + "select count(*) as NumPrenotazioni "
            + "from booking b "
            + "where b.username=? and b.StartTime=? and b.date=?;";

    private static final String queryAvailableRooms = ""
            + "select D.RoomName,D.Capacity - D.Booked as Available, D.Capacity, D.RowNumber	"
            + "from (   "
            + "          select b.RoomName, count(*) as Booked ,r.Capacity, r.RowNumber"
            + "          from booking b inner join room r on (r.RoomName=b.RoomName)"
            + "	    where b.StartTime=? and b.Date=?	"
            + "	    group by b.RoomName "
            + "		) as D "
            + "where D.Capacity - D.Booked > 0 "
            + " union  select  r1.RoomName, r1.Capacity,r1.Capacity,r1.RowNumber "
            + "   from room r1 "
            + "   where r1.roomName not in (select b1.roomName"
            + "			         from booking b1"
            + "                             where b1.StartTime=? and b1.Date=?);";

    private static final String queryCreateReservation = ""
            + "INSERT INTO booking "
            + " VALUES (?,?,?,?,?);";

    private static final String queryDeleteReservation = ""
            + "DELETE FROM booking "
            + "  WHERE (Username = ?) and (StartTime = ?) and (Date =?) and (PCNumber = ?) and (RoomName = ?);";

    private static final String queryAvailablePC = ""
            + " SELECT p.PCNumber,p.RoomName "
            + " FROM pc p "
            + " WHERE p.RoomName=? "
            + "    and "
            + "   NOT EXISTS "
            + "  (SELECT b.PCNumber,b.RoomName "
            + "   FROM booking b "
            + "   WHERE b.RoomName=p.RoomName and b.PCNumber=p.PCNumber and b.Date=? and b.StartTime=?);";

    static {
        try {
            DBConnection = DriverManager.getConnection(DBMSFormat + "://" + DBMSAddress + ":" + DBMSPort + "/" + DBName, DBMSUsername, DBMSPassword);
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    /**
     * checkLogin checks if the user is present in the DB. 
     * If it's present, then loads its personal information
     * 
     * @param username it represents the username of the user that is trying to log in
     * @param password it's the password of the user
     * @return true if the login succeded, false otherwise.
     */
    public static boolean checkLogin(String username, String password) {
        try (
                PreparedStatement ps = DBConnection.prepareStatement(checkLoginQuery);) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            int counter = 0;
            while (rs.next()) {
                counter++;
                User.username = rs.getString("Username");
                User.password = rs.getString("Password");
                User.firstName = rs.getString("FirstName");
                User.lastName = rs.getString("LastName");
                User.matriculationNumber = rs.getInt("MatriculationNumber");
            }
            if (counter == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException io) {
            System.err.println(io.getMessage());
        }
        return false;
    }

    /**
     * 
     * @param username is the username of the user that logged in
     * @return a list of all the reservations made by the user
     */
    public static ArrayList<Reservation> loadUserReservations(String username) {
        try (
                PreparedStatement ps = DBConnection.prepareStatement(loadUserReservationsQuery);) {

            ArrayList<Reservation> reservations = new ArrayList<>();

            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String localDate = date.format(formatter);

            ps.setString(1, username);
            ps.setString(2, localDate);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                reservations.add(new Reservation(rs.getString("Username"), rs.getString("RoomName"), Integer.toString(rs.getInt("PCNumber")), rs.getDate("Date").toString(), rs.getTime("StartTime").toString()));
            }

            return reservations;
        } catch (SQLException io) {
            System.err.println(io.getMessage());
        }
        return null;
    }

    /**
     * 
     * @param user
     * @param rn
     * @param pcnumb
     * @param D
     * @param T
     * @return 
     */
    public static boolean reservePC(String user, String rn, int pcnumb, String D, String T) {
        try (
                PreparedStatement ps = DBConnection.prepareStatement(queryCreateReservation);
                PreparedStatement ps1 = DBConnection.prepareStatement(queryControlReservations);) {

            ps1.setString(1, user);
            ps1.setString(2, T);
            ps1.setString(3, D);

            ResultSet rs1 = ps1.executeQuery();
            int k = 0;
            while (rs1.next()) {
                k = rs1.getInt("NumPrenotazioni");
            }

            if (k > 0) {
                return false;
            }

            ps.setString(1, user);
            ps.setString(2, T);
            ps.setString(3, D);
            ps.setInt(4, pcnumb);
            ps.setString(5, rn);

            int ret = ps.executeUpdate();
            if (ret == 0) {
                return false;
            }
            return true;

        } catch (SQLException io) {
            System.err.println("An error has occured during the reservation of a PC ! \n");
        }
        return true;
    }

    public static boolean DeleteReservation(String user, String rn, int pcnumb, String D, String T) {
        /* rn=RoomName D=Date T=StartTime*/

        try (
                PreparedStatement ps = DBConnection.prepareStatement(queryDeleteReservation);) {

            ps.setString(1, user);
            ps.setString(2, T);
            ps.setString(3, D);
            ps.setInt(4, pcnumb);
            ps.setString(5, rn);

            int ret = ps.executeUpdate();
            if (ret == 0) {
                return false;
            }
            return true;

        } catch (SQLException io) {
            System.err.println("An error has occured during the deleting of a reservation ! \n");
        }
        return true;
    }

    public static List<Room> LoadRooms(String D, String T) {
        /* D = Date , T = StartTime */

        List<Room> available = new ArrayList<>();
        try (
                PreparedStatement ps = DBConnection.prepareStatement(queryAvailableRooms);) {

            ps.setString(1, T);
            ps.setString(2, D);
            ps.setString(3, T);
            ps.setString(4, D);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                Room r = new Room(rs.getString("RoomName"), rs.getInt("Capacity"), rs.getInt("Available"), rs.getInt("RowNumber"));
                available.add(r);
                /* Creation of the Room Bean and adding them into the returned list*/

            }
            return available;
        } catch (SQLException io) {
            System.err.println("An error has occured during the research of a room ! \n");
        }
        return null;
    }

    public static List<PC> LoadAvailablePC(String rn, String D, String T) {
        /* rn = RoomName, D = date, T=time */

        List<PC> available = new ArrayList<>();
        try (
                PreparedStatement ps = DBConnection.prepareStatement(queryAvailablePC);) {
            ps.setString(1, rn);
            ps.setString(2, D);
            ps.setString(3, T);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                PC p = new PC(rs.getInt("PCNumber"), rs.getString("RoomName"));
                available.add(p);
                /* Creation of the PC bean and adding them into the returned list */

            }
            return available;
        } catch (SQLException io) {
            System.err.println("An error has occured during the research of a PC ! \n");
        }
        return null;
    }

}

import java.sql.*;
import java.text.*;
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
    private static final String queryAvailableRooms = "	select D.RoomName,D.Capacity - D.Booked as Available, D.Capacity	"
													  +"from (   "     
													  +"          select b.RoomName, count(*) as Booked ,r.Capacity "
													  +"          from booking b inner join room r on (r.RoomName=b.RoomName)"
													  +"	    where b.StartTime=? and b.Date=?	"
													  +"	    group by b.RoomName "
													  +"		) as D "
													  +"where D.Capacity - D.Booked > 0; ";
    
    private static final String queryCreateReservation = " INSERT INTO booking " 
                                                        +"  VALUES (?,?,?,?,?);";
    
    private static final String queryDeleteReservation=   " DELETE FROM booking " 
                                                          +"  WHERE (Username = ?) and (StartTime = ?) and (Date =?) and (PCNumber = ?) and (RoomName = ?);";
    
    private static final String queryAvailablePC=" SELECT p.PCNumber,p.RoomName "
                                                +" FROM pc p "
                                                +" WHERE p.RoomName=? "
                                                +"    and "
                                                +"   NOT EXISTS "	
                                                +"  (SELECT b.PCNumber,b.RoomName " 
                                                +"   FROM booking b "
                                                +"   WHERE b.RoomName=p.RoomName and b.PCNumber=p.PCNumber and b.Date=? and b.StartTime=?);"; 
    
    static{
        try{
            DBConnection = DriverManager.getConnection(DBMSAddress + ":" + DBMSPort + "/" + DBName, DBMSUsername, DBMSPassword); 
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
    }
    
   /* public static boolean checkLogin(String username, String password){
    
    }
    
    public static List<Reservation> loadUserReservations(String username){
    
    }*/
    
    public static boolean ReservePC(String user, String rn, int pcnumb, String D, String T ){
        try(
            PreparedStatement ps=DBConnection.prepareStatement(queryCreateReservation);
                
		){
                  java.util.Date day=new SimpleDateFormat("yyyy-MM-dd").parse(D);
		  java.sql.Date res_date= new java.sql.Date(day.getTime());

		  Time res_time = Time.valueOf(T);
                  
                  ps.setString(1,user);
                  ps.setTime(2,res_time);
                  ps.setDate(3,res_date);
                  ps.setInt(4,pcnumb);
                  ps.setString(5,rn);
                  
                  int ret=ps.executeUpdate();
                  if(ret==0){
                          return false;
                        }
          return true;
		  
        }catch(SQLException io){
            System.err.println("An error has occured during the reservation of a PC ! \n");
        }catch(ParseException pe){
            System.out.println("An error has occurred during the date parsing ! \n");
        } 
        return true;
    }
                                        
    public static boolean DeleteReservation(String user, String rn, int pcnumb, String D, String T ){   /* rn=RoomName D=Date T=StartTime*/
        
        try(
            PreparedStatement ps=DBConnection.prepareStatement(queryDeleteReservation);
                
		){
                  java.util.Date day=new SimpleDateFormat("yyyy-MM-dd").parse(D); /*Converting the Date String */
		  java.sql.Date res_date= new java.sql.Date(day.getTime());       /* into a java.sql.Date */  

		  Time res_time = Time.valueOf(T);      /* Converting StartTime string into java.sql.Time */
                  
                  ps.setString(1,user);
                  ps.setTime(2,res_time);
                  ps.setDate(3,res_date);
                  ps.setInt(4,pcnumb);
                  ps.setString(5,rn);
                  
                  int ret=ps.executeUpdate();
                  if(ret==0){
                           return false;
                  }
                  return true;
                  
              }catch(SQLException io){
                    System.err.println("An error has occured during the deleting of a reservation ! \n");
              }catch(ParseException pe){
                    System.out.println("An error has occurred during the date parsing ! \n");
              } 
       return true;
    } 
    
    public static List<Room> LoadRooms (String D, String T){ /* D = Date , T = StartTime */
	
	List<Room> available= new ArrayList<>();
	try(
            PreparedStatement ps=DBConnection.prepareStatement(queryAvailableRooms);
                
		){
		  java.util.Date day=new SimpleDateFormat("yyyy-MM-dd").parse(D);
		  java.sql.Date res_date= new java.sql.Date(day.getTime());

		  Time res_time = Time.valueOf(T);
		  
		  ps.setTime(1,res_time);
		  ps.setDate(2,res_date);
		  
		  ResultSet rs = ps.executeQuery();
                  while(rs.next()){
                      
                      Room r=new Room(rs.getString("RoomName"),rs.getInt("Capacity"),rs.getInt("Available"));
                      available.add(r); /* Creation of the Room Bean and adding them into the returned list*/
                      
                  }
                  return available;
        }catch(SQLException io){
            System.err.println("An error has occured during the research of a room ! \n");
        }catch(ParseException pe){
            System.out.println("An error has occurred during the date parsing ! \n");
        } 
        
        return null;
    }
   
    public static List<PC> LoadAvailablePC(String rn, String D, String T){ /* rn = RoomName, D = date, T=time */
        
        List<PC> available=new ArrayList<>();
        try(
            PreparedStatement ps=DBConnection.prepareStatement(queryAvailablePC); 
		
                ){
                  java.util.Date day=new SimpleDateFormat("yyyy-MM-dd").parse(D);
		  java.sql.Date res_date= new java.sql.Date(day.getTime());

		  Time res_time = Time.valueOf(T);
                  
                  ps.setString(1,rn);
                  ps.setDate(2,res_date);
                  ps.setTime(3,res_time);
                  
                  ResultSet rs=ps.executeQuery();
                  while(rs.next()){
                      
                      PC p=new PC(rs.getInt("PCNumber"),rs.getString("RoomName"));
                      available.add(p); /* Creation of the PC bean and adding them into the returned list */
                  }
                  return available;
             }catch(SQLException io){
                    System.err.println("An error has occured during the research of a PC ! \n");
             }catch(ParseException pe){
                    System.out.println("An error has occurred during the date parsing ! \n");
             } 
        
      return null;  
    }
    
    
}

    
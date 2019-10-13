
import javafx.beans.property.*;

public class Reservation {
    private SimpleStringProperty username;
    private SimpleStringProperty room;
    private SimpleStringProperty PCnumber;
    private SimpleStringProperty date;
    private SimpleStringProperty hour;

    public Reservation(String username, String room, String PCnumber, String date, String hour){
        
        this.username = new SimpleStringProperty(username);
        this.room = new SimpleStringProperty(room);
        this.PCnumber = new SimpleStringProperty(PCnumber);
        this.date = new SimpleStringProperty(date);
        this.hour = new SimpleStringProperty(hour);
        
    }
    
    public String getUsername() {
        return username.get();
    }

    public String getRoom() {
        return room.get();
    }

    public String getPCnumber() {
        return PCnumber.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getHour() {
        return hour.get();
    }
    
    
}

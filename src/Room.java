
import javafx.beans.property.*;

public class Room {
    private SimpleStringProperty roomName;
    private SimpleIntegerProperty capacity;
    private SimpleIntegerProperty availablePCs;
    
    public Room(String roomName, int capacity, int availablePCs){
        this.roomName = new SimpleStringProperty(roomName);
        this.capacity = new SimpleIntegerProperty(capacity);
        this.availablePCs = new SimpleIntegerProperty(availablePCs);
    }
    
    public String getRoomName() {
        return roomName.get();
    }

    public int getCapacity() {
        return capacity.get();
    }

    public int getAvailablePCs() {
        return availablePCs.get();
    }
    
}

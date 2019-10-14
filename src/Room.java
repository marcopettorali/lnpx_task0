
import javafx.beans.property.*;

public class Room {
    private SimpleStringProperty roomName;
    private SimpleIntegerProperty capacity;
    private SimpleIntegerProperty availablePCs;
    private SimpleIntegerProperty rowNumber;
    
    public Room(String roomName, int capacity, int availablePCs, int rowNumber){
        this.roomName = new SimpleStringProperty(roomName);
        this.capacity = new SimpleIntegerProperty(capacity);
        this.availablePCs = new SimpleIntegerProperty(availablePCs);
        this.rowNumber = new SimpleIntegerProperty(rowNumber);
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
    
    public int getRowNumber() {
        return rowNumber.get();
    }

    public void setAvailablePCs(int availablePCs) {
        this.availablePCs = new SimpleIntegerProperty(availablePCs);
    }
    
}

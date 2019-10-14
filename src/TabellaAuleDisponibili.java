
import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


public class TabellaAuleDisponibili {
    private TableView<Room> tb;
    private ObservableList<Room> ObListRoom;
    TabellaAuleDisponibili(double tbHeigth)
    {        
        tb = new TableView<Room>();
        TableColumn Room = new TableColumn("Room"); 
        Room.setCellValueFactory(new PropertyValueFactory<>("roomName")); 
        TableColumn Available = new TableColumn("Capacity"); 
        Available.setCellValueFactory(new PropertyValueFactory<>("capacity")); 
        TableColumn Booked = new TableColumn("availablePCs");   
        Booked.setCellValueFactory(new PropertyValueFactory<>("availablePCs")); 
        tb.getColumns().addAll(Room,Available,Booked);
        //---------------D-----------//
        tb.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tb.setPrefHeight(tbHeigth);
        //--------------------------//
    }
    
    public void RiempiTabellaAuleDisponibili(List<Room> ArrayRooms)
    {
        ObListRoom= FXCollections.observableArrayList();
        ObListRoom.addAll(ArrayRooms);
        tb.setItems(ObListRoom);
        tb.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    public Room getSelected(){
        return tb.getSelectionModel().getSelectedItem();
    }
    
    public void relaseSelection(){
        tb.getSelectionModel().clearSelection();
    }
    
    public void updateRoomsInformation(int row, Room NewRoomInformations)
    {
        ObListRoom.set(row, NewRoomInformations);
        tb.setItems(ObListRoom);
    }
    
    public TableView gettb()
    {
        return tb;
    }
}


import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


public class TabellaAuleDisponibili {
    private final TableView<Room> tb;
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
    
    
    public TableView gettb()
    {
        return tb;
    }
}

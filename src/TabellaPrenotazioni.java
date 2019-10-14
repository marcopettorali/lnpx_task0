
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


public class TabellaPrenotazioni {
    private final TableView<Reservation> tb;
    private ObservableList<Reservation> ReservationList;
    TabellaPrenotazioni()
    {        
        tb = new TableView<Reservation>();
        TableColumn Username = new TableColumn("Username"); 
        Username.setCellValueFactory(new PropertyValueFactory<>("username")); 
        TableColumn Room = new TableColumn("Room"); 
        Room.setCellValueFactory(new PropertyValueFactory<>("room")); 
        TableColumn Day = new TableColumn("PC"); 
        Day.setCellValueFactory(new PropertyValueFactory<>("PCnumber")); 
        TableColumn Hour = new TableColumn("Date"); 
        Hour.setCellValueFactory(new PropertyValueFactory<>("date")); 
        TableColumn PC = new TableColumn("Hour"); 
        PC.setCellValueFactory(new PropertyValueFactory<>("hour"));   
        tb.getColumns().addAll(Room,Day,Hour,PC);
        //---------------D-----------//
        tb.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //--------------------------//
    }
    
    /*
    Questa funzione inserisce all'interno della TabellaPrenotazioni un array 
        di reservation che dovrà essere riempito da DBManager.
    */
    public void RiempiTabellaReservation(ArrayList<Reservation> ArrayReservation)
    {
        ReservationList= FXCollections.observableArrayList();
        for(int i=0;i<ArrayReservation.size();i++)
        {
            ReservationList.add(ArrayReservation.get(i));
        }
        tb.setItems(ReservationList);
        tb.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    public Reservation getSelected(){
        return tb.getSelectionModel().getSelectedItem();
    }
    
    public void relaseSelection(){
        tb.getSelectionModel().clearSelection();
    }
    
    public TableView gettb()
    {
        return tb;
    }
}


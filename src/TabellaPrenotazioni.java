
import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


public class TabellaPrenotazioni {
    private final TableView tb;
    private ObservableList<Reservation> ReservationList;
    TabellaPrenotazioni()
    {        
        tb = new TableView();
        TableColumn Room = new TableColumn("Room"); 
        Room.setCellValueFactory(new PropertyValueFactory<>("Room")); 
        TableColumn Day = new TableColumn("Day"); 
        Day.setCellValueFactory(new PropertyValueFactory<>("Day")); 
        TableColumn Hour = new TableColumn("DataScadenza"); 
        Hour.setCellValueFactory(new PropertyValueFactory<>("Hour")); 
        TableColumn PC = new TableColumn("PC"); 
        PC.setCellValueFactory(new PropertyValueFactory<>("PC"));   
        tb.getColumns().addAll(Room,Day,Hour,PC);
        //---------------D-----------//
        tb.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //--------------------------//
    }
    
    /*
    Questa funzione inserisce all'interno della TabellaPrenotazioni un array 
        di reservation che dovrà essere riempito da DBManager.
    */
    public void RiempiTabellaReservation(List<Reservation> ArrayReservation)
    {
        ReservationList= FXCollections.observableArrayList();
        ReservationList.addAll(ArrayReservation);
        tb.setItems(ReservationList);
        tb.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    public TableView gettb()
    {
        return tb;
    }
}

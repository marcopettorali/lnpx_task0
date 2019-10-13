
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class TabellaAuleDisponibili {
    private final TableView tb;
   
    TabellaAuleDisponibili(double tbHeigth)
    {        
        tb = new TableView();
        TableColumn Room = new TableColumn("Room"); 
        Room.setCellValueFactory(new PropertyValueFactory<>("Room")); 
        TableColumn Available = new TableColumn("Available"); 
        Available.setCellValueFactory(new PropertyValueFactory<>("Available")); 
        TableColumn Booked = new TableColumn("Booked");   
        tb.getColumns().addAll(Room,Available,Booked);
        //---------------D-----------//
        tb.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tb.setPrefHeight(tbHeigth);
        //--------------------------//
    }
    
    public TableView gettb()
    {
        return tb;
    }
}

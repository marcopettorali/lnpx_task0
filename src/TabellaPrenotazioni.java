
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class TabellaPrenotazioni {
    private final TableView tb;
   
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
    
    public TableView gettb()
    {
        return tb;
    }
}

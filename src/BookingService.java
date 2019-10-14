
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.*;



public class BookingService extends Application {
    private TextField TFUsername;
    private PasswordField TFPassword;
    private TextField TFPasswordVisible;
    private CheckBox SHPassword;
    private Text TxUsername;
    private Text TxPassword;
    private Group GruppoElementi;
    private Scene scene;
    private DatePicker DPGiorno; //02
    private TabellaPrenotazioni TabPrenotazioni;
    private TabellaAuleDisponibili TabAuleDisp;
    private String username;
    //-------------------D----------------//
    public static final int WindowHeight = 600;
    public static final int WindowWidth = 1000;
    private final Pane map = new Pane();
    PCIcon[] pcarray = null; 
    //------------------------------------//
    private DBManager DBM;
    private ComboBox CBOrari; 
    private String OrarioScelto;
    
    public static void main(String[] args){
    Application.launch(BookingService.class, args);
    }
    
    @Override
    public void start(Stage primaryStage){ 
               DBM=new DBManager();
               login();
               //-----------D---------//
               primaryStage.setHeight(WindowHeight);
               primaryStage.setWidth(WindowWidth);
               //---------------------//
               primaryStage.setScene(scene);
               primaryStage.show();
               primaryStage.setResizable(false);
            }
    
    /* Questa funzione viene chiamata all'apertura della pagina 
        1- Inizializzai i TextFiled (Username e Password ), le Label e il Button Di LOGIN necessari al Login
        2- il BTNLogin invoca al click la funzione di autenticazione VerificaCredenziali() che andrà a verificare
            se l'utente è effettivamente registrato nel Database
    */
    private void login()
    {
               TFUsername=new TextField("");          
               TFUsername.setPromptText("Your Username");
               TFPassword=new PasswordField();
               TFPassword.setPromptText("Your password");
               //-----------D--------//
               // text field to show password as unmasked
               TFPasswordVisible = new TextField();
               TFPasswordVisible.setManaged(false);
               TFPasswordVisible.setVisible(false);
               SHPassword = new CheckBox("Show/Hide Password");
               
               TFPasswordVisible.managedProperty().bind(SHPassword.selectedProperty());
               TFPasswordVisible.visibleProperty().bind(SHPassword.selectedProperty());
               
               TFPassword.managedProperty().bind(SHPassword.selectedProperty().not());
               TFPassword.visibleProperty().bind(SHPassword.selectedProperty().not());
               
               TFPasswordVisible.textProperty().bindBidirectional(TFPassword.textProperty());
               
               TxUsername = new Text("Username:");
               TxPassword = new Text("Password:");
               //--------------------//
               Button BTNLogin = new Button("LOGIN");
               BTNLogin.setOnAction((ActionEvent ev)->{VerificaCredenziali();});
               
               VBox vb=new VBox(10);
               TFUsername.setMaxWidth(WindowWidth * 3 / 10);
               TFPassword.setMaxWidth(WindowWidth * 3 / 10);
               TFPasswordVisible.setMaxWidth(WindowWidth * 3 / 10);
               vb.setPrefSize(WindowWidth, WindowHeight);
               vb.setAlignment(Pos.CENTER);
               vb.getChildren().addAll(TxUsername,TFUsername,TxPassword,TFPassword,TFPasswordVisible,SHPassword,BTNLogin); 
               GruppoElementi=new Group(vb);
               
               scene= new Scene(GruppoElementi);
    }
    
    /* Questa funzione è chiamata dal Button BTNLogin è deve verificare se l'utente è 
       iscritto al sistema. Se l'utente è iscritto viene caricata la nuova interfaccia
        chiamdo InterfacciaDiPrenotazione();
    */
    private void VerificaCredenziali()
    {
        username = TFUsername.getText();
        String password=TFPassword.getText();
       
        if(DBM.checkLogin(username,password))   
        {
            TabPrenotazioni=new TabellaPrenotazioni();
            ArrayList<Reservation> LReservations = DBM.loadUserReservations(username);
            //System.out.println(LReservations.get(0).getDate());
            TabPrenotazioni.RiempiTabellaReservation(LReservations);
            InterfacciaDiPrenotazione();
            
        }
    }
    
    /* Questa funzione è invocata da VerificaCredenziali() 
        - Crea l'interfaccia principale
    */
    private void InterfacciaDiPrenotazione()
    {
       
        TabAuleDisp= new TabellaAuleDisponibili(WindowHeight * 10 / 20);
        
        DPGiorno=new DatePicker();
        DPGiorno.setShowWeekNumbers(false);
        DPGiorno.setMaxWidth(150);   
        ObservableList<String> comboItems = FXCollections.observableArrayList(
            "8:30:00","9:30:00","10:30:00","11:30:00","12:30:00","13:30:00"
            );
       CBOrari = new ComboBox(comboItems);
        CBOrari.setOnAction((Event ev) -> {
        OrarioScelto =  CBOrari.getSelectionModel().getSelectedItem().toString();    
        });
        
        Button BTNFind = new Button("FIND");
        BTNFind.setOnAction((ActionEvent ev)->{CaricaAule();});
        Button BTNDelete = new Button("DELETE");
        BTNDelete.setOnAction((ActionEvent ev)->{AnnullaPrenotazione();});
                
        HBox HBCerca= new HBox(10);
        HBCerca.getChildren().addAll(CBOrari,BTNFind);
        
        VBox VBCerca= new VBox(5);
        VBCerca.getChildren().addAll(DPGiorno,HBCerca,TabPrenotazioni.gettb(),BTNDelete);
        Button BTNReserve=new Button("RESERVE");
        //-----------D--------//
        BTNReserve.setPrefHeight(WindowHeight * 1 / 20);
        //--------------------//
        
        
        VBox VBAule= new VBox(5);
        VBAule.getChildren().addAll(TabAuleDisp.gettb(),BTNReserve,map);
        BTNReserve.setOnAction((ActionEvent ev)->{BookPC();});
        //------------------D-----------//
        SplitPane sp = new SplitPane();
        StackPane sp1 = new StackPane();
        StackPane sp2 = new StackPane();
        sp1.getChildren().add(VBCerca);
        sp1.setPrefHeight(WindowHeight);
        sp1.setMaxWidth(WindowWidth/2);
        sp1.setMinWidth(WindowWidth/2);
        sp2.getChildren().addAll(VBAule);
        sp2.setPrefHeight(WindowHeight);
        sp2.setMaxWidth(WindowWidth/2);
        sp2.setMinWidth(WindowWidth/2);
        sp.getItems().addAll(sp1,sp2);
        //HBox HBTot = new HBox(30);
        //HBTot.getChildren().addAll(VBCerca,VBAule);
        
        //-----------------------------//
        
        GruppoElementi.getChildren().removeAll(GruppoElementi.getChildren());
        GruppoElementi.getChildren().addAll(sp);
        
    }
    
    /* Funzione chiamata da InterfacciaDiPrenotazione() al click di Button BTNFind
       - Carica le aule disponibili per il giorno e l'orario selezionato
       - Carica le Aule in una TableView 
    */
    private void CaricaAule()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateFormat dateFormat2= new SimpleDateFormat("HH:mm:ss");
        LocalDate DataSelezionata=DPGiorno.getValue();
        List<Room> LAvailableRoom= DBM.LoadRooms(DataSelezionata.format(formatter),OrarioScelto);
        TabAuleDisp.RiempiTabellaAuleDisponibili(LAvailableRoom);
        if(pcarray != null){
            map.getChildren().removeAll(pcarray);
            pcarray = null;
        }
    }
    
    /* Funzione chiamata da InterfacciaDiPrenotazione() al click di Button BTNDelete
       - Annulla una prenotazione fatta dall'utente 
    */
    private void AnnullaPrenotazione()
    {
        /*DeleteReservation()*/
    }
    
    /* Funzione chiamata da InterfacciaDiPrenotazione() al click di Button BTNReserve
        - Effettua una prenotazione
        - Mostra la postazione prenotata 
    */
    private void BookPC()
    {
        /*ReservePC()*/
        /*LoadAvailablePCs()*/
        //Al posto del 5 e del 20 vanno messi il numero di righe e la capacità delle aule da recuperare nel DB
        if(pcarray != null){
            map.getChildren().removeAll(pcarray);
            pcarray = null;
        }
        String roomName = TabAuleDisp.getSelected().getRoomName();
        int roomCapacity = TabAuleDisp.getSelected().getCapacity();
        int rowNumber = TabAuleDisp.getSelected().getRowNumber();
        TabAuleDisp.relaseSelection();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List <PC> pcavaiablelist = DBM.LoadAvailablePC(roomName,DPGiorno.getValue().format(formatter),OrarioScelto);
        int indexPcSelected = pcavaiablelist.get(0).getPCnumber();
        pcarray = drawmap(rowNumber,roomCapacity,indexPcSelected);
        DBM.ReservePC(username,roomName,indexPcSelected,DPGiorno.getValue().format(formatter),OrarioScelto);
        map.getChildren().addAll(pcarray);
        //Up the reservation table
        ArrayList<Reservation> LReservations = DBM.loadUserReservations(username);
        TabPrenotazioni.RiempiTabellaReservation(LReservations);
    }
    
    private PCIcon[] drawmap(int RowNumber,int Capacity,int selectedIndex) {
        pcarray = new PCIcon[Capacity];
        int Max = RowNumber;
        double x_offset;
        double y_offset;
        if((Capacity/RowNumber) > RowNumber)
            Max = Capacity/RowNumber;
        double MapSize = WindowWidth/2;
        if(MapSize > (WindowHeight * 9 / 20))
            MapSize = WindowHeight * 9 / 20;
        y_offset = (WindowHeight * 9 / 20) - MapSize;
        x_offset = (WindowWidth/2) - MapSize;
        double Dim = MapSize  / (2*Max);
        for(int i=0 ; i < Capacity ; i++){
            PCIcon NewPc = new PCIcon(i/RowNumber ,i%RowNumber,Dim, i%RowNumber * (Capacity/RowNumber) + i/RowNumber + 1 ,x_offset,y_offset);
            pcarray[i] = NewPc;
            if(i == (selectedIndex-1)){
                NewPc.FillYellow();
            }
        }
        return pcarray;
    }
    
    }
    

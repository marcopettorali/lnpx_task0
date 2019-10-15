
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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.*;



public class BookingService extends Application {
    private TextField TFUsername;
    private PasswordField TFPassword;
    private TextField TFPasswordVisible;
    private CheckBox SHPassword;
    private Text TxUsername;
    private Text TxPassword;
    private Label TxMessaggiErrore;
    private Group GruppoElementi;
    private Scene scene;
    private DatePicker DPGiorno; //02
    private TableReservations TabPrenotazioni;
    private TableAvailableRooms TabAuleDisp;
    private String username;
    private VBox VBAule;
    //-------------------D----------------//
    public static final int WindowHeight = 600;
    public static final int WindowWidth = 1000;
    private final Pane map = new Pane();
    PCIcon[] pcarray = null; 
    //------------------------------------//
    private ComboBox CBOrari; 
    private String OrarioScelto;
    private User ActualUser;
    
    public static void main(String[] args){
    Application.launch(BookingService.class, args);
    }
    private Text TxReservations;
    private Text TxRooms;
    private LocalDate DataSelezionata;
    
    @Override
    public void start(Stage primaryStage){ 
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
               TxMessaggiErrore=new Label("");
               TxMessaggiErrore.setTextFill(Color.RED);
               TxMessaggiErrore.setAlignment(Pos.CENTER);
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
               TxMessaggiErrore.setMaxWidth(WindowWidth * 3/10);
               TFPasswordVisible.setMaxWidth(WindowWidth * 3 / 10);
               vb.setPrefSize(WindowWidth, WindowHeight);
               vb.setAlignment(Pos.CENTER);
               vb.getChildren().addAll(TxUsername,TFUsername,TxPassword,TFPassword,TFPasswordVisible,SHPassword,BTNLogin,TxMessaggiErrore); 
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
       
        if(DBManager.checkLogin(username,password))   
        {
            TabPrenotazioni=new TableReservations();
            ArrayList<Reservation> LReservations = DBManager.loadUserReservations(username);
            TabPrenotazioni.FillTableReservations(LReservations);
            InterfacciaDiPrenotazione();
        }
        else
        {
            TxMessaggiErrore.setText("Wrong Username or Password");
        }
    }
    
    /* Questa funzione è invocata da VerificaCredenziali() 
        - Crea l'interfaccia principale
    */
    private void InterfacciaDiPrenotazione()
    {
        TxMessaggiErrore.setText("");
        TabAuleDisp= new TableAvailableRooms(WindowHeight * 10 / 20);
        DPGiorno=new DatePicker();
        DPGiorno.setShowWeekNumbers(false);
        DPGiorno.setMaxWidth(150);   
        DPGiorno.setDayCellFactory(picker -> new DateCell() {
        @Override
        public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            LocalDate today = LocalDate.now().plusDays(1) ;

            setDisable(empty || date.compareTo(today) < 0 );
        }
    });
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
        TxReservations = new Text("Your Reservations");
        TxRooms = new Text("Available Rooms");
        VBox VBCerca= new VBox(5);
        VBCerca.getChildren().addAll(DPGiorno,HBCerca,TxReservations,TabPrenotazioni.gettb(),BTNDelete);
        Button BTNReserve=new Button("RESERVE");
        //-----------D--------//
        BTNReserve.setPrefHeight(WindowHeight * 1 / 20);
        //--------------------//
        
        
        VBAule= new VBox(5);
        VBAule.getChildren().addAll(TxRooms,TabAuleDisp.gettb(),BTNReserve,map,TxMessaggiErrore);
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
        TxMessaggiErrore.setText("");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateFormat dateFormat2= new SimpleDateFormat("HH:mm:ss");
        DataSelezionata=DPGiorno.getValue();
        if((DataSelezionata!=null)&&(OrarioScelto!=null))
        {
            List<Room> LAvailableRoom= DBManager.LoadRooms(DataSelezionata.format(formatter),OrarioScelto);
            TabAuleDisp.FillTableAvailableRooms(LAvailableRoom);
            if(pcarray != null){
                map.getChildren().removeAll(pcarray);
                pcarray = null;
            }   
        }
        else
        {
          TxMessaggiErrore.setText("Select Date and Hour");
        }
    }
    
    /* Funzione chiamata da InterfacciaDiPrenotazione() al click di Button BTNDelete
       - Annulla una prenotazione fatta dall'utente 
    */
    private void AnnullaPrenotazione()
    {
        TxMessaggiErrore.setText("");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Reservation Res=TabPrenotazioni.getSelected();
        if(Res!=null)
        {
            
            String room=TabPrenotazioni.getSelected().getRoom();
            int PCnumber=Integer.parseInt(TabPrenotazioni.getSelected().getPCnumber());
            String date=TabPrenotazioni.getSelected().getDate();
            String hour=TabPrenotazioni.getSelected().getHour();
            DBManager.DeleteReservation(username,room,PCnumber,date,hour);
            System.out.println(DataSelezionata + "  " + date);
            if((DataSelezionata!=null)&&(date.compareTo(DataSelezionata.format(formatter))==0))
            {
                List<Room> LAvailableRoom=DBManager.LoadRooms(DataSelezionata.format(formatter),OrarioScelto);
                TabAuleDisp.FillTableAvailableRooms(LAvailableRoom);
            }
            /* *************************************************************** */
            ArrayList<Reservation> LReservations = DBManager.loadUserReservations(username);
            TabPrenotazioni.FillTableReservations(LReservations);
            TabPrenotazioni.relaseSelection();
            /* *************************************************************** */
        }
        else
        {
            TxMessaggiErrore.setText("Choose one reservation");
        }
    }
    
    /* Funzione chiamata da InterfacciaDiPrenotazione() al click di Button BTNReserve
        - Effettua una prenotazione
        - Mostra la postazione prenotata 
    */
    private void BookPC()
    {
        TxMessaggiErrore.setText("");
        if(pcarray != null){
            map.getChildren().removeAll(pcarray);
            pcarray = null;
        }
        
        Room SelectedRoom=TabAuleDisp.getSelected();
        if(SelectedRoom != null){
            String roomName = SelectedRoom.getRoomName();
            int roomCapacity = SelectedRoom.getCapacity();
            int rowNumber = SelectedRoom.getRowNumber();
            int AvailablePC= SelectedRoom.getAvailablePCs();
            int index= TabAuleDisp.gettb().getSelectionModel().getFocusedIndex();
            TabAuleDisp.relaseSelection();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            List <PC> pcavaiablelist = DBManager.LoadAvailablePC(roomName,DPGiorno.getValue().format(formatter),OrarioScelto);
            if(!pcavaiablelist.isEmpty())
            {
                int indexPcSelected = pcavaiablelist.get(0).getPCnumber();
                if(DBManager.ReservePC(username,roomName,indexPcSelected,DPGiorno.getValue().format(formatter),OrarioScelto))
                {
                    TxMessaggiErrore.setText("");
                    /* ---- R --- Modificare solo la riga della tabella -- */
                    System.out.println(index);
                    SelectedRoom.setAvailablePCs(AvailablePC - 1);
                    TabAuleDisp.updateRoomsInformation(index, SelectedRoom);
                    /* ---------------------------------------------------- */
                    pcarray = drawmap(rowNumber,roomCapacity,indexPcSelected);
            
                    map.getChildren().addAll(pcarray);
        
                    //Up the reservation table
                    ArrayList<Reservation> LReservations = DBManager.loadUserReservations(username);
                    TabPrenotazioni.FillTableReservations(LReservations);
                }
                else
                {
                    TxMessaggiErrore.setText("NOT ALLOWED: Only one PC");
                }
            }
            else
            {
                /* Nessuna Postazione Disponibile */
                System.out.println(index);
                SelectedRoom.setAvailablePCs(0);
                TabAuleDisp.updateRoomsInformation(index, SelectedRoom);
            }
        }
        else{
            TxMessaggiErrore.setText("Choose one room");
        }
    }
    
    private PCIcon[] drawmap(int RowNumber,int Capacity,int selectedIndex) {
        pcarray = new PCIcon[Capacity];
        int Max = RowNumber;
        double x_offset;
        double y_offset;
        if((Capacity/RowNumber) > RowNumber)
            Max = Capacity/RowNumber;
        double MapSize = WindowWidth/2;
        if(MapSize > (WindowHeight * 5 / 20))
            MapSize = WindowHeight * 5 / 20;
        y_offset = (WindowHeight * 5 / 20) - MapSize;
        x_offset = (WindowWidth/2) - MapSize;
        double Dim = MapSize  / (2*Max);
        for(int i=0 ; i < Capacity ; i++){
            PCIcon NewPc = new PCIcon(i/RowNumber ,i%RowNumber,Dim, i%RowNumber * (Capacity/RowNumber) + i/RowNumber + 1 ,x_offset,y_offset);
            pcarray[i] = NewPc;
            if((i%RowNumber * (Capacity/RowNumber) + i/RowNumber + 1) == selectedIndex){
                NewPc.FillYellow();
            }
        }
        return pcarray;
    }
    
    }
    

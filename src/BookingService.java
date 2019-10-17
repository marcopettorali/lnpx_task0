
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.application.Application;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.*;

/**
 * BookingService is the main class of the application. It displays the GUI and
 * contains the methods for the event handlers
 */
public class BookingService extends Application {

    private TextField TFUsername;
    private PasswordField TFPassword;
    private TextField TFPasswordVisible;
    private CheckBox SHPassword;
    private Text TxUsername;
    private Text TxPassword;
    private Label TxMessaggiErrore;
    private Group ElementsGroup;
    private Scene scene;
    private DatePicker DPDay; //02
    private TableReservations TabReservations;
    private TableAvailableRooms TabAvailableRooms;
    private VBox VBRooms;
    //-------------------D----------------//

    /**
     * WindowHeight represents the height of the window
     *
     */
    public static final int WindowHeight = 600;

    /**
     * WindowWidth represents the width of the window
     *
     */
    public static final int WindowWidth = 1000;
    private final Pane map = new Pane();
    PCIcon[] pcarray = null;
    //------------------------------------//
    private ComboBox CBOrari;
    private String SelectedHour;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Application.launch(BookingService.class, args);
    }
    private Text TxReservations;
    private Text TxRooms;
    private LocalDate SelectedDate;

    @Override
    public void start(Stage primaryStage) {
        login();
        //-----------D---------//
        primaryStage.setHeight(WindowHeight);
        primaryStage.setWidth(WindowWidth);
        //---------------------//
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.setTitle("PC Booking");
    }

    /**
     * This function is executed at the openining of the UI 1- Initializes all
     * Textfields, Labels and buttons needed for the Login procedure 2- BTNLogin
     * call on click() the authentication's function CheckLoginInformations()
     */
    private void login() {
        TxMessaggiErrore = new Label("");
        TxMessaggiErrore.setTextFill(Color.RED);
        TxMessaggiErrore.setAlignment(Pos.CENTER);
        TFUsername = new TextField("");
        TFUsername.setPromptText("Your Username");
        TFPassword = new PasswordField();
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
        BTNLogin.setOnAction((ActionEvent ev) -> {
            CheckLoginInformations();
        });

        VBox vb = new VBox(10);
        TFUsername.setMaxWidth(WindowWidth * 3 / 10);
        TFPassword.setMaxWidth(WindowWidth * 3 / 10);
        TxMessaggiErrore.setMaxWidth(WindowWidth * 3 / 10);
        TFPasswordVisible.setMaxWidth(WindowWidth * 3 / 10);
        vb.setPrefSize(WindowWidth, WindowHeight);
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(TxUsername, TFUsername, TxPassword, TFPassword, TFPasswordVisible, SHPassword, BTNLogin, TxMessaggiErrore);
        ElementsGroup = new Group(vb);

        scene = new Scene(ElementsGroup);
    }

    /**
     * This function is called by the button BTNLogin 1- Check if the user exist
     * in the DB 2- IF the user exist in the DB CheckLoginInformations() fills
     * the Table that contain all the valid Reservation of the user and calls
     * the function ReservationInterface() ELSE CheckLoginInformations() print
     * an error message
     */
    private void CheckLoginInformations() {
        String username = TFUsername.getText();
        String password = TFPassword.getText();

        if (DBManager.checkLogin(username, password)) {
            TabReservations = new TableReservations();
            ArrayList<Reservation> LReservations = DBManager.loadUserReservations(username);
            TabReservations.FillTableReservations(LReservations);
            ReservationInterface();
        } else {
            TxMessaggiErrore.setText("Wrong Username or Password");
        }
    }

    /**
     * This Function is called by CheckLoginInformations() 1- Initializes the
     * Reservation Interface
     */
    private void ReservationInterface() {
        TxMessaggiErrore.setText("");
        TabAvailableRooms = new TableAvailableRooms(WindowHeight * 10 / 20);
        DPDay = new DatePicker();
        DPDay.setShowWeekNumbers(false);
        DPDay.setMaxWidth(150);
        DPDay.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0);
            }
        });
        ObservableList<String> comboItems = FXCollections.observableArrayList(
                "08:30:00", "09:30:00", "10:30:00", "11:30:00", "12:30:00", "13:30:00", "14:30:00", "15:30:00", "16:30:00", "17:30:00"
        );
        CBOrari = new ComboBox(comboItems);
        CBOrari.setOnAction((Event ev) -> {
            SelectedHour = CBOrari.getSelectionModel().getSelectedItem().toString();
        });

        Button BTNFind = new Button("FIND");
        BTNFind.setOnAction((ActionEvent ev) -> {
            LoadRooms();
        });
        Button BTNDelete = new Button("DELETE");
        BTNDelete.setOnAction((ActionEvent ev) -> {
            DeleteReservation();
        });

        HBox HBCerca = new HBox(10);
        HBCerca.getChildren().addAll(CBOrari, BTNFind);
        TxReservations = new Text("Your Reservations");
        TxRooms = new Text("Available Rooms");
        VBox VBCerca = new VBox(5);
        VBCerca.getChildren().addAll(DPDay, HBCerca, TxReservations, TabReservations.gettb(), BTNDelete);
        Button BTNReserve = new Button("RESERVE");
        //-----------D--------//
        BTNReserve.setPrefHeight(WindowHeight * 1 / 20);
        //--------------------//

        VBRooms = new VBox(5);
        VBRooms.getChildren().addAll(TxRooms, TabAvailableRooms.gettb(), BTNReserve, map, TxMessaggiErrore);
        BTNReserve.setOnAction((ActionEvent ev) -> {
            BookPC();
        });
        //------------------D-----------//
        SplitPane sp = new SplitPane();
        StackPane sp1 = new StackPane();
        StackPane sp2 = new StackPane();
        sp1.getChildren().add(VBCerca);
        sp1.setPrefHeight(WindowHeight);
        sp1.setMaxWidth(WindowWidth / 2);
        sp1.setMinWidth(WindowWidth / 2);
        sp2.getChildren().addAll(VBRooms);
        sp2.setPrefHeight(WindowHeight);
        sp2.setMaxWidth(WindowWidth / 2);
        sp2.setMinWidth(WindowWidth / 2);
        sp.getItems().addAll(sp1, sp2);
        //HBox HBTot = new HBox(30);
        //HBTot.getChildren().addAll(VBCerca,VBRooms);

        //-----------------------------//
        ElementsGroup.getChildren().removeAll(ElementsGroup.getChildren());
        ElementsGroup.getChildren().addAll(sp);

    }

    /**
     * This function by the Button BTNFind (Initialized in
     * ReservationInterface()) 1- Load in the Table the available rooms for the
     * day and hour selected 2- IF the selected day is the current day and the
     * hour selected is before the actual hour prints an error message
     */
    private void LoadRooms() {
        TxMessaggiErrore.setText("");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        SelectedDate = DPDay.getValue();
        DateTimeFormatter dtt = DateTimeFormatter.ofPattern("HH:mm:ss");

        if ((SelectedDate != null) && (SelectedHour != null)) {
            LocalTime l = LocalTime.parse(SelectedHour);
            if (SelectedDate.isEqual(LocalDate.now()) && (l.isBefore(LocalTime.now()))) {
                TxMessaggiErrore.setText("This program is not able to go back in time");

            } else {
                List<Room> LAvailableRoom = DBManager.loadRooms(SelectedDate.format(formatter), SelectedHour);
                TabAvailableRooms.FillTableAvailableRooms(LAvailableRoom);
                if (pcarray != null) {
                    map.getChildren().removeAll(pcarray);
                    pcarray = null;
                }
            }
        } else {
            TxMessaggiErrore.setText("Select Date and Hour");
        }
    }

    /**
     * This function is called by Button BTNDelete (Initialized on
     * ReservationInterface() 1- Delete the selected reservation from the DB 2-
     * If the Reservation deleted belongs to the loaded Table that contains the
     * Available Rooms DeleteReservation() updates that table
     */
    private void DeleteReservation() {
        TxMessaggiErrore.setText("");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Reservation Res = TabReservations.getSelected();
        if (Res != null) {

            String room = TabReservations.getSelected().getRoom();
            int PCnumber = Integer.parseInt(TabReservations.getSelected().getPCnumber());
            String date = TabReservations.getSelected().getDate();
            String hour = TabReservations.getSelected().getHour();
            DBManager.deleteReservation(User.username, room, PCnumber, date, hour);
            System.out.println(SelectedDate + "  " + date);
            if ((SelectedDate != null) && (date.compareTo(SelectedDate.format(formatter)) == 0)) {
                List<Room> LAvailableRoom = DBManager.loadRooms(SelectedDate.format(formatter), SelectedHour);
                TabAvailableRooms.FillTableAvailableRooms(LAvailableRoom);
            }
            /* *************************************************************** */
            ArrayList<Reservation> LReservations = DBManager.loadUserReservations(User.username);
            TabReservations.FillTableReservations(LReservations);
            TabReservations.relaseSelection();
            /* *************************************************************** */
        } else {
            TxMessaggiErrore.setText("Choose one reservation");
        }
    }

    /**
     * This function is called by BTNReserve Initialized on
     * ReservationInterface() 1- Loads the available PCs from the DB 2- If there
     * is an available PC on the room selected and the user has not a
     * reservation for that day an hour the system reserve the first available
     * PC and updates the Table that contains the Reservations
     */
    private void BookPC() {
        TxMessaggiErrore.setText("");
        if (pcarray != null) {
            map.getChildren().removeAll(pcarray);
            pcarray = null;
        }

        Room SelectedRoom = TabAvailableRooms.getSelected();
        if (SelectedRoom != null) {
            String roomName = SelectedRoom.getRoomName();
            int roomCapacity = SelectedRoom.getCapacity();
            int rowNumber = SelectedRoom.getRowNumber();
            int AvailablePC = SelectedRoom.getAvailablePCs();
            int index = TabAvailableRooms.gettb().getSelectionModel().getFocusedIndex();
            TabAvailableRooms.relaseSelection();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            List<PC> pcavaiablelist = DBManager.loadAvailablePCs(roomName, DPDay.getValue().format(formatter), SelectedHour);
            if (!pcavaiablelist.isEmpty()) {
                int indexPcSelected = pcavaiablelist.get(0).getPCnumber();
                if (DBManager.reservePC(User.username, roomName, indexPcSelected, DPDay.getValue().format(formatter), SelectedHour)) {
                    TxMessaggiErrore.setText("");
                    /* ---- R --- Modificare solo la riga della tabella -- */
                    System.out.println(index);
                    SelectedRoom.setAvailablePCs(AvailablePC - 1);
                    TabAvailableRooms.updateRoomsInformation(index, SelectedRoom);
                    /* ---------------------------------------------------- */
                    pcarray = drawmap(rowNumber, roomCapacity, indexPcSelected);

                    map.getChildren().addAll(pcarray);

                    //Up the reservation table
                    ArrayList<Reservation> LReservations = DBManager.loadUserReservations(User.username);
                    TabReservations.FillTableReservations(LReservations);
                } else {
                    TxMessaggiErrore.setText("NOT ALLOWED: Only one PC");
                }
            } else {
                /* Nessuna Postazione Disponibile */
                System.out.println(index);
                SelectedRoom.setAvailablePCs(0);
                TabAvailableRooms.updateRoomsInformation(index, SelectedRoom);
            }
        } else {
            TxMessaggiErrore.setText("Choose one room");
        }
    }

    /**
     * drawMap draws the map of the computers located in a selected room
     *
     * @param RowNumber it represents the number of rows in the classroom
     * @param Capacity it represents the capacity of the room
     * @param selectedIndex it indicates to the function which position to
     * highlight
     * @return it returns an array of PCIcon
     */
    private PCIcon[] drawmap(int RowNumber, int Capacity, int selectedIndex) {
        pcarray = new PCIcon[Capacity];
        int Max = RowNumber;
        double x_offset;
        double y_offset;
        if ((Capacity / RowNumber) > RowNumber) {
            Max = Capacity / RowNumber;
        }
        double MapSize = WindowWidth / 2;
        if (MapSize > (WindowHeight * 5 / 20)) {
            MapSize = WindowHeight * 5 / 20;
        }
        y_offset = (WindowHeight * 5 / 20) - MapSize;
        x_offset = (WindowWidth / 2) - MapSize;
        double Dim = MapSize / (2 * Max);
        for (int i = 0; i < Capacity; i++) {
            PCIcon NewPc = new PCIcon(i / RowNumber, i % RowNumber, Dim, i % RowNumber * (Capacity / RowNumber) + i / RowNumber + 1, x_offset, y_offset);
            pcarray[i] = NewPc;
            if ((i % RowNumber * (Capacity / RowNumber) + i / RowNumber + 1) == selectedIndex) {
                NewPc.FillYellow();
            }
        }
        return pcarray;
    }

}

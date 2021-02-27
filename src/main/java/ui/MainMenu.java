package ui;

import domain.BirthdayCake;
import domain.MyInterface;
import domain.Order;
import domain.Status;
import exceptions.CustomException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;
import service.BirthdayCakeFileService;
import service.BirthdayCakeService;
import service.OrderFileService;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import service.OrderService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainMenu extends Application {
    private final BirthdayCakeFileService birthdayCakeFileService;
    private final BirthdayCakeService birthdayCakeCtrl;
    private final OrderFileService orderCtrl;
    private final OrderService orderService;
    private final TextField nameTextField = new TextField();
    private final TextField nameTextField2 = new TextField();
    private final TextField addressTextField = new TextField();
    private final TextField addressTextField2 = new TextField();
    private final TextField phoneTextField = new TextField();
    private final TextField cakeNameTextField = new TextField();
    private final TextField creamTextField = new TextField();
    private final TextField priceTextField = new TextField();
    private final TextField weightTextField = new TextField();
    private final TextField dateTextField = new TextField();
    private final TextField statusTextField = new TextField();
    private final TextField idTextField = new TextField();
    private final TableView<BirthdayCake> cakeTableView = new TableView<>();
    private ObservableList<BirthdayCake> cakeObservableList;
    private final TableView<Order> orderTableView = new TableView<>();
    private ObservableList<Order> orderObservableList;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public MainMenu(BirthdayCakeFileService bFCtrl, BirthdayCakeService bCtrl, OrderFileService oCtrl, OrderService orderService){
        this.birthdayCakeFileService = bFCtrl;
        this.birthdayCakeCtrl = bCtrl;
        this.orderCtrl = oCtrl;
        this.orderService = orderService;
    }

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        try{
            Image logo = new Image("logo.jpg");
            primaryStage.getIcons().add(logo);
            TabPane tabPane = new TabPane();
            VBox mainVLayout = new VBox();

            MenuButton contextCakeMenu = new MenuButton("Filter cakes");
            MenuButton contextOrderMenu = new MenuButton("Filter orders");
            Menu help = new Menu("Help");
            MenuItem currentVersion = new MenuItem("Check current version");
            currentVersion.setOnAction(event -> {
                HBox hBox = new HBox();
                Label label = new Label("You are running the latest version.");
                hBox.getChildren().add(label);
                hBox.setAlignment(Pos.CENTER);
                Stage newStage = new Stage();
                Image logo2 = new Image("exclamation.png");
                newStage.getIcons().add(logo2);
                Scene newScene = new Scene(hBox, 200, 50);
                newScene.getStylesheets().add("popupStylesheet.css");
                newStage.setScene(newScene);
                newStage.setResizable(false);
                newStage.show();
            });
            MenuItem filterByName = new MenuItem("Filter by Name");
            filterByName.setOnAction(event -> {
                MyInterface<BirthdayCake> myInterface = (param) -> this.birthdayCakeCtrl.filterByName(param);
                filter("Filter", "Filter by name", myInterface, "cake");
            });
            MenuItem filterByPrice = new MenuItem("Highest Price");
            filterByPrice.setOnAction(event -> {
                MyInterface<BirthdayCake> myInterface = (param -> this.birthdayCakeCtrl.filterByPrice());
                filter("Filter", "Just press the button to get the most expensive cakes", myInterface, "cake");
            });
            MenuItem filterByWeight = new MenuItem("Filter Weight");
            filterByWeight.setOnAction(event -> {
                MyInterface<BirthdayCake> myInterface = (param -> {
                    if (param.isEmpty() || !checkString(param)){
                        displayMessage("Please introduce a number", Alert.AlertType.ERROR);
                        return new ArrayList<>(this.birthdayCakeCtrl.findAll());
                    } else
                        return this.birthdayCakeCtrl.filterByWeight(param);
                });
                filter("Filter", "Filter by weight", myInterface, "cake");
            });
            MenuItem filterByDate = new MenuItem("Filter by Date");
            filterByDate.setOnAction(event -> {
                MyInterface<Order> myInterface = (param -> this.orderService.filterByDate(param));
                filter("Filter", "Filter by date", myInterface, "orders");
            });
            MenuItem filterByCutomerName = new MenuItem("Filter by Customer Name");
            filterByCutomerName.setOnAction(event -> {
                MyInterface<Order> myInterface = (param -> this.orderService.filterByName(param));
                filter("Filter", "Filter by customer name", myInterface, "orders");
            });
            MenuItem filterByStauts = new MenuItem("Filter by Status");
            filterByStauts.setOnAction(event -> {
                MyInterface<Order> myInterface = (param -> {
                    if (param.isEmpty()) {
                        displayMessage("Fill in the field please", Alert.AlertType.ERROR);
                        return new ArrayList<>(this.orderCtrl.findAll());
                    }
                    if(param.equals("Finished") || param.equals("NotFinished") || param.equals("Cancelled") || param.equals("Unknown"))
                        return this.orderService.filterByStatus(param);
                    else {
                        String message = param + " is not a valid status";
                        displayMessage(message, Alert.AlertType.ERROR);
                        return new ArrayList<>(this.orderService.findAll());
                    }
                });
                filter("Filter", "Filter by status", myInterface, "orders");
            });
            contextCakeMenu.getItems().addAll(filterByName, filterByPrice, filterByWeight);
            contextOrderMenu.getItems().addAll(filterByDate, filterByCutomerName, filterByStauts);
            help.getItems().add(currentVersion);
            MenuBar menuBar = new MenuBar(help);
            menuBar.setId("menu-bar");

            HBox hLayoutMenuBar = new HBox();
            hLayoutMenuBar.getChildren().addAll(contextCakeMenu, contextOrderMenu, menuBar);
            hLayoutMenuBar.setAlignment(Pos.CENTER_RIGHT);
            hLayoutMenuBar.setId("menu-bar-hLayout");
            mainVLayout.getChildren().addAll(hLayoutMenuBar, tabPane);

            Scene scene = new Scene(mainVLayout, 500, 550);
            Tab cakeTab = new Tab();
            Tab orderTab = new Tab();
            Tab orderDBTab = new Tab();

            Image title = new Image("title.png");
            ImageView titleView = new ImageView();
            titleView.setFitWidth(400);
            titleView.setFitHeight(200);
            titleView.setImage(title);

            //birthday cake tab
            VBox vLayoutCakeTab = new VBox();
            vLayoutCakeTab.setAlignment(Pos.CENTER);
            vLayoutCakeTab.setSpacing(10);

            GridPane cakeGridPane = new GridPane();
            cakeGridPane.setAlignment(Pos.CENTER);
            cakeGridPane.setVgap(5);
            cakeGridPane.setHgap(5);
            Separator hSeparator = new Separator(Orientation.HORIZONTAL);
            HBox hLayoutButtons = new HBox();
            hLayoutButtons.setAlignment(Pos.CENTER);
            hLayoutButtons.setSpacing(10);
            hLayoutButtons.setId("btn-layout");
            Button addCakeButton = new Button("Add");
            Button deleteCakeButton = new Button("Delete");
            Button updateCakeButton = new Button("Update");
            addCakeButton.setId("addBtn");
            deleteCakeButton.setId("deleteBtn");
            updateCakeButton.setId("updateBtn");

            ArrayList<BirthdayCake> cakeArrayList = new ArrayList<>(birthdayCakeCtrl.findAll());
            cakeObservableList = FXCollections.observableArrayList(cakeArrayList);
            cakeTableView.setItems(cakeObservableList);
            TableColumn<BirthdayCake, String> cakeIdColumn = new TableColumn<>("Id");
            cakeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            TableColumn<BirthdayCake, String> cakeNameColumn = new TableColumn<>("Name");
            cakeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            TableColumn<BirthdayCake, String> cakeCreamColumn = new TableColumn<>("Cream");
            cakeCreamColumn.setCellValueFactory(new PropertyValueFactory<>("cream"));
            TableColumn<BirthdayCake, String> cakePriceColumn = new TableColumn<>("Price");
            cakePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            TableColumn<BirthdayCake, String> cakeWeightColumn = new TableColumn<>("Weight");
            cakeWeightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
            cakeTableView.getColumns().setAll(cakeIdColumn, cakeNameColumn, cakeCreamColumn, cakePriceColumn, cakeWeightColumn);

            Label nameLabel2 = new Label("Name");
            Label creamLabel = new Label("Cream");
            Label priceLabel = new Label("Price");
            Label weightLabel = new Label("Weight");
            cakeGridPane.add(nameLabel2, 0, 0);
            cakeGridPane.add(cakeNameTextField, 1, 0, 2, 1);
            cakeGridPane.add(creamLabel, 0, 1);
            cakeGridPane.add(creamTextField, 1, 1, 2, 1);
            cakeGridPane.add(priceLabel, 0, 2);
            cakeGridPane.add(priceTextField, 1, 2, 2, 1);
            cakeGridPane.add(weightLabel, 0, 3);
            cakeGridPane.add(weightTextField, 1, 3, 2, 1);

            hLayoutButtons.getChildren().addAll(addCakeButton, deleteCakeButton, updateCakeButton);
            vLayoutCakeTab.getChildren().addAll(cakeTableView, cakeGridPane, hSeparator, hLayoutButtons);
            cakeTab.setContent(vLayoutCakeTab);

            cakeTableView.getSelectionModel().selectedItemProperty().addListener(event -> {
                BirthdayCake cake = cakeTableView.getSelectionModel().getSelectedItem();
                if (cake == null) System.err.println("Cake is null, but it's fine");
                else {
                    cakeNameTextField.setText(cake.getName());
                    creamTextField.setText(cake.getCream());
                    priceTextField.setText(Double.toString(cake.getPrice()));
                    weightTextField.setText(Integer.toString(cake.getWeight()));
                }
            });

            addCakeButton.setOnAction(event -> {
                String name = cakeNameTextField.getText();
                String cream = creamTextField.getText();
                String price = priceTextField.getText();
                String weight = weightTextField.getText();
                if (name.isEmpty() || cream.isEmpty() || price.isEmpty() || weight.isEmpty()) {
                    displayMessage("Please fill in all the fields", Alert.AlertType.ERROR);
                    return;
                }
                if (!checkString(price) || !checkString(weight)) displayMessage("Incorrect price/weight", Alert.AlertType.ERROR);
                else {
                    BirthdayCake cake = new BirthdayCake(name, cream, Double.parseDouble(price), Integer.parseInt(weight));
                    System.err.println(cake);
                    try{
                        birthdayCakeFileService.add(cake);
                        cakeObservableList = FXCollections.observableArrayList(birthdayCakeFileService.findAll());
                        cakeTableView.setItems(cakeObservableList);
                        clearTextFields();
                        displayMessage("Cake added successfully", Alert.AlertType.INFORMATION);
                    } catch (CustomException e) {
                        displayMessage(e.getMessage(), Alert.AlertType.ERROR);
                    }
                }
            });

            deleteCakeButton.setOnAction(event -> {
                int selectedCake = cakeTableView.getSelectionModel().getSelectedIndex();
                selectedCake++;
                if (selectedCake < 0) displayMessage("Select a cake first!", Alert.AlertType.ERROR);
                else
                    try{
                        BirthdayCake cake = birthdayCakeCtrl.findCakeById(selectedCake);
                        try{
                            birthdayCakeCtrl.delete(cake);
                            birthdayCakeFileService.delete(cake);
                            cakeObservableList = FXCollections.observableArrayList(birthdayCakeFileService.findAll());
                            cakeTableView.setItems(cakeObservableList);
                            displayMessage("Cake successfully deleted", Alert.AlertType.INFORMATION);
                        } catch (CustomException e) {
                            displayMessage("There was an error while deleting the cake" + e, Alert.AlertType.ERROR);
                        }
                    } catch (CustomException e) {
                        displayMessage("Cake not in repo!", Alert.AlertType.ERROR);
                    }
            });

            updateCakeButton.setOnAction(event -> {
                BirthdayCake cake = cakeTableView.getSelectionModel().getSelectedItem();
                if (cake == null) displayMessage("Select a cake first!", Alert.AlertType.ERROR);
                else
                    try{
                        String name = cakeNameTextField.getText();
                        String cream = creamTextField.getText();
                        String price = priceTextField.getText();
                        String weight = weightTextField.getText();
                        if (name.isEmpty() && cream.isEmpty() && price.isEmpty() && weight.isEmpty()) {
                            displayMessage("Please fill in at least one field", Alert.AlertType.ERROR);
                            return;
                        }
                        if (!checkString(price) || !checkString(weight)) displayMessage("Incorrect price/weight", Alert.AlertType.ERROR);
                        try{
                            cake.setName(name);
                            cake.setCream(cream);
                            cake.setPrice(Double.parseDouble(price));
                            cake.setWeight(Integer.parseInt(weight));
                            birthdayCakeCtrl.update(cake, cake.getId());
                            birthdayCakeFileService.update(cake, cake.getId());
                            clearTextFields();
                            cakeObservableList = FXCollections.observableArrayList(birthdayCakeFileService.findAll());
                            cakeTableView.setItems(cakeObservableList);
                            displayMessage("Cake updated successfully!", Alert.AlertType.INFORMATION);
                        } catch (CustomException e) {
                            displayMessage("There was a problem while updating the cake", Alert.AlertType.ERROR);
                        }
                    } catch (CustomException e) {
                        displayMessage("Cake not in repo!", Alert.AlertType.ERROR);
                    }
            });

            GridPane orderGrid = new GridPane();
            VBox vLayout = new VBox();

            Image cakeImage = new Image("cake1.jpg");
            ImageView iv = new ImageView();
            ImageView imageViewChocolateCake = new ImageView();
            ImageView imageViewCakePicture = new ImageView();
            iv.setImage(cakeImage);
            iv.setFitHeight(300);
            iv.setFitWidth(500);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            iv.setCache(true);
            vLayout.getChildren().add(titleView);
            vLayout.getChildren().add(orderGrid);
            vLayout.setAlignment(Pos.CENTER);
            orderGrid.setHgap(5);
            orderGrid.setVgap(5);

            Label nameLabel = new Label("Name:");
            Label addressLabel = new Label("Address:");
            Label addressTextFieldDesc = new Label("(street, no, flat, city)");
            Label phoneLabel = new Label("Phone number:");

            Label chooseCakeLabel = new Label("Cake:");
            ChoiceBox<String> comboBox = new ChoiceBox<>();
            comboBox.setId("combo-box");
            Button placeOrderButton = new Button("Place your order");
            placeOrderButton.setId("order-btn");

            imageViewChocolateCake.setFitWidth(300);
            imageViewChocolateCake.setFitHeight(300);
            imageViewCakePicture.setFitHeight(50);
            imageViewCakePicture.setFitWidth(65);

            //creating a popup
            BorderPane display = new BorderPane();
            display.setCenter(imageViewChocolateCake);
            Popup popup = new Popup();
            popup.setWidth(500);
            popup.setHeight(500);
            popup.getContent().addAll(display);

            comboBox.getItems().add("");
            for (BirthdayCake cake : birthdayCakeFileService.findAll())
                comboBox.getItems().add(cake.getName());

            comboBox.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue) -> {
                int cakeSelected = comboBox.getSelectionModel().getSelectedIndex();
                String cakeImageName = "cake" + cakeSelected + ".jpg";
                if (newValue != null){
                    if (comboBox.getSelectionModel().getSelectedIndex() != 0){
                        Image temp = new Image(cakeImageName);
                        imageViewCakePicture.setDisable(false);
                        imageViewCakePicture.setOpacity(100);
                        imageViewCakePicture.setImage(temp);
                        imageViewChocolateCake.setImage(temp);
                        imageViewCakePicture.setOnMouseEntered(event -> popup.show(primaryStage));
                        imageViewCakePicture.setOnMouseExited(event -> popup.hide());
                    } else {
                        imageViewCakePicture.setDisable(true);
                        imageViewCakePicture.setOpacity(0);
                    }
                }

            });

            //set the halignment to center
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setHalignment(HPos.CENTER);
            Label dateLabel = new Label("Choose date");
            DatePicker datePicker = new DatePicker();
            datePicker.setEditable(false);
            datePicker.setValue(LocalDate.now());

            orderGrid.setAlignment(Pos.CENTER);
            orderGrid.add(nameLabel, 0, 0);
            orderGrid.add(nameTextField, 1, 0);
            orderGrid.add(addressTextFieldDesc, 1, 1);
            orderGrid.add(addressLabel, 0, 2);
            orderGrid.add(addressTextField, 1, 2);
            orderGrid.add(phoneLabel, 0 , 3);
            orderGrid.add(phoneTextField, 1, 3);
            orderGrid.add(chooseCakeLabel, 0, 4);
            orderGrid.add(comboBox, 1, 4);
            orderGrid.add(imageViewCakePicture, 2, 4);
            orderGrid.add(dateLabel, 0, 5);
            orderGrid.add(datePicker, 1, 5, 2, 1);

            orderGrid.add(hSeparator, 0, 6, 3, 1);
            orderGrid.add(placeOrderButton, 2, 7);
            orderGrid.getColumnConstraints().add(new ColumnConstraints(100));
            orderGrid.getColumnConstraints().add(new ColumnConstraints(200));
            orderGrid.setAlignment(Pos.CENTER);
            orderGrid.getColumnConstraints().add(col2);
            orderTab.setContent(vLayout);


            //button action handling
            placeOrderButton.setOnAction(event -> {
                String customerName = nameTextField.getText();
                String address = addressTextField.getText();
                String phone = phoneTextField.getText();
                String date = datePicker.getValue().format(dateFormatter);
                int cakeId = comboBox.getSelectionModel().getSelectedIndex();
                if (customerName.isEmpty() && address.isEmpty() && phone.isEmpty())
                    displayMessage("Please, enter your name, address and phone number", Alert.AlertType.ERROR);
                else if (customerName.isEmpty() && address.isEmpty())
                    displayMessage("Please, enter your name and address", Alert.AlertType.ERROR);
                    else if (customerName.isEmpty()) displayMessage("Please, enter your name", Alert.AlertType.ERROR);
                        else if (address.isEmpty()) displayMessage("Please, enter your address", Alert.AlertType.ERROR);
                            else if (phone.isEmpty()) displayMessage("Please, enter your phone number", Alert.AlertType.ERROR);
                if (!checkString(phone)) displayMessage("Incorrect phone number", Alert.AlertType.ERROR);
                try{
                    BirthdayCake cake = birthdayCakeCtrl.findCakeById(cakeId);
                    System.out.println(cake);
                    if (cake != null) {
                        Order o = new Order(customerName, cake, address, phone, date);
                        orderCtrl.add(o);
                        orderObservableList = FXCollections.observableArrayList(orderCtrl.findAll());
                        orderTableView.setItems(orderObservableList);
                        clearTextFields();
                        datePicker.setValue(LocalDate.now());
                        comboBox.getSelectionModel().selectFirst();
                        displayMessage("Order number " + o.getId() + " has been submitted!\n" +
                                "Total payment: $" + o.getCake().getPrice(), Alert.AlertType.INFORMATION);
                    }
                    else displayMessage("An error occurred, please try again!", Alert.AlertType.ERROR);
                }catch (CustomException e) {
                    System.err.println(e.getMessage());
                    System.err.println("Cake not in repo");
                    displayMessage("Please select a cake!", Alert.AlertType.ERROR);
                }
            });

            //order data base tab
            VBox vLayout2 = new VBox();
            vLayout2.setSpacing(10);
            orderObservableList = FXCollections.observableArrayList(orderCtrl.findAll());
            orderTableView.setItems(orderObservableList);
            TableColumn<Order, String> idColumn = new TableColumn<>("Id");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            TableColumn<Order, String> nameColumn = new TableColumn<>("Customer Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            TableColumn<Order, String> addressColumn = new TableColumn<>("Address");
            addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
            TableColumn<Order, String> phoneColumn = new TableColumn<>("Phone");
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            TableColumn<Order, String> cakeColumn = new TableColumn<>("Product");
            cakeColumn.setCellValueFactory(new PropertyValueFactory<>("cake"));
            TableColumn<Order, String> dateColumn = new TableColumn<>("Due Date");
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
            TableColumn<Order, String> statusColumn = new TableColumn<>("Status");
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

            statusColumn.setCellFactory(new Callback<TableColumn<Order, String>, TableCell<Order, String>>() {
                @Override
                public TableCell<Order, String> call(TableColumn<Order, String> param) {
                    return new TableCell<>() {
                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!isEmpty()) {
                                this.setTextFill(Color.BLACK);
                                if (item.equals("NotFinished") || item.contains("Unknown"))
                                    this.setTextFill(Color.GREY);
                                if (item.equals("Finished"))
                                    this.setTextFill(Color.GREEN);
                                if (item.equals("Cancelled"))
                                    this.setTextFill(Color.RED);
                                setText(item);
                            }
                        }
                    };
                }
            });

            orderTableView.getColumns().setAll(idColumn, nameColumn, addressColumn, phoneColumn, cakeColumn, dateColumn, statusColumn);

            ChoiceBox<String> changeStatus = new ChoiceBox<>();
            changeStatus.getItems().addAll("", "Unknown", "NotFinished", "Finished", "Cancelled");
            GridPane changeStatusGrid = new GridPane();
            changeStatusGrid.setVgap(5);
            changeStatusGrid.setHgap(5);
            changeStatusGrid.setAlignment(Pos.CENTER);
            changeStatusGrid.setId("change-status-grid");
            Label idLabel = new Label("Id");
            Label customerNameLabel = new Label("Name");
            Label addressLabel2 = new Label("Address");
            Label dateLabel2 = new Label("Date");
            Label statusLabel = new Label("Status");
            Label changeStatusLabel = new Label("Change status");
            idTextField.setEditable(false);
            nameTextField2.setEditable(false);
            addressTextField2.setEditable(false);
            dateTextField.setEditable(false);
            statusTextField.setEditable(false);

            changeStatusGrid.add(idLabel, 0 ,0);
            changeStatusGrid.add(idTextField, 1, 0, 2, 1);
            changeStatusGrid.add(customerNameLabel, 0, 1);
            changeStatusGrid.add(nameTextField2, 1, 1, 2, 1);
            changeStatusGrid.add(addressLabel2, 0, 2);
            changeStatusGrid.add(addressTextField2, 1, 2, 2, 1);
            changeStatusGrid.add(dateLabel2, 0, 3);
            changeStatusGrid.add(dateTextField, 1, 3, 2, 1);
            changeStatusGrid.add(statusLabel, 0, 4);
            changeStatusGrid.add(statusTextField, 1, 4, 2, 1);
            changeStatusGrid.add(changeStatusLabel, 0, 5);
            changeStatusGrid.add(changeStatus, 1, 5);

            changeStatus.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue) -> {
                Order order = orderTableView.getSelectionModel().getSelectedItem();
                String status = changeStatus.getSelectionModel().getSelectedItem();
                if (newValue != null)
                    if (status.isEmpty()) System.err.println("This is empty, but it's fine");
                if (o != null){
                    if (status.isEmpty()){
                        displayMessage("Choose a status", Alert.AlertType.ERROR);
                    } else {
                        order.setStatus(Status.valueOf(status));
                        orderService.update(order);
                        orderCtrl.update(order);
                        clearTextFields();
                        displayMessage("Status changed successfully", Alert.AlertType.INFORMATION);
                        orderObservableList = FXCollections.observableArrayList(orderService.findAll());
                        orderTableView.setItems(orderObservableList);
                        orderTableView.refresh();
                    }
                }
            });

            orderTableView.getSelectionModel().selectedItemProperty().addListener(event -> {
                Order o = orderTableView.getSelectionModel().getSelectedItem();
                if (o == null) System.err.println("Order is null, but it's fine");
                else {
                    idTextField.setText(Integer.toString(o.getId()));
                    nameTextField2.setText(o.getCustomerName());
                    addressTextField2.setText(o.getAddress());
                    dateTextField.setText(o.getArrivalDate());
                    statusTextField.setText(o.getStatus());
                }
            });

            vLayout2.getChildren().addAll(orderTableView, changeStatusGrid);
            orderDBTab.setContent(vLayout2);

            cakeTab.setText("Cakes");
            orderTab.setText("Order");
            orderDBTab.setText("Orders Data Base");
            cakeTab.setClosable(false);
            orderTab.setClosable(false);
            orderDBTab.setClosable(false);

            tabPane.getTabs().add(cakeTab);
            tabPane.getTabs().add(orderTab);
            tabPane.getTabs().add(orderDBTab);

            scene.getStylesheets().add("stylesheet.css");
            primaryStage.setScene(scene);
            primaryStage.setTitle("CakeShop");
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Error while starting app " + e);
            alert.showAndWait();
        }
    }

    private boolean checkString(String string) {
        for (int i = 0; i < string.length(); i++)
            if (Character.isLetter(string.charAt(i))) return false;
        return true;
    }

    private void displayMessage(String message, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearTextFields(){
        nameTextField.setText("");
        addressTextField.setText("");
        nameTextField2.setText("");
        addressTextField2.setText("");
        phoneTextField.setText("");
        cakeNameTextField.setText("");
        creamTextField.setText("");
        priceTextField.setText("");
        weightTextField.setText("");
        idTextField.setText("");
        dateTextField.setText("");
        statusTextField.setText("");
    }

    private void filter(String windowTitle, String message, MyInterface obj, String table){
        GridPane gridPane = new GridPane();
        Label text = new Label(message);
        TextField textField = new TextField("");
        Button btn = new Button("Filter");
        gridPane.add(text, 0, 0, 3, 1);
        gridPane.add(textField, 0, 1, 3, 1);
        gridPane.add(btn, 1, 2);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(5);

        ColumnConstraints col = new ColumnConstraints();
        col.setHalignment(HPos.CENTER);
        gridPane.getColumnConstraints().add(col);

        if (table.equals("cake"))
            btn.setOnAction(event -> {
                List<BirthdayCake> list = obj.foo(textField.getText());
                cakeObservableList = FXCollections.observableList(list);
                cakeTableView.setItems(cakeObservableList);
            });
        if (table.equals("orders"))
            btn.setOnAction(event -> {
                List<Order> list = obj.foo(textField.getText());
                orderObservableList = FXCollections.observableList(list);
                orderTableView.setItems(orderObservableList);
            });

        Stage stage = new Stage();
        Image logo = new Image("magnyfing_glass.png");
        stage.getIcons().add(logo);
        Scene scene = new Scene(gridPane,300, 150);
        scene.getStylesheets().add("popupStylesheet.css");
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            cakeObservableList = FXCollections.observableArrayList(this.birthdayCakeCtrl.findAll());
            cakeTableView.setItems(cakeObservableList);
            orderObservableList = FXCollections.observableArrayList(this.orderService.findAll());
            orderTableView.setItems(orderObservableList);
        });
        stage.setResizable(false);
        stage.show();
    }
}

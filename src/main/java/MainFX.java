import domain.BirthdayCake;
import domain.Order;
import javafx.application.Application;
import javafx.stage.Stage;
import repository.BirthdayCakeRepoFile;
import repository.BirthdayCakeRepository;
import repository.OrderRepoFile;
import repository.OrderRepository;
import service.BirthdayCakeFileService;
import service.BirthdayCakeService;
import service.OrderFileService;
import service.OrderService;
import ui.MainMenu;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class MainFX extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("app.config"));

            String cakesFileName = properties.getProperty("CakesFile");
            if (cakesFileName == null){
                //the property does not exist in the file
                cakesFileName = "default.txt";
                System.err.println("Cakes file not found. Using default file " + cakesFileName);
            }
            String ordersFileName = properties.getProperty("OrdersFile");
            if (ordersFileName == null){
                ordersFileName = "default.txt";
                System.err.println("Orders file not found. Using default file " + ordersFileName);
            }
            BirthdayCakeRepository birthdayCakeRepository = new BirthdayCakeRepository();
            BirthdayCakeRepoFile birthdayCakeRepoFile = new BirthdayCakeRepoFile(cakesFileName);
            BirthdayCakeFileService birthdayCakeFileService = new BirthdayCakeFileService(birthdayCakeRepoFile);
            for (BirthdayCake c : birthdayCakeRepoFile.getAll())
                birthdayCakeRepository.add(c);
            OrderRepository orderRepository = new OrderRepository();
            OrderRepoFile orderRepoFile = new OrderRepoFile(ordersFileName, birthdayCakeRepository);
            for (Order o : orderRepoFile.getAll())
                orderRepository.add(o);
            BirthdayCakeService birthdayCakeCtrl = new BirthdayCakeService(birthdayCakeRepository);
            OrderFileService orderCtrl = new OrderFileService(orderRepoFile);
            OrderService orderService = new OrderService(orderRepository);
            MainMenu ob = new MainMenu(birthdayCakeFileService, birthdayCakeCtrl, orderCtrl, orderService);
            ob.start(primaryStage);
        }catch (IOException ex){
            System.err.println("Error reading the configuration file" + ex);
        }
    }
    // TODO: 1/4/2021 bugs
    /*
     * Bugs List:
     * showing many statuses in the table when filtering by status
     * changeStatus choiceBox not spanning
     *
     */
}

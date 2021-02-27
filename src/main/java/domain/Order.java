package domain;

import exceptions.CustomException;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Order implements Identifiable<Integer>, Serializable {

    private static int idCounter = getNoOfLines("orders.txt");
    private int id;
    private String customerName;
    private BirthdayCake cake;
    private String arrivalDate;
    private String address;
    private String status;
    private String phoneNumber;

    private int calculateId(){
        return ++idCounter;
    }

    public Order(){
        this.id = 0;
        this.customerName = "";
        this.cake = null;
        this.address = "";
        this.status = Status.NotFinished.toString();
        this.phoneNumber = "0000";
    }

    public Order(String customerName, BirthdayCake cake, String address, String phoneNumber, String date) {
        this.id = this.calculateId();
        this.customerName = customerName;
        this.cake = cake;
        this.address = address;
        this.status = Status.NotFinished.toString();
        this.phoneNumber = phoneNumber;
        this.arrivalDate = date;
    }

    public Order(int id, String customerName, BirthdayCake cake, String address, String phoneNumber, String date, String status) {
        this.id = id;
        this.customerName = customerName;
        this.cake = cake;
        this.address = address;
        this.status = status;
        this.phoneNumber = phoneNumber;
        this.arrivalDate = date;
    }

    public Integer getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status.toString();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BirthdayCake getCake() {
        return cake;
    }

    public void setCakes(BirthdayCake cake) {
        this.cake = cake;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Order number " + id + ":" +
                "\n--------------" +
                "\nName: " + customerName +
                "\nAddress: " + address +
                "\nPhone: " + phoneNumber +
                "\nYour order:\n" + cake +
                "Arrival Date: " + arrivalDate;
                //"\nCost: " + totalCost;
    }

    private static int getNoOfLines (String file) {
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int lines = 0;
            while (reader.readLine() != null) lines++;
            reader.close();
            return lines;
        } catch (IOException e){
            throw new CustomException("e");
        }
    }
}

package domain;

import java.io.Serializable;

public class BirthdayCake implements Identifiable<Integer>, Serializable {

    private static int idCounter = 1;
    private int id;
    private String name;
    private String cream;
    private double price;
    private int weight;

    public BirthdayCake(){
        name = "";
        cream = "";
        price = 0;
        weight = 0;
    }

    private int calculateId(){
        return idCounter++;
    }

    public BirthdayCake(String name, String cream, double price, int weight){
        this.id = this.calculateId();
        this.name = name;
        this.cream = cream;
        this.price = price;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getCream() {
        return cream;
    }

    public double getPrice() {
        return price;
    }

    public int getWeight() {
        return weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCream(String cream) {
        this.cream = cream;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "Cake " + id +
                "\nDescription: " + name + ", " + cream +
                "\nWeight: " + weight +
                "\nPrice: " + price +
                "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof BirthdayCake) {
            BirthdayCake cake = (BirthdayCake) o;
            return cake.price == price;
        }
        return false;
    }

}
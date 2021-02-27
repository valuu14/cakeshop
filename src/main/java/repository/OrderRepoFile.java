package repository;

import domain.BirthdayCake;
import domain.Order;
import domain.Status;
import exceptions.CustomException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class OrderRepoFile extends AbstractRepository<Integer, Order> {
    private final String file;
    private BirthdayCakeRepository cakeRepo;

    public OrderRepoFile(String file, BirthdayCakeRepository repo){
        this.file = file;
        this.cakeRepo = repo;
        readFile();
    }

    private void readFile(){
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] el = line.split(";");
                if (el.length != 7) {
                    System.err.println("Not a valid number of attributes" + line);
                    continue;
                }
                try{
                    BirthdayCake c = cakeRepo.findById(Integer.parseInt(el[2]));
                    Status status = Status.valueOf(Status.class, el[6]);
                    Order o = new Order(Integer.parseInt(el[0]), el[1], c, el[3], el[4], el[5], status.toString());
                    super.add(o);
                } catch (NumberFormatException n){
                    System.err.println("The id, price or weight are not valid numbers" + el[0] + el[2] + el[3]);
                }
            }
        } catch (IOException e){
            throw new CustomException("Error reading file" + file + e);
        }
    }

    private void writeFile(){
        try (PrintWriter writer = new PrintWriter(file)){
            for(Order o : getAll()){
                String line = o.getId() + ";" + o.getCustomerName() + ";" + o.getCake().getId() + ";" + o.getAddress() + ";" + o.getPhoneNumber() + ";" + o.getArrivalDate() + ";" + o.getStatus();
                writer.println(line);
            }
        } catch (IOException e){
            throw new CustomException("Error writing file" + file + e);
        }
    }

    @Override
    public void add(Order o){
        try{
            super.add(o);
            writeFile();
        } catch (RuntimeException e){
            throw new CustomException("Cake wasn't added" + e + o);
        }
    }

    @Override
    public void delete(Order o) {
        try{
            super.delete(o);
            writeFile();
        } catch (RuntimeException e){
            throw new CustomException("Cake wasn't deleted" + e + o);
        }
    }

    @Override
    public void update(Order o, Integer id){
        try {
            super.update(o, id);
            writeFile();
        } catch (RuntimeException e){
            throw new CustomException("Cake wasn't updated" + e + o);
        }
    }
}

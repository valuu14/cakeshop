package repository;

import domain.BirthdayCake;
import exceptions.CustomException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class BirthdayCakeRepoFile extends AbstractRepository<Integer, BirthdayCake> {
    private final String file;

    public BirthdayCakeRepoFile(String file){
        this.file = file;
        readFile();
    }

    private void readFile(){
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] el=line.split(";");
                if (el.length != 4) {
                    System.err.println("Not a valid number of attributes" + line);
                    continue;
                }
                try{
                    //int Id = Integer.parseInt(el[0]);
                    double price = Double.parseDouble(el[2]);
                    int weight = Integer.parseInt(el[3]);
                    BirthdayCake c = new BirthdayCake(el[0], el[1], price, weight);
                    super.add(c);
                } catch (NumberFormatException n){
                    System.err.println("The id, price or weight are not valid numbers" + el[0] + el[2] + el[3]);
                }
            }
        } catch (IOException e){
            throw new CustomException("Error reading file" + file + e);
        }
    }

    private void writeFile(){
        try(PrintWriter writer = new PrintWriter(file)){
            for(BirthdayCake c : getAll()){
                String line = c.getName() + ";" + c.getCream() + ";" + c.getPrice() + ";" + c.getWeight();
                writer.println(line);
            }
        } catch (IOException e){
            throw new CustomException("Error writing file" + file + e);
        }
    }

    @Override
    public void add(BirthdayCake c){
        try{
            super.add(c);
            writeFile();
        } catch (RuntimeException e){
            throw new CustomException("Cake wasn't added" + e + c);
        }
    }

    @Override
    public void delete(BirthdayCake c) {
        try{
            super.delete(c);
            writeFile();
        } catch (RuntimeException e){
            throw new CustomException("Cake wasn't deleted" + e + c);
        }
    }

    @Override
    public void update(BirthdayCake c, Integer id){
        try {
            super.update(c, id);
            writeFile();
        } catch (RuntimeException e){
            throw new CustomException("Cake wasn't updated" + e + c);
        }
    }
}

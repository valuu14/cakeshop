package repository;

import domain.Order;
import exceptions.CustomException;

import java.io.*;
import java.util.Map;

public class OrderRepoSerialization extends AbstractRepository<Integer, Order> {
    private final String file;

    public OrderRepoSerialization(String file){
        this.file = file;
        readFile();
    }

    private void writeFile(){
        try(ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file))){
            stream.writeObject(repo);
        } catch (IOException e){
            throw new CustomException("Couldn't write file" + file + e);
        }
    }

    @SuppressWarnings("unchecked")
    private void readFile(){
        try(ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file))){
            repo = (Map<Integer, Order>)stream.readObject();
        } catch (IOException | ClassNotFoundException e){
            throw new CustomException("Couldn't read from file" + file + e);
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

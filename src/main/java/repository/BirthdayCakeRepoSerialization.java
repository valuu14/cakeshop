package repository;

import domain.BirthdayCake;
import exceptions.CustomException;

import java.io.*;
import java.util.Map;

public class BirthdayCakeRepoSerialization extends AbstractRepository<Integer, BirthdayCake> {
    private final String file;

    public BirthdayCakeRepoSerialization(String file){
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
            repo = (Map<Integer, BirthdayCake>)stream.readObject();
        } catch (IOException | ClassNotFoundException e){
            throw new CustomException("Couldn't read from file" + file + e);
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

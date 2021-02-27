package service;

import domain.BirthdayCake;
import repository.BirthdayCakeRepoFile;

import java.util.Collection;

public class BirthdayCakeFileService {
    private BirthdayCakeRepoFile repo;

    public BirthdayCakeFileService(BirthdayCakeRepoFile repo){
        this.repo = repo;
    }

    public void add(BirthdayCake c){
        repo.add(c);
    }

    public void delete(BirthdayCake c){
        repo.delete(c);
    }

    public void update(BirthdayCake c, int id){
        repo.update(c, id);
    }

    public BirthdayCake findCakeById(int id){
        return repo.findById(id);
    }

    public Collection<BirthdayCake> findAll() {return repo.findAll();}
}

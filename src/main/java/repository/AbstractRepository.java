package repository;

import domain.Identifiable;
import exceptions.CustomException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRepository <Id, T extends Identifiable<Id>> implements Repository <T, Id>{
    protected Map <Id, T> repo;

    public AbstractRepository(){
        repo = new HashMap<>();
    }

    public void add(T el){
        if (repo.containsKey(el.getId())){
            throw new CustomException("The element is already in the repo.");
        }
        else
            repo.put(el.getId(), el);
    }

    public void delete(T el){
        if ((repo.containsKey(el.getId())))
            repo.remove(el.getId());
        else
            throw new CustomException("Element could not be deleted.");
    }

    public void update(T el, Id id){
        if (repo.containsKey(el.getId()))
            repo.put(el.getId(), el);
        else
            throw new CustomException("Element could not be updated.");
    }

    /**
     *
     * @param id some description
     * @return an object if id in repo
     * @throws CustomException otherwise
     */
    public T findById(Id id){
        if (repo.containsKey(id))
            return repo.get(id);
        else
            throw new CustomException("Element not in the repo.");
    }

    public Iterable<T> getAll(){
        return repo.values();
    }

    public Collection<T> findAll() {return repo.values();}

    @Override
    public String toString() {
        String str = "";
        for (T el : repo.values()){
            str += el.toString() + "\n";
        }
        return str;
    }
}

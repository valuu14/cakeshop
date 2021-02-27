package service;

import domain.BirthdayCake;
import domain.BirthdayCakeComparator;
import domain.Order;
import exceptions.CustomException;
import repository.BirthdayCakeRepoFile;
import repository.BirthdayCakeRepository;
import repository.OrderRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class BirthdayCakeService {
    private BirthdayCakeRepository repo;

    public BirthdayCakeService(BirthdayCakeRepository repo) {
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
        try{
            return repo.findById(id);
        } catch (CustomException e){
            throw new CustomException("Exception from service");
        }

    }

    public Collection<BirthdayCake> findAll(){
        return repo.findAll();
    }

    /**
     *
     * @param name  name of the cake
     * @return a list of all cakes that contain the given name
     */
    public List<BirthdayCake> filterByName(String name){
        return repo.findAll().stream()
                .filter(c -> c.getName().contains(name))
                .collect(Collectors.toList());
    }

    /**
     *
     * @return a list of <class>BirthdayCake</class> with all the cakes which have the highest price
     */
    public List<BirthdayCake> filterByPrice(){
        return repo.findAll().stream()
                .collect(groupingBy(BirthdayCake::getPrice))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getKey))
                .get()
                .getValue();
    }

    public List<BirthdayCake> filterByWeight(String weight){
        return repo.findAll().stream()
                .filter(c -> c.getWeight() == Integer.parseInt(weight))
                .collect(Collectors.toList());
    }
}

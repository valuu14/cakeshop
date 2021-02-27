package service;

import domain.BirthdayCake;
import domain.Order;
import repository.OrderRepoFile;

import java.util.Collection;

public class OrderFileService{
    private OrderRepoFile repo;

    public OrderFileService(OrderRepoFile repo){
        this.repo = repo;
    }

    public void add(Order o){
        repo.add(o);
    }

    public void delete(Order o){
        repo.delete(o);
    }

    public void update(Order o){
        repo.update(o, o.getId());
    }

    public Order findCakeById(int id){
        return repo.findById(id);
    }

    public Collection<Order> findAll() {return repo.findAll();}
}

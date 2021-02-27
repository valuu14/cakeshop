package service;

import domain.BirthdayCake;
import domain.Order;
import repository.BirthdayCakeRepository;
import repository.OrderRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {
    private OrderRepository repo;

    public OrderService(OrderRepository repo) {
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

    public Collection<Order> findAll(){return repo.findAll();}

    /**
     *
     * @param name name of the customer
     * @return a list of all orders from a certain customer
     */
    public List<Order> filterByName (String name){
        return repo.findAll().stream()
                .filter(o -> o.getCustomerName().contains(name))
                .collect(Collectors.toList());
    }

    /**
     *
     * @param status
     * @return a list of all orders with the given status
     */
    public List<Order> filterByStatus (String status) {
        return repo.findAll().stream()
                .filter(o -> o.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public List<Order> filterByDate (String date) {
        return repo.findAll().stream()
                .filter(o -> o.getArrivalDate().equals(date))
                .collect(Collectors.toList());
    }
}

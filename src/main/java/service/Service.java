package service;

public interface Service <T, Tid> {
    void add(T obj);
    void delete(T obj);
    void update(T obj, Tid id);
    T findById(Tid id);
}

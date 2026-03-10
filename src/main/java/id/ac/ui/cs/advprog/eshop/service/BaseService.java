package id.ac.ui.cs.advprog.eshop.service;

import java.util.List;

public interface BaseService<T, ID> {
    T create(T entity);
    List<T> findAll();
    T findById(ID id);
    void update(ID id, T entity);
    void deleteById(ID id);
}
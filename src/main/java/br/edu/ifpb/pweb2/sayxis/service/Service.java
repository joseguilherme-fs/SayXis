package br.edu.ifpb.pweb2.sayxis.service;

import java.util.List;

public interface Service<T, ID> {

    public List<T> findAll();

    public T findById(ID id);

    public T save(T t);

}
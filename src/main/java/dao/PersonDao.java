package model;

import Doop.PersonBean;

import java.util.List;

public interface PersonDao {
    void insert(PersonBean person) throws Exception;

    void remove(String dni) throws Exception;


    void update(PersonBean person) throws Exception;

    PersonBean findById(String dni) throws Exception;


    List<PersonBean> findAll() throws Exception;
}

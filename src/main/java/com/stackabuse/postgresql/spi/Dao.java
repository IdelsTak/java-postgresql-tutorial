/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stackabuse.postgresql.spi;

import java.util.Collection;
import java.util.Optional;

/**
 *
 * @author Hiram K. <https://github.com/IdelsTak>
 * @param <T>
 */
public interface Dao<T, I> {
     
    Optional<T> get(int id);
     
    Collection<T> getAll();
     
    Optional<I> save(T t);
     
    void update(T t);
     
    void delete(T t);
}

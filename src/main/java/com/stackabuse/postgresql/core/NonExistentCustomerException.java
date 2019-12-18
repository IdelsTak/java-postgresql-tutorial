/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stackabuse.postgresql.core;

import com.stackabuse.postgresql.api.NonExistentEntityException;


public class NonExistentCustomerException extends NonExistentEntityException {

    private static final long serialVersionUID = 8633588908169766368L;

    public NonExistentCustomerException() {
        super("Customer does not exist");
    }
    
}

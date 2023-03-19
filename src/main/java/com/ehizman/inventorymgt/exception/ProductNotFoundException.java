package com.ehizman.inventorymgt.exception;


public class ProductNotFoundException extends InventoryMgtApplicationException {
    public ProductNotFoundException(String s) {
        super(s);
    }
}

package com.ehizman.inventorymgt.exception;

public class OutOfStockException extends InventoryMgtApplicationException {
    public OutOfStockException(String s) {
        super(s);
    }
}

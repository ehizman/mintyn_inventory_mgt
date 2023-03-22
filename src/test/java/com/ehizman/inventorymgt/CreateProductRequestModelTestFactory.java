package com.ehizman.inventorymgt;

import com.ehizman.inventorymgt.ui.model.CreateProductRequestModel;

public class CreateProductRequestModelTestFactory {
    public static CreateProductRequestModel getCreateOrderRequestModel() {
        CreateProductRequestModel requestModel = new CreateProductRequestModel();
        requestModel.setDescription("Test Description");
        requestModel.setPrice(100.00);
        requestModel.setProductName("Test Product");
        requestModel.setInitialStockLevel(20);

        return requestModel;
    }
}

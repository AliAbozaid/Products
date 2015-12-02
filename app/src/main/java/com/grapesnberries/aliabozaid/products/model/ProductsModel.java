package com.grapesnberries.aliabozaid.products.model;

import java.util.ArrayList;

/**
 * Created by aliabozaid on 11/24/15.
 */
public class ProductsModel {

    public int id;
    public String productDescription;
    public image image;

    public class image{
        public int width;
        public int height;
        public String url;
    }
    public int price;
}

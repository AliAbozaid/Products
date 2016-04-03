package com.grapesnberries.aliabozaid.products.model;

import android.util.TypedValue;

import com.grapesnberries.aliabozaid.products.adapters.ProductsAdapter;

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

        /*public int getHeight() {
            //return height+"dp";
            return ProductsAdapter.getHeight(height);
        }*/
    }
    public int price;
}

package com.grapesnberries.aliabozaid.products.contollers;

import com.grapesnberries.aliabozaid.products.model.ProductsModel;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by aliabozaid on 11/28/15.
 */
public interface ProductController {
    @GET("/products")
    Call<ArrayList<ProductsModel>> getProducts(@Query("count") int count, @Query("from") int from);
}

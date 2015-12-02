package com.grapesnberries.aliabozaid.products.contollers;

import com.grapesnberries.aliabozaid.products.ProductsActivity;
import com.grapesnberries.aliabozaid.products.module.ApiModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by aliabozaid on 11/28/15.
 */
@Singleton
@Component(modules = {ApiModule.class,})
public interface ApiComponent {
    void inject(ProductsActivity activity);
}


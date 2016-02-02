package com.grapesnberries.aliabozaid.products;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.grapesnberries.aliabozaid.products.adapters.ProductsAdapter;
import com.grapesnberries.aliabozaid.products.contollers.ProductController;
import com.grapesnberries.aliabozaid.products.model.ProductsModel;
import com.grapesnberries.aliabozaid.products.singleton.MyApplication;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductsActivity extends AppCompatActivity implements ProductsAdapter.GetAfter{

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private ProductsAdapter productsAdapter;
    ArrayList<ProductsModel> products;
    ProductController productController;


    @Inject
    Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        products = new ArrayList<>();

        MyApplication app = (MyApplication) getApplication();
        app.getApiComponent().inject(this);

        recyclerView.setHasFixedSize(true);

        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(getNumberOfColumn(), StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        productController = retrofit.create(ProductController.class);
        productsAdapter = new ProductsAdapter(ProductsActivity.this, products);
        recyclerView.setAdapter(productsAdapter);
        loadMore(0);

    }
    private void loadMore(int from)
    {
        Call<ArrayList<ProductsModel>> call = productController.getProducts(BuildConfig.COUNT, from);
        /*call.enqueue(new Callback<ArrayList<ProductsModel>>() {

            @Override
            public void onResponse(Response<ArrayList<ProductsModel>> response, Retrofit retrofit) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                products.addAll(response.body());
                productsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("test", t.toString());
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Toast.makeText(ProductsActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });*/
        call.enqueue(new Callback<ArrayList<ProductsModel>>() {
            @Override
            public void onResponse(Response<ArrayList<ProductsModel>> response) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if(response.body() != null)
                {
                    products.addAll(response.body());
                    productsAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("test", t.toString());
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Toast.makeText(ProductsActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //get number of column based on device aspects
    private int getNumberOfColumn()
    {
        return getResources().getInteger(R.integer.num_of_column);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_products, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(ProductsActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getAfterIndex(int index) {
        //call request to get after index
        loadMore(index);
    }
}

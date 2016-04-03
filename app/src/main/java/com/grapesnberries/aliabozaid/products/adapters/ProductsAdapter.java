package com.grapesnberries.aliabozaid.products.adapters;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import com.grapesnberries.aliabozaid.products.BuildConfig;
import com.grapesnberries.aliabozaid.products.R;
import com.grapesnberries.aliabozaid.products.model.ProductsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by aliabozaid on 11/26/15.
 */
public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static Context context;
    ArrayList<ProductsModel> productsItems;
    GetAfter getAfterListner;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_PROGRESS = 1;
    private boolean flag;

    ViewDataBinding binding;

    //interface to handle last item and call api
    public interface GetAfter{
        void getAfterIndex(int index);
    }

    //constructor to initialize
    public ProductsAdapter(Context context, ArrayList<ProductsModel> productsItems)
    {
        this.context = context;
        this.productsItems = productsItems;
        getAfterListner = (GetAfter) context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType)
        {
            //inflate item row
            case TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_row, parent, false);
                //ProductsAdapterBinding binding = ProductsAdapterBinding.inflate(R.layout.products_row, viewGroup, false);


                flag = true;
                return new Holder(view);
            //inflate progress bar
            case TYPE_PROGRESS:

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_bar, parent, false);
                //to make it bellow all rows
                StaggeredGridLayoutManager.LayoutParams layoutParams1 = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams());
                layoutParams1.setFullSpan(true);
                return new HolderProgress(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_row, parent, false);
                return new Holder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch ( viewHolder.getItemViewType () ) {

            //add items
            case TYPE_ITEM:
                Holder holder = (Holder) viewHolder;
                holder.getBinding().setVariable(BR.productsItems, productsItems);
                holder.getBinding().setVariable(BR.index, position);
                holder.getBinding().executePendingBindings();

                /*holder.itemDescription.setText(productsItems.get(position).productDescription);
                holder.itemPrice.setText(productsItems.get(position).price+"");*/
                int imageHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, productsItems.get(position).image.height, context.getResources().getDisplayMetrics());
                //set image view size
                setImageHeight(imageHeight, holder.itemImage);
                Picasso.with(context).load(productsItems.get(position).image.url).into(holder.itemImage);

                break;
            case TYPE_PROGRESS:
                //check for internet connection
                HolderProgress holderProgress = (HolderProgress) viewHolder;
                holderProgress.progressBar.setVisibility(View.VISIBLE);

                getAfterListner.getAfterIndex(productsItems.size());

                break;


        }

    }
    /*public static int getHeight(int height){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics());
    }*/

    /*@BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.with(view.getContext())
                .load(imageUrl)
                .into(view);
    }*/

    @Override
    public int getItemCount() {
        return productsItems.size()+1;
    }
    void setImageHeight(int height, ImageView imageView)
    {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
        params.height = height;
        imageView.setLayoutParams(params);
    }

    //holder for item row
    public class Holder extends RecyclerView.ViewHolder{
       @Bind(R.id.item_image)
        ImageView itemImage;
        /*@Bind(R.id.item_price)
        TextView itemPrice;
        @Bind(R.id.item_description)
        TextView itemDescription;*/

        public Holder(View view){
            super(view);
            ButterKnife.bind(this, view);
            binding = DataBindingUtil.bind(view);
        }
        public ViewDataBinding getBinding() {
            return binding;
        }

    }

    //holder for progressbar
    public class HolderProgress extends RecyclerView.ViewHolder{
        @Bind(R.id.progress_bar_last)
        ProgressBar progressBar;

        public HolderProgress(View view){
            super(view);
            ButterKnife.bind(this, view);
        }

    }


    @Override
    public int getItemViewType(int position) {
        int viewType;
        //to avoid duplicates first call
        if(productsItems.size() == 0) return -1;
        //if last item show progressbar
        if (position == productsItems.size() ) {
            viewType = TYPE_PROGRESS;
        } else {
            viewType = TYPE_ITEM;
        }
        return viewType;

    }
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }
}

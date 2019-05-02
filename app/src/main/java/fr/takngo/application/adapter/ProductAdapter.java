package fr.takngo.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fr.takngo.application.R;
import fr.takngo.application.entity.Product;
import fr.takngo.application.entity.Service;

public class ProductAdapter extends BaseAdapter {

    private Context context;
    private List<Product> datas;

    public ProductAdapter(Context context, List<Product> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater inflater = LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.one_product,null);
        }
        TextView tv_name = view.findViewById(R.id.tv_product_name);
        TextView tv_price = view.findViewById(R.id.tv_product_price);

        Product current = (Product) getItem(i);
        tv_name.setText(current.getName());
        tv_price.setText(current.getPrice() + " €");

        return view;
    }
}

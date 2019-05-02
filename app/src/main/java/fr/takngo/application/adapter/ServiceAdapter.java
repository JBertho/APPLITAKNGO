package fr.takngo.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fr.takngo.application.R;
import fr.takngo.application.entity.Road;
import fr.takngo.application.entity.Service;

public class ServiceAdapter extends BaseAdapter {

    private Context context;
    private List<Service> datas;

    public ServiceAdapter(Context context, List<Service> datas) {
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
            view = inflater.inflate(R.layout.one_service,null);
        }
        TextView tv_name = view.findViewById(R.id.tv_service_name);
        TextView tv_adress = view.findViewById(R.id.tv_service_adress);

        Service current = (Service) getItem(i);
        tv_name.setText(current.getName());
        tv_adress.setText(current.getAddress());

        return view;
    }
}

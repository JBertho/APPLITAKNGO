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

public class RoadAdapter extends BaseAdapter {

    private Context context;
    private List<Road> datas;

    public RoadAdapter(Context context, List<Road> datas) {
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
            view = inflater.inflate(R.layout.one_road,null);
        }
        TextView tv_id = view.findViewById(R.id.tv_road_id);
        TextView tv_start = view.findViewById(R.id.tv_road_star_street);

        Road current = (Road)getItem(i);
        tv_id.setText("Date: "+ current.getAppointment());
        tv_id.setTextColor(view.getResources().getColor(R.color.hint_text));
        tv_start.setText(current.getStart_street());
        tv_start.setTextColor(view.getResources().getColor(R.color.h1_text));

        return view;
    }
}

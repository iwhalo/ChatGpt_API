package com.example.chatgpt_api;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author xiaoyun
 * @Description: (用一句话描述)
 * @date 2023/2/15 14:41
 */
public class CarAdapter extends ArrayAdapter<Car> {
    private int resourceId;

    /*
    * @Desc : 创建一个ViewHolder类
    * @Author : xiaoyun
    * @Created_Time : 2023/2/15 15:04
    * @Project_Name : CarAdapter.java
    * @PACKAGE_NAME : com.example.chatgpt_api
    * @Params :
    */
    class ViewHolder{
        ImageView carimage;
        TextView carname;
    }
    public CarAdapter(@NonNull Context context, int resource, @NonNull List<Car> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Car car = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.carname = (TextView) view.findViewById(R.id.carname);

            view.setTag(viewHolder);
        } else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.carname.setText(car.getCarname());
        return view;
    }

}

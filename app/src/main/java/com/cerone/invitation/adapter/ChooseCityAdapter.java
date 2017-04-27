package com.cerone.invitation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.example.dataobjects.City;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by peekay on 14/3/17.
 */

public class ChooseCityAdapter extends ArrayAdapter<City>{

    private List<City> cities;
    Context context;

    public ChooseCityAdapter(Context context, int resource, List<City> cities) {
        super(context, resource);
        this.context = context;
        this.setCities(cities);
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public City getItem(int position) {
        return cities.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.public_city_item, parent, false);
        int screenHeight = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, (int)((screenHeight-((screenHeight*10)/100))*0.33));
        row.setLayoutParams(params);
        City city = getItem(position);
        TextView cityName = (TextView) row.findViewById(R.id.city_name);
        ImageView cityImage = (ImageView) row.findViewById(R.id.city_image);
        cityName.setText(city.getName());
        Picasso.with(context).load(city.getImage_path()).into(cityImage);
        row.setOnClickListener(createOnClickListener(position,parent));
        return row;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
        notifyDataSetChanged();
    }

    public View.OnClickListener createOnClickListener(final int position, final ViewGroup parent) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        };
    }
}


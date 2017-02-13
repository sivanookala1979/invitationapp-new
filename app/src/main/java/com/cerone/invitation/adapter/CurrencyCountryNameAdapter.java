package com.cerone.invitation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.FontTypes;
import com.example.dataobjects.CurrencyTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by connectcorners on 4/5/16.
 */
public class CurrencyCountryNameAdapter extends ArrayAdapter<CurrencyTypes> implements Filterable {

    Context context;
    List<CurrencyTypes> originalData;
    List<CurrencyTypes> filteredData;
    FontTypes fontTypes;
    private ItemFilter mFilter = new ItemFilter();
    public CurrencyCountryNameAdapter(Context context, int textViewResourceId, List<CurrencyTypes> customers) {
        super(context, textViewResourceId, customers);
        // copy all the customers into a master list
        this.context = context;
        originalData = customers;
        filteredData =customers;
        fontTypes =new FontTypes(context);
    }


    public CurrencyTypes getItem(int position)
    {
        return 	filteredData.get(position);
    }
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.country_code_dialog_row, parent, false);

        TextView code=(TextView)row.findViewById(R.id.country_code);
        TextView name=(TextView)row.findViewById(R.id.country_name);
        CurrencyTypes customer =filteredData.get(position);
        code.setText(customer.getCountryCode());
        name.setText(customer.getCountryName());
        fontTypes.setTypeface(code,name);
        return row;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            final List<CurrencyTypes> list = originalData;

            int count = list.size();
            if (constraint != null) {
                ArrayList<CurrencyTypes> suggestions = new ArrayList<CurrencyTypes>(count);
                for (CurrencyTypes customer : originalData) {
                    if (customer.getCountryName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(customer);
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results != null && results.count > 0) {
                filteredData = (ArrayList<CurrencyTypes>) results.values;
                notifyDataSetChanged();
            } else{
                filteredData = new ArrayList<CurrencyTypes>();
                notifyDataSetInvalidated();
            }


        }

    }
}

package com.example.kangaroonew;



import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListView extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] maintitle;
    private final String[] subtitle;
    private final String[] subtitle2;


    public MyListView(Activity context, String[] maintitle, String[] subtitle, String[] subtitle2) {
        super(context, R.layout.mylist, maintitle);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.maintitle = maintitle;
        this.subtitle = subtitle;
        this.subtitle2 = subtitle2;


    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mylist, null, true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);

        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        TextView subtitleText2 = (TextView) rowView.findViewById(R.id.subtitle2);

        titleText.setText(maintitle[position]);

        subtitleText.setText(subtitle[position]);
        subtitleText2.setText(subtitle2[position]);

        return rowView;

    }

    ;
}
package com.example.bottomnavigationdemo.Poetry_Show;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bottomnavigationdemo.R;

import java.util.List;

public class Poem_Adapter extends ArrayAdapter<Poem_Content> {

    private int resourceId;
    public Poem_Adapter(@NonNull Context context, int resource, @NonNull List<Poem_Content> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }
    public View getView(int p, View view, ViewGroup parent){
        Poem_Content pome_Content=getItem(p);
        View view1= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView textView_pomename=view1.findViewById(R.id.textView_pomename);
        TextView textView_dynasty=view1.findViewById(R.id.textView_dynasty);
        TextView textView_name=view1.findViewById(R.id.textView_name);
        TextView textView_content=view1.findViewById(R.id.textView_content);
        textView_pomename.setText(pome_Content.getPoemname());
        textView_dynasty.setText(pome_Content.getDynasty());
        textView_name.setText(pome_Content.getName());
        textView_content.setText(pome_Content.getContent());
        return view1;
    }
}

package com.example.readnreturn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CourseGVAdapter1 extends ArrayAdapter<CourseModel1> {

    public CourseGVAdapter1(@NonNull Context context, ArrayList<CourseModel1> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.card_item1, parent, false);
        }

        CourseModel1 courseModel1 = getItem(position);
        TextView courseTV = listitemView.findViewById(R.id.idTVCourse1);
        TextView courseTV2 = listitemView.findViewById(R.id.idTVCourse2);
        ImageView courseIV = listitemView.findViewById(R.id.idIVcourse1);

        courseTV.setText(courseModel1.getCourse_name());
        courseTV2.setText(courseModel1.getAuthor_name());
        courseIV.setImageResource(courseModel1.getImgid());
        return listitemView;
    }
}
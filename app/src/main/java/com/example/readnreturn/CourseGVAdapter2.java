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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CourseGVAdapter2 extends ArrayAdapter<CourseModel2> {

    public CourseGVAdapter2(@NonNull Context context, ArrayList<CourseModel2> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_card_myorders, parent, false);
        }

        CourseModel2 courseModel2 = getItem(position);
        TextView name = listitemView.findViewById(R.id.idTV1);
        TextView author = listitemView.findViewById(R.id.idTV2);
        TextView status = listitemView.findViewById(R.id.idTV3);
        ImageView imageView = listitemView.findViewById(R.id.idIV1);

        name.setText(courseModel2.getCourse_name());
        author.setText(courseModel2.getAuthor_name());
        status.setText(courseModel2.getStatus());
        imageView.setImageResource(courseModel2.getImgid());
        return listitemView;
    }
}
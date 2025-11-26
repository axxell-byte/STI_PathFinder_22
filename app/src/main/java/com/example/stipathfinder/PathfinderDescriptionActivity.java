package com.example.stipathfinder;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PathfinderDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathfinder_description);

        TextView descriptionText = findViewById(R.id.descriptionText);

        String content =
                "<font color='#0072B2'>STI</font> " +
                        "<font color='#FFDE21'>Pathfinder</font> is a student-created project designed to help new learners navigate " +
                        "<font color='#0072B2'>STI College Iligan</font> with ease and confidence. Our goal is to provide a simple and accessible digital guide that allows students to quickly locate classrooms, offices, and important school facilities. We developed this app to reduce confusion, save time, and make the transition into a new school environment smoother. As researchers from <font color='#0072B2'>MAWD 12A</font>, we believe that technology can make school life easier, and this application is our way of supporting future students as they begin their journey at STI.";

        descriptionText.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
    }
}

package com.example.stipathfinder;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PathfinderDeveloperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathfinder_developers);

        TextView developerText = findViewById(R.id.developerText);

        String content = "STI Pathfinder was developed by a dedicated team of MAWD 12A students who combined their skills in design, development, and research to create a helpful navigation tool for STI College Iligan.<br><br>" +
                "<b>Vince Edward Gregorio</b> - contributed through Blender 3D animation and front-end design, ensuring the visuals were engaging and clear.<br><br>" +
                "<b>Gian Ruedas</b> - our back-end developer, worked through Android Studio, Godot and 3D blueprint creation to build the system’s functional structure.<br><br>" +
                "<b>Eira Chenelle Villarin</b> - focused on research writing and interface design, strengthening both the content and the user experience.<br><br>" +
                "<b>Jay Jr. Zalsos</b> - developed the interactive prototype in Figma, contributed to the research paper, and worked on front-end design to make the app simple and easy to use.<br><br>" +
                "Together, the team combined creativity, technical skills, and dedication to bring STI Pathfinder to life as a useful guide for new students.";

        developerText.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
    }
}

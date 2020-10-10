package io.agora.openvcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import io.agora.openvcall.R;

public class AdminUpload extends AppCompatActivity {
    private Button image,video,pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upload);
        image=(Button)findViewById(R.id.Image_upload);
        video=(Button)findViewById(R.id.upload_videos);
        pdf=(Button)findViewById(R.id.pdf_upload);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AdminUpload.this,image_upload.class));

            }
        });
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminUpload.this,pdf_upload.class));

            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminUpload.this,video_upload.class));

            }
        });
    }
}
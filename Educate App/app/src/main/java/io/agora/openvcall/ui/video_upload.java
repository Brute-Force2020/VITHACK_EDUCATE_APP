package io.agora.openvcall.ui;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import io.agora.openvcall.R;

public class video_upload extends AppCompatActivity {
    private Button ch_video, up_video;
    private StorageReference storageReference;
    private StorageTask storageTask;
    private ProgressDialog progressDialog;
    public Uri videouri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_upload);
        progressDialog = new ProgressDialog(video_upload.this);
        storageReference = FirebaseStorage.getInstance().getReference("Videos");
        ch_video = (Button)findViewById(R.id.choose_video);
        up_video = (Button)findViewById(R.id.upload_video_btn);
        ch_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storageTask!=null && storageTask.isInProgress()){
                    Toast.makeText(video_upload.this,"Uploading...", Toast.LENGTH_SHORT).show();
                }
                else{
                    filechooser();
                }

            }
        });
        up_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileuploader();
            }
        });
    }
    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void fileuploader(){
        StorageReference ref = storageReference.child(System.currentTimeMillis()+"."+getExtension(videouri));
        storageTask=ref.putFile(videouri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(video_upload.this,"Video uploaded succesfully", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(video_upload.this,"Please upload a video only", Toast.LENGTH_SHORT).show();

                    }
                });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            videouri=data.getData();
        }
    }

    private void filechooser(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
}

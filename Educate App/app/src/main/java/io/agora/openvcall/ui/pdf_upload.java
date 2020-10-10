package io.agora.openvcall.ui;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
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

public class pdf_upload extends AppCompatActivity {
    private Button ch_file, up_file;
    private TextView file_tv;

    private StorageReference storageReference;
    private StorageTask storageTask;
    private ProgressDialog progressDialog;
    public Uri docuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_upload);
        progressDialog = new ProgressDialog(pdf_upload.this);
        storageReference = FirebaseStorage.getInstance().getReference("PDFs");
        ch_file = (Button)findViewById(R.id.choose_pdf);
        up_file = (Button)findViewById(R.id.pdf_upload);
        ch_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storageTask!=null && storageTask.isInProgress()){
                    Toast.makeText(pdf_upload.this,"Uploading...", Toast.LENGTH_SHORT).show();
                }
                else{
                    filechooser();
                }

            }
        });
        up_file.setOnClickListener(new View.OnClickListener() {
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
        StorageReference ref = storageReference.child(System.currentTimeMillis()+"."+getExtension(docuri));
        storageTask=ref.putFile(docuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(pdf_upload.this,"PDF uploaded succesfully", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(pdf_upload.this,"Please upload a pdf only", Toast.LENGTH_SHORT).show();

                    }
                });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            docuri=data.getData();
        }
    }

    private void filechooser(){
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    }
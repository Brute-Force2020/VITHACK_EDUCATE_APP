package io.agora.tutor;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.agora.models.Course;
import io.agora.openvcall.R;

import java.util.Objects;
import java.util.UUID;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class AddCourseActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private static final int PERMISSION_REQUEST_CODE = 200;
    String[] perms = {"android.permission.READ_EXTERNAL_STORAGE"};
    private StorageReference storageReference;
    private Uri filePath;
    private Bitmap bitmap;
    private ImageView imgCoursePic;
    private String profilePicLink;
    TextView tvAddCourse;
    MaterialButton btnAdd;
    TextInputLayout tvName, tvDesc, tvTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        imgCoursePic = findViewById(R.id.img_profile);
        tvAddCourse = findViewById(R.id.add_course);
        btnAdd = findViewById(R.id.btnAdd);


        tvAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    chooseImage();
                } else {
                    requestPermission();
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = getKey();
                FirebaseDatabase.getInstance().getReference().child("courses").child(key).setValue(new Course(key, tvName.getEditText().getText().toString(),
                        tvTopic.getEditText().getText().toString(),
                        FirebaseAuth.getInstance().getUid(), filePath.toString(),
                        tvDesc.getEditText().getText().toString()))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Course Added Successfully", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            filePath = data.getData();
            uploadImage();
        }
    }


    private void uploadImage() {
        if (filePath != null) {
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference reference = storageReference.child(UUID.randomUUID().toString());

            reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgCoursePic.setImageBitmap(bitmap);
                            profilePicLink = uri.toString();
                            progressDialog.dismiss();
                        }
                    });
                }
            })
                    .addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Objects.requireNonNull(getApplicationContext()), READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, perms, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (storageAccepted)
                    chooseImage();
                else {
                    Toast.makeText(getApplicationContext(), "Permission Denied, You need to enable the permission.", Toast.LENGTH_LONG).show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                            showMessageOKCancel(
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(perms,
                                                    PERMISSION_REQUEST_CODE);
                                        }
                                    });
                        }
                    }

                }
            }
        }

    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Objects.requireNonNull(getApplicationContext()))
                .setMessage("You need to allow access to the permission")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private String getKey() {
        long unixTime = System.currentTimeMillis();
        return String.valueOf(unixTime);
    }


}
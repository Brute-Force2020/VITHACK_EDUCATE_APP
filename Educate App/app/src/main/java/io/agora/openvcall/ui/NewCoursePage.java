package io.agora.openvcall.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.agora.openvcall.R;

public class NewCoursePage extends AppCompatActivity {

    TextView name;
    TextView dur;
    TextView disp;
    Button reg;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference db,dbInput;
    String course;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course_page2);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Choose New Course");

        name = findViewById(R.id.Cname);
        disp= findViewById(R.id.cDesp);
        dur = findViewById(R.id.Cduration);
        reg = findViewById(R.id.RegCourse);

        course = getIntent().getStringExtra("Course");

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbInput = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                dbInput.child("CoursesRegistered").child(course).setValue(1);
                new AlertDialog.Builder(NewCoursePage.this)
                        .setTitle("Congratulations!")
                        .setMessage("Appointment placed successfully!")
                        .setNegativeButton("OK", null)
                        .show();
            }
        });



        db = FirebaseDatabase.getInstance().getReference().child("AllCourses").child(course);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                disp.setText("Description:\n"+dataSnapshot.child("Description").getValue().toString());
                name.setText("Course Name:\n "+dataSnapshot.child("Name").getValue().toString());
                dur.setText("Course Duration:\n "+dataSnapshot.child("Duration").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            // back button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
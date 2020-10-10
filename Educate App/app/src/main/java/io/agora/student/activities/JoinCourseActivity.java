package io.agora.student.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import io.agora.models.Course;
import io.agora.openvcall.R;

import java.util.Objects;

public class JoinCourseActivity extends AppCompatActivity {

    String courseId;
    DatabaseReference coursesRef;
    TextView tvTitle, tvDescription;
    RatingBar ratingBar;
    MaterialButton btnJoin;
    ImageView image;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_course);

        initView();

        courseId = getIntent().getStringExtra("course");
        coursesRef = FirebaseDatabase.getInstance().getReference().child("courses");
        fetchCourseDetails(courseId);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(mAuth.getUid())).child("myCourses").child(getKey()).setValue(course.getCourseId());
                coursesRef.child(courseId).child("registeredUsers").child(getKey()).setValue(mAuth.getUid());
            }
        });
    }

    private void initView() {
        mAuth = FirebaseAuth.getInstance();
        image = findViewById(R.id.image);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tv_description);
        btnJoin = findViewById(R.id.join);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void fetchCourseDetails(String courseId) {
        coursesRef.child(courseId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    course = snapshot.getValue(Course.class);
                    tvTitle.setText(course.getCourseName());
                    tvDescription.setText(course.getCourseDescription());
                    Glide.with(getApplicationContext()).load(course.getCoursePic()).addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }
                    }).into(image);

                    ratingBar.setRating(Float.parseFloat(course.getRating()));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getKey() {
        long unixTime = System.currentTimeMillis();
        return String.valueOf(unixTime);
    }


}
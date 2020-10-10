package io.agora.tutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.agora.models.Course;
import io.agora.openvcall.R;
import io.agora.student.adapters.CoursesAdapter;

import java.util.ArrayList;
import java.util.List;

public class TeacherDashboardActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycler_view_courses);
        courseList = new ArrayList<>();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddCourseActivity.class));
            }
        });

    }

    private void fetchMyCourses() {
        FirebaseDatabase.getInstance().getReference().child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    courseList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Course course = dataSnapshot.getValue(Course.class);
                        if (course.getTutorId().equals(FirebaseAuth.getInstance().getUid())) {
                            courseList.add(course);
                        }
                    }
                    recyclerView.setAdapter(new CoursesAdapter(courseList, null));
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
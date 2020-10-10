package io.agora.student.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.agora.models.Course;
import io.agora.openvcall.R;
import io.agora.student.adapters.CoursesAdapter;
import io.agora.student.interfaces.ItemClickInterface;

/*import com.orion.educate.R;
import com.orion.educate.models.Course;
import com.orion.educate.student.adapters.CoursesAdapter;
import com.orion.educate.student.interfaces.ItemClickInterface;*/

import java.util.ArrayList;
import java.util.List;

public class CoursesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView topicName;
    String topic;
    DatabaseReference coursesRef;
    List<Course> courseList;
    private static ItemClickInterface itemClickInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        topic = getIntent().getStringExtra("topic");
        recyclerView = findViewById(R.id.recycler_view_courses);
        topicName = findViewById(R.id.topic_name);
        topicName.setText(topic);
        courseList = new ArrayList<>();
        coursesRef = FirebaseDatabase.getInstance().getReference().child("courses");

        itemClickInterface = new ItemClickInterface() {
            @Override
            public void clickTopic(String topic) {
                Intent intent = new Intent(getApplicationContext(), CoursesActivity.class);
                intent.putExtra("topic", topic);
                startActivity(intent);
            }

            @Override
            public void clickCourse(String id) {
                Intent intent = new Intent(getApplicationContext(), JoinCourseActivity.class);
                intent.putExtra("courseId", id);
                startActivity(intent);
            }
        };

        fetchCourses();

    }

    private void fetchCourses() {
        coursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Course course = dataSnapshot.getValue(Course.class);
                    if (course.getTopic().equals(topic)) {
                        courseList.add(course);
                    }
                }
                recyclerView.setAdapter(new CoursesAdapter(courseList, itemClickInterface));
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
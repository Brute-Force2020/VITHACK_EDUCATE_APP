package io.agora.student.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.agora.models.Course;
import io.agora.openvcall.R;
import io.agora.student.adapters.CoursesAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DashboardFragment extends Fragment {

    DatabaseReference coursesRef;
    List<Course> courseList;
    List<String> courseIdList;
    RecyclerView recyclerViewCourses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initView(view);
        //fetchMyCourses();
        return view;
    }

    private void initView(View view) {
        recyclerViewCourses = view.findViewById(R.id.recycler_view_courses);
        courseIdList = new ArrayList<>();
        courseList = new ArrayList<>();
        coursesRef = FirebaseDatabase.getInstance().getReference().child("courses");
    }

    private void fetchMyCourses() {
        FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("myCourses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    courseIdList.clear();
                    courseList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        courseIdList.add(dataSnapshot.getValue(String.class));
                    }
                    coursesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (courseIdList.contains(dataSnapshot.getKey())) {
                                    courseList.add(dataSnapshot.getValue(Course.class));
                                }
                            }
                            recyclerViewCourses.setAdapter(new CoursesAdapter(courseList, null));
                            recyclerViewCourses.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
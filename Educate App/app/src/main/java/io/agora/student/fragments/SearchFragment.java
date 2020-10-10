package io.agora.student.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.agora.student.activities.CoursesActivity;
import io.agora.student.adapters.TopicAdapter;
import io.agora.student.activities.CoursesActivity;
import io.agora.student.interfaces.ItemClickInterface;

import io.agora.openvcall.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private DatabaseReference topicRef;
    private List<String> topicList;
    private RecyclerView recyclerView;
    ItemClickInterface itemClickInterface;
    TextView search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        itemClickInterface = new ItemClickInterface() {
            @Override
            public void clickTopic(String topic) {
                Intent intent = new Intent(getActivity(), CoursesActivity.class);
                intent.putExtra("topic", topic);
                startActivity(intent);
            }

            @Override
            public void clickCourse(String id) {
                //
            }
        };

        initView(view);
        fetchTopics();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Click", Toast.LENGTH_SHORT).show();
            }
        });

        return view;


    }

    private void initView(View view) {
        search=view.findViewById(R.id.tv_search);
        topicList = new ArrayList<>();
        topicRef = FirebaseDatabase.getInstance().getReference().child("topics");
        recyclerView = view.findViewById(R.id.recycler_view_topics);
    }

    private void fetchTopics() {
        topicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                topicList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    topicList.add(dataSnapshot.getValue(String.class));
                }
                recyclerView.setAdapter(new TopicAdapter(topicList, itemClickInterface, getActivity()));
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
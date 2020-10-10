package io.agora.student.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import io.agora.student.fragments.DashboardFragment;
import io.agora.student.fragments.ProfileFragment;
import io.agora.student.fragments.SearchFragment;
import io.agora.student.interfaces.BottomNavigationInterface;
import io.agora.student.interfaces.ItemClickInterface;
import io.agora.openvcall.R;

public class StudentActivity extends AppCompatActivity {

    private static BottomNavigationInterface flowControlInterface;
    private static ItemClickInterface itemClickInterface;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();

        launchFragment(new DashboardFragment());

        flowControlInterface = new BottomNavigationInterface() {
            @Override
            public void launchHome() {
                launchFragment(new DashboardFragment());
            }

            @Override
            public void launchSearchCourse() {
                launchFragment(new SearchFragment());
            }

            @Override
            public void launchAccount() {
                launchFragment(new ProfileFragment());
            }

            @Override
            public void launchNotifications() {
                //Not decided yet
            }
        };

        itemClickInterface = new ItemClickInterface() {
            @Override
            public void clickTopic(String topic) {
                Intent intent = new Intent(getApplicationContext(), CoursesActivity.class);
                intent.putExtra("topic", topic);
                startActivity(intent);
            }

            @Override
            public void clickCourse(String id) {
                //Empty
            }
        };

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    getBottomNavBarControlInterface().launchHome();
                    return true;
                case R.id.search:
                    getBottomNavBarControlInterface().launchSearchCourse();
                    return true;
                case R.id.profile:
                    getBottomNavBarControlInterface().launchAccount();
                    return true;
            }
            return false;
        }
    };

    private void launchFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "").commit();
    }

    public static BottomNavigationInterface getBottomNavBarControlInterface() {
        return flowControlInterface;
    }

    public static ItemClickInterface getItemClickInterface() {
        return itemClickInterface;
    }
}
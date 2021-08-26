package akshay.kumar.instalite.Messages.Notification;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import akshay.kumar.instalite.Messages.Adapter.PageAdapter;
import akshay.kumar.instalite.R;


public class ChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = findViewById(R.id.ChatActivity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Instagram");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.ChatActivity_mainTabPager);
        pageAdapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);

        tabLayout = findViewById(R.id.ChatActivity_maintabs);
        tabLayout.setupWithViewPager(viewPager);

    }
}
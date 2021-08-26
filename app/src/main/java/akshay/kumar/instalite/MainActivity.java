package akshay.kumar.instalite;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import akshay.kumar.instalite.Fragment.HomeFragment;
import akshay.kumar.instalite.Fragment.NotificationFragment;
import akshay.kumar.instalite.Fragment.PostDetailFragment;
import akshay.kumar.instalite.Fragment.ProfileFragment;
import akshay.kumar.instalite.Fragment.SearchFragment;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    MeowBottomNavigation bottomNavigation;
    Fragment fragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_search));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.plusicon));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_heart));
        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.profileuser));
        bottomNavigation.show(1 , true);
        replace(new HomeFragment());
        Bundle intent = getIntent().getExtras();
        if (intent != null){
            String publisher = intent.getString("publisherId");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileId", publisher);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model,Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                switch (model.getId())
                {
                    case 1:
                        replace(new HomeFragment());
                        break;

                    case 2:
                        replace(new SearchFragment());
                        break;

                    case 3:
                        replace(new PostDetailFragment());
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                        break;

                    case 4:
                        replace(new NotificationFragment());
                        break;

                    case 5:
                        replace(new ProfileFragment());
                        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                        editor.putString("profileId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        editor.apply();
                        break;
                }

                return null;
            }
        });


    }
    private void replace(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();

    }
}



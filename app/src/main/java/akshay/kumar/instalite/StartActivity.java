package akshay.kumar.instalite;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class StartActivity extends AppCompatActivity {




    ImageView insta_logo;
    TextView androrealm;
    TextView frm;
    FirebaseAuth Fauth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_main);
        Fauth=FirebaseAuth.getInstance();
        if (isOnline()) {

            load();
        } else {
            try {
                new AlertDialog.Builder(StartActivity.this)
                        .setTitle("Error")
                        .setMessage("Internet not available, Cross check your internet connectivity")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                load();
                            }
                        }).show();
            } catch (Exception e) {
                Toast.makeText(StartActivity.this, "Show Dialog:"+ e.getMessage(), Toast.LENGTH_SHORT).show( );

            }
        }


    }

    public void load(){
        insta_logo = (ImageView)findViewById(R.id.insta_logo);
        androrealm = (TextView)findViewById(R.id.androrealm);
        frm = (TextView)findViewById(R.id.from);

        insta_logo.animate().alpha(0f).setDuration(0);
        androrealm.animate().alpha(0f).setDuration(0);

        insta_logo.animate().alpha(1f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                androrealm.animate().alpha(1f).setDuration(800);

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, SecondActivity.class);
                startActivity(intent);
                finish();

            }
        },3000);
    }

    private boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            return false;
        }
        return true;
    }


}
package akshay.kumar.instalite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.NotNull;


public class SecondActivity extends AppCompatActivity {

    ImageView imageView1,imageView3;
    Button login,register;
    FirebaseUser firebaseUser;
    TextView textView;
    Dialog dialog;


    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String EMAIL = "email";
    private static final String TAG = "FacebookAuthentication";



    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;
    private View view;

    private LinearLayout linearLayout;


    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser !=null){
            startActivity(new Intent(SecondActivity.this, MainActivity.class));
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        imageView3=findViewById(R.id.imageView3);
        imageView1=findViewById(R.id.imageView1);
        login=findViewById(R.id.LogIn_main);
        linearLayout=findViewById(R.id.linearLayout);
        textView =findViewById(R.id.textViewL);

        linearLayout.animate().alpha(0f).setDuration(10);
        TranslateAnimation animation =new TranslateAnimation(0,0,0,-1000);
        animation.setDuration(900);
        animation.setFillAfter(false);
        animation.setAnimationListener(new SecondActivity.MyAnimationListener());
        imageView1.setAnimation(animation);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SecondActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


       textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SecondActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    updateUI(user);
                } else {
                    updateUI(null);
                }
            }
        };

    }

    public void startGoogleLogin(View view) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityIfNeeded(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try{

            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(SecondActivity.this,"Signed In Successfull",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(SecondActivity.this, RegisterActivity.class);
            startActivity(intent);
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e){
            Toast.makeText(SecondActivity.this,"Sign In Failed",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {

        if (acct != null) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SecondActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();

                    } else {
                        Toast.makeText(SecondActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        else{
            Toast.makeText(SecondActivity.this, "acc failed", Toast.LENGTH_SHORT).show();
        }
    }
    protected void updateUI(FirebaseUser user) {
        if (user.isEmailVerified()){
            Intent intent = new Intent(SecondActivity.this,RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }





    public void openMoreDialog(View view) {
        dialog.getWindow().setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
    private  class  MyAnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            imageView1.clearAnimation();
            imageView1.setVisibility(View.VISIBLE);
            linearLayout.animate().alpha(1f).setDuration(1000);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
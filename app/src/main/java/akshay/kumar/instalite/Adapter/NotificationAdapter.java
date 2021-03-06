package akshay.kumar.instalite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import akshay.kumar.instalite.Model.Notification;
import akshay.kumar.instalite.Model.Post;
import akshay.kumar.instalite.Model.User;
import akshay.kumar.instalite.R;
import com.bumptech.glide.Glide;
import akshay.kumar.instalite.Fragment.PostDetailFragment;
import akshay.kumar.instalite.Fragment.ProfileFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private Context mContext;
    private List<Notification>mNotifications;
    FirebaseUser firebaseUser;

    public NotificationAdapter(Context mContext, List<Notification> mNotifications) {
        this.mContext = mContext;
        this.mNotifications = mNotifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.notification_item,parent,false);
        return new NotificationAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
        final Notification notification=mNotifications.get(position);
        getUser(holder.imageProfile,holder.username,notification.getUserid());

    holder.comment.setText(notification.getText());


    //here add if else condition
        holder.postImage.setVisibility(View.VISIBLE);

        getPostImage(holder.postImage,notification.getPostid());

       // holder.postImage.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit().
                            putString("postid",notification.getPostid()).apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().
                            beginTransaction().replace(R.id.fragment_container,new PostDetailFragment()).commit();

                    mContext.getSharedPreferences("PROFILE",Context.MODE_PRIVATE).edit().
                            putString("profileId",notification.getUserid()).apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container,new ProfileFragment()).commit();


            }
        });
    }

    private void getPostImage(final ImageView postImage, String postid) {
        FirebaseDatabase.getInstance().getReference().child("Posts").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                Glide.with(mContext).load(post.getPostimageurl()).into(postImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getUser(final ImageView imageProfile,final TextView username,String userId) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                try {
                    Picasso.get().load(user.getImageurl()).into(imageProfile);
                    username.setText(user.getUsername());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       public ImageView imageProfile;
       public ImageView postImage;
       private TextView username;
       private  TextView comment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile=itemView.findViewById(R.id.image_profile);
            postImage=itemView.findViewById(R.id.post_image);
            username=itemView.findViewById(R.id.username);
            comment=itemView.findViewById(R.id.comment);



        }
    }

}

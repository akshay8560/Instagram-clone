package akshay.kumar.instalite.Messages.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import akshay.kumar.instalite.Messages.MessageActivity;
import akshay.kumar.instalite.Messages.Model.Chat;
import akshay.kumar.instalite.Messages.Model.Users;
import akshay.kumar.instalite.Model.User;
import akshay.kumar.instalite.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {


    String TAG = "FriendsAdapter";

    private Context mcontext;
    private List<User> muser;
    private boolean ischat;

    private FirebaseUser firebaseUser;
    String theLastMessage;
    User users;

    public FriendsAdapter(Context mcontext, List<Users> muser, boolean ischat) {
        this.mcontext = mcontext;

        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.friends_single_layout,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        users = muser.get(position);
        holder.username.setText(users.getUsername());

        Glide.with(mcontext)
                .load(users.getImageurl())
                .into(holder.profileimage);

        if (ischat){
            lastMessage(users.getId(), holder.last_msg);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, MessageActivity.class);
                intent.putExtra("userid",users.getId());
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return muser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username,last_msg;
        public CircleImageView profileimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = (TextView)itemView.findViewById(R.id.FriendSingle_userName);
            last_msg = (TextView)itemView.findViewById(R.id.FriendSingle_lastMsg);
            profileimage = (CircleImageView)itemView.findViewById(R.id.FriendSingle_user_img);
        }
    }

    //check for last message
    private void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                        }
                    }
                }

                switch (theLastMessage){
                    case  "default":
                        last_msg.setText(users.getUsername());
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

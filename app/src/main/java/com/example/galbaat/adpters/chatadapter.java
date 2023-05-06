package com.example.galbaat.adpters;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galbaat.R;
import com.example.galbaat.models.messagemodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class chatadapter extends RecyclerView.Adapter {
    // we need array list for stroe meassage
    ArrayList<messagemodel>  messagemodels;
    Context context;
    String recID;
    int Sender_view_type = 1;
    int receiver_view_type = 2;

    public chatadapter(ArrayList<messagemodel> messagemodels, Context context) {
        this.messagemodels = messagemodels;
        this.context = context;
    }

    public chatadapter(ArrayList<messagemodel> messagemodels, Context context, String recID) {
        this.messagemodels = messagemodels;
        this.context = context;
        this.recID = recID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==Sender_view_type){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new senderviewholder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciever,parent,false);
            return new recieverviewholder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
  // know we have to check which is receiver and who is sender
        if(messagemodels.get(position).getuID().equals(FirebaseAuth.getInstance().getUid())){
            return Sender_view_type;
        }else{
            return receiver_view_type;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
messagemodel messagemodel = messagemodels.get(position);
  // when we press text for long period of time
    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            new AlertDialog.Builder(context)
                    .setTitle("Delete")
                    // message shown to user

                    .setMessage("Are you shure you want to delete this message:")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        // when user click yes
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            String sender_room = FirebaseAuth.getInstance().getUid()+recID;
                            database.getReference().child("chats").child(sender_room)
                                    .child(messagemodel.getMessageID())
                                    .setValue(null);
                        }
                        // when user click no
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();         // to show dialog
            return false;
        }
    });
// this is time for sender
if(holder.getClass()==senderviewholder.class){
    ((senderviewholder) holder).senderMsg.setText(messagemodel.getMessage());
    Date date = new Date(messagemodel.getTimestamp());
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
    String strdate = simpleDateFormat.format(date);
    ((senderviewholder) holder).senderTime.setText(strdate.toString());
}else{
    // this is time for reciever
    ((recieverviewholder) holder).receiverMsg.setText(messagemodel.getMessage());
    Date date = new Date(messagemodel.getTimestamp());
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
    String strdate = simpleDateFormat.format(date);
    ((recieverviewholder) holder).receiverTime.setText(strdate.toString());
}
    }

    @Override
    public int getItemCount() {
        return messagemodels.size();
    }

    // we have to create two view holder class one for sender and one for reciever
    public class recieverviewholder extends RecyclerView.ViewHolder{
        // it contain both message and time
        TextView receiverMsg,receiverTime;


        public recieverviewholder(@NonNull View itemView) {
            super(itemView);
            receiverMsg = itemView.findViewById(R.id.recievertext);
            receiverTime = itemView.findViewById(R.id.reciever_time);

        }
    }
    // know we create another view holder for sender

    public class senderviewholder extends RecyclerView.ViewHolder{
      TextView senderMsg,senderTime;
        public senderviewholder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);
        }
    }
}

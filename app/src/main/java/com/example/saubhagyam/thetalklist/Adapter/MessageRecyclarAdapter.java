package com.example.saubhagyam.thetalklist.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.saubhagyam.thetalklist.Bean.MessageModel;
import com.example.saubhagyam.thetalklist.CircleTransform;
import com.example.saubhagyam.thetalklist.R;
import com.example.saubhagyam.thetalklist.TTL;
import com.rockerhieu.emojicon.EmojiconTextView;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Saubhagyam on 21/06/2017.
 */

public class MessageRecyclarAdapter extends RecyclerView.Adapter<MessageRecyclarAdapter.MyViewHolder> {

    final Context context;
    private List<MessageModel> messageModelList;
    String pic;

    String op;
    String min;
    String hr;

    String time;

    public MessageRecyclarAdapter(Context context, List<MessageModel> messageModelList,String pic) {
        this.context = context;
        this.messageModelList = messageModelList;
        this.pic=pic;
    }


    public MessageRecyclarAdapter(Context context, List<MessageModel> messageModelList,String pic,String op,String min,String hr) {
        this.context = context;
        this.messageModelList = messageModelList;
        this.pic=pic;
        this.op=op;
        this.min=min;
        this.hr=hr;
    }

    public void addMsg(MessageModel m) {
        messageModelList.add(0, m);
        Toast.makeText(context, "adapter refresh", Toast.LENGTH_SHORT).show();
        this.notifyDataSetChanged();
    }

    @Override
    public MessageRecyclarAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_sender_user_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageRecyclarAdapter.MyViewHolder holder, int position) {

        Log.e("message adapter list", messageModelList.toString());

        MessageModel messageModel = messageModelList.get(position);


        if (messageModel.getSender_id() == context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0)) {
            SharedPreferences preferences = context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
            String picPath = preferences.getString("pic", "");

            Log.e("message pic path", "https://www.thetalklist.com/uploads/images/" + picPath);
            if (!picPath.equals("")) {
                Glide.with(context).load("https://www.thetalklist.com/uploads/images/" + picPath)
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.userImg);
            } else
                Glide.with(context).load("https://www.thetalklist.com/images/header.jpg")
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.userImg);
            holder.userLayout.setVisibility(View.VISIBLE);
            holder.senderLayout.setVisibility(View.GONE);
            String user_msg=messageModel.getMsg_text();
                    String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(user_msg);
            holder.user_msg.setText(fromServerUnicodeDecoded);
        } else {
            holder.senderLayout.setVisibility(View.VISIBLE);
            holder.userLayout.setVisibility(View.GONE);
            String user_msg=messageModel.getMsg_text();
            String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(user_msg);
            holder.sender_msg.setText(fromServerUnicodeDecoded);


            Log.e("tutor pic",pic);
            if (!pic.equals("")) {
                Glide.with(context).load("https://www.thetalklist.com/uploads/images/" + pic)
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.senderImg);
            } else
                Glide.with(context).load("https://www.thetalklist.com/images/header.jpg")
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.senderImg);

        }
            String date=messageModelList.get(position).getTime();
            Date date_txt=null;
            String[] months={"Jan","Feb","Mar","April","may","June","July","Aug","Sep","Oct","Nov","Dec"};
            try {
            if (date!=null) {
                date_txt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US).parse(date);

                int hour= Integer.parseInt(new SimpleDateFormat("HH", Locale.US).format(date_txt));
                int month= Integer.parseInt(new SimpleDateFormat("MM", Locale.US).format(date_txt));
                int day= Integer.parseInt(new SimpleDateFormat("dd", Locale.US).format(date_txt));

                String newStr= new SimpleDateFormat("HH:mm", Locale.US).format(date_txt); // 9:00
                String h= new SimpleDateFormat("HH", Locale.US).format(date_txt); // 9:00
                String m= new SimpleDateFormat("mm", Locale.US).format(date_txt); // 9:00

                hour=Integer.parseInt(h)+Integer.parseInt(hr)+8;
                int minf=Integer.parseInt(m)+Integer.parseInt(min);


                if (hour>24){
                    if (minf>60) {
                       time = months[month - 1] + " " + String.valueOf(day+1) + " " + (hour - 24 + 1) + ":" + (minf - 60);
                    }else{
                        time = months[month - 1] + " " + String.valueOf(day+1) + " " + (hour - 24 ) + ":" + minf ;
                    }
                    holder.sender_time.setText(time);
                    holder.receiver_time.setText(time);

                }else{
                    if (minf>60) {
                        time = months[month - 1] + " " + String.valueOf(day) + " " + (hour  + 1) + ":" + (minf - 60);
                    }else{
                        time = months[month - 1] + " " + String.valueOf(day) + " " + hour  + ":" + minf ;
                    }
                    String time=/*newStr+" "+*/months[month-1]+" "+String.valueOf(day)+" "+hour+":"+minf;
                    holder.sender_time.setText(time);
                    holder.receiver_time.setText(time);
                }
            }

            } catch (ParseException e) {
                e.printStackTrace();
            }




//notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        final EmojiconTextView user_msg, sender_msg;
        TextView sender_time,receiver_time;
        final LinearLayout senderLayout, userLayout;
        final ImageView senderImg, userImg;

        public MyViewHolder(View itemView) {
            super(itemView);


            senderLayout = (LinearLayout) itemView.findViewById(R.id.chat_sender_layout);
            userLayout = (LinearLayout) itemView.findViewById(R.id.chat_user_layout);
            senderImg = (ImageView) itemView.findViewById(R.id.chat_sender_img);
            userImg = (ImageView) itemView.findViewById(R.id.chat_user_img);
            sender_msg = (EmojiconTextView) itemView.findViewById(R.id.chat_sender_text);
            user_msg = (EmojiconTextView) itemView.findViewById(R.id.chat_user_text);
            sender_time = (TextView) itemView.findViewById(R.id.sender_time);
            receiver_time = (TextView) itemView.findViewById(R.id.receiver_time);
        }
    }
}

package com.example.herehelp.chatting;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herehelp.completehelp.CompleteHelp;
import com.example.herehelp.Data;
import com.example.herehelp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChattingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private String flag;
    public ChattingListAdapter(String flag) {
        this.flag = flag;
    }
    // 닉네임, 최근 대화, 사진
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chatting_list_itemview, parent, false);

        return new ChattingListAdapter.chattingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Object[] keys = Data.chatData.keySet().toArray();
        ((ChattingListAdapter.chattingListViewHolder) viewHolder).tv_nickname.setText(Data.idToNickname.get(keys[position].toString()));
        ((ChattingListAdapter.chattingListViewHolder) viewHolder).tv_lastMsg.setText(Data.chatData.get(keys[position]).get(Data.chatData.get(keys[position]).size() - 1).getMsg());
        // 날짜 포맷 변경
        ((ChattingListAdapter.chattingListViewHolder) viewHolder).tv_lastTime.setText(convertDataFormat(keys, position));
        ((chattingListViewHolder) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ChattingList) context).goToChatting(keys[position].toString(), Data.idToNickname.get(keys[position].toString()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return Data.chatData.size();
    }

    public class chattingListViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_nickname;
        TextView tv_lastMsg;
        TextView tv_lastTime;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        chattingListViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            imageView.setClipToOutline(true);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_lastMsg = itemView.findViewById(R.id.tv_lastMsg);
            tv_lastTime = itemView.findViewById(R.id.tv_lastTime);
        }
    }
    // 날짜 포맷 변경
    private String convertDataFormat(Object keys[], int position) {
        try {
            SimpleDateFormat compareFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date chattingDate = compareFormat.parse(Data.chatData.get(keys[position]).get(Data.chatData.get(keys[position]).size() - 1).getTime());
            Date currentData = compareFormat.parse(getCurrentTime());

            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 1. 오늘
            if (chattingDate.equals(currentData) || chattingDate.after(currentData)) {
                // HH:mm
                SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm");
                Date formatDate = originalFormat.parse(Data.chatData.get(keys[position]).get(Data.chatData.get(keys[position]).size() - 1).getTime());

                return newFormat.format(formatDate);
            }
            // 2. 그 외
            else if (chattingDate.before(currentData)) {
                long calDate = chattingDate.getTime() - currentData.getTime();
                long calDateDays = calDate / ( 24*60*60*1000);
                // 어제
                if (Math.abs(calDateDays) == 1) {
                    return "어제";
                }
                // 이틀 전 이상
                // MM-dd
                else if (Math.abs(calDateDays) > 1 && Math.abs(calDateDays) < 365){
                    SimpleDateFormat newFormat = new SimpleDateFormat("MM월 dd일");
                    Date formatDate = originalFormat.parse(Data.chatData.get(keys[position]).get(Data.chatData.get(keys[position]).size() - 1).getTime());

                    return newFormat.format(formatDate);
                }
                // 일년 전 이상
                else {
                    SimpleDateFormat newFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
                    Date formatDate = originalFormat.parse(Data.chatData.get(keys[position]).get(Data.chatData.get(keys[position]).size() - 1).getTime());

                    return newFormat.format(formatDate);
                }
            }

            return null;
        } catch (Exception e) {
            Data.printError(e);
        }
        return null;
    }
    /*
     * 현재 시간 구하기
     */
    private String getCurrentTime() {
        Date date_now = new Date(System.currentTimeMillis());
        SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Format.format(date_now);
    }
}

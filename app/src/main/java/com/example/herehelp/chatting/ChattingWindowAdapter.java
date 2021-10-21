package com.example.herehelp.chatting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herehelp.Data;
import com.example.herehelp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChattingWindowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Chat_Item> myDataList;

    public ChattingWindowAdapter(ArrayList<Chat_Item> dataList) { myDataList = dataList; }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == Data.ViewType.OPPONENT_MESSAGE) {
            view = inflater.inflate(R.layout.opponent_chatting, parent, false);
            return new opponetChattingViewHolder(view);
        }
        else if (viewType == Data.ViewType.MY_MESSAGE) {
            view = inflater.inflate(R.layout.my_chatting, parent, false);
            return new myChattingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof opponetChattingViewHolder) {
            ((opponetChattingViewHolder) viewHolder).opponentChatting.setText(myDataList.get(position).getMsg());
            // 날짜 포맷 변경
            ((opponetChattingViewHolder) viewHolder).opponentTime.setText(converDataFormat(position));
        } else if (viewHolder instanceof myChattingViewHolder) {
            ((myChattingViewHolder) viewHolder).myChatting.setText(myDataList.get(position).getMsg());
            // 날짜 포맷 변경
            ((myChattingViewHolder) viewHolder).myTime.setText(converDataFormat(position));
        }
    }
    // 날짜 포맷 변경
    private String converDataFormat(int position) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm");
            Date formatDate = originalFormat.parse(myDataList.get(position).getTime());
            return newFormat.format(formatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return myDataList.get(position).getViewType();
    }

    public class opponetChattingViewHolder extends RecyclerView.ViewHolder {
        TextView opponentChatting;
        TextView opponentTime;

        opponetChattingViewHolder(View itemView) {
            super(itemView);
            opponentChatting = itemView.findViewById(R.id.opponentChatting);
            opponentTime = itemView.findViewById(R.id.opponentTime);
        }
    }

    public class myChattingViewHolder extends RecyclerView.ViewHolder {
        TextView myChatting;
        TextView myTime;

        myChattingViewHolder(View itemView) {
            super(itemView);
            myChatting = itemView.findViewById(R.id.myChatting);
            myTime = itemView.findViewById(R.id.myTime);
        }
    }

}

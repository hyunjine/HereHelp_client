package com.example.herehelp.chatting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herehelp.Data;
import com.example.herehelp.main.Popup;
import com.example.herehelp.R;

public class ChattingList extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_list);
        Data.chattingListContext = this;
        recyclerView = findViewById(R.id.chattingList);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new ChattingListAdapter("ChattingList"));
        // 뒤로가기 버튼
        ImageButton btn_goToBack = findViewById(R.id.btn_goToBack);
        btn_goToBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    /*
    채팅창으로 전환
     */
    public void goToChatting(String opponent_id, String opponent_nickname) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ChattingList.this, ChattingWindow.class);
                intent.putExtra("opponent_id", opponent_id);
                intent.putExtra("opponent_nickname", opponent_nickname);
                startActivity(intent);
            }
        });
    }
    /*
    채팅 내용 추가
     */
    public void setChattingListContent() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new ChattingListAdapter("ChattingList"));
            }
        });
    }
    /*
    알림(팝업)
     */
    public void pop(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Popup(ChattingList.this, msg);
            }
        });
    }
}

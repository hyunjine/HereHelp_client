package com.example.herehelp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

public class CompleteHelp extends AppCompatActivity {
    TextView tv_nickname;
    private String opponent_nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.complete_help);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new ChattingListAdapter("CompleteHelp"));

        tv_nickname = findViewById(R.id.tv_nickname);
        // 거래 완료 버튼
        Button btn_complete = findViewById(R.id.btn_complete);
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject obj = new JSONObject();
                    if (!tv_nickname.getText().toString().isEmpty()) {
                        obj.put("flag", "completeTransaction");
                        obj.put("opponent_nickname", opponent_nickname);
                        Data.sendToServer(obj);
                        finish();
                    }
                    else
                        pop("상대방을 선택해주세요.");

                } catch (Exception e) {
                    Data.printError(e);
                }
            }
        });
        // 등록만 해제 버튼
        Button btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("flag", "completeHelp");
                    Data.sendToServer(obj);
                    finish();

                } catch (Exception e) {
                    Data.printError(e);
                }
            }
        });
        ImageButton btn_goToBack = findViewById(R.id.btn_goToBack);
        btn_goToBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void setNickname(String nickname) {
        tv_nickname.setText("도움 주신 분: " + nickname);
        opponent_nickname = nickname;
    }
    public void pop(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Popup(CompleteHelp.this, msg);
            }
        });
    }

}

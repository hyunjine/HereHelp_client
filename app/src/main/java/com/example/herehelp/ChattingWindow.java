package com.example.herehelp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ChattingWindow extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String opponent_id;
    private String opponent_nickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_window);
        // context 초기화
        Data.chattingContext = ChattingWindow.this;
        // 상대 아이디, 닉네임
        Intent intent = getIntent();
        opponent_id = intent.getStringExtra("opponent_id");
        opponent_nickname = intent.getStringExtra("opponent_nickname");

        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        // 상단 상대 닉네임
        TextView top_opponent_name = findViewById(R.id.top_opponent_name);
        top_opponent_name.setText(opponent_nickname);

        EditText et_inputMessage = findViewById(R.id.et_inputMessage);
        // 화면 세팅
        // 1. 채팅 내용이 있는 경우
        if (Data.chatData.containsKey(opponent_id))
            setChattingContent();
        // 2. 채팅 내용이 없는 경우
        else {
            ArrayList<Chat_Item> list = new ArrayList<>();
            Data.chatData.put(opponent_id, list);
        }
        // 전송 버튼
        Button btn_sendMessage = findViewById(R.id.btn_sendMessage);
        btn_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = et_inputMessage.getText().toString();
                // 채팅 전송
                sendBtnClick(msg);
                // 텍스트 삭제
                et_inputMessage.setText("");
            }
        });
        // 뒤로가기 버튼
        ImageButton btn_goToBack = findViewById(R.id.btn_goToBack);
        btn_goToBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 텍스트뷰에 글의 유무에 따라 버튼 속성 변경
        et_inputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_inputMessage.getText().toString().isEmpty()) {
                    btn_sendMessage.setEnabled(false);
                    btn_sendMessage.setTextColor(Color.parseColor( "#9E9797"));
                } else {
                    btn_sendMessage.setEnabled(true);
                    btn_sendMessage.setTextColor(Color.WHITE);
                }
            }
        });
        // 텍스트뷰 클릭 시 스크롤 최하단
        SoftKeyboardDectectorView softKeyboardDecector = new SoftKeyboardDectectorView(this);
        addContentView(softKeyboardDecector, new FrameLayout.LayoutParams(-1, -1));

        softKeyboardDecector.setOnShownKeyboard(new SoftKeyboardDectectorView.OnShownKeyboardListener() {
            @Override
            public void onShowSoftKeyboard() {
                //키보드 등장할 때
                recyclerView.scrollToPosition(Data.chatData.get(opponent_id).size() - 1);
            }
        });
        // 영억 터치 시 키보드 사라짐
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_inputMessage.getWindowToken(), 0);
                    Log.d("tag", "hide");
                }
                return false;
            }
        });
    }
    /*
    전송 버튼 클릭
     */
    private void sendBtnClick(String msg) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("flag", "chatting");
            obj.put("opponent_id", opponent_id);
            obj.put("msg", msg);

            Data.sendToServer(obj);
        } catch (Exception e) {
            Data.printError(e);
        }
    }
    /*
    채팅 내용 추가
     */
    public void setChattingContent() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new ChattingWindowAdapter(Data.chatData.get(opponent_id)));
                recyclerView.scrollToPosition(Data.chatData.get(opponent_id).size() - 1);
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
                new Popup(ChattingWindow.this, msg);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Data.chattingListContext != null)
            ((ChattingList) Data.chattingListContext).setChattingListContent();
    }
}
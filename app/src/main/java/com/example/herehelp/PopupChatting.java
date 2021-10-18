package com.example.herehelp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class PopupChatting extends Dialog {
    private Context context;
    private String opponent_id;
    private String opponent_nickname;
    private String msg;
    private String flag;

    public PopupChatting(Context context, String opponent_id, String opponent_nickname, String msg, String flag) {
        super(context);
        this.context = context;
        this.opponent_id = opponent_id;
        this.opponent_nickname = opponent_nickname;
        this.msg = msg;
        this.flag = flag;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pop_up_chatting);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setGravity(Gravity.TOP);

        TextView tv_popUpOpponentNickname = findViewById(R.id.tv_popUpOpponentNickname);
        tv_popUpOpponentNickname.setText(opponent_nickname + "님");
        TextView tv_popUpMessage = findViewById(R.id.tv_popUpContent);
        tv_popUpMessage.setText(msg);
        // 연락 버튼
        View view = findViewById(R.id.view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.equals("Main"))
                    ((Main) context).goToChatting(opponent_id, opponent_nickname);
                else if (flag.equals("ChattingList"))
                    ((ChattingList) Data.chattingListContext).goToChatting(opponent_id, opponent_nickname);
            }
        });
        // 3초 후 사라짐
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 2500);
    }
}

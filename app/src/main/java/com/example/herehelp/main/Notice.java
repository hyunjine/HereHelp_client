package com.example.herehelp.main;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.herehelp.R;

public class Notice {
    public Notice(Context context, String title, String content) {
        Dialog dlg = new Dialog(context);
        // 타이틀바 숨기기
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.notice);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.show();

        TextView tv_noticeTitle = (TextView) dlg.findViewById(R.id.tv_noticeTitle);
        TextView tv_noticeContent = (TextView) dlg.findViewById(R.id.tv_noticeContent);

        tv_noticeTitle.setText("내용 : " + title);
        tv_noticeContent.setText(content);

        // 확인 버튼
        Button okButton = (Button) dlg.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
    }
}
package com.example.herehelp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Popup {
    public Popup(Context context, String popUpMessage) {
        Dialog dlg = new Dialog(context);
        // 타이틀바 숨기기
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.pop_up);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView tv_popUpMessage = dlg.findViewById(R.id.tv_popUpMessage);
        // 텍스트뷰 초기화
        tv_popUpMessage.setText(popUpMessage);

        dlg.show();
    }
}
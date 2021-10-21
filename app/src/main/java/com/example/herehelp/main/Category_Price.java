package com.example.herehelp.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.herehelp.Data;
import com.example.herehelp.R;

import org.json.JSONObject;

import java.text.DecimalFormat;

public class Category_Price extends Dialog{
    private String beforeStr;

    public Category_Price(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.category_price);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 최소 금액
        EditText et_minimum = findViewById(R.id.et_minimum);
        et_minimum.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(beforeStr)) {
                    beforeStr = new DecimalFormat("###,###").format(Double.parseDouble(s.toString().replaceAll(",", "")));
                    et_minimum.setText(beforeStr);
                    Editable editable = et_minimum.getText();
                    Selection.setSelection(editable, beforeStr.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // 최대 금액
        EditText et_maximum = findViewById(R.id.et_maximum);
        et_maximum.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(beforeStr)) {
                    beforeStr = new DecimalFormat("###,###").format(Double.parseDouble(s.toString().replaceAll(",", "")));
                    et_maximum.setText(beforeStr);
                    Editable editable = et_maximum.getText();
                    Selection.setSelection(editable, beforeStr.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                try {
                    String minimum = et_minimum.getText().toString().replace(",", "");
                    String maximum = et_maximum.getText().toString().replace(",", "");
                    if (minimum.isEmpty() || maximum.isEmpty())
                        ((Main) Data.mainContext).pop("가격(값)을 입력하세요.");
                    else if (Integer.parseInt(minimum) > Integer.parseInt(maximum))
                        ((Main) Data.mainContext).pop("범위가 잘못 되었습니다.");
                    else {
                        JSONObject obj = new JSONObject();

                        obj.put("flag", "selectPrice");
                        obj.put("minimum", minimum);
                        obj.put("maximum", maximum);

                        Data.sendToServer(obj);
                    }
                } catch (Exception e){
                    Data.printError(e);
                }
            }
        });
    }
}

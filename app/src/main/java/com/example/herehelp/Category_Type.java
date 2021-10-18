package com.example.herehelp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

public class Category_Type extends Dialog{
    public Category_Type(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.category_type);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        CheckBox cb_etc = findViewById(R.id.cb_etc);
        CheckBox cb_sellProduct = findViewById(R.id.cb_sellProduct);
        CheckBox cb_shareKnowledge = findViewById(R.id.cb_shareKnowledge);
        CheckBox cb_errand = findViewById(R.id.cb_errand);
        CheckBox cb_job = findViewById(R.id.cb_job);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                try{
                    JSONObject obj = new JSONObject();
                    JSONArray arr = new JSONArray();
                    // 모든 항목이 체크되어 있을 시
                    if (cb_etc.isChecked())
                        arr.put(0);
                    if (cb_sellProduct.isChecked())
                        arr.put(1);
                    if (cb_shareKnowledge.isChecked())
                        arr.put(2);
                    if (cb_errand.isChecked())
                        arr.put(3);
                    if (cb_job.isChecked())
                        arr.put(4);
                    obj.put("flag", "selectType");
                    obj.put("arr", arr);

                    Data.sendToServer(obj);

                } catch (Exception e) {
                    Data.printError(e);
                }
            }
        });
    }
}

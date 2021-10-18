package com.example.herehelp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONObject;

import java.text.DecimalFormat;

public class HelpRequest {
    private Context context;
    private String beforeStr;
    private int category;
    public HelpRequest(Context context) {
        this.context = context;
    }

    public void show() {
        Dialog dlg = new Dialog(context);
        // 타이틀바 숨기기
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.help_request);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.show();

        EditText et_content = dlg.findViewById(R.id.et_content);
        EditText et_price = dlg.findViewById(R.id.et_price);
        et_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(beforeStr)) {
                    beforeStr = new DecimalFormat("###,###").format(Double.parseDouble(s.toString().replaceAll(",", "")));
                    et_price.setText(beforeStr);
                    Editable editable = et_price.getText();
                    Selection.setSelection(editable, beforeStr.length());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        // 선택 박스
        Spinner sp_selectCategory = dlg.findViewById(R.id.sp_selectCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, Data.items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_selectCategory.setAdapter(adapter);
        sp_selectCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 등록 버튼
        Button okButton = dlg.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String content = et_content.getText().toString();
                    String price = et_price.getText().toString().trim();

                    double latitude = new GpsTracker(context).getLatitude();
                    double longitude = new GpsTracker(context).getLongitude();
                    String loc = latitude + "#" + longitude;

                    JSONObject data = new JSONObject();

                    data.put("flag", "helpRequest");
                    data.put("content", content);
                    data.put("price", price.replace(",", ""));
                    data.put("location", loc);
                    data.put("category", category);

                    Data.sendToServer(data);

                    dlg.dismiss();
                } catch (Exception e) {
                    Data.printError(e);
                }
            }
        });
        // 취소 버튼
        Button btn_cancelButton = dlg.findViewById(R.id.btn_cancelButton);
        btn_cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }
}
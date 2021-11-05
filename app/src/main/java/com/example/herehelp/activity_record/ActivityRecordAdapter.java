package com.example.herehelp.activity_record;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herehelp.Data;
import com.example.herehelp.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Record_Item> arrayList;
    private String sign;

    public ActivityRecordAdapter(ArrayList<Record_Item> arr, String sign) {
        this.arrayList = arr;
        this.sign = sign;
    }

    // 닉네임, 최근 대화, 사진
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_record_item, parent, false);

        return new ActivityRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((ActivityRecordViewHolder) viewHolder).tv_opponent.setText(arrayList.get(position).getNickname());
        ((ActivityRecordViewHolder) viewHolder).tv_category.setText("#" + Data.items[arrayList.get(position).getCategory()]);
        ((ActivityRecordViewHolder) viewHolder).tv_price.setText(sign + new DecimalFormat("###,###").format(Integer.parseInt(arrayList.get(position).getPrice())) + "원");
        ((ActivityRecordViewHolder) viewHolder).tv_time.setText(convertDataFormat(arrayList.get(position).getTime()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ActivityRecordViewHolder extends RecyclerView.ViewHolder {
        TextView tv_opponent;
        TextView tv_category;
        TextView tv_price;
        TextView tv_time;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        ActivityRecordViewHolder(View itemView) {
            super(itemView);
            tv_opponent = itemView.findViewById(R.id.tv_opponent);
            tv_category = itemView.findViewById(R.id.tv_category);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }

    private String convertDataFormat(String time) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy년MM월dd일 HH시 mm분");

            Date formatDate = originalFormat.parse(time);

            return newFormat.format(formatDate);

        } catch (Exception e) {
            Data.printError(e);
        }
        return null;
    }
}

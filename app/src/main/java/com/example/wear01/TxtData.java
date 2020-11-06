package com.example.wear01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class TxtData extends WearableActivity {
    private DrawerLayout drawerLayout;
    private TextView tv_txt;
    private ImageButton img_btn_add,img_btn_revom;
    private Button btn_red,btn_black;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt_data);
        drawerLayout = findViewById(R.id.draw);
        tv_txt = findViewById(R.id.tv_txt);
        img_btn_add = findViewById(R.id.add);
        img_btn_revom = findViewById(R.id.revom);
        btn_black = findViewById(R.id.black);
        btn_red = findViewById(R.id.red);
        drawerLayout.setScrimColor(0x00000000);

        Intent intent = getIntent();
        String obj = intent.getStringExtra("txt");
        tv_txt.setText(obj);



        img_btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float count = tv_txt.getTextSize();
                count += 1;
                tv_txt.setTextSize(TypedValue.COMPLEX_UNIT_PX,count);
            }
        });

        img_btn_revom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float count = tv_txt.getTextSize();
                count -= 1;
                tv_txt.setTextSize(TypedValue.COMPLEX_UNIT_PX,count);
            }
        });

        btn_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_txt.setTextColor(Color.RED);
            }
        });

        btn_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_txt.setTextColor(Color.BLACK);
            }
        });
    }
}
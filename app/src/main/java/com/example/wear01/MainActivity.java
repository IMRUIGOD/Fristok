package com.example.wear01;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends WearableActivity {

    private ViewPager vp;
    private List<View> list1 = new ArrayList<View>();
    private  List list2;
    private MyVP adapter;
    private EditText ed;
    private ListView lv;
    private View view1,view2;
    private SimpleAdapter simpleAdapter;
    private SQLiteDatabase db;
    private ContentValues values;
    private MyHelper myHelper;
    private String title,txt;
    private Map map;
    private View include;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myHelper = new MyHelper(this);

        view1 = View.inflate(this,R.layout.f1,null);
        view2 = View.inflate(this,R.layout.activity_new,null);
        include = view1.findViewById(R.id.include);

        setListView();
        setListViewListener();
        //设置ViewPager
        setVPAdapter();




        // Enables Always-on
        setAmbientEnabled();
    }

    private void setListViewListener() {

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TxtData.class);
                Map map = new HashMap();
                map = (Map) list2.get(position);
                intent.putExtra("txt",map.get("txt").toString());
                startActivity(intent);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {



                include.setVisibility(View.VISIBLE);


                view1.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map map = new HashMap();
                        map = (Map) list2.get(position);

                        db = myHelper.getWritableDatabase();
                        Log.i("aaa",map.get("title")+"");
                        db.delete("dataTxt","title = ?",new String[]{map.get("title")+""});
                        db.close();
                        setListView();
                        include.setVisibility(View.GONE);
                    }
                });
                view1.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        include.setVisibility(View.GONE);
                    }
                });
                return true;
            }
        });
    }

    private void setListView() {
        lv = view1.findViewById(R.id.lv);

        db = myHelper.getReadableDatabase();
        Cursor cursor = db.query("dataTxt",null,null,null,null,null,null);
        list2 = new ArrayList();
        if (cursor.getCount()==0){
            map = new HashMap<>();
            map.put("title","没有记录！");
            list2.add(map);
        }else{
            cursor.moveToFirst();
            map = new HashMap<>();
            Log.i("data",cursor.getString(1));
            map.put("title",cursor.getString(1));
            map.put("txt",cursor.getString(2));
            list2.add(map);
            while (cursor.moveToNext()){
                map = new HashMap<>();
                map.put("title",cursor.getString(1));
                map.put("txt",cursor.getString(2));
                list2.add(map);
            }
        }

        simpleAdapter = new SimpleAdapter(
                this,
                list2,
                R.layout.list_item,
                new String[]{"title"},
                new int[]{R.id.item_txt});

        lv.setAdapter(simpleAdapter);
        //关闭
        cursor.close();
        db.close();

    }

    private void setVPAdapter() {
        vp = findViewById(R.id.vp);
        ed=(EditText)view2.findViewById(R.id.et_txt);

        list1.add(view1);
        list1.add(view2);

        adapter = new MyVP(list1);

        vp.setAdapter(adapter);


      vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
          @Override
          public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

          }

          @Override
          public void onPageSelected(int position) {
              setListView();
          }

          @Override
          public void onPageScrollStateChanged(int state) {
                if (state == 0){

                }else if (state == 1){
                   String obj = ed.getText().toString();
                   if (obj == null || obj.isEmpty()){

                   }else {
                       values = new ContentValues();
                       if (obj.length()<14){
                           title = obj;
                       }else {
                           title = obj.substring(0,14);
                       }
                       values.put("title",title);
                       values.put("txt",obj);
                       db = myHelper.getWritableDatabase();
                       db.insert("dataTxt",null,values);
                       Log.i("add","添加成功");
                       db.close();
                       ed.setText("");


                   }
                }else if (state == 2){

                }
          }
      });
    }


}

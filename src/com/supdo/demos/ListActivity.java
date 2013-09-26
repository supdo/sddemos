package com.supdo.demos;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_1);
		ListView lvList = (ListView) findViewById(R.id.lv_list1);
		ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 10; i++) {
			HashMap<String, String> map = new HashMap<String , String>();
			map.put("name", "name-"+String.valueOf(i));
			map.put("age", String.valueOf(i+20));
			listData.add(map);
		}
		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listData,  // 数据源
				R.layout.list_info,  // ListItem的XML实现
				new String[] { "name", "age" },  // 动态数组与ImageItem对应的子项
				new int[] { R.id.tv_list_name, R.id.tv_list_age }  // list_items中对应的的ImageView和TextView
		);
		
		// 绑定数据源
		lvList.setAdapter(listItemAdapter);
		
	}

}

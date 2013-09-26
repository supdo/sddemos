package com.supdo.demos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private ViewPager mViewPager;
	private PagerTitleStrip mPagerTitle;
	private ArrayList<View> views;
	private ArrayList<String> titles;
	private AlertDialog.Builder dlgBder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		dlgBder = new AlertDialog.Builder(this);
		

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mPagerTitle = (PagerTitleStrip)findViewById(R.id.pager_title);

		//将要分页显示的View装入数组中
        LayoutInflater mLists = LayoutInflater.from(this);
        View mListView1 = mLists.inflate(R.layout.activity_list_1, null);
        View mDummyView1 = mLists.inflate(R.layout.fragment_main_dummy, null);
        
        //每个页面的Title数据
        views = new ArrayList<View>();
        views.add(mListView1);
        views.add(mDummyView1);
        
        titles = new ArrayList<String>();
        titles.add("列表内容");
        titles.add("Dummy内容");
        
      //填充ViewPager的数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return titles.get(position);
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				switch (position) {
				case 0:
					initalList();
					break;
				}
				return views.get(position);
			}
		};
		
        
        mViewPager.setAdapter(mPagerAdapter);
	}
	
	private void initalList(){
		final ListView lvList = (ListView) findViewById(R.id.lv_list1);
		ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> mapTitle = new HashMap<String , String>();
		mapTitle.put("name", "姓名");
		mapTitle.put("age", "年龄");
		listData.add(mapTitle);
		for (int i = 0; i < 20; i++) {
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
		
		lvList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				// TODO Auto-generated method stub
				view.setBackgroundColor(Color.BLUE);
				TextView tvName = (TextView)view.findViewById(R.id.tv_list_name);
				String strName = tvName.getText().toString();
				dlgBder.setTitle("提示")
						.setMessage("你点击了："+strName)
						.setNegativeButton("确定", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								view.setBackgroundColor(Color.WHITE);
							}
						})
						.show();
			}
		});
		
		lvList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
				// TODO Auto-generated method stub
				view.setBackgroundColor(Color.BLUE);
				TextView tvName = (TextView)view.findViewById(R.id.tv_list_name);
				String strName = tvName.getText().toString();
				dlgBder.setTitle("提示")
				.setMessage("你确定要删除："+strName+"吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//view.remo
						//mViewPager.getAdapter().getItemPosition(object)
						mViewPager.getAdapter().notifyDataSetChanged();
						lvList.invalidate();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						view.setBackgroundColor(Color.WHITE);
					}
				})
				.show();
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
	        case R.id.menu_logout:
	        	Logout();
	            break;
	        default:
	            break;
	    }
	    return true;
	};
	
	private void Logout(){
		AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this);
    	dlgBuilder.setMessage("确认退出吗？");
    	dlgBuilder.setTitle("提示");
    	dlgBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				SharedPreferences userInfo = getSharedPreferences("user_info", 0);
				SharedPreferences.Editor userInfoEditor = userInfo.edit();
				userInfoEditor.putString("email", "");
				userInfoEditor.commit();
				
				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(intent);
				
				MainActivity.this.finish();
			}
		});
    	
    	dlgBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
    	dlgBuilder.show();
	}
}

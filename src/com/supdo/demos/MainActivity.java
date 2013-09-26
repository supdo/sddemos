package com.supdo.demos;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private ViewPager mViewPager;
	private PagerTitleStrip mPagerTitle;
	private ArrayList<View> views;
	private ArrayList<String> titles;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		

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
				return views.get(position);
			}
		};
		
        
        mViewPager.setAdapter(mPagerAdapter);
	}
	
	public class mPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
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
			return views.get(position);
		}
	
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

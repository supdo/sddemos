package com.supdo.demos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.supdo.demos.TestMainActivity.DummySectionFragment;
import com.supdo.demos.TestMainActivity.SectionsPagerAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	
	private PagerAdapter mPagerAdapter;
	private SimpleAdapter listItemAdapter;
	private ArrayList<HashMap<String, String>> listData;
	private ListView lvList;
	private ViewPager mViewPager;
	private PagerTitleStrip mPagerTitle;
	private ArrayList<View> views;
	private ArrayList<String> titles;
	//private AlertDialog.Builder dlgBder;
	
	SectionsPagerAdapter mSectionsPagerAdapter;
	
	private Button btnNewUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		//dlgBder = new AlertDialog.Builder(this);

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
        mPagerAdapter = new PagerAdapter() {
			
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
					//initalList(views.get(position));
					ListActivity listPaer = new ListActivity();
					return listPaer.getLayoutInflater();
				}
				return views.get(position);
			}
		};
		
        
        mViewPager.setAdapter(mPagerAdapter);
	}
	
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
	
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_test_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}
	
	private void initalList(View view){
		
		btnNewUser = (Button)view.findViewById(R.id.btnNewUser);
		btnNewUser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, NewUserActivity.class);
				startActivity(intent);
			}
		});
		
		lvList = (ListView) findViewById(R.id.lv_list1);
		listData = new ArrayList<HashMap<String, String>>();
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
		listItemAdapter = new SimpleAdapter(this, listData,  // 数据源
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
				AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(MainActivity.this); 
						dlgBuilder.setTitle("提示")
						.setMessage("你点击了："+strName)
						.setNegativeButton("取消", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								view.setBackgroundColor(0xEEEEEE);
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
				AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(MainActivity.this); 
				dlgBuilder.setTitle("提示")
				.setMessage("你确定要删除："+strName+"吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						listData.remove(position);
						//lvList.removeView(view);
						listItemAdapter.notifyDataSetChanged();
						//lvList.invalidate();
						view.setBackgroundColor(0xEEEEEE);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						//view.setBackgroundColor(Color.WHITE);
						view.setBackgroundColor(0xEEEEEE);
					}
				})
				.show();
				return true;
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

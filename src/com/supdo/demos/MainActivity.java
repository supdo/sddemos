package com.supdo.demos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.supdo.demos.orm.DataHelper;
import com.supdo.demos.orm.Users;

//@SuppressLint("ValidFragment")
public class MainActivity extends FragmentActivity  {
	
	//界面元素
	private ViewPager mViewPager;
	private PagerTitleStrip mPagerTitle;
	private View mListView1;
	private View mDummyView1;
	
	private LayoutInflater mLists;
	
	
	private SimpleAdapter listItemAdapter;
	private ArrayList<HashMap<String, Object>> listData;
	private ListView lvList;
	
	private ArrayList<View> views;
	private ArrayList<String> titles;
	private DataHelper dataHelper;
	private Dao<Users, Integer> usersDao;
	
	private Button btnNewUser;
	
	private UserInfoDialog userinfoDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		findViews();
		initialActivity();
	}
	
	private void findViews(){
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mPagerTitle = (PagerTitleStrip)findViewById(R.id.pager_title);
	}
	
	private void initialActivity(){
		dataHelper = new DataHelper(MainActivity.this);

		//将要分页显示的View装入数组中
        mLists = LayoutInflater.from(this);
        mListView1 = mLists.inflate(R.layout.activity_list_1, null);
        mDummyView1 = mLists.inflate(R.layout.fragment_main_dummy, null);
        //每个页面的Title数据
        views = new ArrayList<View>();
        views.add(mListView1);
        views.add(mDummyView1);
        
        titles = new ArrayList<String>();
        titles.add("列表内容");
        titles.add("Dummy内容");
        
        mViewPager.setAdapter(mPagerAdapter);
	}
	
	private PagerAdapter mPagerAdapter = new PagerAdapter() {
		
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
				initalList(views.get(position));
			}
			return views.get(position);
		}
	};
	
	private void initalList(View view){
		btnNewUser = (Button)view.findViewById(R.id.btnNewUser);
		btnNewUser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, NewUserActivity.class);
				startActivityForResult(intent, 101);
			}
		});
		
		lvList = (ListView) findViewById(R.id.lv_list1);
		listData = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> mapTitle = new HashMap<String , Object>();
		mapTitle.put("name", "姓名");
		mapTitle.put("age", "年龄");
		mapTitle.put("phone", "电话");
		listData.add(mapTitle);
		initalListData();
		listItemAdapter = new SimpleAdapter(this, listData,  // 数据源
				R.layout.list_info,  // ListItem的XML实现
				new String[] {"id", "name", "age", "phone" },  // 动态数组与ImageItem对应的子项
				new int[] {R.id.tv_list_userid ,R.id.tv_list_name, R.id.tv_list_age, R.id.tv_list_phoneNum }  // list_items中对应的的ImageView和TextView
		);
		
		// 绑定数据源
		lvList.setAdapter(listItemAdapter);
		
		lvList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView tvUserID = (TextView) view
						.findViewById(R.id.tv_list_userid);
				final int userid = Integer.parseInt(tvUserID.getText().toString());
				view.setBackgroundColor(Color.BLUE);
				/*TextView tvName = (TextView) view
						.findViewById(R.id.tv_list_name);
				String strName = tvName.getText().toString();
				AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(
						MainActivity.this);
				dlgBuilder
						.setTitle("提示")
						.setMessage("你点击了：" + strName)
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub
										view.setBackgroundColor(0xEEEEEE);
									}
								}).show();*/
				
				showUserInfoDialog(userid);
				view.setBackgroundColor(0xEEEEEE);
			}
		});
		
		lvList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent,
					final View view, final int position, long id) {
				// TODO Auto-generated method stub
				view.setBackgroundColor(Color.BLUE);
				TextView tvName = (TextView) view
						.findViewById(R.id.tv_list_name);
				TextView tvUserID = (TextView) view
						.findViewById(R.id.tv_list_userid);
				final int userid = Integer.parseInt(tvUserID.getText().toString());
				String strName = tvName.getText().toString();
				AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(
						MainActivity.this);
				dlgBuilder
						.setTitle("提示")
						.setMessage("你可以对：" + strName + "用户做以下操作:")
						.setPositiveButton("删除",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										listData.remove(position);
										try {
											usersDao = dataHelper.getUserDao();
											usersDao.deleteById(userid);
										} catch (SQLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										listItemAdapter.notifyDataSetChanged();
										view.setBackgroundColor(0xEEEEEE);
									}
								})
						.setNeutralButton("编辑", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(MainActivity.this, NewUserActivity.class);
								Bundle bundle=new Bundle();
								bundle.putInt("userid", userid);
								intent.putExtras(bundle);
								startActivityForResult(intent, 105);
								view.setBackgroundColor(0xEEEEEE);
							}
						})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub
										view.setBackgroundColor(0xEEEEEE);
									}
								}).show();
				return true;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == 101 || requestCode == 105){
			if(resultCode == RESULT_OK){
				initalListData();
				listItemAdapter.notifyDataSetChanged();
				
				AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this);
		    	dlgBuilder.setMessage("添加成功！");
		    	dlgBuilder.setTitle("提示");
		    	dlgBuilder.setNegativeButton("确定", null);
		    	dlgBuilder.show();
			}
		}
	}
	
	private void initalListData() {
		try {
			listData.clear();
			usersDao = dataHelper.getUserDao();
			List<Users> users = usersDao.queryForAll();
			for (Users user : users) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", user.getId());
				map.put("name", user.getName());
				map.put("age", user.getAge() + "");
				map.put("phone", user.getPhoneNum());
				listData.add(map);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	protected void showUserInfoDialog(int id) {
		if(userinfoDialog == null){
			userinfoDialog = new UserInfoDialog();
		}
		Bundle args = new Bundle();
		args.putInt("userid", id);
		userinfoDialog.setArguments(args);
		userinfoDialog.show(getSupportFragmentManager(), "dialog");
	}
	
	@SuppressLint("ValidFragment")
	public class UserInfoDialog extends DialogFragment {

		private View view;
		private TextView tvUserName;
		private TextView tvAge;
		private TextView tvSex;
		private TextView tvComp;
		private TextView tvPhoneNum;
		private Button btnSMS;
		private Button btnPhone;
		private int userid;

		private void initialData(){
			view = getActivity().getLayoutInflater().inflate(R.layout.userinfo_show, null);
			tvUserName = (TextView)view.findViewById(R.id.tvUserName);
			tvAge = (TextView)view.findViewById(R.id.tvAge);
			tvSex = (TextView)view.findViewById(R.id.tvSex);
			tvComp = (TextView)view.findViewById(R.id.tvComp);
			tvPhoneNum = (TextView)view.findViewById(R.id.tvPhone);
			btnSMS = (Button)view.findViewById(R.id.btnSMS);
			btnPhone = (Button)view.findViewById(R.id.btnPhone);
			btnSMS.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+tvPhoneNum.getText().toString()));
					intent.putExtra("sms_body", "102");
					startActivity(intent); 
				}
			});
			btnPhone.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+tvPhoneNum.getText().toString()));
					startActivity(intent);
				}
			});
			
			userid = getArguments().getInt("userid");
			try {
				usersDao = dataHelper.getUserDao();
				Users user = usersDao.queryForId(userid);
				tvUserName.setText(user.getName());
				tvAge.setText(String.valueOf(user.getAge()));
				String strSex = this.getResources().getStringArray(R.array.user_sex)[user.getSex()];
				tvSex.setText(strSex);
				tvComp.setText(user.getCompany());
				tvPhoneNum.setText(user.getPhoneNum());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			initialData();
			Builder retDlg = new AlertDialog.Builder(getActivity());		    
			retDlg.setView(view);
			retDlg.setTitle("用户信息")
			//.setPositiveButton("OK", null)
			//.setNegativeButton("取消", null)
			;
			
			return retDlg.create();
		}
	}
}

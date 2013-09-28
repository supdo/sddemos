package com.supdo.demos;

import java.sql.SQLException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.supdo.demos.orm.DataHelper;
import com.supdo.demos.orm.Users;

public class NewUserActivity extends Activity {

	private Button btnNewUserOK;
	private TextView etNewUserName;
	private TextView etNewUserAge;
	private TextView etNewUserComp;
	private Spinner spnNewUserSex;
	private TextView etNewUserPhoneNum;
	private DataHelper dataHelper;
	private Dao<Users, Integer> usersDao;
	
	private int userid = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_user_layout);

		dataHelper = new DataHelper(NewUserActivity.this);
		
		etNewUserName = (TextView) findViewById(R.id.etNewUserName);
		etNewUserAge = (TextView) findViewById(R.id.etNewUserAge);
		etNewUserComp = (TextView) findViewById(R.id.etNewUserComp);
		etNewUserPhoneNum = (TextView) findViewById(R.id.etNewUserPhoneNum);
		spnNewUserSex = (Spinner) findViewById(R.id.spnNewUserSex);
		
		Bundle bundle=getIntent().getExtras();
		if(bundle != null){
			userid = bundle.getInt("userid", -1);
		}
		Users user = new Users();
		if(userid>-1){
			try {
				usersDao = dataHelper.getUserDao();
				user = usersDao.queryForId(userid);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			etNewUserName.setText(user.getName());
			etNewUserAge.setText(String.valueOf(user.getAge()));
			etNewUserComp.setText(user.getCompany());
			etNewUserPhoneNum.setText(user.getPhoneNum());
		}

		btnNewUserOK = (Button) findViewById(R.id.btnNewUserOK);
		btnNewUserOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					usersDao = dataHelper.getUserDao();
					Users user = new Users();
					if(userid>-1){
						user.setId(userid);
					}
					user.setName(etNewUserName.getText().toString());
					user.setAge(Integer.parseInt(etNewUserAge.getText()
							.toString()));
					user.setCompany(etNewUserComp.getText().toString());
					user.setPhoneNum(etNewUserPhoneNum.getText().toString());
					usersDao.createOrUpdate(user);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent intent = new Intent(NewUserActivity.this,
						MainActivity.class);
				// startActivity(intent);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
}

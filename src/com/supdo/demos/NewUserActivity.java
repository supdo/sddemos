package com.supdo.demos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.supdo.demos.orm.DataHelper;
import com.supdo.demos.orm.Users;

public class NewUserActivity extends Activity {

	//界面元素
	private Button btnNewUserOK;
	private TextView tvNewUserTitle;
	private EditText etNewUserName;
	private EditText etNewUserAge;
	private EditText etNewUserComp;
	private Spinner spnNewUserSex;
	private EditText etNewUserPhoneNum;
	
	private DataHelper dataHelper;
	private Dao<Users, Integer> usersDao;

	private List<View> focusViews = null;
	
	private int userid = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_user_layout);
		
		findViews();
		initialActivity();
		setListeners();
	}
	
	private void findViews(){
		tvNewUserTitle = (TextView) findViewById(R.id.tvNewUserTitle);
		etNewUserName = (EditText) findViewById(R.id.etNewUserName);
		etNewUserAge = (EditText) findViewById(R.id.etNewUserAge);
		etNewUserComp = (EditText) findViewById(R.id.etNewUserComp);
		etNewUserPhoneNum = (EditText) findViewById(R.id.etNewUserPhoneNum);
		spnNewUserSex = (Spinner) findViewById(R.id.spnNewUserSex);
		btnNewUserOK = (Button) findViewById(R.id.btnNewUserOK);
	}
	
	private void initialActivity(){
		dataHelper = new DataHelper(NewUserActivity.this);
		
		Bundle bundle=getIntent().getExtras();
		if(bundle != null){
			userid = bundle.getInt("userid", -1);
		}
		Users user = new Users();
		if(userid>-1){
			tvNewUserTitle.setText("编辑用户资料");
			try {
				usersDao = dataHelper.getUserDao();
				user = usersDao.queryForId(userid);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			etNewUserName.setText(user.getName());
			etNewUserAge.setText(String.valueOf(user.getAge()));
			etNewUserComp.setText(user.getCompany());
			etNewUserPhoneNum.setText(user.getPhoneNum());
			spnNewUserSex.setSelection(user.getSex());
		}
	}
	
	private void setListeners(){
		btnNewUserOK.setOnClickListener(btnOKListenter);
	}
	
	private Button.OnClickListener btnOKListenter = new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			if(validView()){
				try {
					usersDao = dataHelper.getUserDao();
					Users user = new Users();
					if(userid>-1){
						user.setId(userid);
					}
					user.setName(etNewUserName.getText().toString());
					user.setAge(Integer.parseInt(etNewUserAge.getText()
							.toString()));
					user.setSex((int)spnNewUserSex.getSelectedItemId());
					user.setCompany(etNewUserComp.getText().toString());
					user.setPhoneNum(etNewUserPhoneNum.getText().toString());
					usersDao.createOrUpdate(user);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				Toast toast = Toast.makeText(getApplicationContext(),"添加成功", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
					   
				Intent intent = new Intent(NewUserActivity.this,
						MainActivity.class);
				// startActivity(intent);
				setResult(RESULT_OK, intent);
				finish();
			}
		}
	};
	
	private boolean validView(){
		boolean cancel = false;
		focusViews = new ArrayList<View>();
		
		if (TextUtils.isEmpty(etNewUserName.getText().toString())) {
			etNewUserName.setError("用户名不能为空");
			focusViews.add(etNewUserName);
			cancel = true;
		} 
		if (TextUtils.isEmpty(etNewUserAge.getText().toString())) {
			etNewUserAge.setError("年龄不能为空");
			focusViews.add(etNewUserAge);
			cancel = true;
		} else{
			try{
				int age = Integer.parseInt(etNewUserAge.getText().toString());
			} catch (Exception e) {
				e.printStackTrace();
				etNewUserAge.setError("年龄必须是数字");
				focusViews.add(etNewUserAge);
				cancel = true;
			}
		}
		
		/**if(spnNewUserSex.getSelectedItemId() == 0){
			//spnNewUserSex.setBackgroundColor(0xFF0000);
			focusViews.add(spnNewUserSex);
			cancel = true;
		}**/
		
		if (TextUtils.isEmpty(etNewUserComp.getText().toString())) {
			etNewUserComp.setError("公司不能为空");
			focusViews.add(etNewUserComp);
			cancel = true;
		} 
		if (TextUtils.isEmpty(etNewUserPhoneNum.getText().toString())) {
			etNewUserPhoneNum.setError("电话不能为空");
			focusViews.add(etNewUserPhoneNum);
			cancel = true;
		}

		if(cancel){
			focusViews.get(0).requestFocus();
			return false;
		}else{
			return true;
		}
	}
}

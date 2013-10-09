package com.supdo.demos;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaACL;
import com.baidu.frontia.FrontiaAccount;
import com.baidu.frontia.FrontiaData;
import com.baidu.frontia.FrontiaQuery;
import com.baidu.frontia.FrontiaRole;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener;
import com.baidu.frontia.api.FrontiaStorageListener.DataInfoListener;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.OnNavigationListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class FrontiaActivity extends Activity {
	private LinearLayout roleLayout;
	private LinearLayout appdataLayout;
	private TextView etUserName;
	private TextView etPassWord;
	private TextView etPhone;
	private Button btnFind;
	private Button btnSave;
	private Button btnFindUser;
	private Button btnSaveUser;
	private TextView tvAppdataInfo;
	private Button btnCrtRole;
	private Button btnCrtUser;
	private TextView tvRoleInfo;
	
	final String APIKEY = "LMG2xVcHCpZ4qXe3QZADOAIa";
	
	private FrontiaStorage mCloudStorage;
    private FrontiaData dataCanRead;
    private FrontiaData dataCanWrite;
    private FrontiaData dataNotRead;
    private FrontiaData dataNotWrite;
    
    private FrontiaRole mRole;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frontia_layout);
		findViews();
		initialActivity();
		setListeners();
	}
	
	private void findViews() {
		roleLayout = (LinearLayout)findViewById(R.id.roleLayout);
		appdataLayout = (LinearLayout)findViewById(R.id.appdataLayout);
		etUserName = (TextView)findViewById(R.id.etUserName);
		etPassWord = (TextView)findViewById(R.id.etPassWord);
		etPhone = (TextView)findViewById(R.id.etPhone);
		btnFind = (Button)findViewById(R.id.btnFind);
		btnSave = (Button)findViewById(R.id.btnSave);
		btnFindUser = (Button)findViewById(R.id.btnFindUser);
		btnSaveUser = (Button)findViewById(R.id.btnSaveUser);
		tvAppdataInfo = (TextView)findViewById(R.id.tvInfo);
		btnCrtRole = (Button)findViewById(R.id.btnCrtRole);
		btnCrtUser = (Button)findViewById(R.id.btnCrtUser);
		tvRoleInfo = (TextView)findViewById(R.id.tvRoleInfo);
	}

	private void initialActivity() {
		mCloudStorage = Frontia.getStorage();
	
		final String[] actions = new String[] { "应用数据", "个人数据", "个人文件", "角色管理" };  
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP|ActionBar.DISPLAY_SHOW_HOME);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
				android.R.layout.simple_spinner_dropdown_item, actions);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(adapter, new OnNavigationListener() {
			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				Toast.makeText(getBaseContext(),"You selected : " + actions[itemPosition],Toast.LENGTH_SHORT)
				.show();
				switch(itemPosition) {
					case 0:
						appdataLayout.setVisibility(View.VISIBLE);
						roleLayout.setVisibility(View.GONE);
						break;
		        	case 3:
		        		appdataLayout.setVisibility(View.GONE);
						roleLayout.setVisibility(View.VISIBLE);
			            break;
		        	default:
		        		break;
		    }
				return false;
			}
		});
	}

	private void setListeners() {
		btnSaveUser.setOnClickListener(btnSaveUserListenter);
		btnFindUser.setOnClickListener(btnFindUserListenter);
		btnSave.setOnClickListener(btnSaveListenter);
		btnFind.setOnClickListener(btnFindListenter);
		btnCrtRole.setOnClickListener(btnCrtRoleListenter);
		btnCrtUser.setOnClickListener(btnCrtUserListenter);
	}
	
	private Button.OnClickListener btnSaveUserListenter = new Button.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Toast.makeText(getBaseContext(),"创建用户",Toast.LENGTH_SHORT).show();
			FrontiaAccount user = Frontia.getCurrentAccount();
			
			FrontiaACL rwAcl = new FrontiaACL();
	        rwAcl.setAccountReadable(user, true);
	        rwAcl.setAccountWritable(user, true);
	        
	        JSONObject data = new JSONObject();
	        try {
	            data.put("username", etUserName.getText().toString());
	            data.put("password", etPassWord.getText().toString());
	            data.put("phone", etPhone.getText().toString());
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }	        
	        final FrontiaData fData = new FrontiaData(data);
	        fData.setACL(rwAcl);
	        
	        mCloudStorage.insertData(fData, new FrontiaStorageListener.DataInsertListener(){
	        	@Override
				public void onSuccess() {
	        		String newStr = "save "+etUserName.getText().toString()+" data success!\n";
                    tvAppdataInfo.setText(tvAppdataInfo.getText().toString()+newStr);
				}
				@Override
				public void onFailure(int arg0, String arg1) {
					String newStr = "save "+etUserName.getText().toString()+" data failure!\n";
                    tvAppdataInfo.setText(tvAppdataInfo.getText().toString()+newStr);
				}
	        });
		}
	};
	
	private Button.OnClickListener btnFindUserListenter = new Button.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Toast.makeText(getBaseContext(),"查询用户",Toast.LENGTH_SHORT).show();
			FrontiaQuery query = new FrontiaQuery();
			query = query.equals("username", etUserName.getText().toString());
			mCloudStorage.findData(query,
				new DataInfoListener() {
					@Override
					public void onSuccess(List<FrontiaData> dataList) {
						tvAppdataInfo.setText(tvAppdataInfo.getText().toString() + "查询到了" + dataList.size() + "个结果：\n");
                        StringBuilder sb = new StringBuilder();
                        int i = 0;
                        for(FrontiaData d : dataList){
                            sb.append(i).append(":").append(d.getData().toString()).append("\n");
                        }
						tvAppdataInfo.setText(tvAppdataInfo.getText().toString()+sb.toString());
					}
					@Override
					public void onFailure(int errCode, String errMsg) {
						String strError = "errCode:" + errCode+ ", errMsg:" + errMsg ;
						tvAppdataInfo.setText(tvAppdataInfo.getText().toString()+strError);
					}
				});
		}
	};
	
	private Button.OnClickListener btnSaveListenter = new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			FrontiaAccount user = Frontia.getCurrentAccount();
	        final FrontiaData[] datas = new FrontiaData[4];

	        FrontiaACL rwAcl = new FrontiaACL();
	        rwAcl.setAccountReadable(user, true);
	        rwAcl.setAccountWritable(user, true);

	        FrontiaACL rAcl = new FrontiaACL();
	        rAcl.setAccountReadable(user, true);
	        rAcl.setAccountWritable(user, false);

	        FrontiaACL wAcl = new FrontiaACL();
	        wAcl.setAccountReadable(user, false);
	        wAcl.setAccountWritable(user, true);

	        FrontiaACL acl = new FrontiaACL();
	        acl.setAccountReadable(user, false);
	        acl.setAccountWritable(user, false);


	        JSONObject data = new JSONObject();
	        try {
	            data.put("animal", "Panda");
	            data.put("number", 10);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	        datas[0] = new FrontiaData(data);
	        datas[0].setACL(acl);
	        dataNotWrite = datas[0];

	        data = new JSONObject();
	        try {
	            data.put("animal", "Cat");
	            data.put("number", 300);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	        datas[1] = new FrontiaData(data);
	        datas[1].setACL(wAcl);
	        dataNotRead = datas[1];

	        data = new JSONObject();
	        try {
	            data.put("animal", "Dog");
	            data.put("number", 50);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	        datas[2] = new FrontiaData(data);
	        datas[2].setACL(rAcl);
	        dataCanRead = datas[2];

	        data = new JSONObject();
	        try {
	            data.put("animal", "Dragon");
	            data.put("number", 0);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	        datas[3] = new FrontiaData(data);
	        datas[3].setACL(rwAcl);
	        dataCanWrite = datas[3];
	        
	        for(int i=0;i<4;i++){
	            final int idx = i;
	            mCloudStorage.insertData(datas[i],
	                    new FrontiaStorageListener.DataInsertListener() {
	                        @Override
	                        public void onSuccess() {
	                            String newStr = "save "+idx+"data\n";
	                            tvAppdataInfo.setText(tvAppdataInfo.getText().toString()+newStr);
	                        }
	                        @Override
	                        public void onFailure(int errCode, String errMsg) {
	                            String newStr = "errCode:" + errCode
	                                    + ", errMsg:" + errMsg;
	                            tvAppdataInfo.setText(tvAppdataInfo.getText().toString()+newStr);
	                        }
	                    });
	        }
		}
	};
	
	private Button.OnClickListener btnFindListenter = new Button.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			FrontiaQuery query = new FrontiaQuery();

			mCloudStorage.findData(query,
					new DataInfoListener() {

						@Override
						public void onSuccess(List<FrontiaData> dataList) {
	                        StringBuilder sb = new StringBuilder();
                            int i = 0;
                            for(FrontiaData d : dataList){
                                sb.append(i).append(":").append(d.getData().toString()).append("\n");
                            }
							tvAppdataInfo.setText(tvAppdataInfo.getText().toString()+sb.toString());
						}

						@Override
						public void onFailure(int errCode, String errMsg) {
							String strError = "errCode:" + errCode+ ", errMsg:" + errMsg ;
							tvAppdataInfo.setText(tvAppdataInfo.getText().toString()+strError);
						}
					});

		}
	};
	
	private Button.OnClickListener btnCrtRoleListenter = new Button.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			mRole = new FrontiaRole("baidu");
	        FrontiaRole subRole1 = new FrontiaRole("mco");
	        //FrontiaUser curUser = (FrontiaUser)Frontia.getCurrentAccount();

	        FrontiaUser nullUser = new FrontiaUser(null);
	        //mRole.addMember(curUser);
	        mRole.addMember(nullUser);
	        mRole.addMember(subRole1);
	        FrontiaRole subRole2 = new FrontiaRole("cloud");
	        mRole.addMember(subRole2);

	        final String strResult[] = {""};
	        strResult[0] = "开始创建";
	        tvRoleInfo.setText(tvRoleInfo.getText().toString()+strResult[0]+"\n");
	        mRole.create(new FrontiaRole.CommonOperationListener() {

	            @Override
	            public void onSuccess() {
	                strResult[0] = "创建成功！";
	                tvRoleInfo.setText(tvRoleInfo.getText().toString()+strResult[0]+"\n");
	            }

	            @Override
	            public void onFailure(int errCode, String errMsg) {
	                strResult[0] = "创建失败！";
	                tvRoleInfo.setText(tvRoleInfo.getText().toString()+strResult[0]+"\n");
	            }
	        });
			Toast.makeText(getBaseContext(), strResult[0], Toast.LENGTH_SHORT).show();
		}
	};
	
	private Button.OnClickListener btnCrtUserListenter = new Button.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Toast.makeText(getBaseContext(),"创建用户",Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.frontia, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	

}

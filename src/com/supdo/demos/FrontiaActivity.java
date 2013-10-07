package com.supdo.demos;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaACL;
import com.baidu.frontia.FrontiaAccount;
import com.baidu.frontia.FrontiaData;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener;

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
import android.widget.TextView;
import android.widget.Toast;

public class FrontiaActivity extends Activity {
	private Button btnFind;
	private Button btnSave;
	private TextView tvInfo;
	
	final String APIKEY = "LMG2xVcHCpZ4qXe3QZADOAIa";
	
	private FrontiaStorage mCloudStorage;
    private FrontiaData dataCanRead;
    private FrontiaData dataCanWrite;
    private FrontiaData dataNotRead;
    private FrontiaData dataNotWrite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frontia_layout);
		findViews();
		initialActivity();
		setListeners();
	}
	
	private void findViews() {
		btnFind = (Button)findViewById(R.id.btnFind);
		btnSave = (Button)findViewById(R.id.btnSave);
		tvInfo = (TextView)findViewById(R.id.tvInfo);
	}

	private void initialActivity() {
		//初始化Frontia
		Frontia.init(this.getApplicationContext(), APIKEY);
		mCloudStorage = Frontia.getStorage();
		
		final String[] actions = new String[] { "应用数据", "个人数据", "个人文件" };  
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
				return false;
			}
		});
	}

	private void setListeners() {
		btnSave.setOnClickListener(btnSaveListenter);
	}
	
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
	                            tvInfo.setText(tvInfo.getText().toString()+newStr);
	                        }
	                        @Override
	                        public void onFailure(int errCode, String errMsg) {
	                            String newStr = "errCode:" + errCode
	                                    + ", errMsg:" + errMsg;
	                            tvInfo.setText(tvInfo.getText().toString()+newStr);
	                        }
	                    });
	        }
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

package com.supdo.demos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class NewUserActivity extends Activity {

	private Button btnNewUserOK;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_user_layout);
		
		btnNewUserOK = (Button)findViewById(R.id.btnNewUserOK);
		
		btnNewUserOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NewUserActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}

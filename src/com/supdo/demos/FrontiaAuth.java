package com.supdo.demos;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaAuthorizationListener;

public class FrontiaAuth {
	public boolean isInit = false;
	private final static String APPID = "1072676";
	private final static String APIKEY = "LMG2xVcHCpZ4qXe3QZADOAIa";
	private final  Activity mContext;
	
	public FrontiaAuth(Activity activity){
		mContext = activity;
		try{
			isInit = Frontia.init(mContext.getApplicationContext(), APIKEY);
		}catch(Exception ex){
			ex.toString();
		}
	}
	
	public boolean auth(){
		FrontiaAuthorization auth = Frontia.getAuthorization();

        auth.authorize(mContext,
            FrontiaAuthorization.PlatformType.BAIDU,
            new FrontiaAuthorizationListener.AuthorizationListener() {
                @Override
                public void onSuccess(final FrontiaUser user) {
                    Frontia.setCurrentAccount(user);
                    StringBuilder buf = new StringBuilder();
                    buf.append("user: \n")
                            .append(user.getAccessToken()).append(" ").append(user.getExpiresIn()).append("\n")
                            .append(user.getId()).append("\n");
                    //Log.d(TAG, buf.toString());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //saveDataButton.setEnabled(true);
                            Toast.makeText(mContext, "user" + user.getName()
                                    + " logged in", Toast.LENGTH_LONG);
                        }
                    }, 1000);
                    //show();
                    Intent intent = new Intent(mContext, FrontiaActivity.class);
                    mContext.startActivityForResult(intent, 102);
                }
                @Override
                public void onFailure(final int errCode, final String errMsg) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "errCode: " + errCode
                                    + ", errMsg: " + errMsg, Toast.LENGTH_LONG);
                        }
                    });
                }
                @Override
                public void onCancel() {
                    Toast.makeText(mContext, "login cancelled", Toast.LENGTH_LONG);
                }
            });
        return true;
	}
}

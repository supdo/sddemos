package com.supdo.demos.orm;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


public class DataHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "SDDemos.db";
	private static final int DATABASE_VERSION = 2;
	private Dao<Users, Integer> usersDao = null;
	 
	public DataHelper(Context context) {
	  super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		// TODO Auto-generated method stub
		try {
			TableUtils.createTable(connectionSource, Users.class);
		} catch (SQLException e) {
			Log.e(DataHelper.class.getName(), "创建数据库失败", e);
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int arg2, int arg3) {
		// TODO Auto-generated method stub
		try {
			TableUtils.dropTable(connectionSource, Users.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DataHelper.class.getName(), "更新数据库失败", e);
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() {
		super.close();
		usersDao = null;
	}

	public Dao<Users, Integer> getUserDao() throws SQLException {
		if (usersDao == null) {
			usersDao = getDao(Users.class);
		}
		return usersDao;
	}

}

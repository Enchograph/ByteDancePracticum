package com.example.loginproject.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // 数据库常量
    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    // 用户表常量
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // 创建用户表的SQL语句
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_USERS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USERNAME + " TEXT, " +
            COLUMN_PASSWORD + " TEXT" +
            ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 执行创建表的SQL
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 数据库版本升级时，删除旧表并重新创建
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    /**
     * 添加一个新用户
     * 
     * @param username 用户名
     * @param password 密码
     */
    public void addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        // 插入新行
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    /**
     * 检查用户是否存在于数据库中
     * 
     * @param username 用户名
     * @param password 密码
     * @return 如果用户存在且密码匹配，返回true；否则返回false
     */
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // 定义要查询的列
        String[] columns = { COLUMN_ID };
        // 定义查询条件
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        // 定义查询条件的值
        String[] selectionArgs = { username, password };

        // 执行查询
        Cursor cursor = db.query(TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    /**
     * 检查数据库中是否有任何用户
     * 
     * @return 如果有用户，返回true；否则返回false
     */
    public boolean hasUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
}

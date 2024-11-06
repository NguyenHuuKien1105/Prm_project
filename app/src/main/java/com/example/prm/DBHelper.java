package com.example.prm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import androidx.annotation.Nullable;


public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;

    //class table
    public static final String CLASS_TABLE_NAME = "CLASS_TABLE";
    public static final String C_ID = "_CID";
    public static final String CLASS_NAME_KEY = "CLASS_NAME";
    public static final String SUBJECT_NAME_KEY = "SUBJECT_NAME";

    private static final String CREATE_CLASS_TABLE =
            "CREATE TABLE " + CLASS_TABLE_NAME + "( " +
                    C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    CLASS_NAME_KEY + " TEXT NOT NULL, " +
                    SUBJECT_NAME_KEY + " TEXT NOT NULL, " +
                    "UNIQUE (" + CLASS_NAME_KEY + ", " + SUBJECT_NAME_KEY + ")" +
                    ");";

    private static final String DROP_CLASS_TABLE = "DROP TABLE IF EXISTS " + CLASS_TABLE_NAME;
    private static final String SELECT_CLASS_TABLE = "SELECT * FROM " + CLASS_TABLE_NAME;

    //student table
    public static final String STUDENT_TABLE_NAME = "STUDENT_TABLE";
    public static final String S_ID = "SID";
    public static final String STUDENT_NAME_KEY = "STUDENT_NAME";
    public static final String STUDENT_ROLL_KEY = "ROLL";

    private static final String CREATE_STUDENT_TABLE =
            "CREATE TABLE " + STUDENT_TABLE_NAME +
                    "(" +
                    S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    C_ID + " INTEGER NOT NULL, " +
                    STUDENT_NAME_KEY + " TEXT NOT NULL, " +
                    STUDENT_ROLL_KEY + " INTEGER, " +
                    " FOREIGN KEY (" + C_ID + ") REFERENCES " + CLASS_TABLE_NAME + "(" + C_ID + ")" +
                    ");";

    private static final String DROP_STUDENT_TABLE = "DROP TABLE IF EXISTS " + STUDENT_TABLE_NAME;
    private static final String SELECT_STUDENT_TABLE = "SELECT * FROM " + STUDENT_TABLE_NAME;

    // STATUS TABLE
    public static final String STATUS_TABLE_NAME = "STATUS_TABLE";
    public static final String STATUS_ID = "STATUS_ID";
    public static final String DATE_KEY = "STATUS_DATE";
    public static final String STATUS_KEY = "STATUS";

    private static final String CREATE_STATUS_TABLE =
            "CREATE TABLE " + STATUS_TABLE_NAME +
                    "(" +
                    STATUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    S_ID + " INTEGER NOT NULL, " +
                    C_ID + " INTEGER NOT NULL, " +
                    DATE_KEY + " DATE NOT NULL, " +
                    STATUS_KEY + " TEXT NOT NULL, " +
                    " UNIQUE (" + S_ID + ", " + DATE_KEY + "), " +
                    " FOREIGN KEY (" + S_ID + ") REFERENCES " + STUDENT_TABLE_NAME + "(" + S_ID + ")," +
                    " FOREIGN KEY (" + C_ID + ") REFERENCES " + CLASS_TABLE_NAME + "(" + C_ID + ")" +
                    ");";

    private static final String DROP_STATUS_TABLE = "DROP TABLE IF EXISTS " + STATUS_TABLE_NAME;
    private static final String SELECT_STATUS_TABLE = "SELECT * FROM " + STATUS_TABLE_NAME;

    public DBHelper(@Nullable Context context) {
        super(context, "Attendance.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CLASS_TABLE);
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_STATUS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_CLASS_TABLE);
            db.execSQL(DROP_STUDENT_TABLE);
            db.execSQL(DROP_STATUS_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    long addClass(String className, String subjectName) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLASS_NAME_KEY, className);
        values.put(SUBJECT_NAME_KEY, subjectName);

        return database.insert(CLASS_TABLE_NAME, null, values);
    }

    Cursor getClassTable() {
        SQLiteDatabase database = this.getReadableDatabase();

        return database.rawQuery(SELECT_CLASS_TABLE, null);
    }

    int deleteClass(long cid) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.delete(CLASS_TABLE_NAME, C_ID + "=?", new String[]{String.valueOf(cid)});
    }

    long updateClass(long cid, String className, String subjectName) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLASS_NAME_KEY, className);
        values.put(SUBJECT_NAME_KEY, subjectName);

        return database.update(CLASS_TABLE_NAME, values, C_ID + "=?", new String[]{String.valueOf(cid)});
    }

    long addStudent(long cid, int roll, String name) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_ID, cid);
        values.put(STUDENT_ROLL_KEY, roll);
        values.put(STUDENT_NAME_KEY, name);

        return database.insert(STUDENT_TABLE_NAME, null, values);
    }

    Cursor getStudentTable(long cid) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.query(STUDENT_TABLE_NAME, null,
                C_ID + "=?", new String[]{String.valueOf(cid)}, null, null, STUDENT_ROLL_KEY);
    }

    int deleteStudent(long sid) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.delete(STUDENT_TABLE_NAME, S_ID + "=?", new String[]{String.valueOf(sid)});
    }

    long updateStudent(long sid, String name) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STUDENT_NAME_KEY, name);

        return database.update(STUDENT_TABLE_NAME, values, S_ID + "=?", new String[]{String.valueOf(sid)});
    }

//    public long addStatus(long sid, long cid, String date, String status) {
//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(S_ID, sid);
//        values.put(C_ID, cid);
//        values.put(DATE_KEY, date);
//        values.put(STATUS_KEY, status);
//
//        // Check if the entry already exists
//        String whereClause = S_ID + "=? AND " + DATE_KEY + "=?";
//        String[] whereArgs = {String.valueOf(sid), date};
//        Cursor cursor = database.query(STATUS_TABLE_NAME, null, whereClause, whereArgs, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            // Update if exists
//            return updateStatus(sid, date, status);
//        } else {
//            // Insert if does not exist
//            return database.insert(STATUS_TABLE_NAME, null, values);
//        }
//    }

    public long addStatus(long sid, long cid, String date, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(S_ID, sid);
        values.put(C_ID, cid);
        values.put(DATE_KEY, date);
        values.put(STATUS_KEY, status);

        // Kiểm tra xem dữ liệu đã tồn tại trong cơ sở dữ liệu chưa
        String whereClause = S_ID + "=? AND " + DATE_KEY + "=?";
        String[] whereArgs = {String.valueOf(sid), date};
        Cursor cursor = database.query(STATUS_TABLE_NAME, null, whereClause, whereArgs, null, null, null);

        long result = -1; // Mặc định là -1 (thất bại)

        if (cursor.moveToFirst()) {
            // Dữ liệu đã tồn tại, thực hiện cập nhật
            result = updateStatus(sid, date, status);
            if (result > 0) {
                Log.d("STATUS_UPDATE", "Cập nhật trạng thái thành công! ID: " + sid + ", Date: " + date);
            } else {
                Log.d("STATUS_UPDATE", "Cập nhật trạng thái thất bại! ID: " + sid + ", Date: " + date);
            }
        } else {
            // Dữ liệu không tồn tại, thực hiện thêm mới
            result = database.insert(STATUS_TABLE_NAME, null, values);
            if (result > 0) {
                Log.d("STATUS_INSERT", "Thêm trạng thái thành công! ID: " + sid + ", Date: " + date);
            } else {
                Log.d("STATUS_INSERT", "Thêm trạng thái thất bại! ID: " + sid + ", Date: " + date);
            }
        }

        cursor.close(); // Đảm bảo đóng cursor sau khi sử dụng
        return result;
    }


    public long updateStatus(long sid, String date, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS_KEY, status);

        // Corrected table reference to STATUS_TABLE
        String whereClause = DATE_KEY + "=? AND " + S_ID + "=?";
        String[] whereArgs = {date, String.valueOf(sid)};
        return database.update(STATUS_TABLE_NAME, values, whereClause, whereArgs);
    }


//    String getStatus(long sid, String date) {
//        String status = null;
//        SQLiteDatabase database = this.getReadableDatabase();
//        String whereClause = DATE_KEY + "='" + date + "' AND " + S_ID + "=" + sid;
//        Cursor cursor = database.query(STATUS_TABLE_NAME, null, whereClause, null, null, null, null);
//        if (cursor.moveToFirst())
//            status = cursor.getString(cursor.getColumnIndexOrThrow(STATUS_KEY));
//        return status;
//    }

    String getStatus(long sid, String date) {
        String status = null;
        SQLiteDatabase database = this.getReadableDatabase();

        // Thêm dòng Log để kiểm tra điều kiện whereClause
        String whereClause = DATE_KEY + "='" + date + "' AND " + S_ID + "=" + sid;
        Log.d("whereClause", whereClause);  // Dòng này sẽ ghi lại nội dung của điều kiện whereClause

        Cursor cursor = database.query(STATUS_TABLE_NAME, null, whereClause, null, null, null, null);

        // Kiểm tra nếu cursor có dữ liệu hay không
        if (cursor != null && cursor.moveToFirst()) {
            status = cursor.getString(cursor.getColumnIndexOrThrow(STATUS_KEY));
        } else {
            Log.d("DBHelper", "No data found for sid: " + sid + " and date: " + date);  // Thêm log nếu không tìm thấy dữ liệu
        }

        if (cursor != null) {
            cursor.close();
        }

        return status;
    }

    Cursor getDistinctMonths(long cid) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(STATUS_TABLE_NAME, new String[]{DATE_KEY},
                C_ID + "=" + cid, null,
                "substr(" + DATE_KEY + ",4,7)", null, null);
    }

}

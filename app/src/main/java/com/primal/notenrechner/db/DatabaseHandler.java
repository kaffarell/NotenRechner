package com.primal.notenrechner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "subjectsdb";
    private static final String TABLE_NAME = "subjects";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_GRADES = "grades";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SUBJECT_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_GRADES + " TEXT" + ")";
        db.execSQL(CREATE_SUBJECT_TABLE);
    }

    // Upgrade Database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if it exists
        db.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME);
        onCreate(db);
    }

    // Add Subject to database
    public void addSubject(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, subject.getSubjectName());
        values.put(KEY_GRADES, subject.getSubjectGrades());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Get single subject from database
    public Subject getSubject(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
            KEY_NAME, KEY_GRADES}, KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        Subject subject = new Subject(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        return subject;
    }

    // Get all subjects as a list view
    public List<Subject> getAllSubjects() {
        List<Subject> subjectList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do {
                Subject subject = new Subject();
                subject.set_id(Integer.parseInt(cursor.getString(0)));
                subject.setSubjectName(cursor.getString(1));
                subject.setSubjectGrades(cursor.getString(2));
                subjectList.add(subject);
            }while(cursor.moveToNext());
        }

        cursor.close();
        return subjectList;
    }

    // Update single subject
    public int updateSubject(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, subject.getSubjectName());
        values.put(KEY_GRADES, subject.getSubjectGrades());

        return db.update(TABLE_NAME, values, KEY_ID + "= ?",
                new String[] {String.valueOf(subject.get_id())});
    }

    // Delete single subject
    public void deleteSubject(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "= ?", new String[] {String.valueOf(subject.get_id())});
        db.close();
    }

    // Getting count of subjects
    public  int getSubjectsCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }
}

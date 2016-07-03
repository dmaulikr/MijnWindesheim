package com.giovanniterlingen.windesheim;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A schedule app for Windesheim students
 *
 * @author Giovanni Terlingen
 */
public class ScheduleDatabase extends SQLiteOpenHelper {

    private static SQLiteDatabase database;

    public ScheduleDatabase(Context context) {
        super(context, "schedulestore.db", null, 4);
    }

    public void open() {
        database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE `subject` (_id INTEGER PRIMARY KEY AUTOINCREMENT, `component_id` TEXT, `date` TEXT, `start` TEXT, `end` TEXT, `name` TEXT, `room` TEXT, `component` TEXT, `class_id` TEXT, `visible` INTEGER)");
    }

    public void saveScheduleData(String id, String date, String start, String end, String name, String room, String component, String componentId, int visible) {
        database.execSQL("INSERT INTO `subject` (`component_id`, `date`, `start`, `end`, `name`, `room`, `component`, `class_id`, `visible`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, " + visible + ")", new String[]{id, date, start, end, name, room, component, componentId});
    }

    public void clearScheduleData(Date date) {
        String[] weekDates = getWeekDates(date);
        database.execSQL("DELETE FROM `subject` WHERE `date` >= ? AND `date` <= ? AND `visible` = 1", new String[]{weekDates[0], weekDates[1]});
    }

    public void clearOldScheduleData(String date) {
        database.execSQL("DELETE FROM `subject` WHERE `date` < ? AND `visible` = 1", new String[]{date});
    }

    public void clearLessons(long id) {
        Cursor cursor = database.rawQuery("SELECT `component_id` FROM `subject` WHERE `_id` = " + id, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                database.execSQL("UPDATE `subject` SET `visible` = 0 WHERE `component_id` = ? AND `visible` = 1", new String[]{cursor.getString(0)});
            }
            cursor.close();
        }
    }

    public void restoreLessons(long id) {
        Cursor cursor = database.rawQuery("SELECT `component_id` FROM `subject` WHERE `_id` = " + id, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                database.execSQL("UPDATE `subject` SET `visible` = 1 WHERE `component_id` = ? AND `visible` = 0", new String[]{cursor.getString(0)});
            }
            cursor.close();
        }
    }

    public Cursor getLessons(String date, String componentId) {
        return database.rawQuery("SELECT _id, `component_id`, `date`, MIN(`start`), MAX(`end`), `name`, MAX(`room`), `component`, `class_id` FROM `subject` WHERE `date` = ? AND `class_id` = ? AND `visible` = 1 GROUP BY `component_id` ORDER BY `start`, `name`", new String[]{date, componentId});
    }

    public Cursor getSingleLesson(long id) {
        return database.rawQuery("SELECT MIN(`start`), MAX(`end`), `name`, MAX(`room`) FROM `subject` WHERE `_id` = " + id, null);
    }

    public Cursor getFilteredLessonsForAdapter() {
        return database.rawQuery("SELECT `_id`, `name`, `component` FROM `subject` WHERE `visible` = 0 GROUP BY `component_id` ORDER BY `name`", null);
    }

    public Cursor getFilteredLessons() {
        return database.rawQuery("SELECT `component_id` FROM `subject` WHERE `visible` = 0 GROUP BY `component_id`", null);
    }

    public boolean containsWeek(Date date) {
        String[] weekDates = getWeekDates(date);
        Cursor cursor = database.rawQuery("SELECT _id FROM `subject` WHERE `date` >= ? AND `date` <= ? AND `visible` = 1", new String[]{weekDates[0], weekDates[1]});
        boolean bool = cursor.getCount() > 0;
        cursor.close();
        return bool;
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE `subject`");
        database.execSQL("CREATE TABLE `subject` (_id INTEGER PRIMARY KEY AUTOINCREMENT, `component_id` TEXT, `date` TEXT, `start` TEXT, `end` TEXT, `name` TEXT, `room` TEXT, `component` TEXT, `class_id` TEXT, `visible` INTEGER DEFAULT 1)");
    }

    private String[] getWeekDates(Date date) {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
        calendar.add(Calendar.DAY_OF_MONTH, -dayOfWeek);
        String lowestDate = simpleDateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 6);
        String highestDate = simpleDateFormat.format(calendar.getTime());
        return new String[]{lowestDate, highestDate};
    }

}
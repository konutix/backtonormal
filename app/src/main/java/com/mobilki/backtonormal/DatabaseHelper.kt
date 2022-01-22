package com.mobilki.backtonormal

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


const val DATABASE_NAME = "ActivityDB"


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL("CREATE TABLE categories (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(256) UNIQUE)");
        db?.execSQL("CREATE TABLE activities (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(256) UNIQUE, category INTEGER, FOREIGN KEY(category) REFERENCES categories(id))")
        db?.execSQL("INSERT INTO categories (name) VALUES ('Physical')")
        db?.execSQL("INSERT INTO categories (name) VALUES ('Mental')")
        db?.execSQL("INSERT INTO categories (name) VALUES ('Cultural')")
        db?.execSQL("INSERT INTO activities (name, category) VALUES ('Reading a book', (SELECT id FROM categories WHERE name='Cultural'))")
        db?.execSQL("INSERT INTO activities (name, category) VALUES ('Listening to an audiobook', (SELECT id FROM categories WHERE name='Cultural'))")
        db?.execSQL("INSERT INTO activities (name, category) VALUES ('Running', (SELECT id FROM categories WHERE name='Physical'))")
        db?.execSQL("INSERT INTO activities (name, category) VALUES ('Push ups', (SELECT id FROM categories WHERE name='Physical'))")
        db?.execSQL("INSERT INTO activities (name, category) VALUES ('Meditation', (SELECT id FROM categories WHERE name='Mental'))")

        db?.execSQL("CREATE TABLE preferences (id INTEGER PRIMARY KEY AUTOINCREMENT, activity INTEGER UNIQUE, preferred INTEGER, FOREIGN KEY(activity) REFERENCES activities(id))")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun getActivityCategories() : ArrayList<String> {
        val query = "SELECT name FROM categories;";
        var result = this.writableDatabase.rawQuery(query, null)

        val list = ArrayList<String>()
        while (result.moveToNext()) {
            list.add(result.getString(0))
        }

        return list
    }

    fun getAllActivities() : ArrayList<ActivityInfo> {
        val query = "SELECT A.id, A.name, B.name FROM activities as A LEFT JOIN categories as B on A.category=B.id;";
        var result = this.writableDatabase.rawQuery(query, null)

        val list = ArrayList<ActivityInfo>()
        while (result.moveToNext()) {
            var info = ActivityInfo()
            info.id = result.getInt(0)
            info.name = result.getString(1)
            info.cat = result.getString(2)
            list.add(info)
        }

        return list
    }

    fun getActivity(id : Int) : ActivityInfo? {
        val query = "SELECT A.name, B.name FROM activities as A JOIN categories as B ON A.category=B.id WHERE A.id = $id"
        var result = this.writableDatabase.rawQuery(query, null)

        if (result.moveToNext()) {
            var info = ActivityInfo()
            info.id = id
            info.name = result.getString(0)
            info.cat = result.getString(1)
            return info
        }
        return null
    }

    fun preferActivity(id : Int, prefer : Boolean) : Long {
        var cv = ContentValues()
        cv.put("activity", id)
        cv.put("preferred", prefer)
        return this.writableDatabase.replace("preferences", null, cv)
    }

    fun getPreferredActivities() : ArrayList<ActivityInfo> {
        val query = "SELECT A.id, A.name FROM preferences as B LEFT JOIN activities as a on A.id=B.activity WHERE B.preferred=1;"
        var result = this.writableDatabase.rawQuery(query, null)

        val list = ArrayList<ActivityInfo>()
        while (result.moveToNext()) {
            var info = ActivityInfo()
            info.id = result.getInt(0)
            info.name = result.getString(1)

            list.add(info)
        }

        return list
    }

    fun isPreferred(id : Int) : Boolean {
        val query = "SELECT preferred FROM preferences WHERE activity=$id"
        var result = this.writableDatabase.rawQuery(query, null)
        result.moveToNext()
        return result.getInt(0) == 1
    }

    fun test() {
        val query = "SELECT * FROM preferences";
        var result = this.writableDatabase.rawQuery(query, null)

        while (result.moveToNext()) {
            val str = "Num: " + result.count + " Results: " +result.getInt(0) + " " + result.getInt(1) + " " + result.getInt(2)
            println(str)
        }
    }
}
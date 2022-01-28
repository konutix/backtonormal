
package com.mobilki.backtonormal

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList


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

        db?.execSQL("CREATE TABLE tasks (id INTEGER PRIMARY KEY AUTOINCREMENT,task VARCHAR(256) ,task_description VARCHAR(256) UNIQUE, progress INTEGER,task_id integer, FOREIGN KEY(task) REFERENCES activities(name), FOREIGN KEY(task_id) REFERENCES activities(id))")
        db?.execSQL("INSERT INTO tasks(task_description, progress, task, task_id) VALUES ('do 100 repeats',0, (SELECT name FROM activities WHERE name='Push ups'), (SELECT id FROM activities WHERE name='Push ups'))")
        db?.execSQL("INSERT INTO tasks(task_description, progress, task, task_id) VALUES ('run 2500 meters',0, (SELECT name FROM activities WHERE name='Running'), (SELECT id FROM activities WHERE name='Running'))")
        db?.execSQL("INSERT INTO tasks(task_description, progress, task, task_id) VALUES ('read 25 pages',0, (SELECT name FROM activities WHERE name='Reading a book'), (SELECT id FROM activities WHERE name='Reading a book'))")
        db?.execSQL("INSERT INTO tasks(task_description, progress, task, task_id) VALUES ('meditate for 30 minutes',0, (SELECT name FROM activities WHERE name='Meditation'), (SELECT id FROM activities WHERE name='Meditation'))")
        db?.execSQL("INSERT INTO tasks(task_description, progress, task, task_id) VALUES ('listening to the audiobook for 30 minutes ',0, (SELECT name FROM activities WHERE name='Listening to an audiobook'), (SELECT id FROM activities WHERE name='Listening to an audiobook'))")

        db?.execSQL("CREATE TABLE tips (id INTEGER PRIMARY KEY AUTOINCREMENT,title VARCHAR(256) ,description VARCHAR(512), displayed INTEGER)")
        db?.execSQL("INSERT INTO tips(title, description, displayed) VALUES ('Healthy sleep', 'Getting good quality sleep can help you feel like your best self. Healthy sleep patterns improve learning, memory, creativity, and mood. Healthy sleep also strengthens the immune system and makes it easier to maintain a healthy diet.', 0)")
        db?.execSQL("INSERT INTO tips(title, description, displayed) VALUES ('The Slight Edge', 'The secret to becoming successful in life is repeating the small daily habits and actions, over and over. Then the compound effect will naturally carry you to success!', 0)")
        db?.execSQL("INSERT INTO tips(title, description, displayed) VALUES ('Pomodoro', 'This popular time management method asks you to alternate pomodoros — focused work sessions — with frequent short breaks to promote sustained concentration and stave off mental fatigue.', 0)")
        db?.execSQL("INSERT INTO tips(title, description, displayed) VALUES ('Stay hydrated', 'Drinking enough water each day is crucial for many reasons: to regulate body temperature, keep joints lubricated, prevent infections, deliver nutrients to cells. Being well-hydrated also improves sleep quality.', 0)")
        db?.execSQL("INSERT INTO tips(title, description, displayed) VALUES ('Positive posture', 'Good posture is about more than standing up straight so you can look your best. It is an important part of your long-term health. It can also influence your mood.', 0)")
        db?.execSQL("INSERT INTO tips(title, description, displayed) VALUES ('Learning new languages', 'Trying a new language is a lot of fun plus it is a great exercise for your brain.', 0)")

        db?.execSQL("CREATE TABLE daily_tasks (id INTEGER PRIMARY KEY AUTOINCREMENT,title VARCHAR(256),slot INTEGER, complete INTEGER)")
        db?.execSQL("INSERT INTO daily_tasks(title, slot, complete) VALUES ('Go for a walk', 1, 0)")
        db?.execSQL("INSERT INTO daily_tasks(title, slot, complete) VALUES ('Pick up a new language', 2, 0)")
        db?.execSQL("INSERT INTO daily_tasks(title, slot, complete) VALUES ('Tidy your room', 3, 0)")
        db?.execSQL("INSERT INTO daily_tasks(title, slot, complete) VALUES ('Try a new cooking recipe', 0, 0)")
        db?.execSQL("INSERT INTO daily_tasks(title, slot, complete) VALUES ('Visit a museum', 0, 0)")
        db?.execSQL("INSERT INTO daily_tasks(title, slot, complete) VALUES ('Draw something', 0, 0)")

        db?.execSQL("CREATE TABLE last_login (id INTEGER PRIMARY KEY AUTOINCREMENT, day VARCHAR(256))")

        val calendar = Calendar.getInstance()
        val day = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.time)

        db?.execSQL("INSERT INTO last_login(id, day) VALUES (1, '$day')")

        db?.execSQL("CREATE TABLE task_selected (id INTEGER PRIMARY KEY AUTOINCREMENT,task_number INTEGER UNIQUE,chosen INTEGER,FOREIGN KEY(task_number) REFERENCES activities(id));")

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
        val query = "SELECT A.id, A.name, C.name FROM preferences as B LEFT JOIN activities as a on A.id=B.activity JOIN categories as C ON A.category=C.id WHERE B.preferred=1;"
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

    fun isPreferred(id : Int) : Boolean {
        val query = "SELECT preferred FROM preferences WHERE activity=$id"
        var result = this.writableDatabase.rawQuery(query, null)
        if (result.count < 1) {
            return false
        }

        result.moveToNext()
        return result.getInt(0) == 1
    }

    fun getAllTasks(): ArrayList<StatsInfo> {
        val query = "SELECT A.id, A.task, A.task_description, A.progress FROM tasks as A"
        var result = this.writableDatabase.rawQuery(query, null)

        val list = ArrayList<StatsInfo>()
        while (result.moveToNext()) {
            var info = StatsInfo()
            info.id = result.getInt(0)
            info.taskName = result.getString(1)
            info.taskDescription = result.getString(2)
            info.progress = result.getInt(3)
            list.add(info)
        }
        return list;
    }
    fun getAllTrackedTasks(): ArrayList<StatsInfo> {
        //val query = "SELECT A.id, A.task, A.task_description, A.progress FROM tasks as A LEFT JOIN task_selected as B on A.task_id=B.task_number WHERE B.chosen=1;"
        val query = "SELECT A.id, A.task, A.task_description, A.progress, A.task_id  FROM tasks as A LEFT JOIN task_selected as B on A.task_id=B.task_number WHERE B.chosen=1;"
//        val query = "SELECT B.task_number, A.task, A.task_description, A.progress FROM task_selected as B LEFT JOIN tasks as A on A.task_id=B.task_number WHERE B.chosen=1;"
        var result = this.writableDatabase.rawQuery(query, null)

        val list = ArrayList<StatsInfo>()
        while (result.moveToNext()) {
            var info = StatsInfo()
            info.id = result.getInt(0)
            info.taskName = result.getString(1)
            info.taskDescription = result.getString(2)
            info.progress = result.getInt(3)
            info.taskId = result.getInt(4)
            list.add(info)
        }
        return list;
    }
    fun getTask(id : Int) : StatsInfo? {
        val query = "SELECT A.id ,A.task, A.task_description, A.progress FROM tasks as A WHERE A.task_id = $id"
        var result = this.writableDatabase.rawQuery(query, null)
        if (result.moveToNext()) {
            var info = StatsInfo()
            info.id = result.getInt(0)
            info.taskName = result.getString(1)
            info.taskDescription = result.getString(2)
            info.progress = result.getInt(3)
            info.taskId = id
            return info
        }
        return null
    }
    fun isTracked(id : Int) : Boolean {
        val query = "SELECT chosen FROM task_selected  WHERE task_number=$id"
        var result = this.writableDatabase.rawQuery(query, null)
        if (result.count < 1) {
            return false
        }

        result.moveToNext()
        return result.getInt(0) == 1
    }
    fun saveTaskToDataBase(statsInfo : StatsInfo){
        var cv = ContentValues()
        cv.put("id",statsInfo.id)
        cv.put("task", statsInfo.taskName)
        cv.put("task_description", statsInfo.taskDescription)
        cv.put("progress", statsInfo.progress)
        cv.put("task_id", statsInfo.taskId)
        this.writableDatabase.replace("tasks", null, cv)
//        this.writableDatabase.replace("task_selected", null, cv1)
    }
    fun preferTask(id : Int, chosen : Boolean) : Long {
        var cv = ContentValues()
        cv.put("task_number", id)
        cv.put("chosen", chosen)
        return this.writableDatabase.replace("task_selected", null, cv)
    }

    fun getTips() : ArrayList<TipInfo>{

        val query = "SELECT id, title, description, displayed FROM tips"
        var result = this.writableDatabase.rawQuery(query, null)

        val list = ArrayList<TipInfo>()
        while (result.moveToNext()) {
            var tip = TipInfo()
            tip.id = result.getInt(0)
            tip.title = result.getString(1)
            tip.content = result.getString(2)
            tip.displayed = result.getInt(3)
            list.add(tip)
        }

        return list

    }

    fun drawDaily(){

        val query = "SELECT id, title, slot, complete FROM daily_tasks"
        var result = this.writableDatabase.rawQuery(query, null)

        val list = ArrayList<DailyInfo>()
        while (result.moveToNext()) {
            var daily = DailyInfo()
            daily.id = result.getInt(0)
            daily.title = result.getString(1)
            daily.slot = result.getInt(2)
            daily.completed = result.getInt(3)
            list.add(daily)
        }

        list.shuffle()

        var i = 0;

        while(i < 3) {
            var cv = ContentValues()
            cv.put("id", list.get(i).id)
            cv.put("slot", i+1)
            cv.put("title", list.get(i).title)
            cv.put("complete", 0)
            this.writableDatabase.replace("daily_tasks", null, cv)
            i++
        }
        while(i < list.size) {
            var cv = ContentValues()
            cv.put("id", list.get(i).id)
            cv.put("slot", 0)
            cv.put("title", list.get(i).title)
            cv.put("complete", 0)
            this.writableDatabase.replace("daily_tasks", null, cv)
            i++
        }

    }

    fun getLastLogin() : String{

        val query = "SELECT day FROM last_login"
        var result = this.writableDatabase.rawQuery(query, null)

        result.moveToNext()

        return result.getString(0)

    }

    fun setLastLogin(){

        var cv = ContentValues()

        val calendar = Calendar.getInstance()
        val day = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.time)

        cv.put("id", 1)
        cv.put("day", day)

        this.writableDatabase.replace("last_login", null, cv)

    }

    fun getDaily() : ArrayList<DailyInfo>{

        val query = "SELECT id, title, slot, complete FROM daily_tasks WHERE slot > 0"
        var result = this.writableDatabase.rawQuery(query, null)

        val list = ArrayList<DailyInfo>()
        while (result.moveToNext()) {
            var daily = DailyInfo()
            daily.id = result.getInt(0)
            daily.title = result.getString(1)
            daily.slot = result.getInt(2)
            daily.completed = result.getInt(3)
            list.add(daily)
        }

        return list

    }

    fun changeDailyState(daily_task : DailyInfo, state : Int){
        var cv = ContentValues()
        cv.put("id", daily_task.id)
        cv.put("title", daily_task.title)
        cv.put("slot", daily_task.slot)
        cv.put("complete", state)
        this.writableDatabase.replace("daily_tasks", null, cv)
    }
}
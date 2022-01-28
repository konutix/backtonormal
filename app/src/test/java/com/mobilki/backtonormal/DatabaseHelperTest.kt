package com.mobilki.backtonormal


import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class DatabaseHelperTest {
    private lateinit var database : DatabaseHelper


    @Before
    fun setup() {
        database = DatabaseHelper(RuntimeEnvironment.application)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun categoriesTest() {
        val list = database.getActivityCategories()
        assertEquals(list.size, 3)
    }

    @Test
    fun activitiesTest() {
        val list = database.getAllActivities()
        assertEquals(list.size, 5)

        val activity = database.getActivity(list[0].id)
        assertEquals(activity!!.id, list[0].id)
    }

    @Test
    fun preferencesTest() {
        val list = database.getAllActivities()
        assertEquals(database.isPreferred(list[0].id), false)

        var preferences = database.getPreferredActivities()
        assertEquals(preferences.size, 0)

        database.preferActivity(list[0].id, true)

        preferences = database.getPreferredActivities()
        assertEquals(preferences.size, 1)

        assertEquals(database.isPreferred(list[0].id), true)
        database.preferActivity(list[0].id, false)
        assertEquals(database.isPreferred(list[0].id), false)
    }

    @Test
    fun tasksTest() {
        val list = database.getAllActivities()
        val tasks = database.getAllTasks()
        val statsInfo: StatsInfo = tasks[0]
        assertEquals(tasks.size, 5)
        assertEquals(database.getAllTrackedTasks().size, 0)
        assertFalse(database.isTracked(0))
        assertNull(database.getTask(-1))

        database.preferTask(list[0].id, true)
        assertEquals(database.getAllTrackedTasks().size, 1)
        assertEquals(database.isTracked(list[0].id), true)

        database.preferTask(list[0].id, false)
        assertEquals(database.isTracked(list[0].id), false)

        assertEquals(tasks[0].id, database.getTask(tasks[0].id)!!.taskId)

        statsInfo.progress = 20
        database.saveTaskToDataBase(statsInfo)
        assertEquals(tasks[0].progress,20)

    }
    @Test
    fun DailyTest(){
        val daily = database.getDaily()
        assertEquals(daily.size,3)
        val daily1Id : DailyInfo = daily.get(0)
        database.changeDailyState(daily1Id,0)
        assertEquals(daily.get(0).completed,0)
        assertEquals(daily1Id.completed,0)

    }
    @Test
    fun TipsTest(){
        val tips = database.getTips()
        val tip : TipInfo = tips[0]
        assertEquals(tips.size,6)
        assertEquals(tip.id,1)
    }



}
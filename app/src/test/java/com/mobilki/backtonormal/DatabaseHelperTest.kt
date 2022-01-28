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
        assertEquals(tasks.size, 5)
        assertEquals(database.getAllTrackedTasks().size, 0)

        assertNull(database.getTask(-1))

        database.preferTask(list[0].id, true)
        assertEquals(database.getAllTrackedTasks().size, 1)
        assertEquals(database.isTracked(list[0].id), true)

        database.preferTask(list[0].id, false)
        assertEquals(database.isTracked(list[0].id), false)

        assertEquals(tasks[0].id, database.getTask(tasks[0].id)!!.taskId)
    }
}
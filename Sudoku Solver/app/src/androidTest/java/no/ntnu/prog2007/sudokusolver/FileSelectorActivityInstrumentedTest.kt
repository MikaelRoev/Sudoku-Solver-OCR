package no.ntnu.prog2007.sudokusolver

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import no.ntnu.prog2007.sudokusolver.fileSelector.FileSelectorActivity
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class FileSelectorActivityInstrumentedTest {
    private lateinit var scenario: ActivityScenario<FileSelectorActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(FileSelectorActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun testRecyclerViewHasItems() {
        scenario.onActivity { activity ->
            val recyclerView = activity.findViewById<RecyclerView>(R.id.fileRecyclerView)
            assertNotNull(recyclerView.adapter)
            assertTrue(recyclerView.adapter!!.itemCount == 0)
        }
    }

}
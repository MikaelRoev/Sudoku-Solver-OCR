package no.ntnu.prog2007.sudokusolver.ui.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * View model for the info page.
 */
class InfoViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Here is the information about the sudoku solver app!\n" +
                "The first step to start solving the sudoku is to insert it " +
                "to the application. That can be done under the insert page. \n" +
                "1. Click 'insert sudoku'\n" +
                "2. Click on a selected cell, and the number of choice\n\n" +
                "After inserting all the numbers you know, you can solve it, " +
                "and reveal the numbers of your choice!\n" +
                "1. Click 'solve'\n" +
                "2. You will be sent to the solved sudoku\n" +
                "3. Click on a selected cell to reveal the number underneath\n\n" +
                "You can save your sudoku in any part of the prosess. Both before solving, " +
                "and after revealing any of the cells.\n\n" +
                "If you want to come back later and continue on you sudoku, you can find it" +
                "in the file chooser page.\n" +
                "1. Click 'file chooser', this can be done both from the main page, and the " +
                "menu bar\n" +
                "2. All your saved sudokus will now be displayed. Click on the selected one\n" +
                "3. You can now keep revealing the numbers, and keep solving!"
    }
    val text: LiveData<String> = _text
}
package no.ntnu.prog2007.sudokusolver.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to the sudoku solver app!"
    }
    val text: LiveData<String> = _text

}
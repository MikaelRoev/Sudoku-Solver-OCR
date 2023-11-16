package no.ntnu.prog2007.sudokusolver.ui.solver

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SolverViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is solver Fragment"
    }
    val text: LiveData<String> = _text

}
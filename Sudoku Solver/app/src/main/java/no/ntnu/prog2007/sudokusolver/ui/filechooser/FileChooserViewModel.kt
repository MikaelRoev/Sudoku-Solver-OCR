package no.ntnu.prog2007.sudokusolver.ui.filechooser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FileChooserViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is File Chooser Fragment"
    }
    val text: LiveData<String> = _text
}
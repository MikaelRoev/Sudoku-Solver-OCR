package no.ntnu.prog2007.sudokusolver.ui.info

class InfoViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is info Fragment"
    }
    val text: LiveData<String> = _text
}
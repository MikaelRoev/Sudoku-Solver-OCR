package no.ntnu.prog2007.sudokusolver.ui.save_dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import no.ntnu.prog2007.sudokusolver.R
import no.ntnu.prog2007.sudokusolver.databinding.FragmentSavingBinding


class SavingFragment(private val listener: SavingDialogListener) : DialogFragment() {

    // Define a listener interface to communicate with the hosting activity or fragment
    interface SavingDialogListener {
        fun onSaveClicked(fileName: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentSavingBinding.inflate(layoutInflater)
        fragmentBinding.buttonSave.setOnClickListener {
            // Notify the listener (hosting activity or fragment) that the save button is clicked
            val fileName = fragmentBinding.editTextFileName.text.toString().trim()
            if (fileName.isBlank())
                Toast.makeText(requireContext(),
                        R.string.enter_file_name_prompt, Toast.LENGTH_SHORT).show()
            else {
                listener.onSaveClicked(fileName)
                // Dismiss the dialog
                dismiss()
            }
        }
        return fragmentBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), R.style.Theme_SudokuSolver)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return dialog
    }
}
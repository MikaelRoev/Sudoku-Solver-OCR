package no.ntnu.prog2007.sudokusolver.fileSelector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import no.ntnu.prog2007.sudokusolver.R
import java.io.File

/**
 * Adapter for the file selector recycler view.
 * @param files the list of files to select from
 */
class FileSelectorAdapter(private val files: Array<File>?) :
    RecyclerView.Adapter<FileSelectorAdapter.ViewHolder>()
{

    /**
     * ViewHolder for a file selector item in the recycler view.
     * @param view the root view of the file selector item
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val fileNameTextView: TextView

        init {
            fileNameTextView = view.findViewById(R.id.fileNameTextView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.file_selector_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = files?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        if (files == null) return
        val file = files[index]
        holder.fileNameTextView.text = file.name
    }

}

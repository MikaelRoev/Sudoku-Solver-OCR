package no.ntnu.prog2007.sudokusolver

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

/**
 * Manages the reading, writing and deletion of files.
 */
class FileManager {
    companion object {

        /**
         * Reads from the file at the specified path.
         * @param filePath the path of the file to read
         * @return the content of the file as string or null if it could not read the file
         */
        fun readFile(filePath: String): String? {
            val file = File(filePath)
            val sb = StringBuilder()

            try {
                val reader = BufferedReader(FileReader(file))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                reader.close()
            } catch (e: IOException) {
                return null
            }
            return sb.toString()
        }

        /**
         * Writes to file at specified path.
         * @param filePath the path of the file to write to
         * @param content the content to write to file
         * @return true if it was successful in writing to the file or false if not
         */
        fun writeFile(filePath: String, content: String): Boolean {
            val file = File(filePath)

            return try {
                val writer = FileWriter(file)
                writer.write(content)
                writer.close()
                true
            } catch (e: IOException) {
                false
            }
        }

        /**
         * Deletes the file at specified path.
         * @param filePath the path of the file to delete
         * @return true if it was successfully deleted or false if not
         */
        fun deleteFile(filePath: String): Boolean {
            val file = File(filePath)
            if (file.exists()) {
                return file.delete()
            }
            return false
        }
    }
}
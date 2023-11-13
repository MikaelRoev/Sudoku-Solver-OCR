package no.ntnu.prog2007.sudokusolver

import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream

/**
 * Manages the reading, writing and deletion of files.
 * Reference for the file formats:
 * https://sudopedia.sudocue.net/index.php/Sudoku_Clipboard_and_File_Formats
 */
class FileManager {
    companion object {
        val supportedFileExtensions = arrayOf(
            "msk", "sol", "sdk", "sdm", "txt", "spf", "ss"
        )

        /**
         * Copies an input stream into a file.
         * @param inputStream to copy
         * @param file to copy to
         */
        private fun copyInputStreamToFile(inputStream: InputStream, file: File) {
            try {
                FileOutputStream(file, false).use { outputStream ->
                    var read: Int
                    val bytes = ByteArray(DEFAULT_BUFFER_SIZE)
                    while (inputStream.read(bytes).also { read = it} != -1) {
                        outputStream.write(bytes, 0, read)
                    }
                }
            } catch (e: IOException) {
                Log.e("Failed to load file:", e.message.toString())
            }
        }

        /**
         * Checks if a file type is supported by this file manager.
         * @param fileExtension is the file type to check
         * @return true if the file type is supported.
         */
        fun isSupportedFileType(fileExtension: String) =
            supportedFileExtensions.contains(fileExtension)

        /**
         * Reads from the input stream
         * @param inputStream to read
         * @param fileType the file extension to read it as
         * @return the content of the input stream as a grid of integers
         * or null if it could not read the input stream
         */
        fun readInputStream(inputStream: InputStream, fileType: String): List<List<Int>>? {
            val tempFile = File.createTempFile("temp", ".$fileType")
            copyInputStreamToFile(inputStream, tempFile)
            return readFile(tempFile)
        }

        /**
         * Reads from the file
         * @param file to read
         * @return the content of the file as a grid of integers
         * or null if it could not read the file
         */
        fun readFile(file: File): List<List<Int>>? {
            if (!file.isFile) return null
            return when(file.extension.lowercase()) {
                "msk", "sol"    -> { readFileMSKAndSOL(file) }
                "sdk"           -> { readFileSDK(file) }
                "sdm", "txt"    -> { readFileTXTAndSDM(file) }
                "spf", "ss"     -> { readFileSPFAndSS(file) }
                else            -> { null }
            }
        }

        /**
         * Checks if the char is a valid sudoku digit.
         * @return true if it is a valid sudoku digit or false if not
         */
        private fun Char.isValidSudokuDigit() = this in '0'..'9'
                || this == '.' || this == 'X'

        /**
         * Parses the char to integer that is a sudoku digit.
         * @return the sudoku digit.
         */
        private fun Char.toSudokuDigit(): Int {
            if (this == '.' || this == 'X') return 0
            return this.toString().toInt()
        }

        /**
         * Reads files that are in the VBForums Contest format
         * @param file to be read
         * @return the resulting sudoku grid or null if it could not read the file
         */
        private fun readFileMSKAndSOL(file: File): List<List<Int>>? {
            try {
                val grid = List(9) { MutableList(9) { 0 } }
                val reader = BufferedReader(FileReader(file))
                var line: String?
                var rowIndex = 0
                while (reader.readLine().also { line = it } != null
                    && rowIndex < 9) {
                    if (line!!.length < 9) return null

                    for (colIndex in 0 until 9) {
                        val char = line!![colIndex]
                        if (!char.isValidSudokuDigit()) return null
                        grid[rowIndex][colIndex] = char.toSudokuDigit()
                    }
                    rowIndex++
                }
                reader.close()
                return grid
            } catch (e: IOException) {
                return null
            }
        }

        /**
         * Reads files that are in the SadMan Software Sudoku format
         * @param file to be read
         * @return the resulting sudoku grid or null if it could not read the file
         */
        private fun readFileSDK(file: File): List<List<Int>>? {
            try {
                val grid = List(9) { MutableList(9) { 0 } }
                val reader = BufferedReader(FileReader(file))
                var line: String?
                var rowIndex = 0
                var puzzleStarted = false
                while (reader.readLine().also { line = it } != null
                    && rowIndex < 9) {
                    if (line!!.contains("[Puzzle]") && !puzzleStarted) {
                        puzzleStarted = true
                        continue
                    }
                    if (!puzzleStarted) continue
                    if (line!!.length < 9) return null

                    for (colIndex in 0 until 9) {
                        val char = line!![colIndex]
                        if (!char.isValidSudokuDigit()) return null
                        grid[rowIndex][colIndex] = char.toSudokuDigit()
                    }
                    rowIndex++
                }
                reader.close()
                return grid
            } catch (e: IOException) {
                return null
            }
        }

        /**
         * Reads files that are in the Sudoku Puzzle Collection or txt format
         * @param file to be read
         * @return the resulting sudoku grid or null if it could not read the file
         */
        private fun readFileTXTAndSDM(file: File): List<List<Int>>? {
            return try {
                val grid = List(9) { MutableList(9) { 0 } }
                val reader = BufferedReader(FileReader(file))
                var line: String?
                var foundLine = false
                while (reader.readLine().also { line = it } != null && !foundLine) {
                    if (line!!.length < 81 || !line!!.all { it.isValidSudokuDigit() })
                        continue
                    foundLine = true
                    var index = 0
                    line!!.forEach { char ->
                        grid[index / 9][index % 9] = char.toSudokuDigit()
                        index++
                    }
                }
                reader.close()
                grid
            } catch (e: IOException) {
                null
            }
        }

        /**
         * Reads files that are in the SuDoku Solver format or Simple Sudoku format
         * @param file to be read
         * @return the resulting sudoku grid or null if it could not read the file
         */
        private fun readFileSPFAndSS(file: File): List<List<Int>>? {
            try {
                val grid = List(9) { MutableList(9) { 0 } }
                val reader = BufferedReader(FileReader(file))
                var line: String?
                var rowIndex = 0
                while (reader.readLine().also { line = it } != null
                    && rowIndex < 9) {
                    if (line!![0] == '-' || line!![2] == '-') continue
                    if (line!!.length < 9) return null
                    var colIndex = 0
                    var charIndex = 0
                    while (colIndex < 9 && charIndex < line!!.length ) {
                        val char = line!![charIndex]
                        if (char.isValidSudokuDigit()) {
                            grid[rowIndex][colIndex] = char.toSudokuDigit()
                            colIndex++
                        }
                        charIndex++
                    }
                    rowIndex++
                }
                reader.close()
                return grid
            } catch (e: IOException) {
                return null
            }
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
         * Writes a sudoku grid to file with the SuDoku Solver format at specified path.
         * @param filePath the path of the file to write to
         * @param grid the sudoku grid to write to file
         * @return true if it was successful in writing to the file or false if not
         */
        fun writeFileSPF(filePath: String, grid: List<List<Int>>): Boolean {
            val file = File(filePath)

            return try {
                val writer = FileWriter(file)
                for (i in 0 until 9) {
                    for (j in 0 until 9) {
                        val value = grid[i][j]
                        if (value == 0) {
                            writer.write(". ")
                        } else {
                            writer.write("$value ")
                        }

                        if (j == 2 || j == 5) {
                            writer.write("| ")
                        }
                    }
                    writer.write("\r\n")

                    if (i == 2 || i == 5) {
                        writer.write("------+-------+------\r\n")
                    }
                }
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
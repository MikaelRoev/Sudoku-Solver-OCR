package no.ntnu.prog2007.sudokusolver

import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FileManagerUnitTest {

    companion object {
        private var fileNames = mutableListOf<String>()
        private val resultingGrid = listOf(
            listOf(8, 0, 0, /*|*/ 0, 0, 0, /*|*/ 0, 0, 0),
            listOf(0, 0, 3, /*|*/ 6, 0, 0, /*|*/ 0, 0, 0),
            listOf(0, 7, 0, /*|*/ 0, 9, 0, /*|*/ 2, 0, 0),
            //-------------------------------------------
            listOf(0, 5, 0, /*|*/ 0, 0, 7, /*|*/ 0, 0, 0),
            listOf(0, 0, 0, /*|*/ 0, 4, 5, /*|*/ 7, 0, 0),
            listOf(0, 0, 0, /*|*/ 1, 0, 0, /*|*/ 0, 3, 0),
            //-------------------------------------------
            listOf(0, 0, 1, /*|*/ 0, 0, 0, /*|*/ 0, 6, 8),
            listOf(0, 0, 8, /*|*/ 5, 0, 0, /*|*/ 0, 1, 0),
            listOf(0, 9, 0, /*|*/ 0, 0, 0, /*|*/ 4, 0, 0),
        )
        @JvmStatic
        @AfterClass
        fun testDeleteFile() {
            fileNames.forEach { assertTrue(FileManager.deleteFile(it)) }
        }
    }

    @Test
    fun testReadFile_msk() {
        val fileName = "test.msk"
        assertTrue(FileManager.writeFile(fileName,
            "800000000\r\n" +
                "003600000\r\n" +
                "070090200\r\n" +
                "050007000\r\n" +
                "000045700\r\n" +
                "000100030\r\n" +
                "001000068\r\n" +
                "008500010\r\n" +
                "090000400"))
        fileNames.add(fileName)
        assertEquals(resultingGrid, FileManager.readFile(fileName))
    }

    @Test
    fun testReadFile_sol() {
        val fileName = "test.sol"
        assertTrue(FileManager.writeFile(fileName,
            "800000000\r\n" +
                    "003600000\r\n" +
                    "070090200\r\n" +
                    "050007000\r\n" +
                    "000045700\r\n" +
                    "000100030\r\n" +
                    "001000068\r\n" +
                    "008500010\r\n" +
                    "090000400"))
        fileNames.add(fileName)
        assertEquals(resultingGrid, FileManager.readFile(fileName))
    }

    @Test
    fun testReadFile_sdk() {
        val fileName = "test.sdk"
        assertTrue(FileManager.writeFile(fileName,
            "[Puzzle]\r\n"+
                    "800000000\r\n" +
                    "003600000\r\n" +
                    "070090200\r\n" +
                    "050007000\r\n" +
                    "000045700\r\n" +
                    "000100030\r\n" +
                    "001000068\r\n" +
                    "008500010\r\n" +
                    "090000400"))
        fileNames.add(fileName)
        assertEquals(resultingGrid, FileManager.readFile(fileName))
    }

    @Test
    fun testReadFile_txt() {
        val fileName = "test.txt"
        assertTrue(FileManager.writeFile(fileName,
            "\r\n800000000003600000070090200050007000000045700000100030001000068008500010090000400\r\n"))
        fileNames.add(fileName)
        assertEquals(resultingGrid, FileManager.readFile(fileName))
    }
    @Test
    fun testReadFile_sdm() {
        val fileName = "test.sdm"
        assertTrue(FileManager.writeFile(fileName,
            "\r\n800000000003600000070090200050007000000045700000100030001000068008500010090000400\r\n"))
        fileNames.add(fileName)
        assertEquals(resultingGrid, FileManager.readFile(fileName))
    }

    @Test
    fun testReadFile_spf() {
        val fileName = "test.spf"
        assertTrue(FileManager.writeFile(fileName,
              " 8 . . | . . . | . . .\r\n" +
                     " . . 3 | 6 . . | . . .\r\n" +
                     " . 7 . | . 9 . | 2 . .\r\n" +
                     "-------+-------+------\r\n" +
                     " . 5 . | . . 7 | . . .\r\n" +
                     " . . . | . 4 5 | 7 . .\r\n" +
                     " . . . | 1 . . | . 3 .\r\n" +
                     "-------+-------+------\r\n" +
                     " . . 1 | . . . | . 6 8\r\n" +
                     " . . 8 | 5 . . | . 1 .\r\n" +
                     " . 9 . | . . . | 4 . ."))
        fileNames.add(fileName)
        assertEquals(resultingGrid, FileManager.readFile(fileName))
    }

    @Test
    fun testReadFile_ss() {
        val fileName = "test.ss"
        assertTrue(FileManager.writeFile(fileName,
             " *-----------------------*\n" +
                    " | 8 . . | . . . | . . . |\n" +
                    " | . . 3 | 6 . . | . . . |\n" +
                    " | . 7 . | . 9 . | 2 . . |\n" +
                    " |-------+-------+-------|\n" +
                    " | . 5 . | . . 7 | . . . |\n" +
                    " | . . . | . 4 5 | 7 . . |\n" +
                    " | . . . | 1 . . | . 3 . |\n" +
                    " |-------+-------+-------|\n" +
                    " | . . 1 | . . . | . 6 8 |\n" +
                    " | . . 8 | 5 . . | . 1 . |\n" +
                    " | . 9 . | . . . | 4 . . |\n" +
                    " *-----------------------*\n"))
        fileNames.add(fileName)
        assertEquals(resultingGrid, FileManager.readFile(fileName))
    }

    @Test
    fun testWriteFileSPF() {
        val fileName = "testWrite.spf"
        fileNames.add(fileName)
        assertTrue(FileManager.writeFileSPF(fileName, resultingGrid))
        assertEquals(resultingGrid, FileManager.readFile(fileName))
    }

    @Test
    fun testReadFile_unexistingFile() {
        assertNull(FileManager.readFile("unexistingTestFile.txt"))
    }

    @Test
    fun deleteFile_unexistingFile() {
        assertTrue(!FileManager.deleteFile("unexistingTestFile.txt"))
    }
}
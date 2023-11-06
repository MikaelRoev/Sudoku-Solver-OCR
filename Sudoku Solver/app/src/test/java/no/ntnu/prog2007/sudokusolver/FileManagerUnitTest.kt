package no.ntnu.prog2007.sudokusolver

import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test

class FileManagerUnitTest {

    companion object {
        private const val fileName = "testFile.txt"
        private const val content = "this is test content"

        @JvmStatic
        @BeforeClass
        fun testWriteFile() {
            fileName
            FileManager.writeFile(fileName, content)
        }

        @JvmStatic
        @AfterClass
        fun testDeleteFile() {
            assertTrue(FileManager.deleteFile(fileName))
        }
    }

    @Test
    fun testReadFile_txt() {
        assertEquals(content, FileManager.readFile(fileName))
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
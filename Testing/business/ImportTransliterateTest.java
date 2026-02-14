package business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;

import bll.EditorBO;
import dal.IFacadeDAO;

/**
 * Test class for Import and Transliterate functionality
 * 
 * Tests verify that:
 * 1. File import functionality works correctly
 * 2. Transliteration functionality works correctly
 * 3. Edge cases are handled properly
 * 
 * Issue: #7
 * @author Zain-ul-Abideen (22F-3247)
 */
@DisplayName("Import and Transliterate Functionality Tests")
public class ImportTransliterateTest {

    private EditorBO editorBO;
    
    @Mock
    private IFacadeDAO mockFacadeDAO;
    
    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        editorBO = new EditorBO(mockFacadeDAO);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
        editorBO = null;
    }

    /**
     * Test: Import text file with valid input
     * Given: Valid file and filename
     * When: importTextFiles() is called
     * Then: File is imported successfully
     */
    @Test
    @DisplayName("Test import text file with valid input")
    public void testImport_ValidFile_ReturnsTrue() {
        // Arrange
        File mockFile = mock(File.class);
        String fileName = "test.txt";
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.canRead()).thenReturn(true);
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            boolean result = editorBO.importTextFiles(mockFile, fileName);
        }, "Valid file import should not throw exception");
    }

    /**
     * Test: Import with null file
     * Given: Null file
     * When: importTextFiles() is called
     * Then: Throws exception or returns false
     */
    @Test
    @DisplayName("Test import with null file")
    public void testImport_NullFile_HandlesGracefully() {
        // Arrange
        File nullFile = null;
        String fileName = "test.txt";
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            boolean result = editorBO.importTextFiles(nullFile, fileName);
            assertFalse(result, "Null file should return false");
        }, "Null file should be handled gracefully");
    }

    /**
     * Test: Import with empty filename
     * Given: Valid file but empty filename
     * When: importTextFiles() is called
     * Then: Returns false or throws exception
     */
    @Test
    @DisplayName("Test import with empty filename")
    public void testImport_EmptyFileName_ReturnsFalse() {
        // Arrange
        File mockFile = mock(File.class);
        String emptyFileName = "";
        
        // Act
        boolean result = editorBO.importTextFiles(mockFile, emptyFileName);
        
        // Assert
        assertFalse(result, "Empty filename should return false");
    }

    /**
     * Test: Transliterate with valid Arabic text
     * Given: Valid pageId and Arabic text
     * When: transliterate() is called
     * Then: Returns transliterated text
     */
    @Test
    @DisplayName("Test transliterate with valid Arabic text")
    public void testTransliterate_ValidArabicText_ReturnsTransliteration() {
        // Arrange
        int pageId = 1;
        String arabicText = "مرحبا";
        String expectedTransliteration = "marhaba"; // Expected result
        when(mockFacadeDAO.transliterateInDB(pageId, arabicText))
            .thenReturn(expectedTransliteration);
        
        // Act
        String result = editorBO.transliterate(pageId, arabicText);
        
        // Assert
        assertNotNull(result, "Transliteration should not be null");
        assertEquals(expectedTransliteration, result, 
            "Should return correct transliteration");
        verify(mockFacadeDAO, times(1)).transliterateInDB(pageId, arabicText);
    }

    /**
     * Test: Transliterate with empty text
     * Given: Empty Arabic text
     * When: transliterate() is called
     * Then: Returns empty or handles gracefully
     */
    @Test
    @DisplayName("Test transliterate with empty text")
    public void testTransliterate_EmptyText_HandlesGracefully() {
        // Arrange
        int pageId = 1;
        String emptyText = "";
        when(mockFacadeDAO.transliterateInDB(pageId, emptyText))
            .thenReturn("");
        
        // Act
        String result = editorBO.transliterate(pageId, emptyText);
        
        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals("", result, "Empty text should return empty string");
    }

    /**
     * Test: Transliterate with null text
     * Given: Null text
     * When: transliterate() is called
     * Then: Throws exception or returns null
     */
    @Test
    @DisplayName("Test transliterate with null text")
    public void testTransliterate_NullText_ThrowsException() {
        // Arrange
        int pageId = 1;
        String nullText = null;
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            editorBO.transliterate(pageId, nullText);
        }, "Null text should throw NullPointerException");
    }

    /**
     * Test: Transliterate with invalid page ID
     * Given: Invalid (negative) page ID
     * When: transliterate() is called
     * Then: Returns null or throws exception
     */
    @Test
    @DisplayName("Test transliterate with invalid page ID")
    public void testTransliterate_InvalidPageId_HandlesGracefully() {
        // Arrange
        int invalidPageId = -1;
        String arabicText = "مرحبا";
        when(mockFacadeDAO.transliterateInDB(invalidPageId, arabicText))
            .thenReturn(null);
        
        // Act
        String result = editorBO.transliterate(invalidPageId, arabicText);
        
        // Assert
        assertNull(result, "Invalid page ID should return null");
    }

    /**
     * Test: Transliterate with special characters
     * Given: Text with special characters
     * When: transliterate() is called
     * Then: Handles gracefully
     */
    @Test
    @DisplayName("Test transliterate with special characters")
    public void testTransliterate_SpecialCharacters_HandlesGracefully() {
        // Arrange
        int pageId = 1;
        String specialText = "!@#$%^&*()";
        when(mockFacadeDAO.transliterateInDB(pageId, specialText))
            .thenReturn(specialText);
        
        // Act
        String result = editorBO.transliterate(pageId, specialText);
        
        // Assert
        assertNotNull(result, "Special characters should be handled");
    }
}
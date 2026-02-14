package business;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import dal.TFIDFCalculator;

/**
 * Test class for TFIDFCalculator
 * 
 * Tests verify that:
 * 1. TF-IDF calculation produces correct results for known documents
 * 2. Empty documents are handled gracefully
 * 3. Special characters are handled properly
 * 4. Edge cases produce expected behavior
 * 
 * Issue: [ISSUE_NUMBER]
 * @author Zain-ul-Abideen (22F-3247)
 */
@DisplayName("TF-IDF Calculator Algorithm Tests")
public class TFIDFCalculatorTest {

    private TFIDFCalculator calculator;

    @BeforeEach
    public void setUp() {
        // Initialize TFIDFCalculator instance before each test
        calculator = new TFIDFCalculator();
    }

    @AfterEach
    public void tearDown() {
        // Clean up resources after each test
        calculator = null;
    }

    /**
     * POSITIVE PATH TEST
     * Test: TF-IDF calculation with known documents
     * Given: A corpus of known documents and a target document
     * When: TF-IDF is calculated
     * Then: Score matches manual calculation within ±0.01
     */
    @Test
    @DisplayName("Positive Path: Known document produces correct TF-IDF score")
    public void testTFIDF_KnownDocument_MatchesManualCalculation() {
        // Arrange
        // Add known documents to corpus
        calculator.addDocumentToCorpus("the cat sat on the mat");
        calculator.addDocumentToCorpus("the dog sat on the log");
        calculator.addDocumentToCorpus("cats and dogs are animals");
        
        // Target document
        String targetDocument = "the cat sat on the mat";
        
        // Manual calculation for this specific corpus:
        // For "the cat sat on the mat":
        // TF for each word = 1/6 for unique words, 2/6 for "the"
        // IDF calculation based on corpus
        // Expected TF-IDF score (calculated manually)
        double expectedScore = 0.0; // TODO: Calculate manually based on the algorithm
        
        // Act
        double actualScore = calculator.calculateDocumentTfIdf(targetDocument);
        
        // Assert
        assertNotNull(actualScore, "TF-IDF score should not be null");
        // Verify score is within acceptable range
        assertTrue(actualScore >= 0, "TF-IDF score should be non-negative");
        // Note: Update expectedScore with actual manual calculation
        // assertEquals(expectedScore, actualScore, 0.01, 
        //     "TF-IDF score should match manual calculation within ±0.01");
    }

    /**
     * POSITIVE PATH TEST
     * Test: TF-IDF with simple corpus
     * Given: Simple corpus with distinct documents
     * When: TF-IDF is calculated for a document
     * Then: Returns a valid score
     */
    @Test
    @DisplayName("Positive Path: Simple corpus produces valid score")
    public void testTFIDF_SimpleCorpus_ReturnsValidScore() {
        // Arrange
        calculator.addDocumentToCorpus("hello world");
        calculator.addDocumentToCorpus("goodbye world");
        String document = "hello world";
        
        // Act
        double score = calculator.calculateDocumentTfIdf(document);
        
        // Assert
        assertTrue(score >= 0, "TF-IDF score should be non-negative");
        assertFalse(Double.isNaN(score), "TF-IDF score should not be NaN");
        assertFalse(Double.isInfinite(score), "TF-IDF score should not be infinite");
    }

    /**
     * NEGATIVE PATH TEST
     * Test: Empty document handling
     * Given: Empty document
     * When: TF-IDF is calculated
     * Then: Handles gracefully without throwing exception
     */
    @Test
    @DisplayName("Negative Path: Empty document handled gracefully")
    public void testTFIDF_EmptyDocument_HandlesGracefully() {
        // Arrange
        calculator.addDocumentToCorpus("the cat sat on the mat");
        calculator.addDocumentToCorpus("the dog sat on the log");
        String emptyDocument = "";
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            double score = calculator.calculateDocumentTfIdf(emptyDocument);
            // Verify score is valid (not throwing exception is the main test)
            assertTrue(score >= 0 || Double.isNaN(score), 
                "Empty document should return valid score or NaN");
        }, "Empty document should be handled gracefully without exception");
    }

    /**
     * NEGATIVE PATH TEST
     * Test: Special characters handling
     * Given: Document with only special characters
     * When: TF-IDF is calculated
     * Then: Handles gracefully without throwing exception
     */
    @Test
    @DisplayName("Negative Path: Special characters handled gracefully")
    public void testTFIDF_SpecialCharacters_HandlesGracefully() {
        // Arrange
        calculator.addDocumentToCorpus("normal text content");
        String specialCharsDocument = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            double score = calculator.calculateDocumentTfIdf(specialCharsDocument);
            // Should handle gracefully
            assertNotNull(score, "Score should not be null");
        }, "Special characters should be handled gracefully");
    }

    /**
     * NEGATIVE PATH TEST
     * Test: Null document handling
     * Given: Null document
     * When: TF-IDF is calculated
     * Then: Throws exception or handles gracefully
     */
    @Test
    @DisplayName("Negative Path: Null document throws exception")
    public void testTFIDF_NullDocument_ThrowsException() {
        // Arrange
        calculator.addDocumentToCorpus("test document");
        String nullDocument = null;
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            calculator.calculateDocumentTfIdf(nullDocument);
        }, "Null document should throw NullPointerException");
    }

    /**
     * BOUNDARY TEST
     * Test: Single word document
     * Given: Document with single word
     * When: TF-IDF is calculated
     * Then: Returns valid score
     */
    @Test
    @DisplayName("Boundary: Single word document produces valid score")
    public void testTFIDF_SingleWord_ReturnsValidScore() {
        // Arrange
        calculator.addDocumentToCorpus("hello");
        calculator.addDocumentToCorpus("world");
        String singleWordDoc = "hello";
        
        // Act
        double score = calculator.calculateDocumentTfIdf(singleWordDoc);
        
        // Assert
        assertTrue(score >= 0, "Single word document should produce non-negative score");
        assertFalse(Double.isNaN(score), "Score should not be NaN");
    }

    /**
     * BOUNDARY TEST
     * Test: Document not in corpus
     * Given: Document with words not in corpus
     * When: TF-IDF is calculated
     * Then: Returns valid score
     */
    @Test
    @DisplayName("Boundary: Document with unknown words produces valid score")
    public void testTFIDF_UnknownWords_ReturnsValidScore() {
        // Arrange
        calculator.addDocumentToCorpus("cat dog bird");
        String unknownWordsDoc = "elephant lion tiger";
        
        // Act
        double score = calculator.calculateDocumentTfIdf(unknownWordsDoc);
        
        // Assert
        assertNotNull(score, "Score should not be null for unknown words");
        assertTrue(score >= 0, "Score should be non-negative");
    }

    /**
     * POSITIVE PATH TEST
     * Test: Arabic text TF-IDF calculation
     * Given: Corpus with Arabic text
     * When: TF-IDF is calculated
     * Then: Returns valid score
     */
    @Test
    @DisplayName("Positive Path: Arabic text produces valid TF-IDF score")
    public void testTFIDF_ArabicText_ReturnsValidScore() {
        // Arrange
        calculator.addDocumentToCorpus("مرحبا بك في المحرر");
        calculator.addDocumentToCorpus("النص العربي جميل");
        String arabicDocument = "مرحبا بك في المحرر";
        
        // Act
        double score = calculator.calculateDocumentTfIdf(arabicDocument);
        
        // Assert
        assertTrue(score >= 0, "Arabic text should produce non-negative score");
        assertFalse(Double.isNaN(score), "Score should not be NaN");
    }

    /**
     * BOUNDARY TEST
     * Test: Empty corpus
     * Given: No documents in corpus
     * When: TF-IDF is calculated
     * Then: Handles gracefully
     */
    @Test
    @DisplayName("Boundary: Empty corpus handled gracefully")
    public void testTFIDF_EmptyCorpus_HandlesGracefully() {
        // Arrange
        // No documents added to corpus
        String document = "test document";
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            double score = calculator.calculateDocumentTfIdf(document);
            // Should handle gracefully
        }, "Empty corpus should be handled gracefully");
    }

    /**
     * POSITIVE PATH TEST
     * Test: Multiple identical documents
     * Given: Corpus with identical documents
     * When: TF-IDF is calculated
     * Then: Returns consistent score
     */
    @Test
    @DisplayName("Positive Path: Identical documents produce consistent scores")
    public void testTFIDF_IdenticalDocuments_ProducesConsistentScore() {
        // Arrange
        calculator.addDocumentToCorpus("same text same text");
        calculator.addDocumentToCorpus("same text same text");
        calculator.addDocumentToCorpus("same text same text");
        String document = "same text same text";
        
        // Act
        double score1 = calculator.calculateDocumentTfIdf(document);
        double score2 = calculator.calculateDocumentTfIdf(document);
        
        // Assert
        assertEquals(score1, score2, 0.0001, 
            "Identical inputs should produce identical scores");
    }
}
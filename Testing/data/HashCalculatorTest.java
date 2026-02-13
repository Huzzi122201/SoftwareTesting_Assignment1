package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import dal.HashCalculator;

/**
 * Test class for HashCalculator - MD5 Hashing Integrity
 * 
 * Tests verify that:
 * 1. MD5 hash calculation is consistent for same input
 * 2. Different inputs produce different hashes
 * 3. Hash integrity is maintained (simulating original vs current hash)
 * 4. Edge cases are handled (empty, null, special characters)
 * 
 * Issue: #3
 * @author QA Team
 */
@DisplayName("Hash Calculator Integrity Tests")
public class HashCalculatorTest {

    /**
     * Test: Same input produces same hash (consistency)
     * Given: A string input
     * When: Hash is calculated multiple times
     * Then: All hashes are identical
     */
    @Test
    @DisplayName("Test MD5 hash consistency for same input")
    public void testHash_SameInput_ProducesSameHash() throws Exception {
        // Arrange
        String input = "Test content for hashing";

        // Act
        String hash1 = HashCalculator.calculateHash(input);
        String hash2 = HashCalculator.calculateHash(input);
        String hash3 = HashCalculator.calculateHash(input);

        // Assert
        assertNotNull(hash1, "Hash should not be null");
        assertEquals(hash1, hash2, "Hash should be consistent across calls");
        assertEquals(hash2, hash3, "Hash should be consistent across calls");
    }

    /**
     * Test: Different inputs produce different hashes
     * Given: Two different string inputs
     * When: Hashes are calculated
     * Then: Hashes are different
     */
    @Test
    @DisplayName("Test different inputs produce different hashes")
    public void testHash_DifferentInputs_ProduceDifferentHashes() throws Exception {
        // Arrange
        String input1 = "Original content";
        String input2 = "Modified content";

        // Act
        String hash1 = HashCalculator.calculateHash(input1);
        String hash2 = HashCalculator.calculateHash(input2);

        // Assert
        assertNotEquals(hash1, hash2, "Different inputs should produce different hashes");
    }

    /**
     * Test: Hash integrity - original hash preserved, current hash changes
     * Given: Original file content and edited content
     * When: Hashes are calculated for both
     * Then: Original hash differs from current hash (simulating edit scenario)
     */
    @Test
    @DisplayName("Test hash integrity - original vs current hash")
    public void testHashIntegrity_EditedContent_CurrentHashChanges() throws Exception {
        // Arrange - Simulate original import
        String originalContent = "This is the original file content imported from disk";
        String originalHash = HashCalculator.calculateHash(originalContent);
        
        // Act - Simulate user editing the file
        String editedContent = "This is the EDITED file content with changes";
        String currentHash = HashCalculator.calculateHash(editedContent);

        // Assert - Current hash should differ from original
        assertNotEquals(originalHash, currentHash, 
            "Current hash should change after editing content");
        
        // Verify original hash is still valid (can be recalculated)
        String recalculatedOriginalHash = HashCalculator.calculateHash(originalContent);
        assertEquals(originalHash, recalculatedOriginalHash,
            "Original hash should remain unchanged and reproducible");
    }

    /**
     * Test: Empty string produces valid hash
     * Given: Empty string input
     * When: Hash is calculated
     * Then: A valid hash is returned (not null or empty)
     */
    @Test
    @DisplayName("Test hash of empty string")
    public void testHash_EmptyString_ReturnsValidHash() throws Exception {
        // Arrange
        String emptyInput = "";

        // Act
        String hash = HashCalculator.calculateHash(emptyInput);

        // Assert
        assertNotNull(hash, "Hash of empty string should not be null");
        assertFalse(hash.isEmpty(), "Hash of empty string should not be empty");
        assertEquals(32, hash.length(), "MD5 hash should be 32 characters");
    }

    /**
     * Test: Null input handling
     * Given: Null input
     * When: Hash is calculated
     * Then: Exception is thrown
     */
    @Test
    @DisplayName("Test hash of null input throws exception")
    public void testHash_NullInput_ThrowsException() {
        // Arrange
        String nullInput = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            HashCalculator.calculateHash(nullInput);
        }, "Should throw NullPointerException for null input");
    }

    /**
     * Test: Special characters are handled correctly
     * Given: Input with special characters
     * When: Hash is calculated
     * Then: Valid hash is returned
     */
    @Test
    @DisplayName("Test hash with special characters")
    public void testHash_SpecialCharacters_ReturnsValidHash() throws Exception {
        // Arrange
        String specialInput = "!@#$%^&*()_+-=[]{}|;':\",./<>?";

        // Act
        String hash = HashCalculator.calculateHash(specialInput);

        // Assert
        assertNotNull(hash, "Hash should not be null for special characters");
        assertEquals(32, hash.length(), "MD5 hash should be 32 characters");
    }

    /**
     * Test: Arabic text is hashed correctly
     * Given: Arabic text input
     * When: Hash is calculated
     * Then: Valid hash is returned
     */
    @Test
    @DisplayName("Test hash with Arabic text")
    public void testHash_ArabicText_ReturnsValidHash() throws Exception {
        // Arrange
        String arabicInput = "مرحبا بك في المحرر النصي العربي";

        // Act
        String hash = HashCalculator.calculateHash(arabicInput);

        // Assert
        assertNotNull(hash, "Hash should not be null for Arabic text");
        assertEquals(32, hash.length(), "MD5 hash should be 32 characters");
    }

    /**
     * Test: Very long string is hashed correctly
     * Given: Very long string input (>10000 characters)
     * When: Hash is calculated
     * Then: Valid hash is returned
     */
    @Test
    @DisplayName("Test hash with very long string")
    public void testHash_VeryLongString_ReturnsValidHash() throws Exception {
        // Arrange
        StringBuilder longInput = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            longInput.append("a");
        }

        // Act
        String hash = HashCalculator.calculateHash(longInput.toString());

        // Assert
        assertNotNull(hash, "Hash should not be null for long string");
        assertEquals(32, hash.length(), "MD5 hash should be 32 characters");
    }

    /**
     * Test: Hash length is correct for MD5 (32 hex characters)
     * Given: Any input string
     * When: MD5 hash is calculated
     * Then: Hash length is exactly 32 characters
     */
    @Test
    @DisplayName("Test MD5 hash length is 32 characters")
    public void testHash_AnyInput_Returns32Characters() throws Exception {
        // Arrange
        String input = "Test content";

        // Act
        String hash = HashCalculator.calculateHash(input);

        // Assert
        assertEquals(32, hash.length(), "MD5 hash should be exactly 32 characters long");
    }

    /**
     * Test: Hash contains only hexadecimal characters
     * Given: Any input string
     * When: Hash is calculated
     * Then: Hash contains only 0-9 and A-F characters
     */
    @Test
    @DisplayName("Test hash contains only hexadecimal characters")
    public void testHash_AnyInput_ContainsOnlyHexCharacters() throws Exception {
        // Arrange
        String input = "Test content with special chars !@#$";

        // Act
        String hash = HashCalculator.calculateHash(input);

        // Assert
        assertTrue(hash.matches("[0-9A-F]+"), 
            "Hash should contain only hexadecimal characters (0-9, A-F)");
    }

    /**
     * Test: Minor content change produces completely different hash
     * Given: Two strings differing by one character
     * When: Hashes are calculated
     * Then: Hashes are completely different (avalanche effect)
     */
    @Test
    @DisplayName("Test avalanche effect - minor change produces different hash")
    public void testHash_MinorChange_ProducesDifferentHash() throws Exception {
        // Arrange
        String content1 = "This is a test";
        String content2 = "This is a Test"; // Only capital T difference

        // Act
        String hash1 = HashCalculator.calculateHash(content1);
        String hash2 = HashCalculator.calculateHash(content2);

        // Assert
        assertNotEquals(hash1, hash2, 
            "Even minor changes should produce completely different hashes");
    }
}

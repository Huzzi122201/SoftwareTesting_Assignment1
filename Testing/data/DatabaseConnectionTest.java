package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import dal.DatabaseConnection;

/**
 * Test class for DatabaseConnection Singleton pattern
 * 
 * Tests verify that:
 * 1. Only one instance of DatabaseConnection exists
 * 2. getInstance() always returns the same instance
 * 3. Connection is thread-safe
 * 
 * Issue: #1
 * @author QA Team
 */
@DisplayName("Database Connection Singleton Tests")
public class DatabaseConnectionTest {

    /**
     * Test: Singleton pattern ensures same instance is returned
     * Given: DatabaseConnection class with getInstance() method
     * When: getInstance() is called multiple times
     * Then: All calls return the same instance
     */
    @Test
    @DisplayName("Test getInstance() returns same instance")
    public void testSingleton_MultipleGetInstance_ReturnsSameInstance() {
        // Arrange & Act
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();

        // Assert
        assertNotNull(instance1, "First instance should not be null");
        assertNotNull(instance2, "Second instance should not be null");
        assertSame(instance1, instance2, "Both instances should be the same object");
    }

    /**
     * Test: Singleton is thread-safe
     * Given: Multiple threads accessing getInstance()
     * When: Threads call getInstance() concurrently
     * Then: All threads get the same instance
     */
    @Test
    @DisplayName("Test singleton is thread-safe")
    public void testSingleton_ConcurrentAccess_ReturnsSameInstance() throws InterruptedException {
        // Arrange
        final DatabaseConnection[] instances = new DatabaseConnection[10];
        Thread[] threads = new Thread[10];

        // Act - Create multiple threads that get instance
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                instances[index] = DatabaseConnection.getInstance();
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - All instances should be the same
        DatabaseConnection firstInstance = instances[0];
        for (int i = 1; i < instances.length; i++) {
            assertSame(firstInstance, instances[i], 
                "Instance " + i + " should be the same as first instance");
        }
    }

    /**
     * Test: Database connection is not null
     * Given: DatabaseConnection singleton
     * When: getInstance() is called
     * Then: Connection object is not null
     */
    @Test
    @DisplayName("Test connection is not null")
    public void testConnection_GetInstance_ConnectionNotNull() {
        // Arrange & Act
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();

        // Assert
        assertNotNull(dbConnection, "Database connection should not be null");
        assertNotNull(dbConnection.getConnection(), "SQL Connection should not be null");
    }

    /**
     * Test: Multiple getInstance() calls don't create new instances
     * Given: DatabaseConnection singleton
     * When: getInstance() is called 100 times
     * Then: All calls return the same instance
     */
    @Test
    @DisplayName("Test multiple getInstance() calls return same instance")
    public void testSingleton_HundredCalls_ReturnsSameInstance() {
        // Arrange
        DatabaseConnection firstInstance = DatabaseConnection.getInstance();

        // Act & Assert
        for (int i = 0; i < 100; i++) {
            DatabaseConnection instance = DatabaseConnection.getInstance();
            assertSame(firstInstance, instance, 
                "Call " + i + " should return the same instance");
        }
    }
}

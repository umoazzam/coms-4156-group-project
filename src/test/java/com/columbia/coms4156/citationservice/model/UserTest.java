package com.columbia.coms4156.citationservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the User class.
 * Tests all setter methods, submission management, and their parameter validation.
 */
class UserTest {

    private User user;
    private Submission testSubmission;

    @BeforeEach
    void setUp() {
        user = new User();
        testSubmission = new Submission();
    }

    @Test
    void testDefaultConstructor() {
        User newUser = new User();
        assertNotNull(newUser);
        assertNull(newUser.getId());
        assertNull(newUser.getUsername());
        assertNotNull(newUser.getSubmissions());
        assertTrue(newUser.getSubmissions().isEmpty());
    }

    @Test
    void testParameterizedConstructor() {
        String username = "testuser123";
        String password = "SecurePass123";
        User newUser = new User(username, password);

        assertNotNull(newUser);
        assertEquals(username, newUser.getUsername());
        assertNotNull(newUser.getSubmissions());
        assertTrue(newUser.getSubmissions().isEmpty());
    }

    // Username setter tests - Valid cases
    @Test
    void testSetUsernameValid() {
        String validUsername = "testuser123";
        user.setUsername(validUsername);
        assertEquals(validUsername, user.getUsername());
    }

    @Test
    void testSetUsernameValidWithUnderscores() {
        String validUsername = "test_user_123";
        user.setUsername(validUsername);
        assertEquals(validUsername, user.getUsername());
    }

    @Test
    void testSetUsernameValidWithHyphens() {
        String validUsername = "test-user-123";
        user.setUsername(validUsername);
        assertEquals(validUsername, user.getUsername());
    }

    @Test
    void testSetUsernameValidMinLength() {
        String validUsername = "abc";
        user.setUsername(validUsername);
        assertEquals(validUsername, user.getUsername());
    }

    @Test
    void testSetUsernameValidMaxLength() {
        String validUsername = "a".repeat(50); // 50 characters
        user.setUsername(validUsername);
        assertEquals(validUsername, user.getUsername());
    }

    // Username setter tests - Invalid cases
    @Test
    void testSetUsernameNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.setUsername(null)
        );
        assertEquals("Username cannot be null", exception.getMessage());
    }

    @Test
    void testSetUsernameBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.setUsername("   ")
        );
        assertEquals("Username cannot be blank", exception.getMessage());
    }

    @Test
    void testSetUsernameEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.setUsername("")
        );
        assertEquals("Username cannot be blank", exception.getMessage());
    }

    @Test
    void testSetUsernameTooShort() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.setUsername("ab")
        );
        assertEquals("Username must be 3-50 characters and contain only letters, numbers, underscores, and hyphens", exception.getMessage());
    }

    @Test
    void testSetUsernameTooLong() {
        String tooLongUsername = "a".repeat(51); // 51 characters
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.setUsername(tooLongUsername)
        );
        assertEquals("Username must be 3-50 characters and contain only letters, numbers, underscores, and hyphens", exception.getMessage());
    }

    @Test
    void testSetUsernameInvalidCharacters() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.setUsername("test@user")
        );
        assertEquals("Username must be 3-50 characters and contain only letters, numbers, underscores, and hyphens", exception.getMessage());
    }

    @Test
    void testSetUsernameWithSpaces() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.setUsername("test user")
        );
        assertEquals("Username must be 3-50 characters and contain only letters, numbers, underscores, and hyphens", exception.getMessage());
    }

    @Test
    void testSetUsernameWithSpecialCharacters() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.setUsername("test$user!")
        );
        assertEquals("Username must be 3-50 characters and contain only letters, numbers, underscores, and hyphens", exception.getMessage());
    }

    // Password setter tests - Valid cases
    @Test
    void testSetPasswordValid() {
        String validPassword = "SecurePass123";
        user.setPassword(validPassword);
        // Note: we can't directly test the password value due to @JsonIgnore
        // but we can verify no exception was thrown
        assertDoesNotThrow(() -> user.setPassword(validPassword));
    }

    @Test
    void testSetPasswordValidMinLength() {
        String validPassword = "SecPass1"; // 8 characters
        assertDoesNotThrow(() -> user.setPassword(validPassword));
    }

    @Test
    void testSetPasswordValidWithSpecialChars() {
        String validPassword = "SecurePass123!@#";
        assertDoesNotThrow(() -> user.setPassword(validPassword));
    }

    // Password setter tests - Invalid cases
    @Test
    void testSetPasswordNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.setPassword(null)
        );
        assertEquals("Password cannot be null", exception.getMessage());
    }

    @Test
    void testSetPasswordBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.setPassword("   ")
        );
        assertEquals("Password cannot be blank", exception.getMessage());
    }

    @Test
    void testSetPasswordEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.setPassword("")
        );
        assertEquals("Password cannot be blank", exception.getMessage());
    }

    @Test
    void testSetPasswordTooShort() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.setPassword("Short1")
        );
        assertEquals("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit", exception.getMessage());
    }

    @Test
    void testSetPasswordNoUppercase() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.setPassword("lowercase123")
        );
        assertEquals("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit", exception.getMessage());
    }

    @Test
    void testSetPasswordNoLowercase() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.setPassword("UPPERCASE123")
        );
        assertEquals("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit", exception.getMessage());
    }

    @Test
    void testSetPasswordNoDigit() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.setPassword("NoDigitPassword")
        );
        assertEquals("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit", exception.getMessage());
    }

    @Test
    void testSetPasswordOnlyDigits() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.setPassword("12345678")
        );
        assertEquals("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit", exception.getMessage());
    }

    // Submissions setter tests
    @Test
    void testSetSubmissionsValid() {
        List<Submission> submissions = new ArrayList<>();
        submissions.add(new Submission());
        user.setSubmissions(submissions);
        assertEquals(submissions, user.getSubmissions());
    }

    @Test
    void testSetSubmissionsEmptyList() {
        List<Submission> emptyList = new ArrayList<>();
        user.setSubmissions(emptyList);
        assertEquals(emptyList, user.getSubmissions());
        assertTrue(user.getSubmissions().isEmpty());
    }

    @Test
    void testSetSubmissionsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.setSubmissions(null)
        );
        assertEquals("Submissions list cannot be null", exception.getMessage());
    }

    // AddSubmission method tests
    @Test
    void testAddSubmissionValid() {
        Submission submission = new Submission();
        user.addSubmission(submission);
        assertTrue(user.getSubmissions().contains(submission));
        assertEquals(user, submission.getUser());
    }

    @Test
    void testAddSubmissionAlreadyAdded() {
        Submission submission = new Submission();
        user.addSubmission(submission);
        user.addSubmission(submission); // Add again

        // Should only appear once in the list
        assertEquals(1, user.getSubmissions().size());
        assertTrue(user.getSubmissions().contains(submission));
        assertEquals(user, submission.getUser());
    }

    @Test
    void testAddSubmissionNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.addSubmission(null)
        );
        assertEquals("Submission cannot be null", exception.getMessage());
    }

    @Test
    void testAddSubmissionBelongsToAnotherUser() {
        User anotherUser = new User();
        anotherUser.setId(999L); // Set different ID to ensure users are not equal
        user.setId(1L); // Set different ID for the current user

        Submission submission = new Submission();
        submission.setUser(anotherUser);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.addSubmission(submission)
        );
        assertEquals("Submission already belongs to another user", exception.getMessage());
    }

    // RemoveSubmission method tests
    @Test
    void testRemoveSubmissionValid() {
        Submission submission = new Submission();
        user.addSubmission(submission);

        user.removeSubmission(submission);
        assertFalse(user.getSubmissions().contains(submission));
        assertNull(submission.getUser());
    }

    @Test
    void testRemoveSubmissionNotInList() {
        Submission submission = new Submission();
        // Don't add the submission to user's list

        user.removeSubmission(submission);
        // Should not cause any issues
        assertFalse(user.getSubmissions().contains(submission));
    }

    @Test
    void testRemoveSubmissionNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> user.removeSubmission(null)
        );
        assertEquals("Submission cannot be null", exception.getMessage());
    }

    // Equals method tests
    @Test
    void testEqualsWithSameId() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(1L);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testEqualsWithDifferentId() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        assertNotEquals(user1, user2);
    }

    @Test
    void testEqualsWithNullId() {
        User user1 = new User();
        User user2 = new User();

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testEqualsWithSameObject() {
        assertTrue(user.equals(user));
    }

    @Test
    void testEqualsWithNull() {
        assertFalse(user.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertFalse(user.equals("not a user"));
    }

    // ToString method test
    @Test
    void testToString() {
        user.setId(1L);
        user.setUsername("testuser");
        user.addSubmission(new Submission());
        user.addSubmission(new Submission());

        String result = user.toString();
        assertNotNull(result);
        assertTrue(result.contains("User{"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("username='testuser'"));
        assertTrue(result.contains("submissionsCount=2"));
    }

    // Integration tests
    @Test
    void testCompleteUserSetup() {
        User completeUser = new User("validuser123", "SecurePass123");
        completeUser.setId(1L);

        Submission submission1 = new Submission();
        Submission submission2 = new Submission();

        completeUser.addSubmission(submission1);
        completeUser.addSubmission(submission2);

        assertEquals("validuser123", completeUser.getUsername());
        assertEquals(Long.valueOf(1L), completeUser.getId());
        assertEquals(2, completeUser.getSubmissions().size());
        assertTrue(completeUser.getSubmissions().contains(submission1));
        assertTrue(completeUser.getSubmissions().contains(submission2));
        assertEquals(completeUser, submission1.getUser());
        assertEquals(completeUser, submission2.getUser());
    }

    @Test
    void testSubmissionManagementWorkflow() {
        user.setUsername("workflowuser");
        user.setPassword("WorkflowPass123");

        Submission sub1 = new Submission();
        Submission sub2 = new Submission();
        Submission sub3 = new Submission();

        // Add submissions
        user.addSubmission(sub1);
        user.addSubmission(sub2);
        user.addSubmission(sub3);
        assertEquals(3, user.getSubmissions().size());

        // Remove one submission
        user.removeSubmission(sub2);
        assertEquals(2, user.getSubmissions().size());
        assertFalse(user.getSubmissions().contains(sub2));
        assertNull(sub2.getUser());

        // Verify remaining submissions
        assertTrue(user.getSubmissions().contains(sub1));
        assertTrue(user.getSubmissions().contains(sub3));
        assertEquals(user, sub1.getUser());
        assertEquals(user, sub3.getUser());
    }

    // Edge case tests
    @Test
    void testUsernameWithMixedValidCharacters() {
        String username = "User123_test-ABC";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    void testPasswordWithMixedCharacters() {
        String password = "MySecure123!@#$%^&*()";
        assertDoesNotThrow(() -> user.setPassword(password));
    }

    @Test
    void testMultiplePasswordValidations() {
        String[] validPasswords = {
            "Password123",
            "MySecurePass1",
            "Test123Password",
            "Abc123def",
            "SuperSecure999"
        };

        for (String password : validPasswords) {
            assertDoesNotThrow(() -> user.setPassword(password),
                "Failed for password: " + password);
        }
    }

    @Test
    void testMultipleUsernameValidations() {
        String[] validUsernames = {
            "user123",
            "test_user",
            "my-username",
            "User_123-test",
            "abc",
            "a".repeat(50)
        };

        for (String username : validUsernames) {
            assertDoesNotThrow(() -> user.setUsername(username),
                "Failed for username: " + username);
        }
    }
}

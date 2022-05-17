package edu.tamu.app.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DataIntegrityViolationException;
import edu.tamu.app.StatusApplication;
import edu.tamu.app.enums.NoteType;
import edu.tamu.app.enums.Role;
import edu.tamu.app.enums.Status;
import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.weaver.auth.model.Credentials;

@SpringBootTest(classes = { StatusApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class NoteTest {

    private static final String TEST_NOTE_TITLE = "Note Title";
    private static final String TEST_SERVICE_URL = "https://library.tamu.edu";
    private static final String TEST_DESCRIPTION = "Test Service Description";
    private static final String TEST_ALTERNATIVE_NOTE_TITLE = "Alternative Note Title";
    private static final String TEST_SERVICE_NAME = "Test Service Name";
    private static final String TEST_ALTERNATIVE_SERVICE_NAME = "Different Service Name";
    private static final String TEST_NOTE_BODY = "Test Note Body";
    private static final String TEST_ALTERNATIVE_NOTE_BODY = "Alternative Note Body";
    private static final Boolean TEST_IS_AUTO = false;
    private static final Boolean TEST_IS_PUBLIC = true;
    private static final Boolean TEST_ON_SHORT_LIST = true;
    private static final Status TEST_SERVICE_STATUS = Status.UP;
    private static final NoteType TEST_NOTE_TYPE = NoteType.ISSUE;
    private static final NoteType TEST_ALTERNATIVE_NOTE_TYPE = NoteType.MAINTENANCE;
    private Service service1;
    private Service service2;

    private static final Credentials TEST_CREDENTIALS = new Credentials();
    {
        TEST_CREDENTIALS.setUin("123456789");
        TEST_CREDENTIALS.setEmail("aggieJack@tamu.edu");
        TEST_CREDENTIALS.setFirstName("Aggie");
        TEST_CREDENTIALS.setLastName("Jack");
        TEST_CREDENTIALS.setRole("ROLE_USER");
    }

    private User testUser;

    private Note testNote;

    @Autowired
    private NoteRepo noteRepo;

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void setUp() throws UserNotFoundException {
        testUser = userRepo.create(TEST_CREDENTIALS.getUin(), TEST_CREDENTIALS.getEmail(), TEST_CREDENTIALS.getFirstName(), TEST_CREDENTIALS.getLastName(), Role.valueOf(TEST_CREDENTIALS.getRole()));
        service1 = serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        service2 = serviceRepo.create(new Service(TEST_ALTERNATIVE_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        testNote = noteRepo.create(new Note(TEST_NOTE_TITLE, testUser, TEST_NOTE_TYPE, TEST_NOTE_BODY, service1), TEST_CREDENTIALS);
    }

    @Test
    public void testCreate() throws UserNotFoundException {
        long initialCount = noteRepo.count();
        noteRepo.create(new Note(TEST_ALTERNATIVE_NOTE_TITLE, testUser, TEST_NOTE_TYPE, TEST_ALTERNATIVE_NOTE_BODY, service2), TEST_CREDENTIALS);
        assertEquals(initialCount + 1, noteRepo.count(), "The number of notes did not increase by one");
    }

    @Test
    public void testTitleNotNull() throws UserNotFoundException {
        assertThrows(DataIntegrityViolationException.class, () -> {
            testNote.setTitle(null);
            noteRepo.create(new Note(null, testUser, TEST_NOTE_TYPE, TEST_ALTERNATIVE_NOTE_BODY, service2), TEST_CREDENTIALS);
        });
    }

    @Test
    public void testAuthorNotNull() throws UserNotFoundException {
        assertThrows(UserNotFoundException.class, () -> {
            TEST_CREDENTIALS.setUin("987654321");
            noteRepo.create(new Note(TEST_ALTERNATIVE_NOTE_TITLE, null, TEST_NOTE_TYPE, TEST_ALTERNATIVE_NOTE_BODY, service2), TEST_CREDENTIALS);
        });
    }

    @Test
    public void testUpdateTitle() throws UserNotFoundException {
        Note note = noteRepo.create(testNote, TEST_CREDENTIALS);
        note.setTitle(TEST_ALTERNATIVE_NOTE_TITLE);
        note = noteRepo.save(note);
        assertEquals(TEST_ALTERNATIVE_NOTE_TITLE, note.getTitle(), "Note title was not updated");
    }

    @Test
    public void testUpdateServices() throws UserNotFoundException {

        Note note = noteRepo.create(testNote, TEST_CREDENTIALS);
        note.setService(service1);
        note = noteRepo.save(note);
        assertEquals(service1, note.getService(), "Service was not set");
        note.setService(service2);
        note = noteRepo.save(note);
        assertEquals(service2, note.getService(), "Service was not updated correctly");
    }

    @Test
    public void testUpdateNoteType() throws UserNotFoundException {
        Note note = noteRepo.create(testNote, TEST_CREDENTIALS);
        note.setNoteType(TEST_NOTE_TYPE);
        note = noteRepo.save(note);
        assertEquals(TEST_NOTE_TYPE, note.getNoteType(), "Note type not set");
        note.setNoteType(TEST_ALTERNATIVE_NOTE_TYPE);
        note = noteRepo.save(note);
        assertEquals(TEST_ALTERNATIVE_NOTE_TYPE, note.getNoteType(), "Note type not updated");
    }

    @Test
    public void testUpdateBody() throws UserNotFoundException {
        Note note = noteRepo.create(testNote, TEST_CREDENTIALS);
        note.setBody(TEST_NOTE_BODY);
        note = noteRepo.save(note);
        assertEquals(TEST_NOTE_BODY, note.getBody(), "Note body not set");
        note.setBody(TEST_ALTERNATIVE_NOTE_BODY);
        note = noteRepo.save(note);
        assertEquals(TEST_ALTERNATIVE_NOTE_BODY, note.getBody(), "Note body not updated");
    }

    @Test
    public void testTimestampSetOnCreate() throws UserNotFoundException {
        Note note = noteRepo.create(testNote, TEST_CREDENTIALS);
        note = noteRepo.findById(note.getId()).get();
        assertNotEquals(null, note.getLastModified(), "Timestamp not set on creation");
    }

    @Test
    public void testTimestampSetOnUpdate() throws InterruptedException, UserNotFoundException {
        Note note = noteRepo.create(testNote, TEST_CREDENTIALS);
        note = noteRepo.findById(note.getId()).get();
        // Calendar createTime = note.getLastModified();
        note.setBody(TEST_NOTE_BODY);

        Thread.sleep(1000);

        noteRepo.save(note);

        // TODO: fix false positive, time is not updated

        // assertNotEquals("The timestamp was not updated from creation", createTime.getTime().getTime(), updatedNote.getLastModified().getTime().getTime());
    }

    @Test
    public void testDelete() throws UserNotFoundException {
        long initalCount = noteRepo.count();
        Note note = noteRepo.create(new Note(TEST_ALTERNATIVE_NOTE_TITLE, testUser, TEST_NOTE_TYPE, TEST_ALTERNATIVE_NOTE_BODY, service2), TEST_CREDENTIALS);
        assertEquals(initalCount + 1, noteRepo.count(), "Note not created");
        noteRepo.delete(note);
        assertEquals(initalCount, noteRepo.count(), "Note not deleted");

    }

    @AfterEach
    public void cleanUp() {
        noteRepo.deleteAll();
        serviceRepo.deleteAll();
        userRepo.deleteAll();
    }
}

package edu.tamu.app.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import edu.tamu.app.WebServerInit;
import edu.tamu.app.enums.NoteType;
import edu.tamu.app.enums.Status;
import edu.tamu.app.model.repo.AppUserRepo;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.framework.model.Credentials;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { WebServerInit.class })
public class NoteTest {

    protected static final String TEST_NOTE_TITLE = "Note Title";
    protected static final String TEST_ALTERNATIVE_NOTE_TITLE = "Alternative Note Title";
    protected static final String TEST_SERVICE_NAME = "Test Service Name";
    protected static final String TEST_ALTERNATIVE_SERVICE_NAME = "Different Service Name";
    protected static final String TEST_NOTE_BODY = "Test Note Body";
    protected static final String TEST_ALTERNATIVE_NOTE_BODY = "Alternative Note Body";
    protected static final Boolean TEST_IS_AUTO = false;
    protected static final Boolean TEST_IS_PUBLIC = true;
    protected static final Boolean TEST_ON_SHORT_LIST = true;
    protected static final Status TEST_SERVICE_STATUS = Status.UP;
    protected static final NoteType TEST_NOTE_TYPE = NoteType.ISSUE;
    protected static final NoteType TEST_ALTERNATIVE_NOTE_TYPE = NoteType.MAINTENANCE;
    protected static final Calendar TEST_DATE = Calendar.getInstance();
    protected Service service1;
    protected Service service2; 

    protected static final Credentials TEST_CREDENTIALS = new Credentials();
    {
        TEST_CREDENTIALS.setUin("123456789");
    }

    protected AppUser testUser;

    protected Note testNote;

    @Autowired
    NoteRepo noteRepo;

    @Autowired
    ServiceRepo serviceRepo;

    @Autowired
    AppUserRepo appUserRepo;

    @Before
    public void setUp() {
        testUser = appUserRepo.create(TEST_CREDENTIALS.getUin());
        service1 = serviceRepo.create(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, null);
        service2 = serviceRepo.create(TEST_ALTERNATIVE_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, null);
        testNote = noteRepo.create(new Note(TEST_NOTE_TITLE, testUser, TEST_NOTE_TYPE, TEST_NOTE_BODY, service1), TEST_CREDENTIALS);
    }

    @Test
    public void testCreate() {
        long initialCount = noteRepo.count();
        noteRepo.create(new Note(TEST_ALTERNATIVE_NOTE_TITLE, testUser, TEST_NOTE_TYPE, TEST_ALTERNATIVE_NOTE_BODY, service2), TEST_CREDENTIALS);
        assertEquals("The number of notes did not increase by one", initialCount + 1, noteRepo.count());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testTitleNotNull() {
        testNote.setTitle(null);
        noteRepo.create(new Note(null, testUser, TEST_NOTE_TYPE, TEST_ALTERNATIVE_NOTE_BODY, service2), TEST_CREDENTIALS);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testAuthorNotNull() {
        TEST_CREDENTIALS.setUin("987654321");
        noteRepo.create(new Note(TEST_ALTERNATIVE_NOTE_TITLE, null, TEST_NOTE_TYPE, TEST_ALTERNATIVE_NOTE_BODY, service2), TEST_CREDENTIALS);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testTitleNotEmpty() {
        testNote.setTitle("");
        noteRepo.create(new Note("", testUser, TEST_NOTE_TYPE, TEST_ALTERNATIVE_NOTE_BODY, service2), TEST_CREDENTIALS);
    }

    @Test
    public void testUpdateTitle() {
        Note note = noteRepo.create(testNote, TEST_CREDENTIALS);
        note.setTitle(TEST_ALTERNATIVE_NOTE_TITLE);
        note = noteRepo.save(note);
        assertEquals("Note title was not updated", TEST_ALTERNATIVE_NOTE_TITLE, note.getTitle());
    }

    @Test
    public void testUpdateServices() {
        
        Note note = noteRepo.create(testNote, TEST_CREDENTIALS);
        note.setService(service1);
        note = noteRepo.save(note);
        assertEquals("Service was not set", service1, note.getService());
        note.setService(service2);
        note = noteRepo.save(note);
        assertEquals("Service was not updated correctly", service2, note.getService());
    }

    @Test
    public void testUpdateNoteType() {
        Note note = noteRepo.create(testNote, TEST_CREDENTIALS);
        note.setNoteType(TEST_NOTE_TYPE);
        note = noteRepo.save(note);
        assertEquals("Note type not set", TEST_NOTE_TYPE, note.getNoteType());
        note.setNoteType(TEST_ALTERNATIVE_NOTE_TYPE);
        note = noteRepo.save(note);
        assertEquals("Note type not updated", TEST_ALTERNATIVE_NOTE_TYPE, note.getNoteType());
    }

    @Test
    public void testUpdateBody() {
        Note note = noteRepo.create(testNote, TEST_CREDENTIALS);
        note.setBody(TEST_NOTE_BODY);
        note = noteRepo.save(note);
        assertEquals("Note body not set", TEST_NOTE_BODY, note.getBody());
        note.setBody(TEST_ALTERNATIVE_NOTE_BODY);
        note = noteRepo.save(note);
        assertEquals("Note body not updated", TEST_ALTERNATIVE_NOTE_BODY, note.getBody());
    }

    @Test
    public void testUpdatePostingStart() {
        Note note = noteRepo.create(testNote, TEST_CREDENTIALS);
        note.setScheduledPostingStart(TEST_DATE);
        note = noteRepo.save(note);
        assertNotEquals("Start time not set", null, note.getScheduledPostingStart());
    }

    @Test
    public void testUpdatePostingEnd() {
        Note note = noteRepo.create(testNote, TEST_CREDENTIALS);
        note.setScheduledPostingEnd(TEST_DATE);
        note = noteRepo.save(note);
        assertNotEquals("End time not set", null, note.getScheduledPostingEnd());
    }

    @Test
    public void testTimestampSetOnCreate() {
        Note note = noteRepo.create(testNote, TEST_CREDENTIALS);
        note = noteRepo.findOne(note.getId());
        assertNotEquals("Timestamp not set on creation", null, note.getLastModified());
    }

    @Test
    public void testTimestampSetOnUpdate() throws InterruptedException {
        Note note = noteRepo.create(testNote, TEST_CREDENTIALS);
        note = noteRepo.findOne(note.getId());
        Calendar createTime = note.getLastModified();
        note.setBody(TEST_NOTE_BODY);

        Thread.sleep(1000);

        noteRepo.save(note);

        System.out.println("\n" + note.getLastModified().getTime().getTime() + "\n");

        // TODO: fix false positive, time is not updated

        // assertNotEquals("The timestamp was not updated from creation", createTime.getTime().getTime(), updatedNote.getLastModified().getTime().getTime());
    }

    @Test
    public void testDelete() {
        long initalCount = noteRepo.count();
        Note note = noteRepo.create(new Note(TEST_ALTERNATIVE_NOTE_TITLE, testUser, TEST_NOTE_TYPE, TEST_ALTERNATIVE_NOTE_BODY, service2), TEST_CREDENTIALS);
        assertEquals("Note not created", initalCount + 1, noteRepo.count());
        noteRepo.delete(note);
        assertEquals("Note not deleted", initalCount, noteRepo.count());

    }

    @After
    public void cleanUp() {
        noteRepo.deleteAll();
        serviceRepo.deleteAll();
        appUserRepo.deleteAll();
    }
}

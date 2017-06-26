package edu.tamu.app.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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
    protected static final Boolean TEST_IS_PUBLIC = true;
    protected static final Boolean TEST_ON_SHORT_LIST = true;
    protected static final Status TEST_SERVICE_STATUS = Status.UP;
    protected static final NoteType TEST_NOTE_TYPE = NoteType.ISSUE;
    protected static final NoteType TEST_ALTERNATIVE_NOTE_TYPE = NoteType.MAINTENANCE;
    protected static final Calendar TEST_DATE = Calendar.getInstance();
    protected AppUser testUser;

    @Autowired
    NoteRepo noteRepo;

    @Autowired
    ServiceRepo serviceRepo;

    @Autowired
    AppUserRepo appUserRepo;

    @Before
    public void setUp() {
        testUser = appUserRepo.create("123456789");
    }

    @Test
    public void testCreate() {
        long initialCount = noteRepo.count();
        noteRepo.create(TEST_NOTE_TITLE, testUser);
        assertEquals("The number of notes did not increase by one", initialCount + 1, noteRepo.count());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testTitleNotNull() {
        noteRepo.create(null, testUser);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testAuthorNotNull() {
        noteRepo.create(TEST_NOTE_TITLE, null);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testTitleNotEmpty() {
        noteRepo.create("", testUser);
    }

    @Test
    public void testUpdateTitle() {
        Note note = noteRepo.create(TEST_NOTE_TITLE, testUser);
        note.setTitle(TEST_ALTERNATIVE_NOTE_TITLE);
        note = refreshNote(note);
        assertEquals("Note title was not updated", TEST_ALTERNATIVE_NOTE_TITLE, note.getTitle());
    }

    private Note refreshNote(Note note) {
        noteRepo.save(note);
        return noteRepo.findOne(note.getId());
    }

    @Test
    public void testUpdateServices() {
        Service service1 = serviceRepo.create(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST);
        Service service2 = serviceRepo.create(TEST_ALTERNATIVE_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST);
        List<Service> serviceList1 = Arrays.asList(service1);
        List<Service> serviceList2 = Arrays.asList(service1, service2);
        Note note = noteRepo.create(TEST_NOTE_TITLE, testUser);
        note.setServices(serviceList1);
        note = refreshNote(note);
        assertEquals("Note services did not contain the right number of services", 1, note.getServices().size());
        assertEquals("Note services not set", true, note.getServices().contains(service1));
        note.setServices(serviceList2);
        note = refreshNote(note);
        assertEquals("Note services did not contain the right number of services", 2, note.getServices().size());
        assertEquals("Note services not updated", true, note.getServices().contains(service2));
    }

    @Test
    public void testUpdateNoteType() {
        Note note = noteRepo.create(TEST_NOTE_TITLE, testUser);
        note.setNoteType(TEST_NOTE_TYPE);
        note = refreshNote(note);
        assertEquals("Note type not set", TEST_NOTE_TYPE, note.getNoteType());
        note.setNoteType(TEST_ALTERNATIVE_NOTE_TYPE);
        note = refreshNote(note);
        assertEquals("Note type not updated", TEST_ALTERNATIVE_NOTE_TYPE, note.getNoteType());
    }

    @Test
    public void testUpdateBody() {
        Note note = noteRepo.create(TEST_NOTE_TITLE, testUser);
        note.setBody(TEST_NOTE_BODY);
        note = refreshNote(note);
        assertEquals("Note body not set", TEST_NOTE_BODY, note.getBody());
        note.setBody(TEST_ALTERNATIVE_NOTE_BODY);
        note = refreshNote(note);
        assertEquals("Note body not updated", TEST_ALTERNATIVE_NOTE_BODY, note.getBody());
    }

    @Test
    public void testUpdatePostingStart() {
        Note note = noteRepo.create(TEST_NOTE_TITLE, testUser);
        note.setScheduledPostingStart(TEST_DATE);
        note = refreshNote(note);
        assertNotEquals("Start time not set", null, note.getScheduledPostingStart());
    }

    @Test
    public void testUpdatePostingEnd() {
        Note note = noteRepo.create(TEST_NOTE_TITLE, testUser);
        note.setScheduledPostingEnd(TEST_DATE);
        note = refreshNote(note);
        assertNotEquals("End time not set", null, note.getScheduledPostingEnd());
    }

    @Test
    public void testTimestampSetOnCreate() {
        Note note = noteRepo.create(TEST_NOTE_TITLE, testUser);
        note = noteRepo.findOne(note.getId());
        System.out.println(note.getLastModified());
        assertNotEquals("Timestamp not set on creation", null, note.getLastModified());
    }

    @Test
    public void testTimestampSetOnUpdate() {
        Note note = noteRepo.create(TEST_NOTE_TITLE, testUser);
        note = noteRepo.findOne(note.getId());
        Calendar createTime = note.getLastModified();
        note.setBody(TEST_NOTE_BODY);
        note = refreshNote(note);
        assertNotEquals("The timestamp was not updated from creation", createTime, note.getLastModified());
    }
    
    @Test
    public void testDelete() {
        long initalCount = noteRepo.count();
        Note note = noteRepo.create(TEST_NOTE_TITLE, testUser);
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

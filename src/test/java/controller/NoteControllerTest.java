package controller;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import edu.tamu.app.controller.NoteController;
import edu.tamu.app.enums.NoteType;
import edu.tamu.app.enums.Status;
import edu.tamu.app.model.AppUser;
import edu.tamu.app.model.Note;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.repo.AppUserRepo;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.framework.model.ApiResponse;
import edu.tamu.framework.model.Credentials;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class NoteControllerTest {

    protected static AppUser TEST_USER1 = new AppUser("123456789");
    protected static AppUser TEST_USER2 = new AppUser("987654321");

    protected static final String TEST_NOTE_TITLE1 = "Test Note Title 1";
    protected static final String TEST_NOTE_TITLE2 = "Test Note Title 2";
    protected static final String TEST_NOTE_TITLE3 = "Test Note Title 3";
    protected static final String TEST_MODIFIED_NOTE_TITLE = "Modified Note Title";
    protected static final String TEST_SERVICE_NAME = "Test Service";

    protected static Service TEST_SERVICE = new Service(TEST_SERVICE_NAME, Status.UP, false, true, true, "", "");
    protected static Note TEST_NOTE1 = new Note(TEST_NOTE_TITLE1, TEST_USER1);
    protected static Note TEST_NOTE2 = new Note(TEST_NOTE_TITLE2, TEST_USER1);
    protected static Note TEST_NOTE3 = new Note(TEST_NOTE_TITLE3, TEST_USER1);
    protected static Note TEST_MODIFIED_NOTE = new Note(TEST_MODIFIED_NOTE_TITLE, TEST_USER2, NoteType.ISSUE, "", TEST_SERVICE);
    protected static List<Note> mockNoteList = new ArrayList<Note>(Arrays.asList(new Note[] { TEST_NOTE1, TEST_NOTE2, TEST_NOTE3 }));

    protected static AppUser user = new AppUser("123456789");

    protected static ApiResponse response;

    @Mock
    protected static Credentials credentials;

    @Mock
    protected static AppUserRepo userRepo;
    
    @Mock
    protected NoteRepo noteRepo;
    
    @Mock
    protected ServiceRepo serviceRepo;

    @Mock
    protected SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    protected NoteController noteController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(credentials.getUin()).thenReturn("123456789");
        when(userRepo.findByUin(any(String.class))).thenReturn(user);
        when(noteRepo.findAll()).thenReturn(mockNoteList);
        when(noteRepo.findOne(any(Long.class))).thenReturn(TEST_NOTE1);
        when(noteRepo.create(any(Note.class), any(Credentials.class))).thenReturn(TEST_NOTE1);
        when(noteRepo.save(any(Note.class))).thenReturn(TEST_MODIFIED_NOTE);
        when(serviceRepo.getOne(any(Long.class))).thenReturn(TEST_SERVICE);
        doNothing().when(noteRepo).delete(any(Note.class));
        doNothing().when(noteRepo).delete(any(Note.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAllNotes() {
        response = noteController.getAllNotes();
        assertEquals("Not successful at getting all Notes", SUCCESS, response.getMeta().getType());
        List<Note> list = (List<Note>) response.getPayload().get("ArrayList<Note>");
        assertEquals("The list of services had the worng number of services", mockNoteList.size(), list.size());
    }

    @Test
    public void testNote() {
        response = noteController.getNote(TEST_NOTE1.getId());
        assertEquals("Not successful at getting requested Note", SUCCESS, response.getMeta().getType());
        Note note = (Note) response.getPayload().get("Note");
        assertEquals("Did not get the expected service", TEST_NOTE1.getId(), note.getId());
    }

    @Test
    public void testCreate() {
        response = noteController.create(TEST_NOTE1, credentials);
        assertEquals("Not sucessful at creating Note", SUCCESS, response.getMeta().getType());
    }

    @Test
    public void testUpdate() {
        response = noteController.update(TEST_MODIFIED_NOTE);
        assertEquals("Not successful at updating note", SUCCESS, response.getMeta().getType());
        Note note = (Note) response.getPayload().get("Note");
        assertEquals("Notification Title was not properly updated", TEST_MODIFIED_NOTE.getTitle(), note.getTitle());
        assertEquals("Notification Author was not properly updated", TEST_MODIFIED_NOTE.getAuthor(), note.getAuthor());
    }

    @Test
    public void testRemove() {
        response = noteController.remove(TEST_MODIFIED_NOTE);
        assertEquals("Not successful at removing Note", SUCCESS, response.getMeta().getType());
    }
}

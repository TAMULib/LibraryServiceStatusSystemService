package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import edu.tamu.app.enums.NoteType;
import edu.tamu.app.enums.Status;
import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.Note;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.User;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.app.model.repo.specification.NoteSpecification;
import edu.tamu.app.model.request.FilteredPageRequest;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiResponse;


@RunWith(SpringRunner.class)
public class NoteControllerTest {

    private static User TEST_USER1 = new User("123456789");
    private static User TEST_USER2 = new User("987654321");

    private static final String TEST_NOTE_TITLE1 = "Test Note Title 1";
    private static final String TEST_NOTE_TITLE2 = "Test Note Title 2";
    private static final String TEST_NOTE_TITLE3 = "Test Note Title 3";
    private static final String TEST_MODIFIED_NOTE_TITLE = "Modified Note Title";
    private static final String TEST_SERVICE_NAME = "Test Service";

    private static Service TEST_SERVICE = new Service(TEST_SERVICE_NAME, Status.UP, false, true, true, "", "");
    private static Note TEST_NOTE1 = new Note(TEST_NOTE_TITLE1, TEST_USER1);
    private static Note TEST_NOTE2 = new Note(TEST_NOTE_TITLE2, TEST_USER1);
    private static Note TEST_NOTE3 = new Note(TEST_NOTE_TITLE3, TEST_USER1);
    private static Note TEST_MODIFIED_NOTE = new Note(TEST_MODIFIED_NOTE_TITLE, TEST_USER2, NoteType.ISSUE, "", TEST_SERVICE);
    private static List<Note> mockNoteList = new ArrayList<Note>(Arrays.asList(new Note[] { TEST_NOTE1, TEST_NOTE2, TEST_NOTE3 }));
    private static Page<Note> mockPageableNoteList = new PageImpl<Note>(Arrays.asList(new Note[] { TEST_NOTE1, TEST_NOTE2, TEST_NOTE3  }));

    private static User user = new User("123456789");

    private static ApiResponse response;

    @Mock
    private UserRepo userRepo;

    @Mock
    private NoteRepo noteRepo;

    @Mock
    private ServiceRepo serviceRepo;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Mock
    private Credentials credentials;

    @InjectMocks
    private NoteController noteController;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() throws UserNotFoundException {
        MockitoAnnotations.initMocks(this);
        when(credentials.getUin()).thenReturn("123456789");
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(noteRepo.findAll()).thenReturn(mockNoteList);
        when(noteRepo.findAll(any(NoteSpecification.class), any(Pageable.class))).thenReturn(mockPageableNoteList);
        when(noteRepo.findOne(any(Long.class))).thenReturn(TEST_NOTE1);
        when(noteRepo.create(any(Note.class), any(Credentials.class))).thenReturn(TEST_NOTE1);
        when(noteRepo.update(any(Note.class))).thenReturn(TEST_MODIFIED_NOTE);
        when(serviceRepo.findOne(any(Long.class))).thenReturn(TEST_SERVICE);
        doNothing().when(noteRepo).delete(any(Note.class));
        doNothing().when(noteRepo).delete(any(Note.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPage() {
        FilteredPageRequest mockFilter = new FilteredPageRequest();
        response = noteController.page(mockFilter);
        assertEquals("Not successful at getting paged Notes", SUCCESS, response.getMeta().getStatus());

        Page<Note> page = (Page<Note>) response.getPayload().get("PageImpl");
        assertEquals("The paged list of Notes is the wrong length", mockPageableNoteList.getSize(), page.getSize());
    }

    @Test
    public void testNote() {
        response = noteController.getById(TEST_NOTE1.getId());
        assertEquals("Not successful at getting requested Note", SUCCESS, response.getMeta().getStatus());
        Note note = (Note) response.getPayload().get("Note");
        assertEquals("Did not get the expected service", TEST_NOTE1.getId(), note.getId());
    }

    @Test
    public void testCreate() throws UserNotFoundException {
        response = noteController.create(TEST_NOTE1, credentials);
        assertEquals("Not sucessful at creating Note", SUCCESS, response.getMeta().getStatus());
    }

    @Test
    public void testUpdate() {
        response = noteController.update(TEST_MODIFIED_NOTE);
        assertEquals("Not successful at updating note", SUCCESS, response.getMeta().getStatus());
        Note note = (Note) response.getPayload().get("Note");
        assertEquals("Notification Title was not properly updated", TEST_MODIFIED_NOTE.getTitle(), note.getTitle());
        assertEquals("Notification Author was not properly updated", TEST_MODIFIED_NOTE.getAuthor(), note.getAuthor());
    }

    @Test
    public void testRemove() {
        response = noteController.remove(TEST_MODIFIED_NOTE);
        assertEquals("Not successful at removing Note", SUCCESS, response.getMeta().getStatus());
    }

}

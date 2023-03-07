package edu.ucsd.cse110.sharednotes;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import edu.ucsd.cse110.sharednotes.model.Note;
import edu.ucsd.cse110.sharednotes.model.NoteAPI;
import edu.ucsd.cse110.sharednotes.model.NoteDao;
import edu.ucsd.cse110.sharednotes.model.NoteRepository;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    NoteAPI api = new NoteAPI();
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testGetNote() throws ExecutionException, InterruptedException, TimeoutException {
        Future<String> noteBodyFuture = api.getNoteAsync("This is a brand new note just for me");
        String noteBody = noteBodyFuture.get(1, TimeUnit.SECONDS);
        assertNotNull(noteBody);
        System.out.println("\n"+ noteBody + "\n");
    }

    @Test
    public void testGetNullNote() throws ExecutionException, InterruptedException, TimeoutException {
        Future<String> noteBodyFuture = api.getNoteAsync("ojdfnjeqsjfdcmioeklqwmdfdiuncuidnm");
        String noteBody = noteBodyFuture.get(1, TimeUnit.SECONDS);
        assertEquals("{\"detail\":\"Note not found.\"}", noteBody);
        System.out.println("\n"+ noteBody + "\n");
    }

    @Test
    public void postNote() throws ExecutionException, InterruptedException, TimeoutException {
        Future<String> noteBodyFuture = api.getNoteAsync("This is a brand new note just for me");
        String noteBody = noteBodyFuture.get(1, TimeUnit.SECONDS);
        assertEquals("{\"title\":\"This is a brand new note just for me\",\"content\":\"This is a note.\",\"version\":0}", noteBody);
        String TylerNoteBody = noteBody;
        Note note = Note.fromJSON(TylerNoteBody);
        note.title = "A note for Tyler";
        api.putNote(note);
        Future<String> TylerNoteFuture = api.getNoteAsync("A note for Tyler");
        String TylerNote = TylerNoteFuture.get(1, TimeUnit.SECONDS);
        System.out.println("\n"+ TylerNote + "\n");

        //assertEquals("{\"title\":\"Another new note for Tyler\",\"content\":\"This is a note.\",\"version\":0}", TylerNoteBody);
    }

    @Test
    public void testGetRemote() {
        NoteRepository repo = new NoteRepository(new NoteDao() {
            @Override
            public long upsert(Note note) {
                return 0;
            }

            @Override
            public boolean exists(String title) {
                return false;
            }

            @Override
            public LiveData<Note> get(String title) {
                return null;
            }

            @Override
            public LiveData<List<Note>> getAll() {
                return null;
            }

            @Override
            public int delete(Note note) {
                return 0;
            }
        });
    }

}
package guru.springframework.converters;

import guru.springframework.commands.NotesCommand;
import guru.springframework.domain.Note;
import lombok.Synchronized;
import org.springframework.stereotype.Component;

@Component
public class NotesToNotesCommand implements Converter<Note, NotesCommand> {

    @Synchronized
    @Override
    public NotesCommand convert(Note source) {
        if(source == null) {
            return null;
        }

        final NotesCommand notes = new NotesCommand();
        notes.setId(source.getId());
        notes.setRecipeNotes(source.getRecipeNotes());
        return notes;
    }
}
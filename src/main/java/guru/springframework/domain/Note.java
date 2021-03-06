package guru.springframework.domain;

import lombok.*;
import org.springframework.data.annotation.Id;


@Getter
@Setter
public class Note {
    @Id
    private String id;
    private Recipe recipe;
    private String recipeNotes;

    public Note() {
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Note;
    }

}

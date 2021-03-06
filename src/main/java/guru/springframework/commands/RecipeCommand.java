package guru.springframework.commands;

import guru.springframework.domain.Category;
import guru.springframework.domain.Difficulty;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Note;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class RecipeCommand {
    private String id;

    private String description;

    private Integer prepTime;

    private Integer cookTime;

    private Integer serving;
    private String source;

    private String url;

    private String direction;
    private Difficulty difficulty;
    private Byte[] image;
    private NotesCommand notes;
    private Set<IngredientCommand> ingredients = new HashSet<>();
    private Set<CategoryCommand> categories = new HashSet<>();


}

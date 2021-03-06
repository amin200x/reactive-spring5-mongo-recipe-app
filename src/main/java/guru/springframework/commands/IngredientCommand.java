package guru.springframework.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class IngredientCommand {
    private String id;
    private String description;
    private BigDecimal amount;
    private String recipeId;
    private UnitOfMeasureCommand unitOfMeasure;

}

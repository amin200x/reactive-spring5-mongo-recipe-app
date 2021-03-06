package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Ingredient;

public interface IngredientService {
    Ingredient findById(String id);
    IngredientCommand saveIngredientCommand(IngredientCommand command);
    void deleteById(String id);
    IngredientCommand findByRecipeIdAndId(String recipeId, String id);
    void deleteById(String recipeId, String ingredientId);
}

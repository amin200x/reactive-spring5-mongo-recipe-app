package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Ingredient;
import reactor.core.publisher.Mono;

public interface IngredientService {
    Ingredient findById(String id);
    Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command);
    void deleteById(String id);
    Mono<IngredientCommand> findByRecipeIdAndId(String recipeId, String id);
    Mono<Void> deleteById(String recipeId, String ingredientId);
}

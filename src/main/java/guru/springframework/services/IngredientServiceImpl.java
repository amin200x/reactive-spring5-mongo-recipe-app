package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositores.reactive.RecipeReactiveRepository;
import guru.springframework.repositores.reactive.UnitOgMeasureReactiveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Set;

@Service
public class IngredientServiceImpl implements IngredientService {
    private final RecipeReactiveRepository recipeRepository;
    private final UnitOgMeasureReactiveRepository unitOfMeasureRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(RecipeReactiveRepository recipeRepository, UnitOgMeasureReactiveRepository unitOfMeasureRepository, IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }

    @Override
    public Ingredient findById(String id) {
        return null;
    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {
        Mono<Recipe> recipeMono = recipeRepository.findById(command.getRecipeId());
        if (recipeMono != null) {
            return Mono.just(new IngredientCommand());
        } else {
            Recipe recipe = recipeMono.block();
            Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                    .stream()
                    .filter(ing -> ing.getId().equals(command.getId()))
                    .findFirst();
            if (ingredientOptional.isPresent()) {
                Ingredient foundIngredient = ingredientOptional.get();
                foundIngredient.setAmount(command.getAmount());
                foundIngredient.setDescription(command.getDescription());
                foundIngredient.setUnitOfMeasure(unitOfMeasureRepository.findById(command.getUnitOfMeasure().getId()).blockOptional().orElseThrow(() -> new RuntimeException("UMO not found")));
            } else {
                recipe.addIngredient(ingredientCommandToIngredient.convert(command));
            }
            Recipe savedRecipe = recipeRepository.save(recipe).block();
            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                    .findFirst();

            //check by description
            if (!savedIngredientOptional.isPresent()) {
                //not totally safe... But best guess
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId().equals(command.getUnitOfMeasure().getId()))
                        .findFirst();
            }
            return Mono.just(ingredientToIngredientCommand.convert(savedIngredientOptional.get()));

        }

    }

    @Override
    public void deleteById(String id) {

    }

    @Transactional
    @Override
    public Mono<Void> deleteById(String recipeId, String ingredientId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId).blockOptional();
        if (recipe.isPresent()) {
            Optional<Ingredient> ingredinent = recipe.get().getIngredients().stream()
                    .filter(ing -> ing.getId().equals(ingredientId)).findFirst();
            if (ingredinent.isPresent()) {
                ingredinent.get().setRecipe(null);
                recipe.get().getIngredients().remove(ingredinent.get());
                recipeRepository.save(recipe.get());

            }
        }
        return Mono.empty();
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndId(String recipeId, String id) {
        try {
            return recipeRepository.findById(recipeId)
                    .map(recipe -> recipe.getIngredients()
                            .stream()
                            .filter(ingredient -> ingredient.getId().equals(id))
                            .findFirst())
                    .filter(Optional::isPresent)
                    .map(ingredient -> {
                        IngredientCommand ingredientCommand = ingredientToIngredientCommand.convert(ingredient.get());
                        ingredientCommand.setRecipeId(recipeId);
                        return ingredientCommand;

                    });


        } catch (Exception e) {
            return Mono.just(new IngredientCommand());
        }
    }
}




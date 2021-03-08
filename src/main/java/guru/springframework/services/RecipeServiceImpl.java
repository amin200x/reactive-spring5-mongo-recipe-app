package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositores.reactive.RecipeReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeReactiveRepository recipeRepository;
    private final RecipeToRecipeCommand recipeToRecipeCommand;
    private final RecipeCommandToRecipe recipeCommandToRecipe;

    public RecipeServiceImpl(RecipeReactiveRepository recipeRepository, RecipeToRecipeCommand recipeToRecipeCommand, RecipeCommandToRecipe recipeCommandToRecipe) {
        this.recipeRepository = recipeRepository;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
    }

    @Override
    public Flux<Recipe> getRecipes() {

        return recipeRepository.findAll();
    }

    @Override
    public Mono<Recipe> findById(String id) {
        Mono<Recipe> recipeMono = recipeRepository.findById(id);
        if (recipeMono ==null) {
            throw new NotFoundException("Recipe not found for Id " + id.toString());
        }
        return recipeMono;
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String id) {
        return Mono.just(recipeToRecipeCommand.convert(findById(id).block()));
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand) {
        Recipe recipe = recipeCommandToRecipe.convert(recipeCommand);
        Recipe savedRecipe = recipeRepository.save(recipe).block();
        //Log.debug("Recipe saved with Id: " + savedRecipe.getId());

        return Mono.just(recipeToRecipeCommand.convert(savedRecipe));
    }

    @Override
    public Mono<Void> deleteById(String id) {

        try {
            recipeRepository.deleteById(id);

        } catch (Exception e) {

        } finally {
            return Mono.empty();
        }

    }
}

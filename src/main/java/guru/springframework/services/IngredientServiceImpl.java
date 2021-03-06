package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositores.RecipeRepository;
import guru.springframework.repositores.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository, IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient) {
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
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());
        if (!recipeOptional.isPresent()) {
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();
            Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                    .stream()
                    .filter(ing -> ing.getId().equals(command.getId()))
                    .findFirst();
            if (ingredientOptional.isPresent()) {
                Ingredient foundIngredient = ingredientOptional.get();
                foundIngredient.setAmount(command.getAmount());
                foundIngredient.setDescription(command.getDescription());
                foundIngredient.setUnitOfMeasure(unitOfMeasureRepository.findById(command.getUnitOfMeasure().getId()).orElseThrow(() -> new RuntimeException("UMO not found")));
            } else {
                recipe.addIngredient(ingredientCommandToIngredient.convert(command));
            }
            Recipe savedRecipe = recipeRepository.save(recipe);
            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                    .findFirst();

            //check by description
            if(!savedIngredientOptional.isPresent()){
                //not totally safe... But best guess
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId().equals(command.getUnitOfMeasure().getId()))
                        .findFirst();
            }
            return ingredientToIngredientCommand.convert(savedIngredientOptional.get());

        }

    }

    @Override
    public void deleteById(String id) {

    }

   // @Transactional
    @Override
    public void deleteById(String recipeId, String ingredientId) {
       Optional<Recipe> recipe = recipeRepository.findById(recipeId);
       if (recipe.isPresent()){
          Optional<Ingredient> ingredinent= recipe.get().getIngredients().stream()
                   .filter(ing ->ing.getId().equals(ingredientId)).findFirst();
          if (ingredinent.isPresent()){
              ingredinent.get().setRecipe(null);
              recipe.get().getIngredients().remove(ingredinent.get());
              recipeRepository.save(recipe.get());

          }
       }
    }

    @Override
    public IngredientCommand findByRecipeIdAndId(String recipeId, String id) {
        Optional<IngredientCommand> ingredientCommand;
        try {
            Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
            ingredientCommand = recipeOptional.get().getIngredients().stream()
                    .filter(ing -> ing.getId().equals(id))
                    .map(ing -> ingredientToIngredientCommand.convert(ing)).findFirst();
            return ingredientCommand.orElse(new IngredientCommand());


        } catch (Exception e) {
            return new IngredientCommand();
        }
    }
}




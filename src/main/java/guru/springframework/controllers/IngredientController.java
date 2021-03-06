package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasurService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasurService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasurService = unitOfMeasurService;
    }

    @RequestMapping("/recipe/{id}/ingredients")
    public String show(@PathVariable("id") String id, Model model) {
        model.addAttribute("recipe", recipeService.findById(id));

        return "recipe/ingredient/list";

    }

    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/show")
    public String showByRecipeIdAndId(@PathVariable("recipeId") String recipeId, @PathVariable("id") String id, Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndId(recipeId, id));

        return "recipe/ingredient/show";

    }

    @RequestMapping(value = "/recipe/ingredent", method = RequestMethod.POST)
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
        IngredientCommand ingredientCommand = ingredientService.saveIngredientCommand(command);
        return "redirect:/recipe/" + ingredientCommand.getRecipeId() + "/ingredient/" + ingredientCommand.getId() + "/show";

    }

    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable("recipeId") String recipeId, @PathVariable("id") String id, Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndId(recipeId, id));
        model.addAttribute("umoList", unitOfMeasurService.listAllUmos());

        return "recipe/ingredient/ingredientform";

    }

    @RequestMapping("/recipe/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable("recipeId") String recipeId, Model model) {
        RecipeCommand recipeCommand = recipeService.findCommandById(recipeId);
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeCommand.getId());
        model.addAttribute("ingredient", ingredientCommand);
        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());
        model.addAttribute("umoList", unitOfMeasurService.listAllUmos());

        return "recipe/ingredient/ingredientform";

    }
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteIngredientById(@PathVariable("recipeId") String recipeId, @PathVariable("id") String id, Model model) {
            ingredientService.deleteById(recipeId);

        return "redirect:/recipe/"+recipeId+"/ingredients";

    }
}

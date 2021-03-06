package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@Slf4j
@Controller
public class RecipeController {
    public static final String RECIPE_RECIPEFORM = "recipe/recipeform";
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping("/recipe/show/{id}")
    public String showById(@PathVariable("id") String id, Model model){
         model.addAttribute("recipe", recipeService.findById(id));

         return "recipe/show";

    }

    @RequestMapping("/recipe/new")
    public String newRecipe(Model model){
        model.addAttribute("recipe", Mono.just(new RecipeCommand()));

        return RECIPE_RECIPEFORM;
    }

    @PostMapping("recipe")
   // @RequestMapping(value = "recipe", method = RequestMethod.POST)
    public String saveOrUpdate( @ModelAttribute("recipe") RecipeCommand command){
        /*if (bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });
            return RECIPE_RECIPEFORM;
        }*/

        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(command).block();

        return "redirect:/recipe/show/" + savedRecipeCommand.getId();
    }

    @RequestMapping("/recipe/{id}/update")
    public String update(@PathVariable("id") String id, Model model){
        model.addAttribute("recipe", recipeService.findCommandById(id));

        return RECIPE_RECIPEFORM;

    }
    @RequestMapping("/recipe/{id}/delete")
    public String delete(@PathVariable("id") String id){
            recipeService.deleteById(id);
        return "redirect:/";


    }

   /* @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception ex){
        ModelAndView modelAndView = new ModelAndView("recipe/404error");
        modelAndView.addObject("exception", ex);
        return modelAndView;

    }
*/
   /* @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleNumberFormatException(Exception ex){
        ModelAndView modelAndView = new ModelAndView("recipe/400error");
        modelAndView.addObject("exception", ex);
        return modelAndView;

    }  */
}

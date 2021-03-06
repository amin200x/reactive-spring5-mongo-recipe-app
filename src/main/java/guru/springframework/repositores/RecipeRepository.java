package guru.springframework.repositores;

import guru.springframework.domain.Recipe;
import org.springframework.data.repository.CrudRepository;
 
public interface RecipeRepository extends CrudRepository<Recipe, String> {
    //Iterable findAll();
}

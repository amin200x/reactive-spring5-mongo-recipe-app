package guru.springframework.repositores.reactive;

import com.jayway.jsonpath.internal.path.PredicateContextImpl;
import guru.springframework.domain.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
//@DataMongoTest
class RecipeReactiveRepositoryTest {

    @Autowired
    private RecipeReactiveRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll().block();
    }

    @Test
    void testSave() {
        Recipe recipe = new Recipe();
        recipe.setDescription("Yummy");

        repository.save(recipe).block();
        assertEquals(1L, repository.count().block());
    }
}
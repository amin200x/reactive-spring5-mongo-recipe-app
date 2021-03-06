package guru.springframework.repositores;

import guru.springframework.bootstrap.RecipeBootstrap;
import guru.springframework.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
//@DataMongoTest
class UnitOfMeasureRepositoryIT {

    @Autowired(required = true)
    UnitOfMeasureRepository unitOfMeasureRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    RecipeRepository recipeRepository;


    @BeforeEach
    void setUp() {
        RecipeBootstrap recipeBootstrap = new RecipeBootstrap(recipeRepository, categoryRepository, unitOfMeasureRepository);
        recipeBootstrap.onApplicationEvent(null);

    }

    @Test
    void findByDescription() {
        Optional<UnitOfMeasure> optionalUnitOfMeasure = unitOfMeasureRepository.findByDescription("Teaspoon");
        assertEquals("Teaspoon", optionalUnitOfMeasure.get().getDescription());
    }
}
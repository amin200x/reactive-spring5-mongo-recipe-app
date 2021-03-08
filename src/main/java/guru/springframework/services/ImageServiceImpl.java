package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositores.reactive.RecipeReactiveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@Service
public class ImageServiceImpl implements ImageService {
    private final RecipeReactiveRepository recipeRepository;

    public ImageServiceImpl(RecipeReactiveRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    @Override
    public Mono<Void> saveImageFile(String id, MultipartFile file) {
        System.out.println("file received!");
        Mono<Recipe> recipeMono =  recipeRepository.findById(id)
                .map(recipe -> {
                    try {
                        Byte[] bytes = new Byte[file.getBytes().length];
                        System.out.println("Bytes: " + file.getBytes().length);
                        int i = 0;
                        for (byte b : file.getBytes()) {
                            bytes[i++] = b;
                        }

                        recipe.setImage(bytes);
                        return recipe;
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw  new RuntimeException(e);
                    }
                });
                recipeRepository.save(recipeMono.block());
            return Mono.empty();

    }
}

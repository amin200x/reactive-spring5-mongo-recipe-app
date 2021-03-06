package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositores.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {
    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImageFile(String id, MultipartFile file) {

       try {
           System.out.println("file received!");
           Recipe recipe = recipeRepository.findById(id).get();
           Byte[] bytes = new Byte[file.getBytes().length];
           int i = 0;
           for (byte b : file.getBytes()) {
               bytes[i++] = b;
           }

           recipe.setImage(bytes);
           recipeRepository.save(recipe);
       }catch (Exception e){
           e.printStackTrace();
       }
    }
}

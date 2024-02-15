import java.io.*;
import java.util.*;

public class RecipeDatabase {

    private Map<String, Recipe> recipes;

    public RecipeDatabase() {
        recipes = loadRecipes();
    }

    public void addRecipe(Recipe recipe) {
        recipes.put(recipe.getName(), recipe);
        saveRecipes();
    }

    public Recipe getRecipe(String name) {
        return recipes.get(name);
    }

    private void saveRecipes() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("recipes.db"))) {
            out.writeObject(recipes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeRecipe(String name) {
        if (recipes.containsKey(name)) {
            recipes.remove(name);
            saveRecipes(); // Save changes to the file
        }
    }

    public void editRecipe(String name, Recipe newRecipe) {
        if (recipes.containsKey(name)) {
            recipes.put(name, newRecipe); // This will replace the existing recipe with the new one
            saveRecipes(); // Save changes to the file
        }
    }

    public List<String> getRecipeNames() {
        return new ArrayList<>(recipes.keySet());
    }

    @SuppressWarnings("unchecked")
    private Map<String, Recipe> loadRecipes() {
        File file = new File("recipes.db");
        if (!file.exists()) return new HashMap<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, Recipe>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}

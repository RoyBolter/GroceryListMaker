import java.io.*;
import java.util.*;

public class RecipeDatabase {

    private List<Runnable> observers = new ArrayList<>();
    private Map<String, Recipe> recipes;

    public RecipeDatabase() {
        recipes = loadRecipes();
    }

    public void addRecipe(Recipe recipe) {
        recipes.put(recipe.getName(), recipe);
        saveRecipes();
        notifyObservers();
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
            notifyObservers();
        }
    }

    public void editRecipe(String name, Recipe newRecipe) {
        if (recipes.containsKey(name)) {
            recipes.put(name, newRecipe); // This will replace the existing recipe with the new one
            saveRecipes(); // Save changes to the file
            notifyObservers();
        }
    }

    public List<String> getRecipeNames() {
        return new ArrayList<>(recipes.keySet());
    }

    // Method to add observers
    public void addObserver(Runnable observer) {
        observers.add(observer);
    }

    // Method to notify observers of changes
    private void notifyObservers() {
        observers.forEach(Runnable::run);
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

import java.util.*;

public class RecipeManager {

    private static void listRecipes(RecipeDatabase database) {
        List<String> recipeNames = database.getRecipeNames();
        for (int i = 0; i < recipeNames.size(); i++) {
            System.out.println((i + 1) + ". " + recipeNames.get(i));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RecipeDatabase database = new RecipeDatabase();

        while (true) {
            System.out.println("Options: \n[1] Add Recipe \n[2] Make Grocery List \n[3] Remove Recipe \n[4] Edit Recipe \n[5] Exit");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.println("Enter recipe name:");
                    String name = scanner.nextLine();
                    Recipe recipe = new Recipe(name);

                    System.out.println("Enter ingredients (type 'done' when finished):");
                    while (true) {
                        String ingredient = scanner.nextLine();
                        if ("done".equalsIgnoreCase(ingredient)) break;
                        recipe.addIngredient(ingredient);
                    }

                    database.addRecipe(recipe);
                    break;
                case "2":
                    System.out.println("Select recipes for the grocery list (type 'done' when finished):");
                    listRecipes(database);
                    Set<String> groceryList = new HashSet<>();
                    String line;
                    while (!(line = scanner.nextLine()).equalsIgnoreCase("done")) {
                        try {
                            int recipeIndex = Integer.parseInt(line) - 1;
                            List<String> names = database.getRecipeNames();
                            if (recipeIndex >= 0 && recipeIndex < names.size()) {
                                String recipeName = names.get(recipeIndex);
                                Recipe r = database.getRecipe(recipeName);
                                groceryList.addAll(r.getIngredients());
                            } else {
                                System.out.println("Invalid number. Try again or type 'done'.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number or 'done'.");
                        }
                    }
                    System.out.println("Grocery List:");
                    groceryList.forEach(System.out::println);
                    break;
                case "3":
                case "4":
                    System.out.println(option.equals("3") ? "Select a recipe to remove:" : "Select a recipe to edit:");
                    listRecipes(database);
                    try {
                        int choice = Integer.parseInt(scanner.nextLine()) - 1;
                        List<String> names = database.getRecipeNames();
                        if (choice >= 0 && choice < names.size()) {
                            String selectedRecipeName = names.get(choice);
                            if (option.equals("3")) {
                                database.removeRecipe(selectedRecipeName);
                                System.out.println("Recipe removed.");
                            } else {
                                Recipe newRecipe = new Recipe(selectedRecipeName);
                                System.out.println("Enter new ingredients (type 'done' when finished):");
                                while (!(line = scanner.nextLine()).equalsIgnoreCase("done")) {
                                    newRecipe.addIngredient(line);
                                }
                                database.editRecipe(selectedRecipeName, newRecipe);
                                System.out.println("Recipe edited.");
                            }
                        } else {
                            System.out.println("Invalid selection.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                    break;
                case "5":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}

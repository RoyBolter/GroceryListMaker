import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AddRecipeUI {
    private RecipeDatabase database;

    public AddRecipeUI(RecipeDatabase database) {
        this.database = database;
    }

    public Node getView() {
        VBox layout = new VBox(10);
        TextField recipeNameField = new TextField();
        TextField ingredientField = new TextField();
        Button addButton = new Button("Add Recipe");

        addButton.setOnAction(e -> {
            addRecipe(recipeNameField.getText(), ingredientField.getText());
            showAlert("Recipe Added", "The recipe has been successfully added!");
            recipeNameField.clear();
            ingredientField.clear();
        });

        layout.getChildren().addAll(new Label("Recipe Name:"), recipeNameField,
                new Label("Ingredient (comma-separated):"), ingredientField,
                addButton);

        return layout;
    }

    private void addRecipe(String name, String ingredients) {
        Recipe recipe = new Recipe(name);
        for (String ingredient : ingredients.split(",")) {
            recipe.addIngredient(ingredient.trim());
        }
        database.addRecipe(recipe);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

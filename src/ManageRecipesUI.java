import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ManageRecipesUI {
    private RecipeDatabase database;
    private ObservableList<String> recipeNames;

    private ListView<String> recipeListView = new ListView<>();

    public ManageRecipesUI(RecipeDatabase database) {
        this.database = database;
        this.recipeNames = FXCollections.observableArrayList(database.getRecipeNames());
        this.database.addObserver(this::refreshRecipes); // Register as observer
        refreshRecipes();
    }

    private void refreshRecipes() {
        Platform.runLater(() -> {
            // Clear the existing items and add all fresh from the database
            recipeNames.clear();
            recipeNames.addAll(database.getRecipeNames());
        });
    }


    public Node getView() {
        VBox layout = new VBox(10);
        ListView<String> recipeListView = new ListView<>(recipeNames);
        Button deleteButton = new Button("Delete Selected Recipe");
        Button editButton = new Button("Edit Selected Recipe");

        deleteButton.setOnAction(e -> {
            String selected = recipeListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                database.removeRecipe(selected);
                recipeNames.remove(selected);
            }
        });

        editButton.setOnAction(e -> {
            String selected = recipeListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                editRecipeDialog(selected);
            }
        });


        layout.getChildren().addAll(recipeListView, editButton, deleteButton);
        return layout;
    }

    private void editRecipeDialog(String selectedRecipeName) {
        Recipe selectedRecipe = database.getRecipe(selectedRecipeName);
        if (selectedRecipe == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Recipe not found.");
            alert.showAndWait();
            return;
        }

        Dialog<Recipe> dialog = new Dialog<>();
        dialog.setTitle("Edit Recipe");
        dialog.setHeaderText("Edit recipe details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(selectedRecipe.getName());
        TextArea ingredientsArea = new TextArea(String.join("\n", selectedRecipe.getIngredients()));

        grid.add(new Label("Recipe name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Ingredients (one per line):"), 0, 1);
        grid.add(ingredientsArea, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(nameField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                selectedRecipe.setName(nameField.getText());
                List<String> ingredients = Arrays.asList(ingredientsArea.getText().split("\\n"));
                selectedRecipe.setIngredients(ingredients);
                return selectedRecipe;
            }
            return null;
        });

        Optional<Recipe> result = dialog.showAndWait();

        result.ifPresent(newRecipeDetails -> {
            database.editRecipe(selectedRecipeName, newRecipeDetails); // Ensure editRecipe handles ingredients.
            refreshRecipes();
        });
    }

}


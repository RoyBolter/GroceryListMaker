import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

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
                // You need to implement the editRecipeDialog method to show a dialog to edit the recipe
                // This is a placeholder to indicate where you would invoke the editing functionality
                editRecipeDialog(selected);
            }
        });

        layout.getChildren().addAll(recipeListView, editButton, deleteButton);
        return layout;
    }

    private void editRecipeDialog(String recipeName) {
        // Implement a dialog where the user can edit the selected recipe's details
        // After editing, update the recipe in the database and refresh the list view
    }
}

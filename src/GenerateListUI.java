import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashSet;
import java.util.Set;

public class GenerateListUI {
    private RecipeDatabase database;
    private ObservableList<String> recipeNames;
    private ListView<String> recipeListView = new ListView<>();

    public GenerateListUI(RecipeDatabase database) {
        this.database = database;
        this.recipeNames = FXCollections.observableArrayList(database.getRecipeNames());
        this.database.addObserver(this::refreshRecipes); // Register as observer
        refreshRecipes(); // Initial load
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

        // Set the cell factory
        recipeListView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            cell.textProperty().bind(cell.itemProperty());
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                // Only toggle selection for non-null items (ignore clicks on empty cells)
                if (!cell.isEmpty()) {
                    int index = cell.getIndex();
                    if (recipeListView.getSelectionModel().getSelectedIndices().contains(index)) {
                        recipeListView.getSelectionModel().clearSelection(index);
                    } else {
                        recipeListView.getSelectionModel().select(index);
                    }
                    event.consume();
                }
            });
            return cell;
        });

        recipeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Button generateListButton = new Button("Generate Grocery List");
        Text groceryListText = new Text();

        generateListButton.setOnAction(e -> {
            Set<String> selectedRecipes = new HashSet<>(recipeListView.getSelectionModel().getSelectedItems());
            Set<String> ingredients = new HashSet<>();
            for (String recipeName : selectedRecipes) {
                Recipe recipe = database.getRecipe(recipeName);
                if (recipe != null) {
                    ingredients.addAll(recipe.getIngredients());
                }
            }
            groceryListText.setText(String.join("\n", ingredients));
        });

        layout.getChildren().addAll(recipeListView, generateListButton, groceryListText);
        return layout;
    }
}

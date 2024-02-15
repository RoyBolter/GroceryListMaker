import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashSet;
import java.util.Set;

public class GenerateListUI {
    private RecipeDatabase database;
    private ObservableList<String> recipeNames;

    public GenerateListUI(RecipeDatabase database) {
        this.database = database;
        this.recipeNames = FXCollections.observableArrayList(database.getRecipeNames());
    }

    public Node getView() {
        VBox layout = new VBox(10);
        ListView<String> recipeListView = new ListView<>(recipeNames);
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

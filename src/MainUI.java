import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainUI {
    private RecipeDatabase database;

    public MainUI(RecipeDatabase database) {
        this.database = database;
    }

    public TabPane getMainTabPane() {
        TabPane tabPane = new TabPane();

        Tab addRecipeTab = new Tab("Add Recipe", new AddRecipeUI(database).getView());
        Tab manageRecipesTab = new Tab("Manage Recipes", new ManageRecipesUI(database).getView());
        Tab generateListTab = new Tab("Generate Grocery List", new GenerateListUI(database).getView());

        addRecipeTab.setClosable(false);
        manageRecipesTab.setClosable(false);
        generateListTab.setClosable(false);

        tabPane.getTabs().addAll(addRecipeTab, manageRecipesTab, generateListTab);

        return tabPane;
    }
}

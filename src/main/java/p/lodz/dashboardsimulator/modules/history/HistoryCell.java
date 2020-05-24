package p.lodz.dashboardsimulator.modules.history;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Data cell in JavaFX {@link javafx.scene.control.ListView} used to display travel statistics.
 */
public class HistoryCell extends ListCell<String> {

    @FXML private Label statText;
    @FXML private Button removeButton;
    private Consumer<Integer> onItemClick;


    public HistoryCell(Consumer<Integer> onItemClick) {
        this.onItemClick = onItemClick;
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/history_cell.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if(empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            statText.setText(item);
            removeButton.setOnMouseClicked(event -> onItemClick.accept(getIndex()));
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

}

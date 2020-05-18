package p.lodz.dashboardsimulator.modules.history;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class HistoryCell {
    @FXML private Text statText;
    @FXML private Button removeButton;

    public Text getStatText() {
        return statText;
    }

    public Button getRemoveButton() {
        return removeButton;
    }
}

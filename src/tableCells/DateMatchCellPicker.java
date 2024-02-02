package tableCells;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import model.Match;

/**
 * Custom TableCell for handling date editing in a TableView for Match entities.
 * 
 * @author Imanol
 */
public class DateMatchCellPicker extends TableCell<Match, Date> {

    private DatePicker datePicker;

    /**
     * Constructor for the DateMatchCellPicker.
     */
    public DateMatchCellPicker() {
        // Default constructor
    }

    /**
     * Overrides the startEdit method to create a DatePicker for editing.
     */
    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createDatePicker();
            setText(null);
            setGraphic(datePicker);
        }
    }

    /**
     * Overrides the cancelEdit method to revert changes when editing is canceled.
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getDate().toString());
        setGraphic(null);
    }

    /**
     * Overrides the updateItem method to display the date value properly in the cell.
     *
     * @param item  The date item to be displayed.
     * @param empty True if the cell is empty, false otherwise.
     */
    @Override
    public void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (datePicker != null) {
                    datePicker.setValue(getDate());
                }
                setText(null);
                setGraphic(datePicker);
            } else {
                final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                setText(getDate().format(dateFormat));
                setGraphic(null);
            }
        }
    }

    /**
     * Creates a DatePicker for editing the date value.
     */
    private void createDatePicker() {
        datePicker = new DatePicker(getDate());
        datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        datePicker.setOnAction((e) -> {
            if (datePicker.getValue() == null) {
                cancelEdit();
            } else {
                commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        });
    }

    /**
     * Retrieves the LocalDate representation of the Date item.
     *
     * @return The LocalDate representation of the Date item.
     */
    private LocalDate getDate() {
        return getItem() == null ? LocalDate.now() : getItem().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}

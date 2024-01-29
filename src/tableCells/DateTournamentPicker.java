/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tableCells;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import model.Tournament;

/**
 *
 * @author Fran
 */
public class DateTournamentPicker extends TableCell<Tournament, Date> {

    private DatePicker datePicker;

    public DateTournamentPicker() {

    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createDatePicker();
            setText(null);
            setGraphic(datePicker);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        setText(getDate().format(dateFormat));
        setGraphic(null);
    }

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

    private void createDatePicker() {
        datePicker = new DatePicker(getDate());
        datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        datePicker.setOnAction((e) -> {
            if(datePicker.getValue()==null){
                commitEdit(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            } else{
                commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            
        });

    }

    private LocalDate getDate() {
        return getItem() == null ? LocalDate.now() : getItem().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}

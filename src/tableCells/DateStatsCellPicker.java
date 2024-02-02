/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tableCells;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import model.Match;
import model.Stats;

/**
 *
 * @author imape
 */
public class DateStatsCellPicker extends TableCell<Stats, Match>{
    private DatePicker datePicker;
    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(SimpleDateFormat.class.cast(dateFormat).toPattern());
    
    public DateStatsCellPicker() {

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

        setText(getDate().format(dateFormatter));
        setGraphic(null);
    }

   
    @Override
    public void updateItem(Match item, boolean empty) {
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
                setText(getDate().format(dateFormatter));

                setGraphic(null);

            }
        }
    }

    private void createDatePicker() {
        datePicker = new DatePicker(getDate());
        datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        datePicker.setOnAction((e) -> {
            Match copy = new Match(getItem());
            if(datePicker.getValue() != null){
                copy.setPlayedDate(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                commitEdit(copy);
            }else{
                cancelEdit();
            }
        });

    }

    private LocalDate getDate() {
        return getItem() == null ? LocalDate.now() : getItem().getPlayedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}

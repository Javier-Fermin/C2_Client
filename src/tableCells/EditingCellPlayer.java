/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tableCells;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import model.Player;
import model.Stats;

/**
 *
 * @author javie
 */
public class EditingCellPlayer extends TableCell<Stats,Player>{
    private TextField textField;
    
    public EditingCellPlayer(){
    }
    
    @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }
        
        @Override
        public void cancelEdit() {
            super.cancelEdit();
 
            setText((String) getItem().getNickname());
            setGraphic(null);
        }
 
        @Override
        public void updateItem(Player item, boolean empty) {
            super.updateItem(item, empty);
 
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }
        
        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
            textField.setOnKeyPressed( event -> {
                if( event.getCode() == KeyCode.ENTER ) {
                    Player copy = new Player(getItem());
                    copy.setNickname(textField.getText());
                    commitEdit(copy);
                }
            });
            textField.focusedProperty().addListener(
                (ObservableValue<? extends Boolean> arg0, 
                Boolean arg1, Boolean arg2) -> {
                    if (!arg2) {
                        Player copy = new Player(getItem());
                        copy.setNickname(textField.getText());
                        commitEdit(copy);
                    }
            });
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
}

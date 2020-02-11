package View;

import Dao.Book;
import Dao.loanInfo;
import Utils.valueObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class myBookController implements Initializable {
    private ObservableList<loanInfo> data = FXCollections.observableArrayList();
    @FXML
    private TableView<loanInfo> my_table = new TableView<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initData();
    }

    private void initData() {
        PreparedStatement ps=null;
        try {
            ps = valueObject.con.prepareStatement("{call searchLoaninfo(?)}");
            ps.setString(1,valueObject.user.getAcc());
            ResultSet rs = ps.executeQuery();
            data.clear();
            while (rs.next()){
                data.add(new loanInfo(rs.getString(2),rs.getString(6),rs.getString(3),rs.getString(4)));
            }
            rs.close();
            my_table.setItems(data);
        }catch (SQLException e) {
            e.printStackTrace();
            showAlert("系统错误");
        }finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void initTable() {
        TableColumn<loanInfo, String> codeCol = new TableColumn<loanInfo, String>("书号");
        codeCol.setMinWidth(100);
        codeCol.setCellValueFactory(
                new PropertyValueFactory<loanInfo, String>("code"));
        TableColumn<loanInfo, String> nameCol = new TableColumn<loanInfo, String>("书名");
        nameCol.setMinWidth(150);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<loanInfo, String>("bookname"));
        TableColumn<loanInfo, String> loandateCol = new TableColumn<loanInfo, String>("借阅日期");
        loandateCol.setMinWidth(150);
        loandateCol.setCellValueFactory(
                new PropertyValueFactory<loanInfo, String>("loandate"));
        TableColumn<loanInfo, String> returndateCol = new TableColumn<loanInfo, String>("归还日期");
        returndateCol.setMinWidth(150);
        returndateCol.setCellValueFactory(
                new PropertyValueFactory<loanInfo, String>("returndate"));
        my_table.getColumns().addAll(codeCol,nameCol,loandateCol,returndateCol);
        my_table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<loanInfo>() {
            @Override
            public void changed(ObservableValue<? extends loanInfo> observable, loanInfo oldValue, loanInfo newValue) {
                if(newValue.getReturndate()==null || ("").equals(newValue.getReturndate())){
                    showCAlert(newValue.getBookname(),newValue.getCode());
                }
            }
        });
    }
    private void showAlert(String s){
        Alert alert =new Alert(Alert.AlertType.WARNING);
        alert.headerTextProperty().set(s);
        alert.showAndWait();
    }
    private void showCAlert(String name,String code){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("你确定要归还这本《"+name+"》吗？");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            returnBook(code);
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    private void returnBook(String code) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowTime = dateFormat.format(now);
        PreparedStatement ps=null;
        try {
            ps = valueObject.con.prepareStatement("{call returnBook(?,?)}");
            ps.setString(1,code);
            ps.setString(2,nowTime);
            ps.execute();
            showAlert("归还成功");
            initData();
        }catch (SQLException e) {
            e.printStackTrace();
            showAlert("系统错误");
        }finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}

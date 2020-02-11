package View;

import Dao.Book;
import Utils.valueObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class findBookController<E extends Event> implements Initializable {

    private ObservableList<Book> data = FXCollections.observableArrayList();
    @FXML
    private TextField find_name;
    @FXML
    private TextField find_author;
    @FXML
    private Button find_btn;
    @FXML
    private TableView<Book> find_table = new TableView<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
    }

    private void initTable() {
        TableColumn<Book, String> codeCol = new TableColumn<Book, String>("书号");
        codeCol.setMinWidth(80);
        codeCol.setCellValueFactory(
                new PropertyValueFactory<Book, String>("code"));
        TableColumn<Book, String> nameCol = new TableColumn<Book, String>("书名");
        nameCol.setMinWidth(130);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Book, String>("bookname"));
        TableColumn<Book, String> authorCol = new TableColumn<Book, String>("作者");
        authorCol.setMinWidth(140);
        authorCol.setCellValueFactory(
                new PropertyValueFactory<Book, String>("writer"));
        TableColumn<Book, String> publisherCol = new TableColumn<Book, String>("出版社");
        publisherCol.setMinWidth(100);
        publisherCol.setCellValueFactory(
                new PropertyValueFactory<Book, String>("publisher"));
        TableColumn<Book, String> isloanCol = new TableColumn<Book, String>("是否被借阅");
        isloanCol.setMinWidth(40);
        isloanCol.setCellValueFactory(
                new PropertyValueFactory<Book, String>("loan"));

        find_table.getColumns().addAll(codeCol,nameCol,authorCol,publisherCol,isloanCol);
        find_table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {
            @Override
            public void changed(ObservableValue<? extends Book> observable, Book oldValue, Book newValue) {
                if(("否").equals(newValue.getLoan())){
                    showCAlert(newValue.getBookname(),newValue.getCode());
                }
            }
        });
    }
    private void showCAlert(String name,String code){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setContentText("你确定要借阅这本《"+name+"》吗？");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            loanBook(code);
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    private void loanBook(String code) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowTime = dateFormat.format(now);
        PreparedStatement ps=null;
        try {
            ps = valueObject.con.prepareStatement("{call loanBook(?,?,?)}");
            ps.setString(1,valueObject.user.getAcc());
            ps.setString(2,code);
            ps.setString(3,nowTime);
            ps.execute();
            showAlert("借阅成功");
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

    public void findBook(ActionEvent event) throws SQLException {
        String name = find_name.getText();
        String author = find_author.getText();
        if (("").equals(name) && ("").equals(author)) {
            showAlert("请输入书本信息");
        } else {
            PreparedStatement ps=null;
            try {
                ps = valueObject.con.prepareStatement("select * from Book_info where bookname like ? and writer like ?");
                ps.setString(1, "%"+name+"%");
                ps.setString(2, "%"+author+"%");
                ResultSet rs = ps.executeQuery();
                data.clear();
                while (rs.next()){
                   data.add(new Book(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getBoolean(6)?"是":"否"));
                }
                rs.close();
                find_table.setItems(data);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("系统错误");
            }finally {
                ps.close();
            }

        }
    }
    private void showAlert(String s){
        Alert alert =alert=new Alert(Alert.AlertType.WARNING);
        alert.headerTextProperty().set(s);
        alert.showAndWait();
    }
}

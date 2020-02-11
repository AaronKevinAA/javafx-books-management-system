package View;

import Dao.Book;
import Utils.valueObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class adminBookController  implements Initializable {

    private ObservableList<Book> data = FXCollections.observableArrayList();

    @FXML
    private TextField admin_code;
    @FXML
    private TextField admin_bookname;
    @FXML
    private TextField admin_writer;
    @FXML
    private TextField admin_publisher;
    @FXML
    private Button admin_btn;
    @FXML
    private TableView<Book> admin_table = new TableView<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initDate();

    }

    private void initDate() {
        PreparedStatement ps=null;
        try {
            ps = valueObject.con.prepareStatement("select * from Book_info");
            ResultSet rs = ps.executeQuery();
            data.clear();
            while (rs.next()){
                data.add(new Book(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getBoolean(6)?"是":"否"));
            }
            rs.close();
            admin_table.setItems(data);
        } catch (SQLException e) {
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

    private void showAlert(String s){
        Alert alert =alert=new Alert(Alert.AlertType.WARNING);
        alert.headerTextProperty().set(s);
        alert.showAndWait();
    }

    private void initTable() {

        TableColumn<Book, String> codeCol = new TableColumn<Book, String>("书号");
        codeCol.setMinWidth(80);
        codeCol.setCellValueFactory(
                new PropertyValueFactory<Book, String>("code"));
        TableColumn<Book, String> nameCol = new TableColumn<Book, String>("书名");
        nameCol.setMinWidth(140);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Book, String>("bookname"));
        TableColumn<Book, String> authorCol = new TableColumn<Book, String>("作者");
        authorCol.setMinWidth(140);
        authorCol.setCellValueFactory(
                new PropertyValueFactory<Book, String>("writer"));
        TableColumn<Book, String> publisherCol = new TableColumn<Book, String>("出版社");
        publisherCol.setMinWidth(110);
        publisherCol.setCellValueFactory(
                new PropertyValueFactory<Book, String>("publisher"));
        TableColumn<Book, String> isloanCol = new TableColumn<Book, String>("是否被借阅");
        isloanCol.setMinWidth(40);
        isloanCol.setCellValueFactory(
                new PropertyValueFactory<Book, String>("loan"));
        admin_table.setEditable(true);

        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Book, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Book, String> event) {
                editBook("bookname",event.getNewValue(),event.getRowValue().getCode());
            }
        });
        authorCol.setCellFactory(TextFieldTableCell.forTableColumn());
        authorCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Book, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Book, String> event) {
                editBook("writer",event.getNewValue(),event.getRowValue().getCode());
            }
        });
        publisherCol.setCellFactory(TextFieldTableCell.forTableColumn());
        publisherCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Book, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Book, String> event) {
                editBook("publisher",event.getNewValue(),event.getRowValue().getCode());
            }
        });

        admin_table.getColumns().addAll(codeCol,nameCol,authorCol,publisherCol,isloanCol);
        admin_table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {
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

        alert.setContentText("你确定要删除这本《"+name+"》吗？");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            deleteBook(code);
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    private void deleteBook(String code) {
        PreparedStatement ps=null;
        try {
            ps = valueObject.con.prepareStatement("delete Book_info where code='"+code+"'");
            ps.execute();
            showAlert("删除成功");
            initDate();

        } catch (SQLException e) {
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

    private void editBook(String s,String value,String code) {

        PreparedStatement ps=null;
        try {
            ps = valueObject.con.prepareStatement("update Book_info set "+s+"='"+value+"' where code='"+code+"'");
            ps.execute();
            initDate();
        } catch (SQLException e) {
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

    public void addBook(ActionEvent event){
        String code =admin_code.getText();
        String name= admin_bookname.getText();
        String writer=admin_writer.getText();
        String publisher=admin_publisher.getText();
        if(("").equals(code))
            showAlert("书号不能为空");
        else if(("").equals(name))
            showAlert("书名不能为空");
        else if(("").equals(writer))
            showAlert("作者不能为空");
        else if(("").equals(publisher))
            showAlert("出版社不能为空");
        else{
            showCAlert(code,name,writer,publisher);
        }
    }
    private void showCAlert(String code,String name,String writer,String publisher){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(code+","+name+","+writer+","+publisher);
        alert.setContentText("你确定要新增吗？");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            AddBook(code,name,writer,publisher);
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    private void AddBook(String code, String name, String writer, String publisher) {
        PreparedStatement ps=null;
        try {
            ps = valueObject.con.prepareStatement("insert into Book_info(code,bookname,writer,publisher) values(?,?,?,?)");
            ps.setString(1,code);
            ps.setString(2,name);
            ps.setString(3,writer);
            ps.setString(4,publisher);
            ps.execute();
            showAlert("新增成功");
            initDate();
        } catch (SQLException e) {
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

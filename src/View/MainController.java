package View;

import Dao.User;
import Utils.treeItem;
import Utils.valueObject;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController  implements Initializable {
    private User user=null;
    private ViewAlter viewAlter;
    @FXML
    private TreeView<String> treeView;
    @FXML
    private Menu menu_about;
    @FXML
    private Menu menu_exit;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane pane_under_scroll;
    @FXML
    private Label info_name;
    @FXML
    private Label info_class;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user=valueObject.user;
        setInfo();
        setTreeView();

    }

    private void setInfo() {
        if(valueObject.user.getRole()==2) {
            info_name.setText(user.getName());
            info_class.setText(user.getGrade() + "级" + user.getCclass() + "班");
        }else{
            info_name.setText(user.getAcc()+"管理员");
            info_class.setText("");
        }
    }

    private void setTreeView() {
        TreeItem<String> root = new TreeItem<String>("图书馆管理");
        root.setExpanded(true);
        switch (user.getRole()){
            case 1: // 管理端
                TreeItem<String> item1 = new TreeItem<String>(treeItem.admin_home);
                root.getChildren().add(item1);
                break;
            case 2: // 用户端
                TreeItem<String> item2 = new TreeItem<String>(treeItem.personal_home);
                item2.setExpanded(true);
                item2.getChildren().addAll(new TreeItem<String>(treeItem.find_book),
                        new TreeItem<String>(treeItem.my_book));
                root.getChildren().add(item2);
                break;
        }
        treeView.setRoot(root);
    }

    @FXML
    private void TreeViewClick() throws IOException {
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
        String pagePath = "";
        switch (selectedItem.getValue()) {
            case treeItem.find_book:
                pagePath = treeItem.find_book_path;
                break;
            case treeItem.my_book:
                pagePath=treeItem.my_book_path;
                break;
            case treeItem.admin_home:
                pagePath=treeItem.admin_home_path;
            default:
                break;
        }
        if(!("").equals(pagePath))
            skipView(pagePath);
    }

    public void setApp(ViewAlter viewAlter) {
        this.viewAlter = viewAlter;
    }
    private void skipView(String pagePath) throws IOException {
        ObservableList<Node> scrolChildren = pane_under_scroll.getChildren();
        scrolChildren.clear();
        scrolChildren.add(FXMLLoader.load(getClass().getResource(pagePath)));
    }
//    public void About(ActionEvent event){
//        System.out.println("1");
//        showAlert("图书馆管理系统1.0版本");
//    }
//    private void showAlert(String s){
//        Alert alert =alert=new Alert(Alert.AlertType.INFORMATION);
//        alert.headerTextProperty().set(s);
//        alert.contentTextProperty().set("897227333@qq.com");
//        alert.showAndWait();
//    }
//    public void Exit(ActionEvent event){
//        System.out.println("2");
//        valueObject.user=null;
//        viewAlter.gotoLogin();
//
//
//    }

}

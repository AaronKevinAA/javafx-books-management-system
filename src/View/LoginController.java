package View;

import ConnectSQL.ConnectSQL;
import Dao.User;
import Utils.valueObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private ViewAlter viewAlter;
    @FXML
    private Button Login_btn;
    @FXML
    private TextField Login_acc;
    @FXML
    private PasswordField Login_pwd;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        valueObject.con = ConnectSQL.Con();
        if(valueObject.con==null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.titleProperty().set("警告");
            alert.headerTextProperty().set("系统发生错误");
            alert.showAndWait();
        }
    }
    public void login(ActionEvent event){
        String acc=Login_acc.getText();
        String pwd=Login_pwd.getText();
        if(acc.equals("")||pwd.equals("")){
            showAlert("请填写账户和密码");
        }
        else{
            ResultSet rs=null;
            PreparedStatement ps=null;
            try {
                ps = valueObject.con.prepareStatement("{call adminLogin(?,?)}");
                ps.setString(1, acc);
                ps.setString(2, pwd);
                rs = ps.executeQuery();
                while (rs.next()) {
                        valueObject.user = new User();
                        //索引下标从1开始
                        valueObject.user.setAcc((String) rs.getObject(1));
                        valueObject.user.setPwd((String) rs.getObject(2));
                        valueObject.user.setRole((Integer) rs.getObject(3));
                        valueObject.user.setStates((Boolean) rs.getObject(4));
                        if (valueObject.user.getRole() == 2) {
                            valueObject.user.setName((String) rs.getObject(6));
                            valueObject.user.setSex((String) rs.getObject(7));
                            valueObject.user.setGrade((Integer) rs.getObject(8));
                            valueObject.user.setCclass((Integer) rs.getObject(9));
                        }
                }
            }catch (SQLException e){
                if(e.getErrorCode()!=0)
                    showAlert("系统错误");
                // 否则忽略该错误
            }
            finally {
                try {
                    if(valueObject.user!=null) {
                        ps.close();
                        rs.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(valueObject.user==null)
                showAlert("账户或密码错误");
            else{
                viewAlter.gotoMain();
            }
        }
    }
    private static void showAlert(String s){
        Alert alert =alert=new Alert(Alert.AlertType.WARNING);
        alert.headerTextProperty().set(s);
        alert.showAndWait();
    }

    public void setApp(ViewAlter ViewAlter) {
        this.viewAlter = ViewAlter;
    }
}

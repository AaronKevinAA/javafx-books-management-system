package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.InputStream;

public class ViewAlter extends Application {
    private Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        stage.setTitle("图书馆管理系统");
        gotoLogin();
        stage.show();
    }

    /**
     * 跳转到登录界面
     */
    public void gotoLogin() {
        try {
            LoginController login = (LoginController) replaceSceneContent("login.fxml",430,298);
            login.setApp(this);
        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 跳转到主界面
     */
    public void gotoMain() {
        try {
            MainController main = (MainController) replaceSceneContent("main.fxml",780,496);
            main.setApp(this);
        } catch (Exception ex) {
//            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 替换场景
     * @param fxml
     * @return
     * @throws Exception
     */
    private Initializable replaceSceneContent(String fxml,int width,int height) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        InputStream in = ViewAlter.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(ViewAlter.class.getResource(fxml));
        try {
            AnchorPane page = (AnchorPane) loader.load(in);
            Scene scene = new Scene(page, width, height);
            stage.setScene(scene);
            stage.sizeToScene();
        } catch (Exception e) {
//            logger.log(PlatformLogger.Level.SEVERE, "页面加载异常！");
        } finally {
            in.close();
        }
        return (Initializable) loader.getController();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

package ru.edu.resumeparseclient;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

    /**
     * Контроллер для формы login для входа в систему
     */
    public class InputController extends Application {
        public CheckBox checkboxreg;
        public Button reg;
        public TextField loginreg;
        public PasswordField passwordreg;
        @FXML
        private Button ok = new Button();
        @FXML
        private Button cancel = new Button();
        @FXML
        private TextField login = new TextField();
        @FXML
        private PasswordField password = new PasswordField();

        /**
         * При нажатии кнопки отправляем запрос на авторизацию
         * @throws IOException
         */
        @FXML
        private void buttonOK() throws IOException {
            String text = getAuthorized(login.getText(), password.getText());

            if (text.equals("AUTHORIZED_ADMIN")) {
                Controller.authorized = "AUTHORIZED_ADMIN";
                ResumeClientApplication resumeClientApplication = new ResumeClientApplication("admin.fxml");
                resumeClientApplication.start(new Stage());
                Stage stage = (Stage) ok.getScene().getWindow();
                stage.close();
            }
            else if (text.equals("AUTHORIZED")) {
                Controller.authorized = "AUTHORIZED";
                ResumeClientApplication resumeClientApplication = new ResumeClientApplication("user.fxml");
                resumeClientApplication.start(new Stage());
                Stage stage = (Stage) ok.getScene().getWindow();
                stage.close();
            } else {
                System.out.println(text);
            }
        }
        @FXML
        private void buttonCancel(){
            System.exit(0);
        }

        /**
         * Отправка запроса к серверу на авторизацию
         * @param login
         * @param password
         * @return
         */
        private String getAuthorized(String login, String password) {
            URL url = null;
            try {
                url = new URL("http://localhost:8080/authorized/" + login + "&" + password);
                URLConnection connection = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                return in.readLine();
            } catch (IOException e) {
                return "Server is not available";
            }
        }

        /**
         * Запуск главной формы после успешной авторизации
         * @param stage
         * @throws Exception
         */
        @Override
        public void start(Stage stage) throws Exception {
            FXMLLoader fxmlLoader = new FXMLLoader(Register.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 350);
            stage.setTitle("Вход в систему");
            stage.setScene(scene);
            stage.show();
        }
        public static void main(String[] args) {
            launch(args);
        }

        public void buttonRegister(ActionEvent actionEvent) throws Exception {
            String admin = "No";
            if (checkboxreg.isSelected()) {
                admin = "Yes";
            }
            String text = registerUser(loginreg.getText(), passwordreg.getText(), admin);
            if (text.equals("COMPLETE")) {
                InputController loginForm = new InputController();
                loginForm.start(new Stage());
                Stage stage = (Stage) checkboxreg.getScene().getWindow();
                stage.close();
            } else {
                System.out.println(text);
            }
        }
        public String registerUser(String login, String password, String admin){
            URL url = null;
            try {
                url = new URL("http://localhost:8080/register/" + login + "&" + password + "&" + admin);
                URLConnection connection = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                return in.readLine();
            } catch (IOException e) {
                return "Server is not available";
            }
            }

        public void buttonGetRegister(ActionEvent actionEvent) throws IOException {
            Register reg = new Register();
            reg.start(new Stage());
            Stage stage = (Stage) ok.getScene().getWindow();
            stage.close();
        }
    }

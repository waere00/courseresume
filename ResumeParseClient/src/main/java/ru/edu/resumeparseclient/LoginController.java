package ru.edu.resumeparseclient;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


    /**
     * Контроллер для формы login для входа в систему
     */
    public class LoginController extends Application {
        public CheckBox checkboxreg;

        @FXML
        private Button cancelButton;
        public TextField loginreg;
        public PasswordField passwordreg;
        @FXML
        private Button ok = new Button();
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

            if (text.equals("AUTHORIZED_ADMIN") || text.equals("AUTHORIZED")) {
                FXMLLoader fxmlLoader;
                if (text.equals("AUTHORIZED_ADMIN")) {
                    fxmlLoader = new FXMLLoader(getClass().getResource("admin.fxml"));
                } else {
                    fxmlLoader = new FXMLLoader(getClass().getResource("user.fxml"));
                }
                Stage stage = (Stage) ok.getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load(), 1200, 350);
                stage.setScene(scene);
            } else {
                System.out.println(text);
            }
        }

        /**
         * Отправка запроса к серверу на авторизацию
         * @param login
         * @param password
         * @return
         */
        private String getAuthorized(String login, String password) {
            try {
                URL url = new URL("http://localhost:8080/users/login");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                System.out.println(login);

                String jsonInputString = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                System.out.println(responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        return response.toString();
                    }
                } else {
                    return "UNAUTHORIZED";
                }
            } catch (IOException e) {
                e.printStackTrace();
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
            FXMLLoader fxmlLoader = new FXMLLoader(RegisterController.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 350);
            stage.setTitle("Вход в систему");
            stage.setScene(scene);
            stage.show();
        }
        public static void main(String[] args) {
            launch(args);
        }

        @FXML
        private Label errorMessage;

        public void buttonRegister() throws Exception {
            String admin = checkboxreg.isSelected() ? "Yes" : "No";
            String text = registerUser(loginreg.getText(), passwordreg.getText(), admin);

            if (text.equals("COMPLETE")) {
                LoginController loginForm = new LoginController();
                loginForm.start(new Stage());
                Stage stage = (Stage) checkboxreg.getScene().getWindow();
                stage.close();
            } else {
                errorMessage.setText("Ошибка при регистрации. Возможно, пользоатель уже существует или не заполнены поля");
            }
        }
        public String registerUser(String login, String password, String admin) {
            try {
                URL url = new URL("http://localhost:8080/users/register");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                String adminFlag = admin.equals("Yes") ? "true" : "false";
                String jsonInputString = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\", \"admin\": " + adminFlag + "}";
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        return response.toString();
                    }
                } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    return "User already exists";
                } else {
                    return "Error during registration";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Server is not available";
            }
        }


        public void buttonGetRegister() throws IOException {
            RegisterController reg = new RegisterController();
            reg.start(new Stage());
            Stage stage = (Stage) ok.getScene().getWindow();
            stage.close();
        }
        public void handleCancelButton() throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            Scene scene = new Scene(loader.load(), 850, 350);
            stage.setTitle("Login");
            stage.setScene(scene);
        }

    }

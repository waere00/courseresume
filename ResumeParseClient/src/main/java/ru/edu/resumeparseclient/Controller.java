package ru.edu.resumeparseclient;

import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Controller implements Initializable {

    public static String authorized = "UNAUTHORIZED";

    @FXML
    private Button update = new Button();
    @FXML
    private TableView tableView = new TableView();
    @FXML
    private ComboBox<String> searchCategory;
    @FXML
    private CheckBox caseSensitiveSearch;
    @FXML
    private TextField textFieldSearch;

    private ObservableList<Resume> resumesData = FXCollections.<Resume>observableArrayList();

    @FXML
    private TableColumn<Resume, String> city = new TableColumn<Resume, String>("Город");
    @FXML
    private TableColumn<Resume, String> id = new TableColumn<Resume, String>("Id");
    @FXML
    private TableColumn<Resume, String> title = new TableColumn<Resume, String>("Должность");
    @FXML
    private TableColumn<Resume, String> skills = new TableColumn<Resume, String>("Навыки");
    @FXML
    private TableColumn<Resume, String> gender = new TableColumn<Resume, String>("Пол");
    @FXML
    private TableColumn<Resume, String> url = new TableColumn<Resume, String>("Ссылка на резюме");
    @FXML
    private Button search = new Button();
    //***********************************************************************************************
    //Вкладка добавить резюме
    @FXML
    private TextField textFieldCity = new TextField();
    @FXML
    private TextField textFieldTitle = new TextField();
    @FXML
    private TextArea textFieldSkills = new TextArea();
    @FXML
    private TextField textFieldGender = new TextField();
    @FXML
    private TextField textFieldUrl = new TextField();
    @FXML
    private Button buttonSendToServer = new Button();
    @FXML
    private Label labelText = new Label();
    @FXML
    private Label UploadLabel = new Label();
    @FXML
    private Label UploadLabelText = new Label();

    // json send
    @FXML
    private Tab jsonUploadTab = new Tab();
    @FXML
    private TextArea jsonTextField = new TextArea();
    @FXML
    private Button uploadJsonButton = new Button();
    @FXML
    private TextField filePathField = new TextField();

    @FXML
    private void uploadJsonToServer() {
        uploadToServer(jsonTextField.getText(), labelText);
    }
    public String toJson(ResumeTemp resumeTemp) {
        return String.format("[{\"resume\":{\"area\":{\"name\":\"%s\"},\"title\":\"%s\",\"gender\":{\"name\":\"%s\"},\"skills\":\"%s\",\"alternate_url\":\"%s\"}}]",
                resumeTemp.city, resumeTemp.title, resumeTemp.gender, resumeTemp.skills, resumeTemp.url);
    }

    private void uploadToServer(String jsonString, Label labelText) {
        if (!jsonString.isEmpty()) {
            try {
                // Sending JSON data to the server
                URL url = new URL("http://localhost:8080/uploadJson");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        System.out.println("Server Response: " + response.toString());
                        labelText.setText("JSON data uploaded successfully!");
                    }
                } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    // Invalid JSON format or server-side processing error
                    try (BufferedReader errorReader = new BufferedReader(
                            new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                        StringBuilder errorResponse = new StringBuilder();
                        String errorLine;
                        while ((errorLine = errorReader.readLine()) != null) {
                            errorResponse.append(errorLine.trim());
                        }
                        System.err.println("Server Error Response: " + errorResponse.toString());
                        labelText.setText("Error: " + errorResponse.toString());
                    }
                } else {
                    // Handle other HTTP error responses
                    System.err.println("HTTP Error Response: " + responseCode);
                    labelText.setText("Error uploading JSON data. Please try again.");
                }
            } catch (IOException e) {
                // Handle IOException (network or connection issues)
                System.err.println("IOException: " + e.getMessage());
                labelText.setText("Error: Network or server connectivity issue. Please check your connection and try again.");
            }
        } else {
            labelText.setText("JSON data cannot be empty!");
        }
    }


    /**
     * Метод посылает POST запрос на сервер
     *
     * @throws IOException
     */
    @FXML
    private void sendToServer() {
            ResumeTemp resumeTemp = new ResumeTemp(
                    textFieldCity.getText(),
                    textFieldTitle.getText(),
                    textFieldSkills.getText(),
                    textFieldGender.getText(),
                    textFieldUrl.getText()
            );
            Gson gson = new Gson();
            String jsonString = toJson(resumeTemp);
            uploadToServer(jsonString, labelText);
    }

    @FXML
    private Tab tab = new Tab();
    //***********************************************************************************************

    /**
     * Метод при нажатии кнопки заполняет таблицу данными резюме
     */
    @FXML
    private void getResumesFromTable() {
        resumesData.clear();
        String text = getResumes();
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        try {
            JsonArray jsonArray = jsonParser.parse(text).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                //Помещаем jsonElement в класс ResumeTemp(временный класс)
                ResumeTemp resumeTemp = gson.fromJson(jsonElement, ResumeTemp.class);
                //ResumeTemp помещаем в класс auction
                Resume resume = new Resume(resumeTemp.id,resumeTemp.city,
                        resumeTemp.title,
                        resumeTemp.skills,
                        resumeTemp.gender,
                        resumeTemp.url
                );
                //Resume помещаем в массив resumesData, другими словами, в таблицу для отображения данных
                resumesData.add(resume);
                tableView.setItems(resumesData);
                id.setCellValueFactory(cellData -> cellData.getValue().idProperty());
                city.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
                title.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
                skills.setCellValueFactory(cellData -> cellData.getValue().skillsProperty());
                gender.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
                url.setCellValueFactory(cellData -> cellData.getValue().urlProperty());
                url.setCellFactory(TextFieldTableCell.<Resume>forTableColumn());
                url.setOnEditCommit(
                        (TableColumn.CellEditEvent<Resume, String> t) -> {
                            ((Resume) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setUrl(t.getNewValue());
                        });
                city.setCellFactory(TextFieldTableCell.<Resume>forTableColumn());
                city.setOnEditCommit(
                        (TableColumn.CellEditEvent<Resume, String> t) -> {
                            ((Resume) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setCity(t.getNewValue());
                        });
                title.setCellFactory(TextFieldTableCell.<Resume>forTableColumn());
                title.setOnEditCommit(
                        (TableColumn.CellEditEvent<Resume, String> t) -> {
                            ((Resume) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setTitle(t.getNewValue());
                        });
                skills.setCellFactory(TextFieldTableCell.<Resume>forTableColumn());
                skills.setOnEditCommit(
                        (TableColumn.CellEditEvent<Resume, String> t) -> {
                            ((Resume) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setSkills(t.getNewValue());
                        });
                gender.setCellFactory(TextFieldTableCell.<Resume>forTableColumn());
                gender.setOnEditCommit(
                        (TableColumn.CellEditEvent<Resume, String> t) -> {
                            ((Resume) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setGender(t.getNewValue());
                        });
            }
        } catch (JsonSyntaxException e) {
        }
    }

    /**
     * Метод запрашивает с сервера список резюме
     *
     * @return
     */
    private String getResumes() {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/getResumes");
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void setSearchResume() {
        setSearch(textFieldSearch.getText(), searchCategory.getValue());
    }

    public void setSearch(String searchText, String category) {
        if (searchText == null || searchText.trim().isEmpty() || category == null) {
            return;
        }

        String searchLowerCase = caseSensitiveSearch.isSelected() ? searchText : searchText.toLowerCase();
        Predicate<Resume> complexPredicate = createComplexPredicate(searchLowerCase, category);

        List<Resume> filteredData = resumesData.stream()
                .filter(complexPredicate)
                .collect(Collectors.toList());

        if (!filteredData.isEmpty()) {
            resumesData.clear();
            resumesData.addAll(filteredData);
        }
    }

    private Predicate<Resume> createComplexPredicate(String searchQuery, String category) {
        return resume -> {
            String resumeData = getResumeDataByCategory(resume, category);
            return resumeData != null && resumeData.contains(searchQuery);
        };
    }

    private String getResumeDataByCategory(Resume resume, String category) {
        switch (category) {
            case "Город":
                return caseSensitiveSearch.isSelected() ? resume.getCity() : resume.getCity().toLowerCase();
            case "Пол":
                return caseSensitiveSearch.isSelected() ? resume.getGender() : resume.getGender().toLowerCase();
            case "Должность":
                return caseSensitiveSearch.isSelected() ? resume.getTitle() : resume.getTitle().toLowerCase();
            case "Навыки":
                return caseSensitiveSearch.isSelected() ? resume.getSkills() : resume.getSkills().toLowerCase();
            case "URL":
                return caseSensitiveSearch.isSelected() ? resume.getUrl() : resume.getUrl().toLowerCase();
            default:
                return null;
        }
    }

    /**
     * При инициализации проверяем, зашёл ли пользователь под админом или нет
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchCategory.setItems(FXCollections.observableArrayList("Город", "Пол", "Должность", "Навыки", "URL"));
    }

    @FXML
    private void browseButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void uploadButtonAction() {
        String filePath = filePathField.getText();

        if (!filePath.isEmpty()) {
            try {
                String jsonContent = new String(Files.readAllBytes(new File(filePath).toPath()));
                uploadToServer(jsonContent, UploadLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // No file
        }
    }

    public void saveResumes() {
        List<ResumeTemp> resumeList = new ArrayList<>();
        for (Resume resume : resumesData) {
            ResumeTemp resumeTemp = new ResumeTemp(
                    resume.getId(),
                    resume.getCity(),
                    resume.getTitle(),
                    resume.getSkills(),
                    resume.getGender(),
                    resume.getUrl()
            );
            resumeList.add(resumeTemp);
        }
        Gson gson = new Gson();
        String jsonString = gson.toJson(resumeList);
        try {
            URL url = new URL("http://localhost:8080/update");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                }
            } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                try (BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResponse.append(errorLine.trim());
                    }
                    System.out.println("Server Error Response: " + errorResponse.toString());
                }
            } else {
                System.out.println("Ошибка при отправке данных. Пожалуйста, повторите попытку.");
            }
        } catch (IOException e) {
            // Handle IOException (network or connection issues)
            System.out.println("Ошибка: Проблемы с сетью или сервером. Проверьте подключение и повторите попытку.");
            e.printStackTrace();  // Log the exception for developers
        }
    }

    public void deleteItem(ActionEvent actionEvent) {
        Resume selectedItem = (Resume) tableView.getSelectionModel().getSelectedItem();
        String id = selectedItem.getId();
        resumesData.remove(selectedItem);
        tableView.getItems().remove(selectedItem);
        try {
            URL url = new URL("http://localhost:8080/delete");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = id.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                }
                getResumesFromTable();
    }
} catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
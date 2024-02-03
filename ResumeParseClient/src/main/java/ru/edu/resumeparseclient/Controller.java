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
    private TableColumn<Resume, String> areaName = new TableColumn<Resume, String>("Город");
    @FXML
    private TableColumn<Resume, String> id = new TableColumn<Resume, String>("Id");
    @FXML
    private TableColumn<Resume, String> title = new TableColumn<Resume, String>("Должность");
    @FXML
    private TableColumn<Resume, String> skills = new TableColumn<Resume, String>("Навыки");
    @FXML
    private TableColumn<Resume, String> gender = new TableColumn<Resume, String>("Пол");
    @FXML
    private TableColumn<Resume, String> alternateUrl = new TableColumn<Resume, String>("Ссылка на резюме");
    @FXML
    private Button search = new Button();
    //***********************************************************************************************
    //Вкладка добавить резюме
    @FXML
    private TextField textFieldAreaName = new TextField();
    @FXML
    private TextField textFieldTitle = new TextField();
    @FXML
    private TextArea textFieldSkills = new TextArea();
    @FXML
    private TextField textFieldGender = new TextField();
    @FXML
    private TextField textFieldAlternateUrl = new TextField();
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

    private ObservableList<Resume> masterResumesData = FXCollections.observableArrayList();

    @FXML
    private void uploadJsonToServer() {
        uploadToServer(jsonTextField.getText(), labelText);
    }
    public String toJson(ResumeTemp resumeTemp) {
        return String.format("[{\"resume\":{\"area\":{\"name\":\"%s\"},\"title\":\"%s\",\"gender\":{\"name\":\"%s\"},\"skills\":\"%s\",\"alternate_url\":\"%s\"}}]",
                resumeTemp.areaName, resumeTemp.title, resumeTemp.gender, resumeTemp.skills, resumeTemp.alternateUrl);
    }

    private void uploadToServer(String jsonString, Label labelText) {
        if (!jsonString.isEmpty()) {
            try {
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
                        System.out.println("Server Response: " + response);
                        labelText.setText("JSON успешно загружен.");
                    }
                } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    try (BufferedReader errorReader = new BufferedReader(
                            new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                        StringBuilder errorResponse = new StringBuilder();
                        String errorLine;
                        while ((errorLine = errorReader.readLine()) != null) {
                            errorResponse.append(errorLine.trim());
                        }
                        System.err.println("Server Error Response: " + errorResponse);
                        labelText.setText("Ошибка: " + errorResponse);
                    }
                } else {
                    System.err.println("HTTP Error Response: " + responseCode);
                    labelText.setText("Ошибка при загрузке JSON:" + responseCode);
                }
            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
                labelText.setText("Ошибка подключения к серверу:" + e.getMessage());
            }
        } else {
            labelText.setText("JSON не должен быть пуст.");
        }
    }


//    /**
//     * Метод посылает POST запрос на сервер
//     *
//     * @throws IOException
//     */
//    @FXML
//    private void sendToServer() {
//            ResumeTemp resumeTemp = new ResumeTemp(
//                    textFieldAreaName.getText(),
//                    textFieldTitle.getText(),
//                    textFieldSkills.getText(),
//                    textFieldGender.getText(),
//                    textFieldAlternateUrl.getText()
//            );
//            Gson gson = new Gson();
//            String jsonString = toJson(resumeTemp);
//            uploadToServer(jsonString, labelText);
//    }

    @FXML
    private Tab tab = new Tab();
    //***********************************************************************************************

    /**
     * Метод при нажатии кнопки заполняет таблицу данными резюме
     */
    @FXML
    private void getResumesFromTable() {
        resumesData.clear();
        masterResumesData.clear();
        String text = getResumes();
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        try {
            JsonArray jsonArray = jsonParser.parse(text).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                // Помещаем jsonElement в класс ResumeTemp(временный класс)
                ResumeTemp resumeTemp = gson.fromJson(jsonElement, ResumeTemp.class);
                Resume resume = new Resume(resumeTemp.id,resumeTemp.areaName,
                        resumeTemp.title,
                        resumeTemp.skills,
                        resumeTemp.gender,
                        resumeTemp.alternateUrl
                );
                // Resume помещаем в массив resumesData, другими словами, в таблицу для отображения данных
                masterResumesData.add(resume);
                tableView.setItems(resumesData);
                id.setCellValueFactory(cellData -> cellData.getValue().idProperty());
                areaName.setCellValueFactory(cellData -> cellData.getValue().areaNameProperty());
                title.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
                skills.setCellValueFactory(cellData -> cellData.getValue().skillsProperty());
                gender.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
                alternateUrl.setCellValueFactory(cellData -> cellData.getValue().alternateUrlProperty());
                alternateUrl.setCellFactory(TextFieldTableCell.<Resume>forTableColumn());
                alternateUrl.setOnEditCommit(
                        (TableColumn.CellEditEvent<Resume, String> t) -> {
                            ((Resume) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setAlternateUrl(t.getNewValue());
                        });
                areaName.setCellFactory(TextFieldTableCell.<Resume>forTableColumn());
                areaName.setOnEditCommit(
                        (TableColumn.CellEditEvent<Resume, String> t) -> {
                            ((Resume) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setAreaName(t.getNewValue());
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
            resumesData.setAll(masterResumesData);
        } catch (JsonSyntaxException e) { e.printStackTrace();
        }
    }

    /**
     * Метод запрашивает с сервера список резюме
     *
     * @return
     */
    private String getResumes() {
        URL url;
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
            resumesData.setAll(masterResumesData);
            return;
        }

        String searchLowerCase = caseSensitiveSearch.isSelected() ? searchText : searchText.toLowerCase();
        Predicate<Resume> complexPredicate = createComplexPredicate(searchLowerCase, category);

        List<Resume> filteredData = masterResumesData.stream()
                .filter(complexPredicate)
                .collect(Collectors.toList());

        resumesData.clear();
        resumesData.addAll(filteredData);

    }

    private Predicate<Resume> createComplexPredicate(String searchQuery, String category) {
        return resume -> {
            String resumeData = getResumeDataByCategory(resume, category);
            return resumeData != null && resumeData.contains(searchQuery);
        };
    }

    private String getResumeDataByCategory(Resume resume, String category) {
        if (category.equals("Все")) {
            String allFieldsConcatenated = String.join(" ", resume.getAreaName(), resume.getGender(), resume.getTitle(), resume.getSkills(), resume.getAlternateUrl());
            return caseSensitiveSearch.isSelected() ? allFieldsConcatenated : allFieldsConcatenated.toLowerCase();
        }
        return switch (category) {
            case "Город" -> caseSensitiveSearch.isSelected() ? resume.getAreaName() : resume.getAreaName().toLowerCase();
            case "Пол" -> caseSensitiveSearch.isSelected() ? resume.getGender() : resume.getGender().toLowerCase();
            case "Должность" -> caseSensitiveSearch.isSelected() ? resume.getTitle() : resume.getTitle().toLowerCase();
            case "Навыки" -> caseSensitiveSearch.isSelected() ? resume.getSkills() : resume.getSkills().toLowerCase();
            case "URL" -> caseSensitiveSearch.isSelected() ? resume.getAlternateUrl() : resume.getAlternateUrl().toLowerCase();
            default -> null;
        };
        }
    /**
     * Инициализация, выставление категорий поиска и выбор категории поиска по-умолчанию
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchCategory.setItems(FXCollections.observableArrayList("Все", "Город", "Пол", "Должность", "Навыки", "URL"));
        searchCategory.getSelectionModel().selectFirst();
        getResumesFromTable();
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
        }
    }

    public void saveResumes() {
        List<ResumeTemp> resumeList = new ArrayList<>();
        for (Resume resume : resumesData) {
            ResumeTemp resumeTemp = new ResumeTemp(
                    resume.getId(),
                    resume.getAreaName(),
                    resume.getTitle(),
                    resume.getSkills(),
                    resume.getGender(),
                    resume.getAlternateUrl()
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
                    System.out.println(response);
                }
            } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                try (BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResponse.append(errorLine.trim());
                    }
                    System.out.println("Сервер отправил ошибку: " + errorResponse);
                }
            } else {
                System.out.println("Ошибка при отправке данных. Пожалуйста, повторите попытку.");
            }
        } catch (IOException e) {
            System.out.println("Ошибка: Проблемы с сетью или сервером. Проверьте подключение и повторите попытку.");
            e.printStackTrace();
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
                    System.out.println(response);
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
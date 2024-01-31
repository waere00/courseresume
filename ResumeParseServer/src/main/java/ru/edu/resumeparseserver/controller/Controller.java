package ru.edu.resumeparseserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ru.edu.resumeparseserver.model.ResumeTemp;
import ru.edu.resumeparseserver.parsing.*;
import com.google.gson.JsonSyntaxException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import ru.edu.resumeparseserver.model.Resume;  // Import your Resumes class
import ru.edu.resumeparseserver.service.DataService;

/**
 * Контроллер для взаимосвязи с клиентами по RESTful
 */
@RestController
@RequestMapping
public class Controller {

    @Autowired
    private DataService dataService;
    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    @GetMapping("/")
    public String getMainInfo(){
        return "Кросс-платформенная система хранения и работы с данными резюме соискателей, осуществляющая парсинг резюме пользователей, передаваемых в json-формате и хранение полученных данных в реляционной БД";
    }
    @PostMapping("/update")
    String updateDatabase(@RequestBody List<ResumeTemp> resumeList)
    {
        if (!dataService.loadResumes(resumeList)){
            return "Data loaded successfully";
        }else{
            return "Error when loading data";
        }
    }

    @GetMapping("/getResumes")
    public List<Resume> getResumes() throws FileNotFoundException {
        return dataService.getResumes();
    }
    @PostMapping("/delete")
    public String deleteResume(@RequestBody String id) {
        return dataService.deleteResume(id);
    }
    @PostMapping("/uploadJson")
    public ResponseEntity<String> uploadJson(@RequestBody String jsonString) {
        try {
            log.info("Received JSON data: {}", jsonString);
            System.out.println(jsonString);
            List<Resume> resumesData = new ArrayList<>();
            JsonParserUtil.parseString(resumesData, jsonString);
            dataService.writeResumes(resumesData);

            return ResponseEntity.ok("JSON data successfully processed and uploaded to the database.");
        } catch (JsonSyntaxException e) {
            log.error("Error parsing JSON format: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON format. Please check your input.");
        } catch (Exception e) {
            log.error("Error processing JSON data: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON data.");
        }
    }
}

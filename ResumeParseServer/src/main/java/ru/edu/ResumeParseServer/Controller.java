package ru.edu.ResumeParseServer;

import ru.edu.ResumeParseServer.hibernate.*;
import ru.edu.ResumeParseServer.parsing.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edu.ResumeParseServer.parsing.Resume;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import ru.edu.ResumeParseServer.hibernate.Resumes;  // Import your Resumes class
import ru.edu.ResumeParseServer.parsing.FileParser;

/**
 * Контроллер для взаимосвязи с клиентами по RESTful
 */
@RestController
@RequestMapping
public class Controller {
    private FileParser fileParser = new FileParser();
    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    @GetMapping("/")
    public String getMainInfo(){
        return "Кросс-платформенная система хранения и работы с данными резюме соискателей, осуществляющая парсинг резюме пользователей, передаваемых в json-формате и хранение полученных данных в реляционной БД";
    }
    @PostMapping("/update")
    String updateDatabase(@RequestBody List<ResumeTemp> resumeList)
    {
        if (!fileParser.loadResumes(resumeList)){
            return "Data loaded successfully";
        }else{
            return "Error when loading data";
        }
    }

    @GetMapping("/getResumes")
    public List<Resumes> getResumes() throws FileNotFoundException {
        return fileParser.getResumes();
    }
    @PostMapping("/delete")
    public String deleteResume(@RequestBody String id) {
        return fileParser.deleteResume(id);
    }
    @PostMapping("/uploadJson")
    public ResponseEntity<String> uploadJson(@RequestBody String jsonString) {
        try {
            log.info("Received JSON data: {}", jsonString);
            System.out.println(jsonString);
            List<Resume> resumesData = new ArrayList<>();
            JsonParserUtil.parseString(resumesData, jsonString);
            for (Resume resume : resumesData) {
                    System.out.println(resume.getCity());
                    fileParser.createResumes(new ResumeTemp(resume.getCity(), resume.getTitle(), resume.getSkills(), resume.getGender(), resume.getAlternative_url()));
            }

            return ResponseEntity.ok("JSON data successfully processed and uploaded to the database.");
        } catch (JsonSyntaxException e) {
            log.error("Error parsing JSON format: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON format. Please check your input.");
        } catch (Exception e) {
            log.error("Error processing JSON data: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON data.");
        }
    }


    @GetMapping("/authorized/{login}&{password}")
    private String getAuthorized(@PathVariable String login, @PathVariable String password) {
        List<Users> users = new ArrayList<>();
        users = fileParser.getUsers();
        boolean authorized = false;
        boolean authorized_admins = false;
        for (Users user: users) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                if (user.isAdmin()){
                    authorized_admins = true;
                }else {
                    authorized = true;
                }
            } else {}
        }

        if (authorized_admins) {
            return "AUTHORIZED_ADMIN";
        } else if (authorized) {
            return "AUTHORIZED";
        } else {
            return "UNAUTHORIZED";
        }
    }
    @GetMapping("/register/{login}&{password}&{admin}")
    private String getRegistred(@PathVariable String login, @PathVariable String password, @PathVariable String admin) {
        Users user = new Users();
        boolean auth = false;
        if (!login.isEmpty() && !password.isEmpty() && !admin.isEmpty()) {
            if (admin.equals("Yes")) {
                user = new Users(login, password, true);
            } else {
                user = new Users(login, password, false);
            }
            Transaction transaction = null;
            Session session = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                transaction = session.beginTransaction();
                if (session.bySimpleNaturalId(Users.class).loadOptional(user.getLogin()).isEmpty()) {
                    session.persist(user);
                    auth = true;
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                    auth = false;
                }
                e.printStackTrace();
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
        }
        if (auth){
            return "COMPLETE";
        }
        return "ERROR";
    }
}

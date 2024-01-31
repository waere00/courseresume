package ru.edu.resumeparseserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;

@SpringBootApplication
public class ResumeParseServerApplication {

	public static void main(String[] args) throws FileNotFoundException {
		SpringApplication.run(ResumeParseServerApplication.class, args);
//		DataService fileParser = new DataService();
//		List<Resume> resumes = new ArrayList<>();
//		fileParser.parseFiles(resumes);
//		fileParser.writeResumes(resumes);
	}
}


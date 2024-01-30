package ru.edu.ResumeParseServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;

@SpringBootApplication
public class ResumeParseServerApplication {

	public static void main(String[] args) throws FileNotFoundException {
		SpringApplication.run(ResumeParseServerApplication.class, args);
//		FileParser fileParser = new FileParser();
//		List<Resume> resumes = new ArrayList<>();
//		fileParser.parseFiles(resumes);
//		fileParser.writeResumes(resumes);
	}
}


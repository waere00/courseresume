package ru.edu.ResumeParseServer;

import ru.edu.ResumeParseServer.hibernate.HibernateUtil;
import ru.edu.ResumeParseServer.hibernate.Users;
import ru.edu.ResumeParseServer.parsing.FileParser;
import ru.edu.ResumeParseServer.parsing.Resume;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ResumeParseServerApplication {

	public static void main(String[] args) throws FileNotFoundException {
		SpringApplication.run(ResumeParseServerApplication.class, args);
		List<Users> usersList = new ArrayList<>();
		usersList.add(new Users("user", "user", false));
		usersList.add(new Users("admin", "admin", true));
		Transaction transaction = null;
		Session session = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			for (Users user : usersList) {
				if (session.bySimpleNaturalId(Users.class).loadOptional(user.getLogin()).isEmpty()) {
					session.persist(user);
				}
			}

			transaction.commit();
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		FileParser fileParser = new FileParser();
		List<Resume> resumes = new ArrayList<>();
		fileParser.parseFiles(resumes);
		fileParser.writeResumes(resumes);
	}
}


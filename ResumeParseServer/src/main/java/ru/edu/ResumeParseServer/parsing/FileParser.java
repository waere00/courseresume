package ru.edu.ResumeParseServer.parsing;


import ru.edu.ResumeParseServer.hibernate.HibernateUtil;
import ru.edu.ResumeParseServer.hibernate.Resumes;
import ru.edu.ResumeParseServer.hibernate.Users;
import com.google.gson.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс предназначен для парсинга файлов json и записи данных в БД
 */
public class FileParser {
    /**
     * Метод записывает в массив спарсенные данные резюме
     * @param resumes Массив вакансий
     * @throws FileNotFoundException
     */
    public void parseFiles(List<Resume> resumes) {
        List<String> filePaths = List.of(
                "src/main/resources/resume_1.json",
                "src/main/resources/resume_2.json",
                "src/main/resources/resume_3.json"
        );

        for (String filePath : filePaths) {
            try {
                JsonParserUtil.parseFile(resumes, filePath);
            } catch (IOException e) {
                // Handle file parsing error
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @return возвращает массив юзеров из БД
     */
    public synchronized List<Users> getUsers() {
        Transaction transaction = null;
        List<Users> users = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            users = session.createQuery("from Users", Users.class).list();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return users;
    }

    /**
     *
     * @return возвращает массив резюме из БД
     */
    public synchronized List<Resumes> getResumes() {
        Transaction transaction = null;
        List<Resumes> resumes = new ArrayList<>();
        transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            resumes = session.createQuery("from Resumes", Resumes.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return resumes;
    }
    /**
     * Метод очищает таблицу в БД
     * @param database задаем название таблицы
     */
    public synchronized void truncateTable(String database) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.createNativeQuery("truncate table " + database).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /**
     * Метод записывает в таблицу resumes результаты парсинга
     * @param resumesData передаем результаты парсинга
     */
    public synchronized void writeResumes(List<Resume> resumesData) {
        //Чистим таблицу перед записью
        truncateTable("resumes RESTART IDENTITY");

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Старт транзакции
            transaction = session.beginTransaction();
            // Добавим в БД резюме
            for (Resume resume: resumesData) {
                Resumes resumes = new Resumes(resume.getCity(), resume.getTitle(), resume.getSkills(), resume.getGender(), resume.getAlternative_url());
                session.persist(resumes);
            }
            // Коммит транзакции
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    /**
     * Метод записывает в таблицу resumes отправленные с клиента данные
     * @param resumeTemp передаем результаты парсинга
     */
    public synchronized void createResumes(ResumeTemp resumeTemp) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Старт транзакции
            transaction = session.beginTransaction();
            // Добавим в БД резюме
            Resumes resumes = new Resumes(resumeTemp.getCity(), resumeTemp.getTitle(), resumeTemp.getSkills(), resumeTemp.getGender(), resumeTemp.getUrl());
            session.persist(resumes);
            // Коммит транзакции
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    public synchronized boolean loadResumes(List<ResumeTemp> resumeList) {
        boolean error = false;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Старт транзакции
            transaction = session.beginTransaction();
            session.clear();
            // Добавим в БД резюме
            for (ResumeTemp resumeTemp : resumeList){
                Resumes resum = (Resumes)session.load(Resumes.class, resumeTemp.getId());
                if (resum != null) {
                    resum.setCity(resumeTemp.getCity());
                    resum.setGender(resumeTemp.getGender());
                    resum.setUrl(resumeTemp.getUrl());
                    resum.setSkills(resumeTemp.getSkills());
                    resum.setTitle(resumeTemp.getTitle());
                    session.merge(resum);
                } else{
                    resum = new Resumes(resumeTemp.getCity(), resumeTemp.getTitle(), resumeTemp.getSkills(), resumeTemp.getGender(), resumeTemp.getUrl());
                    session.persist(resum);
                }
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                error = true;
            }
            e.printStackTrace();
        }
        return error;
    }

    public synchronized String deleteResume(String id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Resumes resum = (Resumes)session.load(Resumes.class,id);
            if (resum != null) {
                session.delete(resum);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                return "Error";
            }
            e.printStackTrace();
        }
        return "Deleted";
    }
}


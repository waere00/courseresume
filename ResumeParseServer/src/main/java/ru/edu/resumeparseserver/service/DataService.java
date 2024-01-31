package ru.edu.resumeparseserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edu.resumeparseserver.model.Resume;
import ru.edu.resumeparseserver.model.ResumeTemp;
import ru.edu.resumeparseserver.model.Users;
import ru.edu.resumeparseserver.repository.UserRepository;
import ru.edu.resumeparseserver.repository.ResumeRepository;
import java.util.List;

/**
 * Класс предназначен для записи данных и работы с БД
 */
@Service
public class DataService {
    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     *
     * @return возвращает массив юзеров из БД
     */
    public synchronized List<Users> getUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @return возвращает массив резюме из БД
     */
    public synchronized List<Resume> getResumes() {
        try {
            return resumeRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Метод очищает таблицу в БД
     * @param database задаем название таблицы
     */
    @Transactional
    public synchronized void truncateTable(String database) {
        try {
            resumeRepository.truncateTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод записывает в таблицу resumes результаты парсинга
     * @param resumesData передаем результаты парсинга
     */
    @Transactional
    public synchronized void writeResumes(List<Resume> resumesData) {
        try {
            resumeRepository.saveAll(resumesData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Метод записывает в таблицу resumes отправленные с клиента данные
     * @param resumeTemp передаем результаты парсинга
     */
    @Transactional
    public synchronized void createResumes(ResumeTemp resumeTemp) {
        try {
            Resume resume = new Resume(resumeTemp.getAreaName(), resumeTemp.getTitle(),
                    resumeTemp.getSkills(), resumeTemp.getGender(),
                    resumeTemp.getAlternateUrl());
            resumeRepository.save(resume);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public synchronized boolean loadResumes(List<ResumeTemp> resumeList) {
        try {
            for (ResumeTemp resumeTemp : resumeList) {
                Resume resume = resumeRepository.findById(resumeTemp.getId())
                        .orElse(new Resume(resumeTemp.getAreaName(), resumeTemp.getTitle(),
                                resumeTemp.getSkills(), resumeTemp.getGender(),
                                resumeTemp.getAlternateUrl()));
                resume.setAreaName(resumeTemp.getAreaName());
                resume.setGender(resumeTemp.getGender());
                resume.setAlternateUrl(resumeTemp.getAlternateUrl());
                resume.setSkills(resumeTemp.getSkills());
                resume.setTitle(resumeTemp.getTitle());
                resumeRepository.save(resume);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Transactional
    public synchronized String deleteResume(String id) {
        try {
            resumeRepository.deleteById(Integer.parseInt(id));
            return "Deleted";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }
}


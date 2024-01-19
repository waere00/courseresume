package ru.edu.ResumeParseServer.parsing;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class JsonParserUtil {

    public static void parseFile(List<Resume> resumes, String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement root = JsonParser.parseReader(reader);
            parseJsonArray(resumes, root.getAsJsonArray());
        }
    }

    public static void parseString(List<Resume> resumes, String jsonString) {
        JsonElement root = JsonParser.parseString(jsonString);
        parseJsonArray(resumes, root.getAsJsonArray());
    }

    private static void parseJsonArray(List<Resume> resumes, JsonArray jsonArray) {
        for (JsonElement element : jsonArray) {
            JsonObject resumeObject = element.getAsJsonObject().getAsJsonObject("resume");

            String city = extractString(resumeObject, "area.name");
            String title = extractString(resumeObject, "title");
            String skills = extractString(resumeObject, "skills");
            String gender = extractString(resumeObject, "gender.name");
            String alternative_url = extractString(resumeObject, "alternate_url");

            resumes.add(new Resume(city, title, skills, gender, alternative_url));
        }
    }

    private static String extractString(JsonObject jsonObject, String propertyPath) {
        String[] properties = propertyPath.split("\\.");

        for (String property : properties) {
            JsonElement element = jsonObject.get(property);

            if (element != null && !element.isJsonNull()) {
                if (element.isJsonObject()) {
                    jsonObject = element.getAsJsonObject();
                } else {
                    if (!Objects.equals(element.getAsString(), "")){
                        return element.getAsString();
                    } else {
                        return "Не указано";
                    }
                }
            } else {
                return "Не указано";
            }
        }
        return "Не указано";
    }
}


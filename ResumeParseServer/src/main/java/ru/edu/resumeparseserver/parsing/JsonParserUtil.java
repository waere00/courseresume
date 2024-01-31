package ru.edu.resumeparseserver.parsing;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.edu.resumeparseserver.model.Resume;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

            Resume resume = new Resume();
            resume.setResumeId(extractString(resumeObject, "id"));
            resume.setAge(safeGetInt(resumeObject,"age"));
            resume.setAreaId(extractString(resumeObject, "area.id"));
            resume.setAreaName(extractString(resumeObject, "area.name"));
            resume.setSite(extractString(resumeObject, "site.url"));
            resume.setMetro(extractString(resumeObject, "metro.name"));
            resume.setOwnerId(extractString(resumeObject, "owner.id"));
            resume.setPhoto(extractString(resumeObject, "photo.medium"));
            resume.setTitle(extractString(resumeObject, "title"));
            resume.setGender(extractString(resumeObject, "gender.name"));
            resume.setSalary(extractString(resumeObject, "salary.amount"));
            resume.setSkills(extractString(resumeObject, "skills"));
            resume.setActions(extractString(resumeObject, "actions.download.pdf.url"));
            resume.setContact(processContactArray(resumeObject.getAsJsonArray("contact")));
            resume.setDownload(extractString(resumeObject, "download.pdf.url"));
            resume.setLanguage(processJsonArray(resumeObject.getAsJsonArray("language"), "name", "level.name",true));
            resume.setSchedule(extractString(resumeObject, "schedule.name"));
            resume.setEducation(processEducation(resumeObject.getAsJsonObject("education")));
            resume.setFavorited(safeGetBool(resumeObject,"favorited"));
            resume.setLastName(extractString(resumeObject, "last_name"));
            resume.setPortfolio(extractString(resumeObject, "portfolio.description"));
            resume.setSchedules(extractString(resumeObject, "schedules.name"));
            resume.setSkillSet(processJsonArray(resumeObject.getAsJsonArray("skill_set"), ""));
            resume.setBirthDate(extractString(resumeObject, "birth_date"));
            resume.setCreatedAt(extractString(resumeObject, "created_at"));
            resume.setEmployment(extractString(resumeObject, "employment.name"));
            resume.setExperience(extractString(resumeObject, "experience"));
            resume.setFirstName(extractString(resumeObject, "first_name"));
            resume.setRelocation(extractString(resumeObject,"relocation.type.name"));
            resume.setUpdatedAt(extractString(resumeObject, "updated_at"));
            resume.setCertificate(extractString(resumeObject, "certificate"));
            resume.setCitizenship(extractString(resumeObject, "citizenship.name"));
            resume.setEmployments(extractString(resumeObject, "employments.name"));
            resume.setHasVehicle(extractString(resumeObject, "has_vehicle"));
            resume.setMiddleName(extractString(resumeObject, "middle_name"));
            resume.setTravelTime(extractString(resumeObject, "travel_time.name"));
            resume.setWorkTicket(extractString(resumeObject, "work_ticket.name"));
            resume.setAlternateUrl(extractString(resumeObject, "alternate_url"));
            resume.setHiddenFields(extractString(resumeObject, "hidden_fields"));
            resume.setPaidServices(extractString(resumeObject, "paid_services"));
            resume.setResumeLocale(extractString(resumeObject, "resume_locale.name"));
            resume.setRecommendation(extractString(resumeObject, "recommendation"));
            resume.setSpecialization(processJsonArray(resumeObject.getAsJsonArray("specialization"), "profarea_name", "name",false));
            resume.setTotalExperience(extractString(resumeObject, "total_experience.months"));
            resume.setCanViewFullInfo(safeGetBool(resumeObject,"can_view_full_info"));
            resume.setDriverLicenseTypes(extractString(resumeObject, "driver_license_types"));
            resume.setNegotiationsHistory(extractString(resumeObject, "negotiations_history.url"));
            resume.setBusinessTripReadiness(extractString(resumeObject, "business_trip_readiness.name"));
            resume.setRating(safeGetFloat(resumeObject,"rating"));

            resumes.add(resume);
        }
    }

    private static String extractString(JsonObject jsonObject, String propertyPath) {
        String[] properties = propertyPath.split("\\.");
        JsonElement currentElement = jsonObject;

        for (int i = 0; i < properties.length; i++) {
            String property = properties[i];

            if (currentElement == null || currentElement.isJsonNull()) {
                return "Не указано";
            }

            if (currentElement.isJsonObject()) {
                currentElement = currentElement.getAsJsonObject().get(property);
            } else if (currentElement.isJsonArray()) {
                JsonArray jsonArray = currentElement.getAsJsonArray();
                // Check if the array is empty or the element itself is an empty array
                if (jsonArray.isEmpty() || (jsonArray.size() == 1 && jsonArray.get(0).isJsonArray() && jsonArray.get(0).getAsJsonArray().isEmpty())) {
                    return "Не указано";
                }
                // Handle arrays only if it's the last property in the path
                if (i == properties.length - 1) {
                    return processJsonArray(jsonArray, property);
                } else {
                    // Array found in the middle of the path is not supported
                    return "Не указано";
                }
            } else {
                // Non-object (and non-array) elements found in the middle of the path
                return "Не указано";
            }
        }

        return currentElement == null || (currentElement.isJsonArray() && currentElement.getAsJsonArray().isEmpty()) ? "Не указано" : getElementAsString(currentElement);
    }


    private static String processJsonArray(JsonArray jsonArray, String propertyName) {
        List<String> extractedValues = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            // Handle arrays of simple string values
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                extractedValues.add(element.getAsString());
            }
            // Handle arrays of JSON objects
            else if (element.isJsonObject()) {
                JsonObject jsonObject = element.getAsJsonObject();

                // If a propertyName is provided, extract it
                if (!propertyName.isEmpty() && jsonObject.has(propertyName)) {
                    extractedValues.add(getElementAsString(jsonObject.get(propertyName)));
                }

                // Additional specific handling for nested 'level' objects
                if (jsonObject.has("level") && jsonObject.get("level").isJsonObject()) {
                    JsonObject levelObject = jsonObject.getAsJsonObject("level");
                    if (levelObject.has("name")) {
                        String levelName = getElementAsString(levelObject.get("name"));
                        String valueWithLevel = !extractedValues.isEmpty() ? extractedValues.remove(extractedValues.size() - 1) + ": " + levelName : levelName;
                        extractedValues.add(valueWithLevel);
                    }
                }
            }
        }
        return extractedValues.isEmpty() ? "Не указано" : String.join("; ", extractedValues);
    }


    private static String processJsonArray(JsonArray jsonArray, String primaryPropertyName, String secondaryPropertyName, boolean repeatPrimaryProperty) {
        StringBuilder extractedValues = new StringBuilder();
        boolean isFirst = true;

        for (JsonElement element : jsonArray) {
            if (element.isJsonObject()) {
                JsonObject jsonObject = element.getAsJsonObject();
                String primaryValue = jsonObject.has(primaryPropertyName) ? getElementAsString(jsonObject.get(primaryPropertyName)) : "";
                String secondaryValue;

                // Check if the secondary property is within a nested object
                String[] secondaryPropertyParts = secondaryPropertyName.split("\\.");
                if (secondaryPropertyParts.length > 1 && jsonObject.has(secondaryPropertyParts[0]) && jsonObject.get(secondaryPropertyParts[0]).isJsonObject()) {
                    JsonObject nestedObject = jsonObject.getAsJsonObject(secondaryPropertyParts[0]);
                    secondaryValue = nestedObject.has(secondaryPropertyParts[1]) ? getElementAsString(nestedObject.get(secondaryPropertyParts[1])) : "";
                } else {
                    secondaryValue = jsonObject.has(secondaryPropertyName) ? getElementAsString(jsonObject.get(secondaryPropertyName)) : "";
                }

                if (!secondaryValue.isEmpty()) {
                    if (!extractedValues.isEmpty()) {
                        extractedValues.append("; ");
                    }
                    if (repeatPrimaryProperty || isFirst) {
                        extractedValues.append(primaryValue).append(": ");
                    }
                    extractedValues.append(secondaryValue);
                    isFirst = false;
                }
            }
        }
        return extractedValues.toString();
    }



    private static String processContactArray(JsonArray jsonArray) {
        List<String> contactDetails = new ArrayList<>();

        for (JsonElement element : jsonArray) {
            if (element.isJsonObject()) {
                JsonObject contactObj = element.getAsJsonObject();
                JsonElement valueElement = contactObj.get("value");

                if (valueElement != null && !valueElement.isJsonNull()) {
                    if (valueElement.isJsonPrimitive()) {
                        // Directly add string values like email
                        contactDetails.add(valueElement.getAsString());
                    } else if (valueElement.isJsonObject()) {
                        // For phone numbers, extract the 'formatted' field
                        JsonObject valueObject = valueElement.getAsJsonObject();
                        if (valueObject.has("formatted")) {
                            contactDetails.add(valueObject.get("formatted").getAsString());
                        }
                    }
                }
            }
        }
        if (contactDetails.isEmpty()) {
            return "Не указано";
        }
        return String.join("; ", contactDetails);
    }

    private static String processEducation(JsonObject educationObj) {
        List<String> educationDetails = new ArrayList<>();

        addEducationDetail(educationObj, "level", "name", "Уровень образования", educationDetails);
        addEducationArrayDetail(educationObj, "primary", "Образование", educationDetails);
        addEducationArrayDetail(educationObj, "additional", "Дополнительное образование", educationDetails);
        addSimpleEducationArrayDetails(educationObj, "elementary", "Начальное образование", educationDetails);
        addSimpleEducationArrayDetails(educationObj, "attestation", "Аттестация", educationDetails);

        return educationDetails.isEmpty() ? "Не указано" : String.join("; ", educationDetails);
    }

    private static void addEducationDetail(JsonObject obj, String objKey, String propKey, String label, List<String> details) {
        if (obj.has(objKey) && obj.get(objKey).isJsonObject()) {
            String value = getJsonString(obj.getAsJsonObject(objKey), propKey);
            if (!value.isEmpty()) {
                details.add(label + ": " + value);
            }
        }
    }

    private static void addEducationArrayDetail(JsonObject educationObj, String fieldName, String fieldLabel, List<String> educationDetails) {
        if (educationObj.has(fieldName) && educationObj.get(fieldName).isJsonArray()) {
            String fieldData = processEducationArray(educationObj.getAsJsonArray(fieldName), fieldLabel);
            if (!fieldData.isEmpty()) {
                educationDetails.add(fieldData);
            }
        }
    }

    private static void addSimpleEducationArrayDetails(JsonObject obj, String fieldName, String label, List<String> details) {
        if (obj.has(fieldName) && obj.get(fieldName).isJsonArray()) {
            for (JsonElement element : obj.getAsJsonArray(fieldName)) {
                if (element.isJsonObject()) {
                    StringBuilder entry = new StringBuilder(label + ": ");
                    JsonObject eduObj = element.getAsJsonObject();
                    String name = getJsonString(eduObj, "name");
                    String age = eduObj.has("age") ? String.valueOf(eduObj.get("age").getAsInt()) : "";
                    if (!name.isEmpty()) entry.append(name);
                    if (!age.isEmpty()) {
                        if (!name.isEmpty()) entry.append(", ");
                        entry.append("Возраст: ").append(age);
                    }
                    if (!name.isEmpty() || !age.isEmpty()) details.add(entry.toString());
                }
            }
        }
    }

    private static String processEducationArray(JsonArray educationArray, String educationType) {
        List<String> educationArrayDetails = new ArrayList<>();
        for (JsonElement element : educationArray) {
            if (element.isJsonObject()) {
                JsonObject eduObj = element.getAsJsonObject();
                List<String> entryDetails = new ArrayList<>();

                String name = getJsonString(eduObj, "name");
                String year = eduObj.has("year") ? String.valueOf(eduObj.get("year").getAsInt()) : "";
                String yearLabel = educationType.equals("Дополнительное образование") ? "Дата обучения" : "Год выпуска";
                String result = getJsonString(eduObj, "result");
                String organization = getJsonString(eduObj, "organization");

                if (!name.isEmpty()) {
                    if (educationType.equals("Основное образование")) {
                        entryDetails.add("Образовательное учреждение: " + name);
                    } else {
                        entryDetails.add(name);
                    }
                }
                if (!year.isEmpty()) entryDetails.add(yearLabel + ": " + year);
                if (!result.isEmpty() && educationType.equals("Основное образование")) entryDetails.add("Специализация: " + result);
                if (!organization.isEmpty() && educationType.equals("Основное образование")) entryDetails.add("Факультет: " + organization);

                if (!entryDetails.isEmpty()) educationArrayDetails.add(educationType + ": " + String.join(", ", entryDetails));
            }
        }
        return String.join("; ", educationArrayDetails);
    }



    private static String getJsonString(JsonObject jsonObject, String property) {
        return jsonObject.has(property) && !jsonObject.get(property).isJsonNull() ? jsonObject.get(property).getAsString() : "";
    }



    private static String getElementAsString(JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return "Не указано";
        } else if (element.isJsonPrimitive()) {
            return element.getAsString();
        } else {
            return element.toString();
        }
    }



    private static int safeGetInt(JsonObject jsonObject, String property) {
        if (jsonObject.has(property) && !jsonObject.get(property).isJsonNull()) {
            return jsonObject.get(property).getAsInt();
        }
        return 0;
    }
    private static float safeGetFloat(JsonObject jsonObject, String property) {
        if (jsonObject.has(property) && !jsonObject.get(property).isJsonNull()) {
            return jsonObject.get(property).getAsFloat();
        }
        return 0;
    }

    private static boolean safeGetBool(JsonObject jsonObject, String property) {
        if (jsonObject.has(property) && !jsonObject.get(property).isJsonNull()) {
            return jsonObject.get(property).getAsBoolean();
        }
        return false;
    }
}

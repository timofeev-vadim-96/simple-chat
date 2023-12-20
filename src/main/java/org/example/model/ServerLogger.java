package org.example.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс логгера (также сохраняет историю переписки)
 */
public class ServerLogger {
    Path loggerFilePath;
    Path historyPath;

    /**
     * Конструктор логгера
     *
     * @param serverLogger имя файла, в который будет производиться запись логов
     * @param historyFile  имя файла, в который будет производиться запись истории чата
     */
    public ServerLogger(String serverLogger, String historyFile) {
        loggerFilePath = Paths.get(serverLogger);
        historyPath = Paths.get(historyFile);
        try {
            if (!Files.exists(loggerFilePath)) Files.createFile(loggerFilePath);
            else {
                Files.delete(loggerFilePath);
                Files.createFile(loggerFilePath);
            }
            if (!Files.exists(historyPath)) Files.createFile(historyPath);
            else {
                Files.delete(historyPath);
                Files.createFile(historyPath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для записи логов в файл с временем события
     *
     * @param text   сообщение
     * @param append параметр, указывающий на необходимость перезаписи файла, либо дозаписи в него
     */
    public void writerLogg(String text, boolean append) {
        try (FileWriter fileWriter = new FileWriter(loggerFilePath.toFile(), append)) {
            fileWriter.write(LocalDateTime.now() + ": " + text); //в логфайл дополнительно записывается текущее время
        } catch (IOException e) {
            throw new RuntimeException(String.format("Неудачная попытка записи в логфайл %s!",
                    loggerFilePath.getFileName()) + e);
        }
    }

    /**
     * Метод для записи истории сообщений в файл для дальнейшего восстановления переписки
     *
     * @param text   сообщение
     * @param append параметр, указывающий на необходимость перезаписи файла, либо дозаписи в него
     */
    public void writeHistory(String text, boolean append) {
        try (FileWriter fileWriter = new FileWriter(historyPath.toFile(), append)) {
            fileWriter.write(text);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Неудачная попытка записи в историю сообщений %s!",
                    historyPath.getFileName()) + e);
        }
    }

    /**
     * Метод для возвращения списка сообщений из истории переписки
     *
     * @return список сообщений истории переписки
     */
    public List<String> getHistory() {
        List<String> history = new ArrayList<>();
        try (FileReader fileReader = new FileReader(historyPath.toFile());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                history.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("Ошибка при попытке чтения файла истории чата %s!", historyPath) + e);
        }
        return history;
    }
}

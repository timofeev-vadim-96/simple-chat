package org.example.dao.impl;

import org.example.dao.Logger;

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
public class ServiceLogger implements Logger {
    Path loggerFilePath;

    /**
     * Конструктор служебного логгера
     * @param loggerFilePath путь к файлу
     */
    public ServiceLogger(String loggerFilePath) {
        this.loggerFilePath = Paths.get(loggerFilePath);

        try {
            if (!Files.exists(this.loggerFilePath)) Files.createFile(this.loggerFilePath);
            else {
                Files.delete(this.loggerFilePath);
                Files.createFile(this.loggerFilePath);
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
    public void write(String text, boolean append) {
        try (FileWriter fileWriter = new FileWriter(loggerFilePath.toFile(), append)) {
            fileWriter.write(LocalDateTime.now() + ": " + text); //в логфайл дополнительно записывается текущее время
        } catch (IOException e) {
            throw new RuntimeException(String.format("Неудачная попытка записи в логфайл %s!",
                    loggerFilePath.getFileName()) + e);
        }
    }

    /**
     * Метод для возвращения списка логов из истории переписки
     *
     * @return список логов истории переписки с точным временем
     */
    public List<String> getLogs() {
        List<String> history = new ArrayList<>();
        try (FileReader fileReader = new FileReader(loggerFilePath.toFile());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                history.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("Ошибка при попытке чтения файла истории чата %s!", loggerFilePath) + e);
        }
        return history;
    }
}

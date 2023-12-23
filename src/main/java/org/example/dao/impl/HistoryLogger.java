package org.example.dao.impl;

import org.example.dao.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HistoryLogger implements Logger {
    Path historyFilePath;

    /**
     * Конструктор логгера истории
     *
     * @param historyFilePath путь к файлу
     */
    public HistoryLogger(String historyFilePath) {
        this.historyFilePath = Paths.get(historyFilePath);
        try {
            if (!Files.exists(this.historyFilePath)) Files.createFile(this.historyFilePath);
            else {
                Files.delete(this.historyFilePath);
                Files.createFile(this.historyFilePath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для записи истории сообщений в файл для дальнейшего восстановления переписки
     *
     * @param text   сообщение
     * @param append параметр, указывающий на необходимость перезаписи файла, либо дозаписи в него
     */
    public void write(String text, boolean append) {
        try (FileWriter fileWriter = new FileWriter(historyFilePath.toFile(), append)) {
            fileWriter.write(text);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Неудачная попытка записи в историю сообщений %s!",
                    historyFilePath.getFileName()) + e);
        }
    }

    /**
     * Метод для возвращения списка сообщений из истории переписки
     *
     * @return список сообщений истории переписки
     */
    public List<String> getLogs() {
        List<String> history = new ArrayList<>();
        try (FileReader fileReader = new FileReader(historyFilePath.toFile());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                history.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("Ошибка при попытке чтения файла истории чата %s!", historyFilePath) + e);
        }
        return history;
    }
}

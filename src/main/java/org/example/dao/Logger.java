package org.example.dao;

import java.util.List;

public interface Logger {
    void write(String text, boolean append);
    List<String> getLogs();
}

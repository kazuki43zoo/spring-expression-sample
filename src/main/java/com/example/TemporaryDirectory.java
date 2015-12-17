package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.Serializable;

@Component
public class TemporaryDirectory implements Serializable{
    private static final long serialVersionUID = 1L;
    private final File directory;

    @Autowired
    public TemporaryDirectory(
            @Value("file://#{systemProperties['java.io.tmpdir']}/app") File baseDirectory,
            @Value("#{T(java.util.UUID).randomUUID().toString()}") String id) {
        this.directory = new File(baseDirectory, id);
    }

    @PostConstruct
    public void createDirectory() {
        directory.mkdirs();
    }

    @PreDestroy
    public void removeDirectory() {
        FileSystemUtils.deleteRecursively(directory);
        directory.delete();
    }

    public File getDirectory() {
        return directory;
    }

}

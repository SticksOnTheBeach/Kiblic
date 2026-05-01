package com.sticksonthebeach.syncapp.model;

/**
 * Holds user-specific application configuration (repository path, Drive folder name, etc.).
 * TODO: implement load/save from a config file (e.g., JSON or .properties).
 */
public class Config {

    private String repositoryPath;
    private String driveFolderName;

    public String getRepositoryPath() {
        return repositoryPath;
    }

    public void setRepositoryPath(String repositoryPath) {
        this.repositoryPath = repositoryPath;
    }

    public String getDriveFolderName() {
        return driveFolderName;
    }

    public void setDriveFolderName(String driveFolderName) {
        this.driveFolderName = driveFolderName;
    }
}
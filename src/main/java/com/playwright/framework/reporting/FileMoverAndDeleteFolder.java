package com.playwright.framework.reporting;

import com.playwright.framework.log.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class FileMoverAndDeleteFolder {

    public static void main(String[] args) {
        Path targetRoot = Paths.get("target");

        try {
            // Find the folder with spaces
            File folderWithSpaces = Files.list(targetRoot)
                    .map(Path::toFile)
                    .filter(File::isDirectory)
                    .filter(f -> f.getName().contains("Execution_") && f.getName().contains(" "))
                    .findFirst()
                    .orElse(null);

            if (folderWithSpaces == null) {
                System.out.println("No folder with spaces found in target/");
                return;
            }

            String originalFolderName = folderWithSpaces.getName();

            // Remove spaces to get the cleaned folder name
            String cleanedFolderName = originalFolderName.replaceAll("\\s+", "");
            Path cleanedFolder = targetRoot.resolve(cleanedFolderName);

            // Create the cleaned folder if it doesn't exist
            if (!Files.exists(cleanedFolder)) {
                Files.createDirectories(cleanedFolder);
                System.out.println("Created cleaned folder: " + cleanedFolder);
            } else {
                System.out.println("Cleaned folder already exists: " + cleanedFolder);
            }

            // Move all files from the folder with spaces into the cleaned folder
            File[] filesToMove = folderWithSpaces.listFiles();
            if (filesToMove != null) {
                for (File f : filesToMove) {
                    Path targetPath = cleanedFolder.resolve(f.getName());
                    Files.move(f.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Moved file: " + f.getName() + " -> " + targetPath);
                }
            }

            // Delete the original folder with spaces
            deleteDirectoryRecursively(folderWithSpaces.toPath());
            System.out.println("Deleted original folder with spaces: " + folderWithSpaces);

            System.out.println("Target directory ready for next execution: " + cleanedFolder);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deleteDirectoryRecursively(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted((a, b) -> b.compareTo(a)) // delete children first
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
}

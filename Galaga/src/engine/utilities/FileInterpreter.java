package engine.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import engine.StdDraw;

import java.awt.Color;

public class FileInterpreter {

    private static final String userDir = System.getProperty("user.dir");

    public static String ReadFile(String fileLocation) {
        Path path = Paths.get(userDir, fileLocation);

        String content = "";

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = null;

            while (((line = reader.readLine()) != null)) {
                content += line + "\n";
            }
        }

        catch (IOException ioe) {
            System.out.println(ioe);
        }

        return content;
    }

    public static void writeInFile(String fileLocation, String data) {
        Path path = Paths.get(userDir, fileLocation);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(data);
        }

        catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    public static boolean existsFileIn(String dir, String fileLocation) {
        Path path = Paths.get(dir, fileLocation);

        return Files.exists(path);
    }

    public static Color charToColor(char c) {
        switch (c) {
            case 'R':
                return StdDraw.RED;
            case 'G':
                return StdDraw.GREEN;
            case 'B':
                return StdDraw.BLUE;
            case 'W':
                return StdDraw.WHITE;
            case 'N':
                return StdDraw.BLACK;
            case 'Y':
                return StdDraw.YELLOW;
            case 'L':
                return StdDraw.BOOK_LIGHT_BLUE;
            default:
                return StdDraw.BLACK;
        }
    }

}
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AttendanceMarker {
    public static boolean checkStudent(String id) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("students.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().startsWith(id + ":")) {
                reader.close();
                return true;
            }
        }
        reader.close();
        return false;
    }

    public static void markPresent(String id) throws IOException {
        String name = getStudentName(id);
        if (name == null) {
            System.out.println(id + " is not a registered student.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);

        BufferedWriter writer = new BufferedWriter(new FileWriter("attendance.txt", true));
        writer.write(name + " - " + id + " - Present - " + timestamp + "\n");
        writer.close();
    }

    public static String getStudentName(String id) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("students.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().startsWith(id + ":")) {
                reader.close();
                return line.split(":", 2)[1].trim();
            }
        }
        reader.close();
        return null;
    }
}

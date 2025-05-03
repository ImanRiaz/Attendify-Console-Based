import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class AttendanceMarker {
    public static boolean checkStudent(String id) throws IOException {
        List<String> students = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("students.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            students.add(line.trim());
        }
        reader.close();
        return students.contains(id);
    }

    public static void markPresent(String id) throws IOException {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        BufferedWriter writer = new BufferedWriter(new FileWriter("attendance.txt", true));
        writer.write(id + " - Present at " + timestamp);
        writer.newLine(); // Add line break
        writer.close();
    }
}

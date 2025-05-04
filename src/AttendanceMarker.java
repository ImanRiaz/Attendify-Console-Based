import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AttendanceMarker {
    public static Map<String, String> loadStudents() throws IOException {
        Map<String, String> studentMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader("students.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":");
            if (parts.length == 2) {
                studentMap.put(parts[0].trim(), parts[1].trim());
            }
        }
        reader.close();
        return studentMap;
    }

    public static boolean isStudentEnrolledInSubject(String studentId, String subject) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("subjects.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":");
            if (parts.length == 2) {
                String subjectName = parts[0].trim();
                String[] studentIds = parts[1].split(",");
                if (subjectName.equals(subject)) {
                    for (String id : studentIds) {
                        if (id.trim().equals(studentId)) {
                            reader.close();
                            return true;
                        }
                    }
                }
            }
        }
        reader.close();
        return false;
    }

    public static void markSubjectAttendance(String studentId, String studentName, String subject) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("attendance_" + subject + ".txt", true));
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        writer.write(studentName + " - " + studentId + " - Present - " + time + "\n");
        writer.close();
    }
}

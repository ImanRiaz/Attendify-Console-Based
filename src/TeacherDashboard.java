import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class TeacherDashboard {
    public static void showDashboard(String subject) {
        String enrolledFile = "student_" + subject.toLowerCase() + ".txt";
        String attendanceFile = subject + ".txt";

        Set<String> enrolledStudents = new HashSet<>();
        Map<String, List<String>> attendanceByDate = new TreeMap<>();

        // Load enrolled students
        try (BufferedReader reader = new BufferedReader(new FileReader(enrolledFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 1) {
                    enrolledStudents.add(parts[0].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading enrolled students file.");
            return;
        }

        // Group attendance entries by date
        try (BufferedReader reader = new BufferedReader(new FileReader(attendanceFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length >= 4) {
                    String dateOnly = parts[3].split(" ")[0];
                    attendanceByDate.putIfAbsent(dateOnly, new ArrayList<>());
                    attendanceByDate.get(dateOnly).add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading attendance file.");
            return;
        }

        // Display class-wise stats
        System.out.println("\nClass Attendance History:");
        int classCount = 1;
        for (String date : attendanceByDate.keySet()) {
            List<String> entries = attendanceByDate.get(date);
            Set<String> markedIds = new HashSet<>();

            int present = 0;
            int leave = 0;

            for (String entry : entries) {
                String[] parts = entry.split(" - ");
                if (parts.length >= 4) {
                    String studentId = parts[1].trim();
                    String status = parts[2].trim();

                    markedIds.add(studentId);

                    if (status.equalsIgnoreCase("Present")) present++;
                    else if (status.equalsIgnoreCase("Leave")) leave++;
                }
            }

            int totalEnrolled = enrolledStudents.size();
            int absent = totalEnrolled - present - leave;

            String leaveText = (leave == 0) ? "No Leave" : leave + " Leave";
            System.out.println("Class " + classCount + ": " + date + " - " + present + " Present, " + absent + " Absent, " + leaveText);
            classCount++;
        }

        System.out.println("===============================================");
    }
}

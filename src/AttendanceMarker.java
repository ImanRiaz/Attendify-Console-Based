import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class AttendanceMarker {

    public static boolean checkStudent(String studentId) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                String[] studentData = line.split(":");
                if (studentData[0].equals(studentId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void markAttendance(String studentId, String subject, String teacherId) throws IOException {
        String fileName = subject + ".txt";
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        if (isMarkedToday(studentId, subject)) {
            System.out.println("Attendance already marked today for " + studentId);
            return;
        }

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String entry = studentId + " - Present - " + date;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(entry + "\n");
            System.out.println("Attendance marked for student " + studentId);
        }
    }

    public static boolean isMarkedToday(String studentId, String subject) throws IOException {
        String fileName = subject + ".txt";
        File file = new File(fileName);
        if (!file.exists()) return false;

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length >= 3) {
                    String id = parts[0].contains(" ") ? parts[1].trim() : parts[0].trim();
                    String datePart = parts[parts.length - 1].split(" ")[0];
                    if (id.equals(studentId) && datePart.equals(today)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static String getAttendance(String studentId, String subject) throws IOException {
        String fileName = subject + ".txt";
        File file = new File(fileName);
        if (!file.exists()) {
            return "No attendance data available for " + subject;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int totalClasses = 0;
            int presentClasses = 0;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" - ");
                if (data.length >= 2) {
                    String id = data[0].contains(" ") ? data[1].trim() : data[0].trim();
                    if (id.equals(studentId)) {
                        totalClasses++;
                        if (data[1].equals("Present") || data[2].equals("Present")) {
                            presentClasses++;
                        }
                    }
                }
            }

            if (totalClasses == 0) {
                return "No classes attended yet.";
            }

            double attendancePercentage = ((double) presentClasses / totalClasses) * 100;
            return "Attendance: " + presentClasses + "/" + totalClasses + " (" + String.format("%.2f", attendancePercentage) + "%)";
        }
    }

    public static Map<String, String> loadStudents() throws IOException {
        Map<String, String> studentMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    String studentId = parts[0].trim();
                    String studentName = parts[1].trim();
                    studentMap.put(studentId, studentName);
                }
            }
        }
        return studentMap;
    }

    public static boolean isStudentEnrolledInSubject(String studentId, String subject) {
        String fileName = "student_" + subject.toLowerCase() + ".txt";
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Enrollment file " + fileName + " does not exist.");
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                String[] parts = line.split(":");
                if (parts[0].equals(studentId)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void markSubjectAttendance(String studentId, String studentName, String subject) throws IOException {
        String fileName = subject + ".txt";
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        if (isMarkedToday(studentId, subject)) {
            System.out.println("Attendance already marked today for " + studentId);
            return;
        }

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String entry = studentName + " - " + studentId + " - Present - " + date;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(entry + "\n");
        }
    }
}

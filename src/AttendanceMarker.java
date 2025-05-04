import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

public class AttendanceMarker {

    // Check if the student is registered in the system
    public static boolean checkStudent(String studentId) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();  // Trim to remove leading/trailing spaces
                String[] studentData = line.split(":");
                if (studentData[0].equals(studentId)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Mark attendance for the student in the teacher's subject file
    public static void markAttendance(String studentId, String subject, String teacherId) throws IOException {
        // Check if the teacher's subject file exists
        String fileName = subject + ".txt";
        File file = new File(fileName);
        boolean isNewFile = false;

        // If file does not exist, create a new one
        if (!file.exists()) {
            file.createNewFile();
            isNewFile = true;
        }

        // Append attendance entry to the teacher's subject file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String entry = studentId + " - Present - " + date;

            if (!isNewFile) {
                // Check if the student has already been marked present for the class
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(studentId) && line.contains("Present")) {
                            System.out.println("Attendance already marked for " + studentId);
                            return;
                        }
                    }
                }
            }

            // Write the attendance entry if not already marked
            writer.write(entry + "\n");
            System.out.println("Attendance marked for student " + studentId);
        }
    }

    // Calculate attendance percentage for the student in the subject
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
                if (data[0].equals(studentId)) {
                    totalClasses++;
                    if (data[1].equals("Present")) {
                        presentClasses++;
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

    // Load the students from the 'students.txt' file into a map (studentId -> studentName)
    public static Map<String, String> loadStudents() throws IOException {
        Map<String, String> studentMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();  // Trim spaces
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    String studentId = parts[0].trim();
                    String studentName = parts[1].trim();
                    studentMap.put(studentId, studentName);  // Add to the map
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
                String[] parts = line.split(":");  // Handles both "1001" and "1001:Ali Khan"
                if (parts[0].equals(studentId)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return false;
    }
    
    

    // Mark attendance in a subject file with student name and subject details
    public static void markSubjectAttendance(String studentId, String studentName, String subject) throws IOException {
        String fileName = subject + ".txt";
        File file = new File(fileName);
        boolean isNewFile = false;

        // If file does not exist, create a new one
        if (!file.exists()) {
            file.createNewFile();
            isNewFile = true;
        }

        // Append attendance entry to the subject file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String entry = studentName + " - " + studentId + " - Present - " + date;

            if (!isNewFile) {
                // Check if the student has already been marked present for the class
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(studentId) && line.contains("Present")) {
                            System.out.println("Attendance already marked for " + studentId);
                            return;
                        }
                    }
                }
            }

            // Write the attendance entry if not already marked
            writer.write(entry + "\n");
         //   System.out.println("Attendance marked for student " + studentName + " in subject " + subject);
        }
    }
}

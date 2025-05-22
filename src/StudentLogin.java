import java.io.*;
import java.util.Scanner;

public class StudentLogin {

    private static int failedAttempts = 0;
    private static int lockoutDuration = 60; // seconds

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String studentId = null;

        while (true) {
            System.out.print("Enter Student ID: ");
            studentId = scanner.nextLine().trim();

            System.out.print("Enter Password: ");
            String password = scanner.nextLine().trim();

            if (validateStudentLogin(studentId, password)) {
                System.out.println("Login successful!");
                break;
            } else {
                failedAttempts++;
                System.out.println("Invalid credentials. Attempt " + failedAttempts + "/3");

                if (failedAttempts % 3 == 0) {
                    System.out.println("Too many failed attempts. Try again after " + lockoutDuration + " seconds:");
                    countdown(lockoutDuration);
                    lockoutDuration += 60;
                }
            }
        }

        // Show dashboard
        showStudentDashboard(studentId);
    }

    private static boolean validateStudentLogin(String studentId, String password) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] studentData = line.split(":");
                if (studentData.length < 3) continue; // skip malformed lines

                String id = studentData[0].trim();
                String storedPassword = studentData[2].trim();

                if (id.equals(studentId) && storedPassword.equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void countdown(int seconds) throws InterruptedException {
        for (int i = seconds; i > 0; i--) {
            System.out.print("\rPlease wait: " + i + " seconds ");
            Thread.sleep(1000);
        }
        System.out.print("\rYou can try logging in again now.            \n");
    }

    private static void showStudentDashboard(String studentId) throws IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Student Dashboard =====");
            System.out.println("1. View Attendance Summary");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                displayAttendanceSummary(studentId);
            } else if (choice.equals("2")) {
                System.out.println("Logging out...");
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void displayAttendanceSummary(String studentId) throws IOException {
        File subjectsFile = new File("subjects.txt");
        if (!subjectsFile.exists()) {
            System.out.println("No subjects found.");
            return;
        }

        try (BufferedReader subjectReader = new BufferedReader(new FileReader(subjectsFile))) {
            String line;
            while ((line = subjectReader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length < 2) continue;

                String subjectName = parts[0].trim();
                String studentList = parts[1];

                // Check if the student is enrolled in the subject
                if (!studentList.contains(studentId)) continue;

                String attendanceSummary = getAttendanceForSubject(studentId, subjectName);
                System.out.println("\n" + attendanceSummary);
            }
        }
    }

    private static String getAttendanceForSubject(String studentId, String subjectName) throws IOException {
        String attendanceFileName = subjectName.toLowerCase() + ".txt";
        File attendanceFile = new File(attendanceFileName);

        if (!attendanceFile.exists()) {
            return subjectName + ": No attendance data available.";
        }

        try (BufferedReader attendanceReader = new BufferedReader(new FileReader(attendanceFile))) {
            String line;
            int totalClasses = 0;
            int presentClasses = 0;

            while ((line = attendanceReader.readLine()) != null) {
                String[] attendanceData = line.split(" - ");
                if (attendanceData.length < 3) continue;

                String id = attendanceData[1].trim();
                String status = attendanceData[2].trim();

                if (id.equals(studentId)) {
                    totalClasses++;
                    if (status.equalsIgnoreCase("Present")) {
                        presentClasses++;
                    }
                }
            }

            if (totalClasses == 0) {
                return subjectName + ": No attendance data available.";
            }

            double percentage = (presentClasses * 100.0) / totalClasses;

            String summary = subjectName + ": Total Classes = " + totalClasses +
                    ", Present = " + presentClasses +
                    ", Absent = " + (totalClasses - presentClasses) +
                    "\nAttendance percentage: " + String.format("%.2f", percentage) + "%";

            if (percentage < 75) {
                summary += "\n You are not eligible for exams in " + subjectName;
            }

            return summary;
        }
    }
}

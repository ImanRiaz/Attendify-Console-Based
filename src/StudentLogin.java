import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class StudentLogin {

    private static int failedAttempts = 0;
    private static int lockoutDuration = 60; // seconds

    public static void main(String[] args) throws IOException, InterruptedException {
        try (Scanner scanner = new Scanner(System.in)) {
            String studentId = null;

            while (true) {
                System.out.println("+----------------------------------------+");
                System.out.println("|            STUDENT LOGIN PORTAL        |");
                System.out.println("+----------------------------------------+");

                System.out.print("Enter Student ID: ");
                studentId = scanner.nextLine().trim();

                System.out.print("Enter Password: ");
                String password = scanner.nextLine().trim();

                if (validateStudentLogin(studentId, password)) {
                    System.out.println("\n+-----------------------------+");
                    System.out.println("|     Login successful!      |");
                    System.out.println("+-----------------------------+");
                    break;
                } else {
                    failedAttempts++;
                    System.out.println("\nInvalid credentials. Attempt " + failedAttempts + "/3");

                    if (failedAttempts % 3 == 0) {
                        System.out.println("Too many failed attempts. Try again after " + lockoutDuration + " seconds:");
                        countdown(lockoutDuration);
                        lockoutDuration += 60;
                    }
                }
            }

            showStudentDashboard(studentId);
        }
    }

    private static boolean validateStudentLogin(String studentId, String password) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] studentData = line.split(":");
                if (studentData.length < 3) continue;

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
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n===========================================");
                System.out.println("|           STUDENT DASHBOARD            |");
                System.out.println("===========================================");
                System.out.println("1. View Attendance Summary");
                System.out.println("2. Exit");
                System.out.print("Enter your choice: ");
                String choice = scanner.nextLine().trim();

                if (choice.equals("1")) {
                    displayAttendanceSummary(studentId);
                } else if (choice.equals("2")) {
                    System.out.println("\n+--------------------+");
                    System.out.println("| Logging out...     |");
                    System.out.println("+--------------------+");
                    break;
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
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

                if (!studentList.contains(studentId)) continue;

                String attendanceSummary = getAttendanceForSubject(studentId, subjectName);
                System.out.println("\n+-------------------------------------------+");
                System.out.println(attendanceSummary);
                System.out.println("+-------------------------------------------+");
            }
        }
    }

    private static String getAttendanceForSubject(String studentId, String subjectName) throws IOException {
        String attendanceFileName = subjectName.toLowerCase() + ".txt";
        File attendanceFile = new File(attendanceFileName);

        if (!attendanceFile.exists()) {
            return subjectName + ": No attendance data available.";
        }

        Map<String, Set<String>> dateAttendanceMap = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());

        try (BufferedReader attendanceReader = new BufferedReader(new FileReader(attendanceFile))) {
            String line;
            while ((line = attendanceReader.readLine()) != null) {
                String[] data = line.split(" - ");
                if (data.length < 4) continue;

                String id = data[1].trim();
                String status = data[2].trim();
                String datetime = data[3].trim();
                String dateOnly = datetime.split(" ")[0];

                dateAttendanceMap.putIfAbsent(dateOnly, new HashSet<>());
                if (status.equalsIgnoreCase("Present")) {
                    dateAttendanceMap.get(dateOnly).add(id);
                }
            }
        }

        int totalDays = dateAttendanceMap.size();
        int presentDays = 0;

        for (String date : dateAttendanceMap.keySet()) {
            if (dateAttendanceMap.get(date).contains(studentId)) {
                presentDays++;
            }
        }

        double percentage = totalDays == 0 ? 0 : (presentDays * 100.0) / totalDays;

        StringBuilder summary = new StringBuilder();
        summary.append(subjectName + ": Total = " + totalDays)
               .append(", Present = " + presentDays)
               .append(", Absent = " + (totalDays - presentDays))
               .append("\nAttendance %: " + String.format("%.2f", percentage) + "%");

        if (percentage >= 75) {
            summary.append("\nEligible for exams in " + subjectName + ".");
        } else {
            summary.append("\n*** Not eligible for exams in " + subjectName + " ***");
        }

        if (dateAttendanceMap.containsKey(today)) {
            if (!dateAttendanceMap.get(today).contains(studentId)) {
                summary.append("\nStatus today: Absent");
            } else {
                summary.append("\nStatus today: Present");
            }
        } else {
            summary.append("\nStatus today: Attendance not marked yet");
        }

        return summary.toString();
    }
} 

import java.io.*;

public class StudentLogin {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.print("Enter Student ID: ");
        String studentId = reader.readLine().trim();

        System.out.print("Enter Password: ");
        String password = reader.readLine().trim();

        if (validateStudentLogin(studentId, password)) {
            System.out.println("Login successful!");
            displayAttendanceSummary(studentId);
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private static boolean validateStudentLogin(String studentId, String password) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] studentData = line.split(":");
                if (studentData.length < 3) continue; // skip malformed lines

                String id = studentData[0];
                String storedPassword = studentData[2];

                if (id.equals(studentId) && storedPassword.equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void displayAttendanceSummary(String studentId) throws IOException {
        try (BufferedReader subjectReader = new BufferedReader(new FileReader("subjects.txt"))) {
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
        String attendanceFileName = subjectName.toLowerCase() + ".txt"; // ← FIXED here
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

                String id = attendanceData[1];
                String status = attendanceData[2];

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
                summary += "\n❗ You are not eligible for exams in " + subjectName;
            }

            return summary;
        }
    }
}

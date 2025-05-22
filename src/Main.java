import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int failedAttempts = 0;
            int lockMultiplier = 0;

            String subject = null;

            // Login loop with retry and incremental lockout
            while (true) {
                System.out.print("Enter teacher username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                try {
                    subject = TeacherAuth.login(username, password);

                    if (subject != null) {
                        System.out.println("Login successful. Subject: " + subject);
                        break; // exit loop after successful login
                    } else {
                        failedAttempts++;
                        System.out.println("Invalid login.");

                        if (failedAttempts % 3 == 0) {
                            lockMultiplier++;
                            int lockTimeSeconds = lockMultiplier * 60;

                    for (int i = lockTimeSeconds; i > 0; i--) {
                       System.out.print("\rAccount locked. Try again after: " + i + " seconds");
                        System.out.flush();
                      Thread.sleep(1000);
                    }
                    System.out.print("\r                                      \r"); // Clear the line after countdown

                        }
                    }

                } catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // Post-login: dashboard menu
            System.out.println("\nWelcome to the Teacher Dashboard for subject: " + subject);
            System.out.println("Choose an option:");
            System.out.println("1. View Class Attendance History");
            System.out.println("2. Start Attendance");

            System.out.print("Enter choice (1 or 2): ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                TeacherDashboard.showDashboard(subject);
                return;
            }

            // If attendance is selected
            System.out.println("Waiting for QR code...");

            try {
                Map<String, String> studentMap = AttendanceMarker.loadStudents();

                long lastScanTime = System.currentTimeMillis();
                long timeout = 3 * 60 * 1000; // 3 minutes

                while (true) {
                    if (System.currentTimeMillis() - lastScanTime > timeout) {
                        System.out.println("Timeout: No QR code detected for 3 minutes. Exiting...");
                        break;
                    }

                    BufferedImage frame = WebcamReader.captureFrame();

                    if (frame != null) {
                        String qrData = QRDecoder.decode(frame);

                        if (qrData != null && !qrData.isEmpty()) {
                            lastScanTime = System.currentTimeMillis();

                            String studentId = extractStudentId(qrData);

                            if (studentId == null || !studentMap.containsKey(studentId)) {
                                System.out.println("Student not found in student records.");
                                Thread.sleep(2000);
                                continue;
                            }

                            boolean enrolled = AttendanceMarker.isStudentEnrolledInSubject(studentId, subject);
                            if (!enrolled) {
                                System.out.println("Student is not enrolled in your subject.");
                                Thread.sleep(2000);
                                continue;
                            }

                            String studentName = studentMap.get(studentId);
                            AttendanceMarker.markSubjectAttendance(studentId, studentName, subject);
                            System.out.println(studentName + " (" + studentId + ") marked present for " + subject);
                            Thread.sleep(3000);
                        }
                    }

                    Thread.sleep(500);
                }

            } catch (Exception e) {
                System.out.println("An error occurred during attendance: " + e.getMessage());
                e.printStackTrace();
            } finally {
                WebcamReader.releaseCamera();
                System.out.println("Camera released. Exiting...");
            }
        }
    }

    // Robust QR text parser
    private static String extractStudentId(String qrData) {
        try {
            if (qrData != null && qrData.startsWith("MECARD:N:")) {
                qrData = qrData.substring(9, qrData.length() - 2).trim();
                return qrData;
            }
            return qrData.trim();
        } catch (Exception e) {
            System.out.println("Failed to parse student ID from QR data.");
        }
        return null;
    }
}

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Teacher login
            System.out.print("Enter teacher username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            try {
                // Authenticate teacher
                String subject = TeacherAuth.login(username, password);
                if (subject == null) {
                    System.out.println("Invalid login. Exiting...");
                    return;
                }

                System.out.println("Login successful. Subject: " + subject);
                System.out.println("Waiting for QR code...");

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
                           // System.out.println("QR Detected: " + qrData);
                            lastScanTime = System.currentTimeMillis(); // Reset timeout timer

                            // Extract student ID from QR code
                            String studentId = extractStudentId(qrData);

                            if (studentId == null || !studentMap.containsKey(studentId)) {
                                System.out.println(" Student not found in student records.");
                                Thread.sleep(2000);
                                continue; // Keep scanning
                            }

                            boolean enrolled = AttendanceMarker.isStudentEnrolledInSubject(studentId, subject);
                            if (!enrolled) {
                                System.out.println(" Student is not enrolled in your subject.");
                                Thread.sleep(2000);
                                continue;
                            }

                            String studentName = studentMap.get(studentId);
                            AttendanceMarker.markSubjectAttendance(studentId, studentName, subject);
                            System.out.println( studentName + " (" + studentId + ") marked present for " + subject);
                            Thread.sleep(3000); // Optional delay before next scan
                        }
                    }

                    Thread.sleep(500);
                }

            } catch (Exception e) {
                System.out.println(" An error occurred: " + e.getMessage());
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
                // Remove the "MECARD:N:" part and the ";;" at the end
                qrData = qrData.substring(9, qrData.length() - 2).trim(); // Extract the student ID correctly
              //  System.out.println("Extracted Student ID: " + qrData);
                return qrData;
            }
            // If raw student ID, return it directly (just in case)
            return qrData.trim();
        } catch (Exception e) {
            System.out.println("Failed to parse student ID from QR data.");
        }
        return null;
    }
}

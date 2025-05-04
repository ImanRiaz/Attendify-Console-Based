import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Teacher login
        System.out.print("Enter teacher username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String subject = TeacherAuth.login(username, password);
        if (subject == null) {
            System.out.println("Invalid login. Exiting...");
            return;
        }

        System.out.println("Login successful. Subject: " + subject);
        System.out.println("Waiting for QR code...");

        Map<String, String> studentMap = AttendanceMarker.loadStudents();

        while (true) {
            BufferedImage frame = WebcamReader.captureFrame();

            if (frame != null) {
                String qrData = QRDecoder.decode(frame);
                if (qrData != null) {
                    System.out.println("QR Detected: " + qrData);

                    // Extract student ID from MECARD format
                    if (qrData.startsWith("MECARD:N:") && qrData.endsWith(";;")) {
                        qrData = qrData.substring(9, qrData.length() - 2);
                    }

                    if (!studentMap.containsKey(qrData)) {
                        System.out.println("Student not found in student records.");
                        break;
                    }

                    boolean enrolled = AttendanceMarker.isStudentEnrolledInSubject(qrData, subject);
                    if (!enrolled) {
                        System.out.println("Student is not enrolled in your subject.");
                        break;
                    }

                    String studentName = studentMap.get(qrData);
                    AttendanceMarker.markSubjectAttendance(qrData, studentName, subject);
                    System.out.println(studentName + " (" + qrData + ") marked present for " + subject);

                    break; // Stop after one scan
                }
            }

            Thread.sleep(500); // Avoid overload
        }

        WebcamReader.releaseCamera();
        System.out.println("Camera released. Exiting...");
    }
}

import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Waiting for QR code...");

        while (true) {
            BufferedImage frame = WebcamReader.captureFrame();

            if (frame != null) {
                String qrData = QRDecoder.decode(frame);
                if (qrData != null) {
                    System.out.println(" QR Detected: " + qrData);

                    //  Extract student ID if QR is in MECARD format
                    if (qrData.startsWith("MECARD:N:") && qrData.endsWith(";;")) {
                        qrData = qrData.substring(9, qrData.length() - 2); // get only the ID
                    }

                    boolean isRegistered = AttendanceMarker.checkStudent(qrData);
                    if (isRegistered) {
                        AttendanceMarker.markPresent(qrData);
                        System.out.println(qrData + " marked present.");
                    } else {
                        System.out.println(qrData + " is not a registered student.");
                    }
                    break; // Exit after one scan
                }
            }

            Thread.sleep(500); // Avoid overload
        }
    }
}

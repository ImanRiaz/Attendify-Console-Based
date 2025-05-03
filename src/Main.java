import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Main is running");
        System.out.flush();

        Thread.sleep(1000);

        System.out.println("Waiting for QR code...");
        System.out.println("check point A");
        System.out.flush();

        while (true) {
            BufferedImage frame = WebcamReader.captureFrame();

            if (frame != null) {
                String qrData = QRDecoder.decode(frame);
                if (qrData != null) {
                    System.out.println("QR Detected: " + qrData);
                    System.out.println("check point B");

                    // Extract student ID from MECARD format
                    String id = extractID(qrData);
                    if (id != null) {
                        boolean isRegistered = AttendanceMarker.checkStudent(id);
                        if (isRegistered) {
                            AttendanceMarker.markPresent(id);
                            System.out.println( id + " marked present.");
                            System.out.println("check point C");
                        } else {
                            System.out.println( id + " is not a registered student.");
                        }
                        break; // Stop after one valid or invalid scan
                    } else {
                        System.out.println("QR format not recognized.");
                    }
                }
            }

            Thread.sleep(500); // Avoid CPU overload
         //   System.out.println("check point D");
        }

        WebcamReader.releaseCamera();
        System.out.println("Camera released. Exiting...");
    }

    // Helper method to extract student ID from MECARD
    private static String extractID(String qrData) {
        try {
            if (qrData.startsWith("MECARD:N:") && qrData.endsWith(";;")) {
                return qrData.substring(9, qrData.length() - 2).trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

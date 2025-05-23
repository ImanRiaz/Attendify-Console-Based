import java.io.*;
import java.util.*;

public class TeacherDashboard {
    public static void showDashboard(String subject) {
        String enrolledFile = "student_" + subject.toLowerCase() + ".txt";
        String attendanceFile = subject + ".txt";

        Set<String> enrolledStudentIds = new HashSet<>();
        Map<String, String> studentIdToName = new HashMap<>();
        Map<String, List<String>> attendanceByDate = new TreeMap<>();

        // Load enrolled students
        try (BufferedReader reader = new BufferedReader(new FileReader(enrolledFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 2) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    enrolledStudentIds.add(id);
                    studentIdToName.put(id, name);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading enrolled students file.");
            return;
        }

        // Group attendance entries by date
        try (BufferedReader reader = new BufferedReader(new FileReader(attendanceFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length >= 4) {
                    String dateOnly = parts[3].split(" ")[0];
                    attendanceByDate.putIfAbsent(dateOnly, new ArrayList<>());
                    attendanceByDate.get(dateOnly).add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading attendance file.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n========== Class Attendance History ==========");

            List<String> dateList = new ArrayList<>(attendanceByDate.keySet());
            int index = 0;

            while (index < dateList.size()) {
                String date = dateList.get(index);
                List<String> entries = attendanceByDate.get(date);

                int present = 0, leave = 0;
                Set<String> markedIds = new HashSet<>();
                List<String> presentList = new ArrayList<>();
                List<String> leaveList = new ArrayList<>();

                for (String entry : entries) {
                    String[] parts = entry.split(" - ");
                    if (parts.length >= 4) {
                        String name = parts[0].trim();
                        String studentId = parts[1].trim();
                        String status = parts[2].trim();

                        markedIds.add(studentId);

                        if (status.equalsIgnoreCase("Present")) {
                            present++;
                            presentList.add(name + " (" + studentId + ")");
                        } else if (status.equalsIgnoreCase("Leave")) {
                            leave++;
                            leaveList.add(name + " (" + studentId + ")");
                        }
                    }
                }

                int totalEnrolled = enrolledStudentIds.size();
                int absent = totalEnrolled - present - leave;

                List<String> absentList = new ArrayList<>();
                for (String id : enrolledStudentIds) {
                    if (!markedIds.contains(id)) {
                        String name = studentIdToName.getOrDefault(id, "Unknown");
                        absentList.add(name + " (" + id + ")");
                    }
                }

                System.out.println("\nClass " + (index + 1) + ": " + date);
                System.out.println("Present: " + present + " | Absent: " + absent + " | Leave: " + leave);

                // Validate view details input
                String viewDetails;
                while (true) {
                    System.out.print("View student lists? (Y/N): ");
                    viewDetails = scanner.nextLine().trim().toUpperCase();
                    if (viewDetails.equals("Y") || viewDetails.equals("N")) break;
                    System.out.println("Invalid input. Please enter Y or N.");
                }

                if (viewDetails.equals("Y")) {
                    System.out.println("\n>> Present Students:");
                    if (presentList.isEmpty()) System.out.println("  None");
                    else presentList.forEach(s -> System.out.println("  " + s));

                    System.out.println("\n>> Leave Students:");
                    if (leaveList.isEmpty()) System.out.println("  None");
                    else leaveList.forEach(s -> System.out.println("  " + s));

                    System.out.println("\n>> Absent Students:");
                    if (absentList.isEmpty()) System.out.println("  None");
                    else absentList.forEach(s -> System.out.println("  " + s));
                }

                index++;

                if (index >= dateList.size()) {
                    System.out.println("No more records. Returning to main menu...");
                    return;
                }

                // Validate next class navigation input
                String next;
                while (true) {
                    System.out.print("\nDo you want to view the next class? (Y to continue, M for main menu, E to exit): ");
                    next = scanner.nextLine().trim().toUpperCase();
                    if (next.equals("Y") || next.equals("M") || next.equals("E")) break;
                    System.out.println("Invalid input. Please enter Y, M, or E.");
                }

                if (next.equals("Y")) {
                    continue;
                } else if (next.equals("M")) {
                    System.out.println("Returning to main menu...");
                    return;
                } else if (next.equals("E")) {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
            }

            System.out.println("===============================================");
        }
    }
}

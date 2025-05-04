import java.io.*;

public class TeacherAuth {
    public static String login(String username, String password) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("teachers.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                String user = parts[0].trim();
                String pass = parts[1].trim();
                String subject = parts[2].trim();

                if (user.equals(username) && pass.equals(password)) {
                    reader.close();
                    return subject;
                }
            }
        }
        reader.close();
        return null; // Invalid login
    }
}

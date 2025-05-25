**Overview:**
Attendify is a comprehensive QR-based Attendance Management System designed for educational institutions. 
It enables teachers to efficiently mark student attendance using QR code scanning technology, manage attendance records,
and provides students with detailed access to their attendance summaries. The system is built using Java with 
object-oriented programming principles and utilizes file-based storage for data persistence.
**Features:**
**Teacher Authentication & Security:**
•	Secure teacher login with username and password verification
•	Progressive lockout mechanism with increasing countdown timers after every 3 failed   login attempts
•	Account lockout protection to prevent unauthorized access
•	Enhanced security with retry limitations and time-based access control
**QR Code-Based Attendance:**
•	Real-time webcam integration for QR code scanning
•	Automatic student identification through QR code decoding
•	Instant attendance marking with timestamp recording
•	Duplicate attendance prevention for the same session
**Comprehensive Dashboard:**
•	Teacher dashboard with class-wise attendance history
•	Detailed statistics showing present, absent, and leave counts
•	Date-wise attendance tracking and reporting
•	Subject-specific attendance management
**Student Portal:**
•	Student login system with ID and password authentication
•	Personal attendance summary across all enrolled subjects
•	Attendance percentage calculation with exam eligibility status
•	Real-time attendance tracking and updates
**Advanced Features:**
•	Multi-subject support with enrollment verification
•	Automatic attendance percentage calculation
•	Exam eligibility determination based on attendance criteria (75% threshold)
•	Robust error handling and data validation
•	Modular design for easy maintenance and scalability
**Pre-requisites:**
•	Java Development Kit (JDK): Version 8 or higher
•	Integrated Development Environment: Visual Studio Code (recommended) or any Java IDE
•	Webcam: Enabled on your System
•	Required Libraries: 
o	OpenCV library for Java (webcam integration)
o	ZXing library for QR code decoding and processing
•	Operating System: Windows with webcam support
•	QR code: Use Zxing.appspot.com
•	Link:  https://zxing.appspot.com/generator
**Files Required:**
**Core Application Files:**
•	Main.java - Main application entry point and teacher login handler
•	TeacherAuth.java - Teacher authentication and credential verification
•	TeacherDashboard.java - Dashboard for viewing attendance history and statistics
•	AttendanceMarker.java - Core attendance marking and validation logic
•	StudentLogin.java - Student portal and attendance summary display
•	WebcamReader.java - Webcam integration and frame capture functionality
•	QRDecoder.java - QR code decoding and student ID extraction
**Data Storage Files:**
•	students.txt - Student database with ID, name, and password (format: ID:Name:Password)
•	teachers.txt - Teacher credentials and subject assignments (format: Username,Password,Subject)
•	subjects.txt - Subject enrollment mapping (format: Subject:StudentID1,StudentID2,...)
•	student_<subject>.txt - Subject-specific student enrollment lists
•	<subject>.txt - Attendance records for each subject with timestamps



**How to Run:**
Step 1: Environment Setup
1. Install Java JDK 8 or higher on your system
2. Set up your preferred IDE (Visual Studio Code recommended)
3. Download and configure OpenCV library for Java
4. Add ZXing library to your project dependencies
Step 2: Project Setup
1. Create a new Java project in your IDE
2. Copy all Java source files to your project directory
3. Create the required text files (students.txt, teachers.txt, etc.)
4. Ensure your webcam is connected and functional
Step 3: Library Configuration
1. Add OpenCV JAR file to your project classpath
2. Include ZXing core and javase libraries in dependencies
3. Configure native library path for OpenCV
Step 4: Data File Preparation
1. Populate students.txt with student information
2. Add teacher credentials to teachers.txt
3. Configure subject enrollments in subjects.txt and student_<subject>.txt files
Step 5: Application Execution
1. Compile all Java files in your IDE
2. Run Main.java to start the teacher interface
3. Use StudentLogin.java separately for student access
4. Ensure webcam permissions are granted to the application
**How to Use:
Teacher Login Process:**
•	Launch the application and enter your username and password.
•	After 3 consecutive failed attempts, account will be locked with increasing live countdown timers.
•	Lockout duration increases progressively: 1 minute, 2 minutes, 3 minutes, etc.
•	Enhanced security prevents brute force attacks through progressive delays.



**Teacher Dashboard Operations:**
•	Choose option 1 to view detailed class attendance history
•	Select option 2 to start a new attendance marking session
•	Review statistics showing present, absent, and leave counts for each class

**Attendance Marking Process:**
•	Position students' QR codes in front of the webcam
•	System automatically scans and validates student enrollment
•	Attendance is marked instantly with timestamp recording
•	Duplicate entries are prevented for the same session
•	Session automatically times out after 2 minutes of inactivity
**Student Access:**
•	Students log in using their Student ID and password
•	View comprehensive attendance summary for all enrolled subjects
•	Check attendance percentages and exam eligibility status
•	Monitor real-time attendance updates across subjects
Attendance Tracking:
•	Automatic calculation of attendance percentages
•	Exam eligibility determination (75% attendance required)
•	Date-wise attendance history with detailed statistics
•	Subject-wise performance tracking and reporting
________________________________________
**OOP Technologies used:**
The project effectively applies key Object-Oriented Programming (OOP) principles to build a modular, maintainable Java application:
•**	Encapsulation:** Data and related methods are grouped within classes like TeacherAuth, StudentLogin, ensuring controlled access using access modifiers.
•	**Inheritance:** Shared functionality across classes is implemented through base or parent classes, allowing subclasses to reuse and extend core features, especially in tasks like file handling and user role management.
•	**Polymorphism**: Method overriding and overloading allow flexible behavior, especially in user authentication and display methods.
•	**Abstraction:** Complex operations like QR decoding, file reading, and attendance marking are hidden behind simple method interfaces.
•	**Modularity:** The codebase is organized with a clear class structure, each with a specific role, promoting reusability and clarity.
________________________________________

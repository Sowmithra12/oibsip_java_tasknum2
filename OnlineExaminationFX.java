import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.animation.*;
import javafx.util.Duration;

public class OnlineExaminationFX extends Application {

    private Stage window;
    private Scene loginScene, profileScene, examScene, resultScene;
    private int currentQuestion = 0;
    private int score = 0;
    private String username = "user";
    private String password = "pass";
    private String name = "Sowmithra";
    private String newPass = "";

    private Label scoreLabel = new Label();
    private Label timerLabel = new Label();
    private Timeline timeline;

    private String[] questions = {
        "Which language is used for Android Development?",
        "What is 2 + 2?",
        "Which planet is known as the Red Planet?",
        "Who wrote 'Romeo and Juliet'?",
        "What does HTML stand for?"
    };

    private String[][] options = {
        {"Java", "Python", "Swift", "C++"},
        {"3", "4", "5", "6"},
        {"Earth", "Mars", "Venus", "Jupiter"},
        {"Charles Dickens", "William Shakespeare", "Jane Austen", "Mark Twain"},
        {"HyperText Markup Language", "Hyper Tool Multi Language", "HighText Machine Language", "None"}
    };

    private int[] correctAnswers = {0, 1, 1, 1, 0};

    private ToggleGroup answerGroup = new ToggleGroup();

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        window = stage;
        window.setTitle("Online Examination System");

        // Login Scene
        Label loginLabel = new Label("Login");
        TextField userField = new TextField();
        userField.setPromptText("Username");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        Button loginButton = new Button("Login");
        Label loginMsg = new Label();

        VBox loginLayout = new VBox(10, loginLabel, userField, passField, loginButton, loginMsg);
        loginLayout.setAlignment(Pos.CENTER);
        loginScene = new Scene(loginLayout, 400, 300);
        loginScene.getStylesheets().add("exam.css");

        loginButton.setOnAction(e -> {
            if (userField.getText().equals(username) && passField.getText().equals(password)) {
                window.setScene(profileScene);
            } else {
                loginMsg.setText("Invalid credentials!");
            }
        });

        // Profile Scene
        Label profileLabel = new Label("Update Profile");
        TextField nameField = new TextField(name);
        PasswordField updatePass = new PasswordField();
        updatePass.setPromptText("New Password");
        Button updateButton = new Button("Update");
        Button startExamButton = new Button("Start Exam");
        Label updateMsg = new Label();

        VBox profileLayout = new VBox(10, profileLabel, nameField, updatePass, updateButton, startExamButton, updateMsg);
        profileLayout.setAlignment(Pos.CENTER);
        profileScene = new Scene(profileLayout, 400, 300);
        profileScene.getStylesheets().add("exam.css");

        updateButton.setOnAction(e -> {
            name = nameField.getText();
            if (!updatePass.getText().isEmpty()) {
                password = updatePass.getText();
            }
            updateMsg.setText("Profile updated successfully.");
        });

        startExamButton.setOnAction(e -> {
            currentQuestion = 0;
            score = 0;
            startTimer();
            showQuestion();
            window.setScene(examScene);
        });

        // Exam Scene
        Label questionLabel = new Label();
        RadioButton option1 = new RadioButton();
        RadioButton option2 = new RadioButton();
        RadioButton option3 = new RadioButton();
        RadioButton option4 = new RadioButton();
        option1.setToggleGroup(answerGroup);
        option2.setToggleGroup(answerGroup);
        option3.setToggleGroup(answerGroup);
        option4.setToggleGroup(answerGroup);
        Button nextButton = new Button("Next");

        VBox examLayout = new VBox(10, timerLabel, questionLabel, option1, option2, option3, option4, nextButton);
        examLayout.setAlignment(Pos.CENTER);
        examScene = new Scene(examLayout, 400, 400);
        examScene.getStylesheets().add("exam.css");

        nextButton.setOnAction(e -> {
            RadioButton selected = (RadioButton) answerGroup.getSelectedToggle();
            if (selected != null) {
                int selectedIndex = -1;
                if (selected == option1) selectedIndex = 0;
                if (selected == option2) selectedIndex = 1;
                if (selected == option3) selectedIndex = 2;
                if (selected == option4) selectedIndex = 3;

                if (selectedIndex == correctAnswers[currentQuestion]) {
                    score++;
                }
                currentQuestion++;
                if (currentQuestion < questions.length) {
                    showQuestion();
                } else {
                    timeline.stop();
                    showResult();
                    window.setScene(resultScene);
                }
            }
        });

        // Result Scene
        Button logoutButton = new Button("Logout");
        VBox resultLayout = new VBox(10, scoreLabel, logoutButton);
        resultLayout.setAlignment(Pos.CENTER);
        resultScene = new Scene(resultLayout, 400, 300);
        resultScene.getStylesheets().add("exam.css");

        logoutButton.setOnAction(e -> window.setScene(loginScene));

        window.setScene(loginScene);
        window.show();
    }

    private void showQuestion() {
        Label questionLabel = (Label) ((VBox) examScene.getRoot()).getChildren().get(1);
        questionLabel.setText((currentQuestion + 1) + ". " + questions[currentQuestion]);
        RadioButton option1 = (RadioButton) ((VBox) examScene.getRoot()).getChildren().get(2);
        RadioButton option2 = (RadioButton) ((VBox) examScene.getRoot()).getChildren().get(3);
        RadioButton option3 = (RadioButton) ((VBox) examScene.getRoot()).getChildren().get(4);
        RadioButton option4 = (RadioButton) ((VBox) examScene.getRoot()).getChildren().get(5);

        answerGroup.selectToggle(null);
        option1.setText(options[currentQuestion][0]);
        option2.setText(options[currentQuestion][1]);
        option3.setText(options[currentQuestion][2]);
        option4.setText(options[currentQuestion][3]);
    }

    private void showResult() {
        scoreLabel.setText("Your Score: " + score + "/" + questions.length);
    }

    private void startTimer() {
        timerLabel.setText("Time left: 30 seconds");
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            String timeText = timerLabel.getText().replaceAll("[^0-9]", "");
            int time = Integer.parseInt(timeText);
            time--;
            timerLabel.setText("Time left: " + time + " seconds");
            if (time <= 0) {
                timeline.stop();
                showResult();
                window.setScene(resultScene);
            }
        }));
        timeline.setCycleCount(30);
        timeline.play();
    }
}

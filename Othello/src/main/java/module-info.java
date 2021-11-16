module com.othello.othello {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.othello.othello to javafx.fxml;
    exports com.othello.othello;
}
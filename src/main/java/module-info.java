module com.dolzhenko_m_s.doctorhelpsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires annotations;
    requires lombok;
    requires java.sql;


    opens com.dolzhenko_m_s.doctorhelpsystem to javafx.fxml;
    exports com.dolzhenko_m_s.doctorhelpsystem;
    exports com.dolzhenko_m_s.doctorhelpsystem.models;
}
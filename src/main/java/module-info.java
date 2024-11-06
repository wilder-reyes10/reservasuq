module co.edu.uniquindio.reservasuq {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires org.simplejavamail.core;
    requires org.simplejavamail;


    opens co.edu.uniquindio.reservasuq to javafx.fxml;
    exports co.edu.uniquindio.reservasuq;
    exports co.edu.uniquindio.reservasuq.controladores;
    opens co.edu.uniquindio.reservasuq.controladores to javafx.fxml;
    exports co.edu.uniquindio.reservasuq.controladores.observer;
    opens co.edu.uniquindio.reservasuq.controladores.observer to javafx.fxml;
}
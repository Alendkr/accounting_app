module org.diplom.accounting_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires java.desktop;
    requires jakarta.persistence.api;
    requires io.ebean.datasource;
    requires io.ebean.api;
    requires io.ebean;

    opens org.diplom.accounting_app to javafx.fxml;
    opens org.diplom.accounting_app.models to io.ebean;
    exports org.diplom.accounting_app.models to io.ebean.core;
    exports org.diplom.accounting_app;
    exports org.diplom.accounting_app.controllers;
    opens org.diplom.accounting_app.controllers to javafx.fxml;
}
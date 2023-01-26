
package com.example.di_filtrosyeventos;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public final class DraggablePanelsExample extends Application {
    private final BooleanProperty dragModeActiveProperty =
            new SimpleBooleanProperty(this, "dragModeActive", true);

    @Override
    public void start(final Stage stage) {
        final Node loginPanel =
                makeDraggable(createLoginPanel());
        final Node confirmationPanel =
                makeDraggable(createConfirmationPanel());
        final Node progressPanel =
                makeDraggable(createProgressPanel());

        final Group[] wrapGroupDragged = {new Group()};

        loginPanel.relocate(0, 0);
        confirmationPanel.relocate(0, 75);
        progressPanel.relocate(0, 120);

        final Pane panelsPane = new Pane();
        panelsPane.getChildren().addAll(loginPanel,
                confirmationPanel,
                progressPanel);

        final BorderPane sceneRoot = new BorderPane();

        BorderPane.setAlignment(panelsPane, Pos.TOP_LEFT);
        sceneRoot.setCenter(panelsPane);

        final CheckBox dragModeCheckbox = new CheckBox("Drag mode");
        BorderPane.setMargin(dragModeCheckbox, new Insets(6));
        sceneRoot.setBottom(dragModeCheckbox);

        dragModeActiveProperty.bind(dragModeCheckbox.selectedProperty());

        final Scene scene = new Scene(sceneRoot, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Draggable Panels Example");
        stage.show();

        for (int i = 0; i < panelsPane.getChildren().size(); i++) {
            Group wrapGroup = (Group) panelsPane.getChildren().get(i);
            wrapGroup.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                //detectar grupo arrastrado y grupo al que dejas
                for (int j = 0; j < panelsPane.getChildren().size() - 1; j++) {
                    if (panelsPane.getChildren().get(j).getBoundsInParent().intersects(panelsPane.getChildren().get(j + 1).getBoundsInParent())) {
                        System.out.println("Chocaron");
                        System.out.println(panelsPane.getChildren().size());
                        ObservableList<Node> lista = wrapGroup.getChildren();
                        panelsPane.getChildren().remove(wrapGroup);
                        panelsPane.getChildren().get(j);
                    } else if (panelsPane.getChildren().get(j).getBoundsInParent().intersects(panelsPane.getChildren().get(2).getBoundsInParent())) {
                        System.out.println("Chocaron 2");
                        System.out.println(panelsPane.getChildren().size());
                        panelsPane.getChildren().remove(wrapGroup);
                    }

                }
            });
        }


//        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
//            //detectar grupo arrastrado y grupo al que dejas
//            for (int i = 0; i < panelsPane.getChildren().size() - 1; i++) {
//                if (panelsPane.getChildren().get(i).getBoundsInParent().intersects(panelsPane.getChildren().get(i + 1).getBoundsInParent())) {
//                    System.out.println("Chocaron");
//                    System.out.println(panelsPane.getChildren().size());
//                    Group wrapGroup = new Group(panelsPane.getChildren().get(i));
//                    ObservableList<Node> lista = wrapGroup.getChildren();
//                    event.
//                            panelsPane.getChildren().remove(wrapGroupDragged);
//                } else if (panelsPane.getChildren().get(i).getBoundsInParent().intersects(panelsPane.getChildren().get(2).getBoundsInParent())) {
//                    System.out.println("Chocaron 2");
//                    System.out.println(panelsPane.getChildren().size());
//                }
//
//            }
//        });

    }

    public static void main(final String[] args) {
        launch(args);
    }

    private Node makeDraggable(final Node node) {
        final DragContext dragContext = new DragContext();
        final Group wrapGroup = new Group(node);

        wrapGroup.addEventFilter(
                MouseEvent.ANY,
                new EventHandler<MouseEvent>() {
                    public void handle(final MouseEvent mouseEvent) {
                        if (dragModeActiveProperty.get()) {
                            // disable mouse events for all children
                            mouseEvent.consume();
                        }
                    }
                });

        wrapGroup.addEventFilter(
                MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(final MouseEvent mouseEvent) {
                        if (dragModeActiveProperty.get()) {
                            // remember initial mouse cursor coordinates
                            // and node position
                            dragContext.mouseAnchorX = mouseEvent.getX();
                            dragContext.mouseAnchorY = mouseEvent.getY();
                            dragContext.initialTranslateX =
                                    node.getTranslateX();
                            dragContext.initialTranslateY =
                                    node.getTranslateY();
                        }
                    }
                });

        wrapGroup.addEventFilter(
                MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    public void handle(final MouseEvent mouseEvent) {
                        if (dragModeActiveProperty.get()) {
                            // shift node from its initial position by delta
                            // calculated from mouse cursor movement
                            node.setTranslateX(
                                    dragContext.initialTranslateX
                                            + mouseEvent.getX()
                                            - dragContext.mouseAnchorX);
                            node.setTranslateY(
                                    dragContext.initialTranslateY
                                            + mouseEvent.getY()
                                            - dragContext.mouseAnchorY);
                        }
                    }
                });


        return wrapGroup;
    }

    private static Node createLoginPanel() {
        final ToggleGroup toggleGroup = new ToggleGroup();

        final TextField textField = new TextField();
        textField.setPrefColumnCount(10);
        textField.setPromptText("Your name");

        final PasswordField passwordField = new PasswordField();
        passwordField.setPrefColumnCount(10);
        passwordField.setPromptText("Your password");

        final ChoiceBox<String> choiceBox = new ChoiceBox<String>(
                FXCollections.observableArrayList(
                        "English", "\u0420\u0443\u0441\u0441\u043a\u0438\u0439",
                        "Fran\u00E7ais"));
        choiceBox.setTooltip(new Tooltip("Your language"));
        choiceBox.getSelectionModel().select(0);

        final HBox panel =
                createHBox(6,
                        createVBox(2, createRadioButton("High", toggleGroup, true),
                                createRadioButton("Medium", toggleGroup,
                                        false),
                                createRadioButton("Low", toggleGroup, false)),
                        createVBox(2, textField, passwordField),
                        choiceBox);
        panel.setAlignment(Pos.BOTTOM_LEFT);
        configureBorder(panel);

        return panel;
    }

    private static Node createConfirmationPanel() {
        final Label acceptanceLabel = new Label("Not Available");

        final Button acceptButton = new Button("Accept");
        acceptButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    public void handle(final ActionEvent event) {
                        acceptanceLabel.setText("Accepted");
                    }
                });

        final Button declineButton = new Button("Decline");
        declineButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    public void handle(final ActionEvent event) {
                        acceptanceLabel.setText("Declined");
                    }
                });

        final HBox panel = createHBox(6, acceptButton,
                declineButton,
                acceptanceLabel);
        panel.setAlignment(Pos.CENTER_LEFT);
        configureBorder(panel);

        return panel;
    }

    private static Node createProgressPanel() {
        final Slider slider = new Slider();

        final ProgressIndicator progressIndicator = new ProgressIndicator(0);
        progressIndicator.progressProperty().bind(
                Bindings.divide(slider.valueProperty(),
                        slider.maxProperty()));

        final HBox panel = createHBox(6, new Label("Progress:"),
                slider,
                progressIndicator);
        configureBorder(panel);

        return panel;
    }

    private static void configureBorder(final Region region) {
        region.setStyle("-fx-background-color: white;"
                + "-fx-border-color: black;"
                + "-fx-border-width: 1;"
                + "-fx-border-radius: 6;"
                + "-fx-padding: 6;");
    }

    private static RadioButton createRadioButton(final String text,
                                                 final ToggleGroup toggleGroup,
                                                 final boolean selected) {
        final RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setSelected(selected);

        return radioButton;
    }

    private static HBox createHBox(final double spacing,
                                   final Node... children) {
        final HBox hbox = new HBox(spacing);
        hbox.getChildren().addAll(children);
        return hbox;
    }

    private static VBox createVBox(final double spacing,
                                   final Node... children) {
        final VBox vbox = new VBox(spacing);
        vbox.getChildren().addAll(children);
        return vbox;
    }

    private static final class DragContext {
        public double mouseAnchorX;
        public double mouseAnchorY;
        public double initialTranslateX;
        public double initialTranslateY;
    }

    private static void chocan(Node n1, Node n2) {
        if (n1.getBoundsInParent().intersects(n2.getBoundsInParent())) {
            System.out.println("Se chocan");
        }

    }

    private static void cambiarPadre(Node n1, Node n2) {
//        n1.getch
    }

    private static void transferirDraggable(Group g) {
        g.setOnDragDetected(mouseEvent -> {
            Dragboard db = g.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putAll((Map<? extends DataFormat, ?>) g.getChildren());
            db.setContent(content);
        });

//        g.setOnDragOver(mouseEvent -> {
//            if (mouseEvent.getDragboard().has) {
//
//            }
//        });
    }
}


//Todor Guichin
//Compare Decks
//2016
//pushing to git
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class deck extends Application {

    final private Desktop desktop = Desktop.getDesktop();

    //textarea fields
    TextArea fileTextLeft;
    TextArea fileTextRight;

    //Menu file
    Menu menu;

    //menu bar
    MenuBar bar;

    //file items
    MenuItem item;
    MenuItem item3;

    File chosenFileLeft;
    File chosenFileRight;

    Scanner readFirstDeck;
    Scanner readSecondDeck;
    TextArea textDeckDiff;
    TextArea textDeckDiff2;
    ArrayList<String> deck1;
    ArrayList<String> deck2;

    File firstDeckFile;
    File secondDeckFile;

    public HashMap<String, Integer> deck1Hash = new HashMap<>();
    public HashMap<String, Integer> deck2Hash = new HashMap<>();

    private Boolean boolSideboard = false;

    @Override
    public void start(final Stage stage) throws Exception {

        //Menu
        menu = new Menu("_File");

        //MenuItems
        item = new MenuItem("_New");
        item.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                refresh();
            }
        });

        menu.getItems().add(item);

        //MenuBar
        bar = new MenuBar();
        bar.getMenus().addAll(menu);

//        open.setOnAction(new EventHandler<ActionEvent>() {
//
//            @Override
//            public void handle(ActionEvent t) {
//                FileChooser choose = new FileChooser();
//                choose.setTitle("Choose Deck");
//                choose.setInitialDirectory(new File(System.getProperty("user.home")));
//                choose.getExtensionFilters().addAll(
//                        new FileChooser.ExtensionFilter("Text Files", "*.txt"),
//                        new FileChooser.ExtensionFilter("All Files", "*.*")
//                );
//                File chosenFile = choose.showOpenDialog(stage);
//                if (chosenFile != null) {
//                    fileText.setText(fileText.getText() + "\n" + "Chosen File: " + chosenFile);
//                }
//                openFile(chosenFile);
//
////                JOptionPane.showMessageDialog(null, "Open file.");
//            }
//        });
        //outerLayout
        BorderPane layout = new BorderPane();
        layout.setPrefSize(1200, 800);

        //innerLayout
        BorderPane innerLayout = new BorderPane();
        GridPane gridButtons = new GridPane();

        //BUTTONS FOR CHOOSING DECKS IN HBOX
        HBox hboxSelectDeck = new HBox(860);

        //CHOOSE FIRST DECK
        Button firstDeck = new Button("Choose First Deck");
        firstDeck.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                FileChooser firstDeckChooser = new FileChooser();
                firstDeckChooser.setTitle("Choose First Deck");
                firstDeckChooser.setInitialDirectory(new File(System.getProperty("user.home")));

                firstDeckChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Text Files", "*.txt"));

                firstDeckFile = firstDeckChooser.showOpenDialog(stage);
                fileTextLeft.setText("");
                if (firstDeckFile != null) {
                    try {
                        readFirstDeck = new Scanner(firstDeckFile);
                        String temp = "";
                        deck1Hash.clear();
                        boolSideboard = false;
                        while (readFirstDeck.hasNext()) {
                            temp = readFirstDeck.nextLine();

                            fileTextLeft.setText(fileTextLeft.getText() + temp + "\n");

                            //SAVE THE DECKS IN HASH TO COMPARE DIFFERENCES
                            if (temp.toLowerCase().contains("sideboard")) {
                                boolSideboard = true;
                            } else if (!temp.isEmpty() && !temp.toLowerCase().contains("sideboard") && boolSideboard == false) {
                                char[] splitDeck1Hash = temp.toCharArray();
                                int digit = 0;
                                StringBuffer string = new StringBuffer();
                                for (int i = 0; i < temp.length(); i++) {
                                    char next = splitDeck1Hash[i];
                                    if (Character.isDigit(next)) {
                                        digit = Character.getNumericValue(next);
                                    } else if (Character.isLetter(next) || next == ' ') {
                                        string.append(next);
                                    }
                                }
                                deck1Hash.put(string.toString(), digit);
                            } else if (!temp.isEmpty() && !temp.toLowerCase().contains("sideboard") && boolSideboard == true) {
                                char[] splitDeck1Hash = temp.toCharArray();
                                int digit = 0;
                                StringBuffer string = new StringBuffer();
                                string.append("S");
                                for (int i = 0; i < temp.length(); i++) {
                                    char next = splitDeck1Hash[i];
                                    if (Character.isDigit(next)) {
                                        digit = Character.getNumericValue(next);
                                    } else if (Character.isLetter(next) || next == ' ') {
                                        string.append(next);
                                    }
                                }
                                deck1Hash.put(string.toString(), digit);
                            }
                        }

                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(deck.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                try {
                    Scanner checkDiff = new Scanner(firstDeckFile);
                    deck1 = new ArrayList<String>();
                    String temp = "";
                    while (checkDiff.hasNext()) {
                        temp = checkDiff.nextLine();
                        deck1.add(temp);
                    }

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(deck.class.getName()).log(Level.SEVERE, null, ex);
                }
                if ((!fileTextLeft.getText().isEmpty()) && (!fileTextRight.getText().isEmpty())) {

                    textDeckDiff.setText("");
                    textDeckDiff2.setText("");
                    //COMPARE DECK 1 TO DECK TWO AND SHOW DIFFERNCES FOR DECK 2
                    for (Map.Entry<String, Integer> check : deck1Hash.entrySet()) {
                        if (deck2Hash.containsKey(check.getKey())) {
                            int deck2Value = deck2Hash.get(check.getKey());
                            int deck1Value = check.getValue();
                            if (deck2Value < deck1Value || deck2Value > deck1Value) {
                                int diffAmount = deck2Value - deck1Value;
                                textDeckDiff2.setText(textDeckDiff2.getText() + diffAmount + " " + check.getKey() + "\n");
                            }
                        }
                        if (!deck2Hash.containsKey(check.getKey())) {
                            textDeckDiff2.setText(textDeckDiff2.getText() + "M " + check.getValue() + " " + check.getKey() + "\n");
                        }
                    }
                    //COMPARE DECK 2 AND DECK 1 AND SHOW DIFFERENCES FOR DECK 1
                    for (Map.Entry<String, Integer> check : deck2Hash.entrySet()) {
                        if (deck2Hash.containsKey(check.getKey())) {
                            int deck1Value = deck1Hash.get(check.getKey());
                            int deck2Value = check.getValue();
                            if (deck1Value < deck2Value || deck1Value > deck2Value) {
                                int diffAmount = deck1Value - deck2Value;
                                textDeckDiff.setText(textDeckDiff.getText() + diffAmount + " " + check.getKey() + "\n");
                            }
                        }
                        if (!deck1Hash.containsKey(check.getKey())) {
                            textDeckDiff.setText(textDeckDiff.getText() + "M " + check.getValue() + " " + check.getKey() + "\n");
                        }
                    }
                }
            }
        }
        );

        //Todor Guichin
        //Compare Decks
        //2016
        //CHOOSE SECOND DECK
        Button secondDeck = new Button("Choose Second Deck");

        secondDeck.setOnAction(
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent t
                    ) {
                        FileChooser secondDeckChooser = new FileChooser();

                        secondDeckChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                        secondDeckChooser.getExtensionFilters().addAll(
                                new FileChooser.ExtensionFilter("Text Files", "*.txt")
                        );
                        secondDeckFile = secondDeckChooser.showOpenDialog(stage);
                        fileTextRight.setText("");
                        if (secondDeckFile != null) {
                            try {
                                readSecondDeck = new Scanner(secondDeckFile);
                                String temp = "";
                                deck2Hash.clear();
                                boolSideboard = false;
                                while (readSecondDeck.hasNext()) {
                                    temp = readSecondDeck.nextLine();
                                    fileTextRight.setText(fileTextRight.getText() + temp + "\n");

                                    //SAVE THE DECKS IN HASH TO COMPARE DIFFERENCES
                                    if (temp.toLowerCase().contains("sideboard")) {
                                        boolSideboard = true;
                                    } else if (!temp.isEmpty() && !temp.toLowerCase().contains("sideboard") && boolSideboard == false) {
                                        char[] splitDeck1Hash = temp.toCharArray();
                                        int digit = 0;
                                        StringBuffer string = new StringBuffer();
                                        for (int i = 0; i < temp.length(); i++) {
                                            char next = splitDeck1Hash[i];
                                            if (Character.isDigit(next)) {
                                                digit = Character.getNumericValue(next);
                                            } else if (Character.isLetter(next) || next == ' ') {
                                                string.append(next);
                                            }
                                        }
                                        deck2Hash.put(string.toString(), digit);
                                    } else if (!temp.isEmpty() && !temp.toLowerCase().contains("sideboard") && boolSideboard == true) {
                                        char[] splitDeck1Hash = temp.toCharArray();
                                        int digit = 0;
                                        StringBuffer string = new StringBuffer();
                                        string.append("S");
                                        for (int i = 0; i < temp.length(); i++) {
                                            char next = splitDeck1Hash[i];
                                            if (Character.isDigit(next)) {
                                                digit = Character.getNumericValue(next);
                                            } else if (Character.isLetter(next) || next == ' ') {
                                                string.append(next);
                                            }
                                        }
                                        deck2Hash.put(string.toString(), digit);
                                    }
                                }
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(deck.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        ///rodDev
                        try {
                            Scanner checkDiff = new Scanner(secondDeckFile);
                            deck2 = new ArrayList<String>();
                            String temp = "";
                            while (checkDiff.hasNext()) {
                                temp = checkDiff.nextLine();
                                deck2.add(temp);
                            }

                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(deck.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if ((!fileTextLeft.getText().isEmpty()) && (!fileTextRight.getText().isEmpty())) {

                            textDeckDiff.setText("");
                            textDeckDiff2.setText("");
                            //COMPARE DECK 1 TO DECK TWO AND SHOW DIFFERNCES FOR DECK 2
                            for (Map.Entry<String, Integer> check : deck1Hash.entrySet()) {
                                if (deck2Hash.containsKey(check.getKey())) {
                                    int deck2Value = deck2Hash.get(check.getKey());
                                    int deck1Value = check.getValue();
                                    if (deck2Value < deck1Value || deck2Value > deck1Value) {
                                        int diffAmount = deck2Value - deck1Value;
                                        textDeckDiff2.setText(textDeckDiff2.getText() + diffAmount + " " + check.getKey() + "\n");
                                    }
                                }
                                if (!deck2Hash.containsKey(check.getKey())) {
                                    textDeckDiff2.setText(textDeckDiff2.getText() + "M " + check.getValue() + " " + check.getKey() + "\n");
                                }
                            }
                            //COMPARE DECK 2 AND DECK 1 AND SHOW DIFFERENCES FOR DECK 1
                            for (Map.Entry<String, Integer> check : deck2Hash.entrySet()) {
                                if (deck2Hash.containsKey(check.getKey())) {
                                    int deck1Value = deck1Hash.get(check.getKey());
                                    int deck2Value = check.getValue();
                                    if (deck1Value < deck2Value || deck1Value > deck2Value) {
                                        int diffAmount = deck1Value - deck2Value;
                                        textDeckDiff.setText(textDeckDiff.getText() + diffAmount + " " + check.getKey() + "\n");
                                    }
                                }
                                if (!deck1Hash.containsKey(check.getKey())) {
                                    textDeckDiff.setText(textDeckDiff.getText() + "M " + check.getValue() + " " + check.getKey() + "\n");
                                }
                            }
                        }
                    }
                }
        );

        //textarea on left of innerlayout
        fileTextLeft = new TextArea();

        fileTextLeft.setWrapText(true);
        fileTextLeft.setPrefSize(300, 500);
        fileTextLeft.setEditable(false);
        fileTextLeft.setStyle("-fx-background-color: #ebeef4");

        //textarea on right of innerlayout
        fileTextRight = new TextArea();

        fileTextRight.setWrapText(
                true);
        fileTextRight.setPrefSize(
                300, 500);
        fileTextRight.setEditable(
                false);
        fileTextRight.setStyle(
                "-fx-background-color: #ebeef4");

        //left innerlayout VBOX
        VBox leftInnerLayout = new VBox();

        leftInnerLayout.getChildren()
                .addAll(firstDeck, fileTextLeft);

        //right innerlayout VBOX
        VBox rightInnerLayout = new VBox();

        rightInnerLayout.getChildren()
                .addAll(secondDeck, fileTextRight);

        //center innerlayhout VBOX
        Label deckDiff = new Label("Differences");

        textDeckDiff = new TextArea();

        textDeckDiff.setPrefSize(
                200, 300);
        textDeckDiff.setEditable(
                false);
        textDeckDiff2 = new TextArea();

        textDeckDiff2.setPrefSize(
                200, 300);
        textDeckDiff2.setEditable(
                false);

        HBox difftextHBOX = new HBox();

        difftextHBOX.getChildren()
                .addAll(textDeckDiff, textDeckDiff2);
        difftextHBOX.setSpacing(
                30);

        VBox boxDeckDiff = new VBox();

        boxDeckDiff.getChildren()
                .addAll(deckDiff, difftextHBOX);

        //addgridbuttons to topinnerlayout
        innerLayout.setLeft(leftInnerLayout);

        innerLayout.setCenter(boxDeckDiff);

        BorderPane.setMargin(boxDeckDiff,
                new Insets(100, 30, 0, 30));
        innerLayout.setRight(rightInnerLayout);

        //size and color innerlayout
        innerLayout.setPrefSize(
                500, 500);
        innerLayout.setStyle(
                "-fx-background-color: #899bc1;");

        //enter inner into outer borderlayout
        layout.setCenter(innerLayout);

        BorderPane.setAlignment(innerLayout, Pos.CENTER);

        BorderPane.setMargin(innerLayout,
                new Insets(50));
        layout.setTop(bar);

        //set outlayout color
        layout.setStyle(
                "-fx-background-color: #e6e6fa;");

        Scene panel = new Scene(layout);

        stage.setTitle(
                "Deck Comparison");
        stage.setScene(panel);

        stage.setResizable(
                false);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void openDeckText(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                    deck.class.getName()).log(
                            Level.SEVERE, null, ex
                    );
        }
    }

    public void refresh() {
        //textarea fields
        fileTextLeft.setText("");
        fileTextRight.setText("");
        textDeckDiff.setText("");
        textDeckDiff2.setText("");
        
        deck1Hash.clear();
        deck2Hash.clear();
    }

}

//Todor Guichin
//Compare Decks
//2016

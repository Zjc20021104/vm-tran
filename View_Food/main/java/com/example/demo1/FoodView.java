package com.example.demo1;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.scene.control.TextField;
//import javafx.*;
//import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.Map;

class FoodRecordView {
    private boolean isPopup_list = false;

    private Stage popupStage_list;

    private AnchorPane popupContent;

    private final FoodView foodView = new FoodView();




    public void initPopupStage(Stage owner) {
        if (popupStage_list == null) {
            popupStage_list = new Stage();
        }
        popupStage_list.initModality(Modality.NONE);
        popupStage_list.initOwner(owner);
        popupStage_list.setResizable(false);
        popupContent = new AnchorPane();
        popupContent.setStyle("-fx-background-color: #FFFFFF;"); // 白色背景
        Scene popupScene = new Scene(popupContent, 294, 437);
        popupStage_list.setTitle("当前食品列表");
        popupStage_list.setScene(popupScene);
    }

    public void showRecordWindow() {

        if (!isPopup_list) {
            popupStage_list.show();
            isPopup_list = true;
        } else {
            popupStage_list.hide();
            isPopup_list = false;
        }

        popupStage_list.setOnCloseRequest(e -> {
            isPopup_list = false;
        });
    }

    public void addRecord(String foodName, int weight, int heat, int recordCount, int index) {//暂定单位为千卡/千克
        Label recordLabel = new Label(foodName + " " + weight + "千克 " + "\n" + heat + "千卡");
        recordLabel.setFont(Font.font("Arial", 15));
        popupContent.getChildren().add(recordLabel);
        AnchorPane.setTopAnchor(recordLabel, 10.0 + 60 * recordCount);
        AnchorPane.setLeftAnchor(recordLabel, 80.0);
        Image icon = new Image(foodView.pathList[index]);
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(25);
        iconView.setFitHeight(25);
        iconView.setPreserveRatio(true);
        StackPane pane = new StackPane(iconView);
        pane.setStyle("-fx-border-color: gray; -fx-border-width: 1.5; -fx-padding: 10; -fx-border-radius: 10; -fx-border-style: solid outside;");
        popupContent.getChildren().add(pane);
        AnchorPane.setTopAnchor(pane, 10.0 + 60 * recordCount);
        AnchorPane.setLeftAnchor(pane, 10.0);

        ColorAdjust colorAdjustPressed = new ColorAdjust();
        colorAdjustPressed.setBrightness(+0.3);

        Button modifyButton = new Button();
        modifyButton.setStyle(
                "-fx-background-image: url('file:src/Icons/modify.png');" + // 使用图片作为背景
                        "-fx-background-size: cover;" +                // 覆盖整个按钮
                        "-fx-border-width: 0;" +
                        "-fx-background-color: transparent ;" +                  // 圆角半径
                        "-fx-padding: 0;");
        modifyButton.setPrefSize(20, 20);
        modifyButton.setOnAction(e -> {
            Stage popupStage_modify = new Stage();
            popupStage_modify.initModality(Modality.WINDOW_MODAL);
            popupStage_modify.initOwner(popupStage_list);
            popupStage_modify.setResizable(false);
            AnchorPane popupContent_modify = new AnchorPane();
            Label header = new Label("修改数据");
            header.setFont(Font.font("Arial Rounded MT Bold", 15));
            popupContent_modify.getChildren().add(header);
            AnchorPane.setTopAnchor(header, 50.0);
            AnchorPane.setLeftAnchor(header, 120.0);
            TextField textField = new TextField();
            textField.setPromptText("输入重量");
            popupContent_modify.getChildren().add(textField);
            AnchorPane.setTopAnchor(textField, 100.0);
            AnchorPane.setLeftAnchor(textField, 80.0);

            Button button = new Button("保存");
            button.setStyle(
                    "-fx-background-color: #00CC00;" +
                            "-fx-text-fill: black;" +
                            "-fx-border-radius: 10;" +
                            "-fx-background-radius: 10;" +
                            "-fx-padding: 5 25;" +
                            "-fx-font-size: 15;");
            button.setOnAction(e2 -> {
                if (!textField.getText().isEmpty()) {
                    try {
                        int weight_modify = Integer.parseInt(textField.getText());
                        recordLabel.setText(foodName + " " + weight_modify + "千克 " + "\n" + weight_modify * heat / weight  + "千卡");//可能改成modify*heat就行
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid input: " + textField.getText());
                    }
                }
                popupStage_modify.close();
            });
            button.setCursor(Cursor.HAND);
            button.setOnMousePressed(e2 -> button.setEffect(colorAdjustPressed));
            button.setOnMouseReleased(e2 -> button.setEffect(null));
            popupContent_modify.getChildren().add(button);
            AnchorPane.setTopAnchor(button, 150.0);
            AnchorPane.setLeftAnchor(button, 115.0);

            popupContent_modify.setStyle("-fx-background-color: #FFFFFF;"); // 白色背景
            Scene popupScene_modify = new Scene(popupContent_modify, 294, 437);
            popupStage_modify.setScene(popupScene_modify);
            popupStage_modify.show();
        });
        modifyButton.setCursor(Cursor.HAND);
        modifyButton.setOnMousePressed(e -> modifyButton.setEffect(colorAdjustPressed));
        modifyButton.setOnMouseReleased(e -> modifyButton.setEffect(null));
        popupContent.getChildren().add(modifyButton);
        AnchorPane.setTopAnchor(modifyButton, 20.0 + 60 * recordCount);
        AnchorPane.setLeftAnchor(modifyButton, 220.0);

        Button deleteButton = new Button();
        deleteButton.setStyle(
                "-fx-background-image: url('file:src/Icons/delete.png');" + // 使用图片作为背景
                        "-fx-background-size: cover;" +                // 覆盖整个按钮
                        "-fx-border-width: 0;" +
                        "-fx-background-color: transparent ;" +                  // 圆角半径
                        "-fx-padding: 0;");
        deleteButton.setPrefSize(20, 20);
        deleteButton.setOnAction(e -> {
            popupContent.getChildren().remove(recordLabel);
            popupContent.getChildren().remove(pane);
            popupContent.getChildren().remove(deleteButton);
            popupContent.getChildren().remove(modifyButton);
            FoodView.recordCount--;
        });
        deleteButton.setCursor(Cursor.HAND);
        deleteButton.setOnMousePressed(e -> deleteButton.setEffect(colorAdjustPressed));
        deleteButton.setOnMouseReleased(e -> deleteButton.setEffect(null));
        popupContent.getChildren().add(deleteButton);
        AnchorPane.setTopAnchor(deleteButton, 20.0 + 60 * recordCount);
        AnchorPane.setLeftAnchor(deleteButton, 250.0);

    }
}
class FoodAddView{
    private int weight_consume = 0;
    public void initPopupStage(Stage owner, String typeName, String iconPath, int heat, InputResponseHandler handler) {
        Stage popupStage_add = new Stage();
        popupStage_add.initModality(Modality.WINDOW_MODAL);
        popupStage_add.initOwner(owner);
        popupStage_add.setResizable(false);
        AnchorPane popupContent = new AnchorPane();
        Label header = new Label("添加食品");
        header.setFont(Font.font("Arial Rounded MT Bold", 15));
        popupContent.getChildren().add(header);
        AnchorPane.setTopAnchor(header, 50.0);
        AnchorPane.setLeftAnchor(header, 120.0);
        Image icon = new Image(iconPath);
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(25);
        iconView.setFitHeight(25);
        iconView.setPreserveRatio(true);
        StackPane pane = new StackPane(iconView);
        pane.setStyle("-fx-border-color: gray; " +
                "-fx-border-width: 1.5; " +
                "-fx-padding: 10; " +
                "-fx-border-radius: 10; " +
                "-fx-border-style: solid outside;");
        popupContent.getChildren().add(pane);
        AnchorPane.setTopAnchor(pane, 100.0);
        AnchorPane.setLeftAnchor(pane, 90.0);
        Label exerciseLabel = new Label(typeName);
        exerciseLabel.setFont(Font.font("Arial", 15));
        popupContent.getChildren().add(exerciseLabel);
        AnchorPane.setTopAnchor(exerciseLabel, 100.0);
        AnchorPane.setLeftAnchor(exerciseLabel, 150.0);
        Label consumeLabel = new Label(heat + "千卡/千克");
        consumeLabel.setFont(Font.font("Arial Black", 12));
        consumeLabel.setStyle("-fx-text-fill: #808080;");
        popupContent.getChildren().add(consumeLabel);
        AnchorPane.setTopAnchor(consumeLabel, 125.0);
        AnchorPane.setLeftAnchor(consumeLabel, 150.0);
        TextField textField = new TextField();
        textField.setPromptText("输入重量");
        popupContent.getChildren().add(textField);
        AnchorPane.setTopAnchor(textField, 175.0);
        AnchorPane.setLeftAnchor(textField, 80.0);

        Button button = new Button("保存");
        button.setStyle(
                "-fx-background-color: #00CC00;" +
                        "-fx-text-fill: black;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 5 25;" +
                        "-fx-font-size: 15;");
        button.setOnAction(e -> {
            if (!textField.getText().isEmpty()) {
                try {
                    weight_consume = Integer.parseInt(textField.getText());
                    handler.handle(weight_consume);
                }catch (NumberFormatException ex) {
                    System.out.println("Invalid input: " + textField.getText());
                }
            }
            popupStage_add.close();
        });
        button.setCursor(Cursor.HAND);
        ColorAdjust colorAdjustPressed = new ColorAdjust();
        colorAdjustPressed.setBrightness(-0.3);
        button.setOnMousePressed(e -> button.setEffect(colorAdjustPressed));
        button.setOnMouseReleased(e -> button.setEffect(null));
        popupContent.getChildren().add(button);
        AnchorPane.setTopAnchor(button, 230.0);
        AnchorPane.setLeftAnchor(button, 115.0);

        popupContent.setStyle("-fx-background-color: #FFFFFF;"); // 白色背景
        Scene popupScene = new Scene(popupContent, 294, 437);
        popupStage_add.setScene(popupScene);
        popupStage_add.show();

    }
}
public class FoodView extends Application {
    private static int iconCount = 0;
    private static int buttonCount = 0;
    private static int foodCount = 0;
    private static int foodDetailCount = 0;

    protected static int recordCount = 0;
    private static final String icon_beef = "file:src/Food_Icons/beef.jpg";
    private static final String icon_egg = "file:src/Food_Icons/egg.jpg";
    private static final String icon_juice = "file:src/Food_Icons/juice.jpg";
    private static final String icon_melon = "file:src/Food_Icons/melon.jpg";
    private static final String icon_milk = "file:src/Food_Icons/milk.jpg";
    private static final String icon_noodle = "file:src/Food_Icons/noodle.jpg";
    private static final String icon_pend = "file:src/Food_Icons/pend.jpg";
    private static final String icon_rice = "file:src/Food_Icons/rice.jpg";

    private Label currentTypeLabel = null;
    public FoodRecordView foodRecordView;
    public FoodAddView foodAddView;

    public final String[] pathList = {"file:src/Food_Icons/juice.jpg", "file:src/Food_Icons/milk.jpg", "file:src/Food_Icons/pend.jpg", "file:src/Food_Icons/rice.jpg", "file:src/Food_Icons/noodle.jpg", "file:src/Food_Icons/melon.jpg", "file:src/Food_Icons/egg.jpg", "file:src/Food_Icons/beef.jpg"};

    public final String[] foodList = {"果汁", "牛奶", "面包", "米饭", "面条", "西瓜", "鸡蛋", "牛排"};
    public final Map<String, Integer> consumeMap = Map.of(
            "果汁", 481,
            "面包", 915,
            "米饭", 650,
            "面条", 662,
            "西瓜", 361,
            "鸡蛋", 722,
            "牛奶", 542,
            "牛排", 1301
    );

    private int consume = 0;
    //暂时先分饮料，主食，肉类，蔬果
    private final AnchorPane commonPanel = new AnchorPane();
    private final AnchorPane drinkPanel = new AnchorPane();
    private final AnchorPane principlePanel = new AnchorPane();
    private final AnchorPane meetPanel = new AnchorPane();
    private final AnchorPane vegetablePanel = new AnchorPane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Label timeLabel = createTimeLabel(); // 创建时间标签
        setupTimeline(timeLabel); // 设置时间轴
        AnchorPane subPanel = createTimePanel(timeLabel); // 创建子面板
        AnchorPane root = setupRootPane(subPanel);  // 设置根面板
        foodRecordView = new FoodRecordView();
        foodAddView = new FoodAddView();
        foodRecordView.initPopupStage(primaryStage);

        addPanels();
        setSizes();

        addHeader(root);
        addBackButton(root);
        addBottomPanelWithButtons(root);
        addSidePanelWithButtons(root);



        root.getChildren().add(commonPanel);

        setTypeIndicator(root, "常见食品");
        setPopup_add(primaryStage);

        Scene scene = new Scene(root, 294, 637); // 创建场景
        primaryStage.setScene(scene); // 设置舞台
        primaryStage.setTitle("FoodView"); // 设置标题
        primaryStage.setResizable(false); // 禁止调整窗口大小
        primaryStage.show(); // 显示舞台
    }
    private void setSizes(){
        commonPanel.setPrefHeight(294);
        commonPanel.setPrefWidth(294);
        commonPanel.setLayoutX(55);
        commonPanel.setLayoutY(100);

        drinkPanel.setPrefHeight(294);
        drinkPanel.setPrefWidth(294);
        drinkPanel.setLayoutX(55);
        drinkPanel.setLayoutY(100);

        principlePanel.setPrefHeight(294);
        principlePanel.setPrefWidth(294);
        principlePanel.setLayoutX(55);
        principlePanel.setLayoutY(100);

        meetPanel.setPrefHeight(294);
        meetPanel.setPrefWidth(294);
        meetPanel.setLayoutX(55);
        meetPanel.setLayoutY(100);

        vegetablePanel.setPrefHeight(294);
        vegetablePanel.setPrefWidth(294);
        vegetablePanel.setLayoutX(55);
        vegetablePanel.setLayoutY(100);


    }
    private void resetCounts() {
        iconCount = 0;
        buttonCount = 0;
        foodCount = 0;
        foodDetailCount = 0;
    }
    private void addPanels() {
        addIcon(commonPanel, icon_juice, iconCount++);
        addExerciseName(commonPanel, "果汁", foodCount++);
        addExerciseDetail(commonPanel, "481千卡/千克", foodDetailCount++);

        addIcon(commonPanel, icon_milk, iconCount++);
        addExerciseName(commonPanel, "牛奶", foodCount++);
        addExerciseDetail(commonPanel, "542千卡/千克", foodDetailCount++);

        addIcon(commonPanel, icon_pend, iconCount++);
        addExerciseName(commonPanel, "面包", foodCount++);
        addExerciseDetail(commonPanel, "915千卡/千克", foodDetailCount++);

        addIcon(commonPanel, icon_rice, iconCount++);
        addExerciseName(commonPanel, "米饭", foodCount++);
        addExerciseDetail(commonPanel, "650千卡/千克", foodDetailCount++);

        addIcon(commonPanel, icon_noodle, iconCount++);
        addExerciseName(commonPanel, "面条", foodCount++);
        addExerciseDetail(commonPanel, "662千卡/千克", foodDetailCount++);

        addIcon(commonPanel, icon_melon, iconCount++);
        addExerciseName(commonPanel, "西瓜", foodCount++);
        addExerciseDetail(commonPanel, "361千卡/千克", foodDetailCount++);

        addIcon(commonPanel, icon_egg, iconCount++);
        addExerciseName(commonPanel, "鸡蛋", foodCount++);
        addExerciseDetail(commonPanel, "722千卡/千克", foodDetailCount++);

        addIcon(commonPanel, icon_beef, iconCount++);
        addExerciseName(commonPanel, "牛排", foodCount++);
        addExerciseDetail(commonPanel, "1301千卡/千克", foodDetailCount++);
        for (int i = 0; i < 8; i++) {
            addCircularButton(commonPanel, buttonCount, buttonCount++);
        }
        resetCounts();

        addIcon(drinkPanel, icon_juice, iconCount++);
        addExerciseName(drinkPanel, "果汁", foodCount++);
        addExerciseDetail(drinkPanel, "481千卡/千克", foodDetailCount++);
        addCircularButton(drinkPanel, 0, buttonCount++);
        addIcon(drinkPanel, icon_milk, iconCount++);
        addExerciseName(drinkPanel, "牛奶", foodCount++);
        addExerciseDetail(drinkPanel, "542千卡/千克", foodDetailCount++);
        addCircularButton(drinkPanel, 1, buttonCount++);
        resetCounts();

        addIcon(principlePanel, icon_pend, iconCount++);
        addExerciseName(principlePanel, "面包", foodCount++);
        addExerciseDetail(principlePanel, "915千卡/千克", foodDetailCount++);
        addCircularButton(principlePanel, 2, buttonCount++);
        addIcon(principlePanel, icon_rice, iconCount++);
        addExerciseName(principlePanel, "米饭", foodCount++);
        addExerciseDetail(principlePanel, "650千卡/千克", foodDetailCount++);
        addCircularButton(principlePanel, 3, buttonCount++);
        addIcon(principlePanel, icon_noodle, iconCount++);
        addExerciseName(principlePanel, "面条", foodCount++);
        addExerciseDetail(principlePanel, "662千卡/千克", foodDetailCount++);
        addCircularButton(principlePanel, 4, buttonCount++);
        resetCounts();

        addIcon(vegetablePanel, icon_melon, iconCount++);
        resetCounts();
        addExerciseName(vegetablePanel, "西瓜", foodCount++);
        addExerciseDetail(vegetablePanel, "361千卡/千克", foodDetailCount++);
        addCircularButton(vegetablePanel, 5, buttonCount++);
        resetCounts();

        addIcon(meetPanel, icon_egg, iconCount++);
        addExerciseName(meetPanel, "鸡蛋", foodCount++);
        addExerciseDetail(meetPanel, "722千卡/千克", foodDetailCount++);
        addIcon(meetPanel, icon_beef, iconCount++);
        addExerciseName(meetPanel, "牛排", foodCount++);
        addExerciseDetail(meetPanel, "1301千卡/千克", foodDetailCount++);
        addCircularButton(meetPanel, 6, buttonCount++);
        addCircularButton(meetPanel, 7, buttonCount++);
        resetCounts();
    }
    private void setTypeIndicator(AnchorPane rootPanel, String typeName) {
        if (currentTypeLabel != null) {
            rootPanel.getChildren().remove(currentTypeLabel);
        }
        Label typeLabel = new Label(typeName);
        typeLabel.setFont(Font.font("Arial Rounded MT Bold", 15));
        typeLabel.setStyle("-fx-text-fill: #000000;" + // 灰色字体
                "-fx-border-color: #66FFB2; " +       // 边框颜色
                "-fx-border-width: 0 0 5 0; " +           // 边框宽度
                "-fx-border-style: solid;");
        rootPanel.getChildren().add(typeLabel);
        AnchorPane.setTopAnchor(typeLabel, 70.0);
        AnchorPane.setLeftAnchor(typeLabel, 220.0);
        currentTypeLabel = typeLabel;
    }

    private void setPopup_add(Stage primaryStage) {
        Stage popupStage_add = new Stage();
        popupStage_add.initOwner(primaryStage);
        popupStage_add.initModality(Modality.NONE);
        popupStage_add.setTitle("当前食品列表");

        AnchorPane popupContent = new AnchorPane();
        popupContent.setStyle("-fx-background-color: #FFFFFF;"); // 白色背景
        Scene popupScene = new Scene(popupContent, 294, 437);
        popupStage_add.setScene(popupScene);
    }
    private Label createTimeLabel() {
        Label timeLabel = new Label();
        timeLabel.setFont(Font.font("Arial Rounded MT Bold", 15));
        return timeLabel;
    }

    private void setupTimeline(Label timeLabel) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            timeLabel.setText(currentTime.format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private AnchorPane createTimePanel(Label timeLabel) {
        AnchorPane subPanel = new AnchorPane();
        subPanel.setStyle("-fx-background-color: #EEEEEE; " + // 灰色背景
                "-fx-border-color: gray; " +       // 边框颜色
                "-fx-border-width: 0 0 2 0; " +           // 边框宽度
                "-fx-border-style: solid;");
        AnchorPane.setTopAnchor(timeLabel, 9.0);
        AnchorPane.setLeftAnchor(timeLabel, 5.0);
        subPanel.getChildren().add(timeLabel);
        return subPanel;
    }

    private AnchorPane setupRootPane(AnchorPane subPanel) {
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #FFFFFF;"); // 白色背景
        root.getChildren().add(subPanel);
        AnchorPane.setTopAnchor(subPanel, 0.0);
        AnchorPane.setLeftAnchor(subPanel, 0.0);
        AnchorPane.setRightAnchor(subPanel, 0.0);
        AnchorPane.setBottomAnchor(subPanel, 600.0);
        return root;
    }

    private void addIcon(AnchorPane rootPanel, String iconPath, int iconCount) {
        Image icon = new Image(iconPath);
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(25);
        iconView.setFitHeight(25);
        iconView.setPreserveRatio(true);
        StackPane pane = new StackPane(iconView);
        pane.setStyle("-fx-border-color: gray; -fx-border-width: 1.5; -fx-padding: 10; -fx-border-radius: 10; -fx-border-style: solid outside;");

        rootPanel.getChildren().add(pane);
        AnchorPane.setTopAnchor(pane, 5.0 + 60 * iconCount);
        AnchorPane.setLeftAnchor(pane, 20.0);
    }

    private void addCircularButton(AnchorPane rootPanel, int index, int buttonCount) {
        Button button = new Button();
        button.setStyle(
                "-fx-background-image: url('file:src/Icons/add.png');" + // 使用图片作为背景，替换为你的图像文件路径
                        "-fx-background-size: cover;" +                // 覆盖整个按钮
                        "-fx-border-width: 0;" +
                        "-fx-background-color: transparent ;" +                  // 圆角半径
                        "-fx-padding: 0;");
        button.setPrefSize(20, 20);
        button.setOnAction(e -> {
            foodAddView.initPopupStage((Stage) rootPanel.getScene().getWindow(), foodList[index], pathList[index], consumeMap.get(foodList[index]), input -> {
                consume = input * consumeMap.get(foodList[index]) / 60;//可能不用除60
                foodRecordView.addRecord(foodList[index], input, consume, recordCount++, index);
            });
        });
        button.setCursor(Cursor.HAND);
        ColorAdjust colorAdjustPressed = new ColorAdjust();
        colorAdjustPressed.setBrightness(-0.3);
        button.setOnMousePressed(e -> button.setEffect(colorAdjustPressed));
        button.setOnMouseReleased(e -> button.setEffect(null));

        rootPanel.getChildren().add(button);
        AnchorPane.setTopAnchor(button, 15.0 + 60 * buttonCount);
        AnchorPane.setLeftAnchor(button, 200.0);
    }

    private void addExerciseName(AnchorPane rootPanel, String exerciseName, int exerciseCount) {
        Label exerciseLabel = new Label(exerciseName);
        exerciseLabel.setFont(Font.font("Arial", 15));
        rootPanel.getChildren().add(exerciseLabel);
        AnchorPane.setTopAnchor(exerciseLabel, 5.0 + 60 * exerciseCount);
        AnchorPane.setLeftAnchor(exerciseLabel, 80.0);
    }

    private void addExerciseDetail(AnchorPane rootPanel, String exerciseDetail, int exerciseDetailCount) {
        Label exerciseLabel = new Label(exerciseDetail);
        exerciseLabel.setFont(Font.font("Arial Black", 12));
        exerciseLabel.setStyle("-fx-text-fill: #808080;");
        rootPanel.getChildren().add(exerciseLabel);
        AnchorPane.setTopAnchor(exerciseLabel, 30.0 + 60 * exerciseDetailCount);
        AnchorPane.setLeftAnchor(exerciseLabel, 80.0);
    }

    private void addHeader(AnchorPane rootPanel) {
        Label headerLabel = new Label("添加食品");
        headerLabel.setFont(Font.font("Arial Rounded MT Bold", 15));
        rootPanel.getChildren().add(headerLabel);
        AnchorPane.setTopAnchor(headerLabel, 50.0);
        AnchorPane.setLeftAnchor(headerLabel, 120.0);
    }

    private void addBackButton(AnchorPane rootPanel) {
        Button button = new Button();
        button.setStyle(
                "-fx-background-image: url('file:src/Icons/back.png');" + // 使用图片作为背景，替换为你的图像文件路径
                        "-fx-background-size: cover;" +                // 覆盖整个按钮
                        "-fx-border-width: 0;" +
                        "-fx-background-color: transparent ;" +                  // 圆角半径
                        "-fx-padding: 0;");
        button.setPrefSize(20, 20);
        button.setOnAction(e -> {
            System.out.println("Back Button clicked, waiting for implementation.");
        });
        button.setCursor(Cursor.HAND);
        ColorAdjust colorAdjustPressed = new ColorAdjust();
        colorAdjustPressed.setBrightness(-0.3);
        button.setOnMousePressed(e -> button.setEffect(colorAdjustPressed));
        button.setOnMouseReleased(e -> button.setEffect(null));

        rootPanel.getChildren().add(button);
        AnchorPane.setTopAnchor(button, 45.0);
        AnchorPane.setLeftAnchor(button, 15.0);
    }

    private void addBottomPanelWithButtons(AnchorPane rootPanel) {
        AnchorPane bottomPanel = new AnchorPane();
        bottomPanel.setStyle("-fx-background-color: #EEEEEE; " + // 灰色背景
                "-fx-border-color: gray; " +       // 边框颜色
                "-fx-border-width: 2 0 0 0; " +           // 边框宽度
                "-fx-border-style: solid;");
        AnchorPane.setTopAnchor(bottomPanel, 575.0);
        AnchorPane.setLeftAnchor(bottomPanel, 0.0);
        AnchorPane.setRightAnchor(bottomPanel, 0.0);
        AnchorPane.setBottomAnchor(bottomPanel, 0.0);
        rootPanel.getChildren().add(bottomPanel);

        Button button = new Button("完成");
        button.setStyle(
                "-fx-background-color: #00CC00;" + // 绿色背景
                        "-fx-text-fill: black;" + // 黑色字体
                        "-fx-border-radius: 10;" + // 圆角半径
                        "-fx-background-radius: 10;" + // 圆角背景
                        "-fx-padding: 5 25;" + // 按钮内边距
                        "-fx-font-size: 15;");
        button.setOnAction(e -> {
            rootPanel.getScene().getWindow().hide();
        });
        button.setCursor(Cursor.HAND);
        ColorAdjust colorAdjustPressed = new ColorAdjust();
        colorAdjustPressed.setBrightness(-0.3);
        button.setOnMousePressed(e -> button.setEffect(colorAdjustPressed));
        button.setOnMouseReleased(e -> button.setEffect(null));

        bottomPanel.getChildren().add(button);
        AnchorPane.setTopAnchor(button, 12.0);
        AnchorPane.setLeftAnchor(button, 200.0);

        Button button2 = new Button();
        button2.setStyle(
                "-fx-background-image: url('file:src/Icons/finish.png');" + // 使用图片作为背景
                        "-fx-background-size: cover;" +                // 覆盖整个按钮
                        "-fx-border-width: 0;" +
                        "-fx-background-color: transparent ;" +                  // 圆角半径
                        "-fx-padding: 0;");

        button2.setPrefSize(40, 40);
        button2.setOnAction(e -> {
            foodRecordView.showRecordWindow();
        });
        button2.setCursor(Cursor.HAND);
        button2.setOnMousePressed(e -> button2.setEffect(colorAdjustPressed));
        button2.setOnMouseReleased(e -> button2.setEffect(null));

        bottomPanel.getChildren().add(button2);
        AnchorPane.setTopAnchor(button2, 8.0);
        AnchorPane.setLeftAnchor(button2, 30.0);
    }

    private void addSidePanelWithButtons(AnchorPane rootPanel) {
        AnchorPane sidePanel = new AnchorPane();
        sidePanel.setStyle("-fx-background-color: #EEEEEE; " + // 灰色背景
                "-fx-border-color: gray; " +       // 边框颜色
                "-fx-border-width: 2 2 0 0; " +
                "-fx-border-radius: 0 10 0 0; " +
                "-fx-border-style: solid;");
        AnchorPane.setTopAnchor(sidePanel, 97.0);
        AnchorPane.setLeftAnchor(sidePanel, 0.0);
        AnchorPane.setRightAnchor(sidePanel, 230.0);
        AnchorPane.setBottomAnchor(sidePanel, 62.0);
        rootPanel.getChildren().add(sidePanel);

        String[] names = {"常见", "饮料", "主食", "蔬果", "肉类"};

        for (int i = 0; i < 5; i++) {
            Button button = new Button(names[i]);
            button.setPrefSize(50, 20);
            button.setStyle(
                    "-fx-background-color: #FFFFFF;" + // 白色背景
                            "-fx-text-fill: black;" + // 黑色字体
                            "-fx-border-color: gray;" + // 黑色边框
                            "-fx-border-width: 2 0 2 2;" + // 边框宽度
                            "-fx-border-radius: 10 0 0 10;" + // 圆角半径
                            "-fx-background-radius: 10 0 0 10;" + // 圆角背景
                            "-fx-padding: 5 5;" + // 按钮内边距
                            "-fx-font-size: 10;");
            button.setCursor(Cursor.HAND);
            ColorAdjust colorAdjustPressed = new ColorAdjust();
            colorAdjustPressed.setBrightness(-0.3);
            button.setOnMousePressed(e -> button.setEffect(colorAdjustPressed));
            button.setOnMouseReleased(e -> button.setEffect(null));

            sidePanel.getChildren().add(button);
            AnchorPane.setTopAnchor(button, 10.0 + 50 * i);
            AnchorPane.setLeftAnchor(button, 12.0);

            int index = i;
            button.setOnAction(e -> {
                switch (index) {
                    case 0:
                        rootPanel.getChildren().removeAll(commonPanel, drinkPanel, principlePanel, vegetablePanel, meetPanel);
                        rootPanel.getChildren().add(commonPanel);
                        break;
                    case 1:
                        rootPanel.getChildren().removeAll(commonPanel, drinkPanel, principlePanel, vegetablePanel, meetPanel);
                        rootPanel.getChildren().add(drinkPanel);
                        break;
                    case 2:
                        rootPanel.getChildren().removeAll(commonPanel, drinkPanel, principlePanel, vegetablePanel, meetPanel);
                        rootPanel.getChildren().add(principlePanel);
                        break;
                    case 3:
                        rootPanel.getChildren().removeAll(commonPanel, drinkPanel, principlePanel, vegetablePanel, meetPanel);
                        rootPanel.getChildren().add(vegetablePanel);
                        break;
                    case 4:
                        rootPanel.getChildren().removeAll(commonPanel, drinkPanel, principlePanel, vegetablePanel, meetPanel);
                        rootPanel.getChildren().add(meetPanel);
                        break;
                }
                setTypeIndicator(rootPanel, names[index] + "食品");
            });
        }
    }
}


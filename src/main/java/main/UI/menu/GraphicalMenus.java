package main.UI.menu;

import javafx.stage.Stage;
import lombok.Getter;
import main.Configuration;
import main.gaze.devicemanager.AbstractGazeDeviceManager;
import main.gaze.devicemanager.TobiiGazeDeviceManager;

public class GraphicalMenus {

    @Getter
    private final AbstractGazeDeviceManager gazeDeviceManager = new TobiiGazeDeviceManager();
    @Getter
    private final Configuration configuration = new Configuration();
    @Getter
    private final String gazePlayInstallationRepo;

    final public Stage primaryStage;

    @Getter
    private final HomeScreen homeScreen;
    @Getter
    private final OptionsMenu optionsMenu;
    @Getter
    private final QuickMenu quickMenu;

    public GraphicalMenus(Stage primaryStage, String gazePlayInstallationRepo) {
        this.primaryStage = primaryStage;
        this.gazePlayInstallationRepo = gazePlayInstallationRepo;
        this.optionsMenu = new OptionsMenu(this);
        this.quickMenu = new QuickMenu(this);
        this.homeScreen = new HomeScreen(this);
    }
}

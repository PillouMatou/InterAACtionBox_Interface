package main.UI.menu;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import main.Configuration;
import main.gaze.devicemanager.AbstractGazeDeviceManager;
import main.gaze.devicemanager.TobiiGazeDeviceManager;
import main.utils.NamedProcess;

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

    public NamedProcess process = new NamedProcess();

    public GraphicalMenus(Stage primaryStage, String gazePlayInstallationRepo) {
        this.primaryStage = primaryStage;
        this.gazePlayInstallationRepo = gazePlayInstallationRepo;
        this.optionsMenu = new OptionsMenu(this);
        this.homeScreen = new HomeScreen(this);
    }

}


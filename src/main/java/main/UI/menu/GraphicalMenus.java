package main.UI.menu;

import javafx.stage.Stage;
import lombok.Getter;
import main.Configuration;
import main.ConfigurationBuilder;
import main.Main;
import main.gaze.devicemanager.AbstractGazeDeviceManager;
import main.gaze.devicemanager.TobiiGazeDeviceManager;
import main.utils.NamedProcess;
import main.utils.UpdateManager;

public class GraphicalMenus {

    final public Stage primaryStage;
    @Getter
    private final AbstractGazeDeviceManager gazeDeviceManager = new TobiiGazeDeviceManager();
    @Getter
    private final ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
    @Getter
    private final Configuration configuration = new Configuration();
    @Getter
    private final HomeScreen homeScreen;
    @Getter
    private final OptionsMenu optionsMenu;
    @Getter
    private final UpdateMenu updateMenu;
    @Getter
    private final ContactUs contactUs;

    public NamedProcess process = new NamedProcess();

    public GraphicalMenus(Stage primaryStage, Main main) {
        this.primaryStage = primaryStage;
        UpdateManager updateManager = new UpdateManager(configuration);
        this.homeScreen = new HomeScreen(this, updateManager,main,configuration);
        this.optionsMenu = new OptionsMenu(this,main,configuration);
        this.updateMenu = new UpdateMenu(this, updateManager, main, configuration);
        this.contactUs = new ContactUs(this,main, configuration);
    }

}


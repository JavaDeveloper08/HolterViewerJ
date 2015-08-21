package holter_app;

import java.awt.EventQueue;

import mvc.*;
import mvc.models.AppMainModel;
import mvc.models.AppViewerModel;
import mvc.views.AppMainView;
import mvc.views.AppViewerView;

/**
 * @name HolterADS1292
 * @author Artur Tynecki
 * @brief simple application to control HolterADS1292 device, download and view results
 * @version 1.0
 * 
 * @section DESCRIPTION
 * It is simple application for control HolterADS1292 by serial port. 
 * Application allows:
 * 	1. live stream date from device
 * 	2. save backup on device
 * 	3. erase all data form backup on device
 * 	4. send current time to device
 * 	5. get and display device state
 * 	6. download backup form device and write to CSV file with patient data
 * 	7. viewing data from CSV file by Viewer application
 * 
 * Graphical User Interface based on MCV (Model-View-Controller) pattern.
 * 
 * Project is divided to packages: 
 * 
 * [Adapters]
 * 		Adapters contain classes which provide I/O methods for serial port, file and chart module.
 * 
 * [Parsers]
 * 		Parsers are use to putting raw data to correct objects.
 * 		Contain parser to serial port data and file data.
 * 
 * [Data]
 * 		Data contain classes which are representation of specified object (patient, sample, time).
 * 
 * [MVC]
 * 		This packages contains classes which are components of MVC pattern (models, views, controller, exception).
 * 		Utils class contains auxiliary methods of project.
 * 
 * [Holter_app]
 *  	Contains main of project.
 *  
 * Remember that you must attached all libraries from lib directory.
 *
 */

public class HolterADS1292 {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	AppMainModel mainModel = new AppMainModel();
				AppMainView mainView = new AppMainView();
				AppViewerModel viewerModel = new AppViewerModel();
				AppViewerView viewerView = new AppViewerView();
				AppController controller = new AppController(mainModel, mainView, viewerModel, viewerView);
				mainView.setController(controller);
				viewerView.setController(controller);
				mainView.setVisible(true);
            }
        });
	}
}

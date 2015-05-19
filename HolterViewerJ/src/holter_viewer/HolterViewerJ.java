package holter_viewer;

import java.awt.EventQueue;
import mvc.*;

public class HolterViewerJ {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	AppModel model = new AppModel();
				AppView view = new AppView();
				AppController controller = new AppController(model, view);
				view.setController(controller);
				view.setVisible(true);
            }
        });

	}

}

package holter_app;

import java.awt.EventQueue;
import mvc.*;

/**
 * Project is divided to common and user packages. 
 * The common packages contain common and abstract classes which are used in different applications.
 * 
 * Class types in common packages:
 * [Adapters]
 * 		Adapters, are to lowest level classes which provide I/O methods used by Communicators.
 * 
 * [Communicator]
 * 		Raw data collected in Adapters, are passed to Communicators (Communicators, implement AdapterListenerInterface).
 * 		Moreover this class creates and manages data parsers.
 * 		In common package are two types of Communicators: AbstractCommunicator and FileStramCommunicator.
 * 		The first one is abstract class which user must extend and implement some abstract method.
 * 		The second one is implemented AbstractCommunicator class which provide load file as a i.e COMM port stream.
 * 
 * [FileAdapters]
 * 		Providing I/O operations to files.
 * 		FastFileAdapter using faster MappedByteBuffers and is recommended with binary files.
 * 		SimpleFileAdapter is using standard File Streams, and is recommended with text files.
 * 
 * [Parsers]
 * 		Parsers are use to putting raw data to correct objects.
 * 		AbstractParser implements whole parsing mechanism, so users job is to define frame patterns, 
 * 		and implement method which handling parsed data.
 * 		At the end of packing data to objects user must call sendDataToListeners() passing AbstractParserData object.
 * 		Each parser works in separate thread.
 * 
 * [Processing]
 * 		Some classes which share useful data processing functions.
 * 
 * [Views]
 * 		This is GUI Layer divided to generic views which are common in all applications.
 * 
 * [Link]
 *  	Classes of this type are separated layer, called link layer. It is intended to link others layer together.
 *  	User, extends AbstractLink class give the form of his application.
 *  
 *  
 * Remember that you must attached all libraries from lib directory.
 *
 */

public class HolterADS1292 {

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

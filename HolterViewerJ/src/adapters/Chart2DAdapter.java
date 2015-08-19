package adapters;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis.AxisTitle;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;

import java.awt.Color;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @class Chart2DAdapter
 * @brief adapter to Chart2D operations
 * @extends TimerTask
 * @implements MouseLisnster
 */
public class Chart2DAdapter extends TimerTask implements MouseListener{
    protected Chart2D chart;
    protected int maxPoints;
    protected Timer timer;

    public Chart2DAdapter(int maxPoints){
    	chart = new Chart2D();
    	this.maxPoints = maxPoints;
        chart.setBackground(Color.LIGHT_GRAY);
    	chart.setGridColor(Color.BLACK);
        chart.addMouseListener(this);
        setXLabel("");
        setYLabel("");
     
        timer = new Timer();
        timer.schedule(this, 10, 1000);
    }
    
	@Override
	public void run() {
		chart.validate();
		chart.updateUI();
	}
	
	 /**
     * @methods getChartPanel()
     * @brief getter to chart panel
     * @return chart panel
     */
	public Chart2D getChartPanel(){
		return chart;
	}

	/**
    * @methods setGrid()
    * @brief set grid on chart panel
    * @param enabled - enabled command
    */
	public void setGrid(boolean enabled){
        chart.getAxisX().setPaintGrid(enabled);
        chart.getAxisY().setPaintGrid(enabled);
	}
	
	/**
	* @methods setXLabel()
	* @brief set X label on chart panel
	* @param xlabel - X label name
	*/
    public void setXLabel(String xlabel){
    	chart.getAxisX().setAxisTitle(new AxisTitle(xlabel));
    }
    
	/**
	* @methods setYLabel()
	* @brief set Y label on chart panel
	* @param ylabel - Y label name
	*/
    public void setYLabel(String ylabel){
    	chart.getAxisY().setAxisTitle(new AxisTitle(ylabel));
    }
    
	/**
	 * @methods createNewTraceLtd()
	 * @brief create and add new LTDtrace to chart panel
	 * @param c - trace color
	 * @param name - trace name
	 */
	public void createNewTraceLtd(Color c, String name){
		ITrace2D trace = new Trace2DLtd(maxPoints); 
	    trace.setColor(c);
	    trace.setName(name);
	    trace.setVisible(true);
	    chart.addTrace(trace);
	}

	/**
	 * @methods addPoint()
	 * @param add point with coordinates X and Y
	 * @param x - x value
	 * @param y - y value
	 */
	public void addPoint(double x, double y) {
		chart.getTraces().iterator().next().addPoint(x, y);
	}
	
	/**
	 * @methods addPoint()
	 * @param add point with coordinates Y and increment X coordinates by space
	 * @param y - y value
	 * @param xsapce - space between next x value
	 */
	public void addPoint(double y, int xspace) {
		ITrace2D trace;
		trace = chart.getTraces().iterator().next();
		int index = (int)Math.round(trace.getMaxX());
		trace.addPoint(index+xspace, y);
	}
	
	/**
	 * @methods clearTraces()
	 * @param remove all points from trace
	 */
	public void clearTraces(){
		for (ITrace2D trace : chart.getTraces()) {
			trace.removeAllPoints();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch (e.getModifiers()) {
		case InputEvent.BUTTON1_MASK:
			clearTraces();
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) { }

	@Override
	public void mouseExited(MouseEvent arg0) { }

	@Override
	public void mousePressed(MouseEvent arg0) { }

	@Override
	public void mouseReleased(MouseEvent arg0) { }
}

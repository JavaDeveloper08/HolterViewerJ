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

public class SimpleChartAdapter extends TimerTask implements MouseListener{
    protected Chart2D chart;
    protected int maxPoints;
    protected Timer timer;

    
    public SimpleChartAdapter(int maxPoints){

    	chart = new Chart2D();
    	this.maxPoints = maxPoints;
        chart.setBackground(Color.BLACK);
    	chart.setGridColor(Color.DARK_GRAY);
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

	public void setGrid(boolean enabled){
        chart.getAxisX().setPaintGrid(enabled);
        chart.getAxisY().setPaintGrid(enabled);
	}
	
    public void setXLabel(String xlabel){
    	chart.getAxisX().setAxisTitle(new AxisTitle(xlabel));
    }
    
    public void setYLabel(String ylabel){
    	chart.getAxisY().setAxisTitle(new AxisTitle(ylabel));
    }
    
    public Chart2D getChartPanel(){
    	return chart;
    }
	/**
	 * Adding new Ltd trace to ChartPanel
	 * @param c - trace color
	 * @param size - trace size
	 * @param name - trace name
	 */
	public void createNewTraceLtd(Color c, String name){
		createNewTraceLtd(c, name, true);
	}
	/**
	 * Adding new Ltd trace to ChartPanel
	 * @param c - trace color
	 * @param size - trace size
	 * @param name - trace name
	 */
	public void createNewTraceLtd(Color c, String name, boolean visible){
		ITrace2D trace = new Trace2DLtd(maxPoints); 
	    trace.setColor(c);
	    trace.setName(name);
	    trace.setVisible(visible);
	    chart.addTrace(trace);
	    
	}

	public void setTraceVisible(String name, boolean visible){
		for (ITrace2D trace : chart.getTraces()) {
			if (trace.getName().equals(name)) {
				trace.setVisible(visible);
			}
		}
	}
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void addPoint(double x, double y) {
		chart.getTraces().iterator().next().addPoint(x, y);
	}
	/**
	 * 
	 * @param y
	 */
	public void addPoint(double y) {
		ITrace2D trace;
		trace = chart.getTraces().iterator().next();
		int index = (int)Math.round(trace.getMaxX());
		trace.addPoint(++index, y);
	}
	/**
	 * Adding point to specific trace
	 * @param y - value
	 * @param name - trace name
	 */
	public void addPoint(double y, String name) {
		for (ITrace2D trace : chart.getTraces()) {
			if (trace.getName().equals(name)) {
				int index = (int)Math.round(trace.getMaxX());
				trace.addPoint(++index, y);
			}
		}
	}
	/**
	 * Adding point to specific trace
	 * @param y - value
	 * @param name - trace name
	 */
	public void addPoint(double x, double y, String name) {
		for (ITrace2D trace : chart.getTraces()) {
			if (trace.getName().equals(name)) {
				trace.addPoint(x, y);
			}
		}
	}
	
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
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}

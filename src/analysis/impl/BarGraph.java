package analysis.impl;


import java.util.Map;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

public class BarGraph extends ApplicationFrame {
	static CategoryDataset dataset;
	
	public  BarGraph(Map<String, Double> DataSet) {
        super("Music");
        dataset = createDataset(DataSet);   
    }
    private static CategoryDataset createDataset(Map<String, Double> obj) {

        // row keys...
        String series1 = "Genre";
        // column keys...
        Set<String> genres = obj.keySet();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(String genre : genres) {
        	dataset.addValue(obj.get(genre), series1, genre);
           
        }
        
        
        
        return dataset;
    }

    public static JFreeChart createBarGraph() {
    	

        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
            "Genre Preference",       // BarGraph title
            "Genre",               // X axis label
            "Listening Preference",                  // Y axis label
            dataset,                  // Music data
            PlotOrientation.VERTICAL, 
            true,                    
            true,                     
            false                     
        );
        return chart;
        /*ChartPanel chartPanel = new ChartPanel(chart);
        return chartPanel;*/
    }
    
}

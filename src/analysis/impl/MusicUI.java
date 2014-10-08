package analysis.impl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartPanel;

import database.impl.DBAccessor;
public class MusicUI implements ActionListener {

	/**
	 * @param args
	 */
	JFrame fr;
	JLabel lb;
	JLabel lbStatus,lbUserlist,lbUserHistory,lbRec;
	JTable gridViewUserName,gridViewSong,gridViewHistory;
	JButton btnGenRec,btnNext,btnPrevious,btnPlay;
	JTextArea txt;
	JPanel panel;
	JScrollPane panel1;
	JScrollPane panelUserList,panelSongList,panelHistory,panelBar;
	Controller controller;
	Collaborative collaborative;
    Collaborative2 collaborative2;

	//JComboBox comboBox;
	ChartPanel bargraph=null;
	JPanel jPa;
	ChartPanel chartPanel = null;
	
	int offset = 0;
	String columnNames[] = { "UserID", "UserName" };
	String trackNames[] = { "TrackID", "TrackName" };


	MusicUI()
	{
		collaborative = new Collaborative();
        collaborative2 = new Collaborative2();

		fr=new JFrame("Music Recommendation System");
		fr.setLayout(null);
        //fr.setLayout(new BorderLayout());

		lb= new JLabel("Current Status");
		lbStatus=new JLabel();
		lbUserlist= new JLabel("User List");
		lbUserHistory= new JLabel("User Song History");
		lbRec= new JLabel("Recommended Songs");
		
		String str="Select user from the user list and click on \n'Generate Recommendation' button to generate\n song recommendation for the user";
		txt= new JTextArea(str);
		txt.setBounds(35, 110, 280, 90);
		txt.enable(false);
		
		// Create columns names
		controller = new Controller();
		String data[][] = controller.getData(offset,10);

		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		gridViewUserName = new JTable() {
			public boolean isCellEditable(int rowIndex, int colIndex) {
				  return false; //Disallow the editing of any cell
				  }
		};
		gridViewUserName.setModel(model);
		
		String trackdata[][] = null;
		DefaultTableModel model1 = new DefaultTableModel(trackdata, trackNames);
		
		gridViewSong = new JTable() {
			public boolean isCellEditable(int rowIndex, int colIndex) {
				  return false; //Disallow the editing of any cell
				  }
		};
		String trackdataHistory[][] = null;
		DefaultTableModel model2 = new DefaultTableModel(trackdataHistory, trackNames);
		gridViewHistory = new JTable() {
			public boolean isCellEditable(int rowIndex, int colIndex) {
				  return false; //Disallow the editing of any cell
				  }
		};
			
		gridViewHistory.setModel(model2);
		gridViewSong.setModel(model1);
		
		//comboBox=new JComboBox();
		//Color cr=new Color();
		
		//Setting the selection mode to single row selection
		gridViewUserName.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		gridViewSong.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		gridViewHistory.setEnabled(false);
		//setting bounds
		panel=new JPanel();
		panelUserList=new JScrollPane(gridViewUserName);
		panelSongList= new JScrollPane(gridViewSong);
		
		
		panelHistory=new JScrollPane(gridViewHistory);
		panelHistory.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		panel.setLayout(new BorderLayout(100,100));
		
		btnGenRec=new JButton("Generate Recommendation");
		btnGenRec.setEnabled(false);
		btnNext=new JButton("Next page");
		btnPlay=new JButton("Play");
		
		jPa = new JPanel();
		jPa.setLayout(new BorderLayout(100,100));
		jPa.setBounds(750, 100, 600, 300);
		
		panel1=new JScrollPane();
		chartPanel = new ChartPanel(null);
		
		jPa.add(chartPanel, BorderLayout.CENTER);
		//panelBar.repaint();
		
		panelBar = new  JScrollPane(panel1);
		
		
		btnPrevious=new JButton("Previous page");
		if(offset == 0) {
			btnPrevious.setEnabled(false);
		}
		//Color cr=new Color(0,0,0);
		panel.setBounds(30, 100, 300, 100);
		panelUserList.setBounds(30, 220, 300, 200);
		panelHistory.setBounds(400,80,300, 180);
		panelSongList.setBounds(400, 320, 300, 180);
		panelBar.setBounds(750, 80, 300, 200);
		
		lbUserlist.setBounds(30, 200, 100, 20);
		lbUserHistory.setBounds(400,60, 150, 20);
		lbRec.setBounds(400, 300, 150, 20);
		
		
		lb.setBounds(30, 10, 100, 100);
		
		btnNext.setBounds(160, 500, 120, 30);
		btnPrevious.setBounds(30, 500, 120, 30);
		btnGenRec.setBounds(30, 550, 200, 30);
		btnPlay.setBounds(400, 550, 120, 30);
		btnPlay.setEnabled(false);
		
		// adding action Listener
		gridViewUserName.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				btnGenRec.setEnabled(true);
			}
		});
		//gridViewUserName.set
		//
        gridViewSong.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				btnPlay.setEnabled(true);
			}
		});
		//
		
		btnGenRec.addActionListener(this);
		btnNext.addActionListener(this);
		btnPrevious.addActionListener(this);
		btnPlay.addActionListener(this);
		
		//add panel to frame
		panel.add(txt,BorderLayout.CENTER);
		//panel1.add(gridView,BorderLayout.CENTER);
		//adding to the frame
		
		panelBar=new JScrollPane();// Give the Chart Object Here
	
		
		fr.add(lb);
		fr.add(lbUserlist);
		fr.add(lbUserHistory);
		fr.add(lbRec);
		fr.add(btnGenRec);
		fr.add(btnNext);
		fr.add(btnPrevious);
		fr.add(btnPlay);
		fr.add(panel);
		fr.add(panelUserList);
		fr.add(panelSongList);
		fr.add(panelHistory);
		fr.add(jPa);
		
		fr.setSize(1000,700);
		fr.setVisible(true);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnNext)) {
			btnPrevious.setEnabled(true);
			offset = offset + 10;
			String data[][] = controller.getData(offset,10);
			DefaultTableModel model = (DefaultTableModel) gridViewUserName.getModel();
	        model.setDataVector(data, columnNames);
	        model.fireTableDataChanged();
	        btnGenRec.setEnabled(false);

		}
		if(e.getSource().equals(btnPrevious)) {
			btnPrevious.setEnabled(true);
			offset = offset - 10;
			String data[][] = controller.getData(offset,10);
			DefaultTableModel model = (DefaultTableModel) gridViewUserName.getModel();
	        model.setDataVector(data, columnNames);
	        model.fireTableDataChanged();
	        if(offset <=0 ) {
	        	btnPrevious.setEnabled(false);
	        }
	        btnGenRec.setEnabled(false);
		}
		if (e.getSource().equals(btnGenRec)) {
	        btnGenRec.setEnabled(false);

		    txt= new JTextArea("Recommendation Generated");
		    
			int row = gridViewUserName.getSelectedRow();
			String userId = (String)gridViewUserName.getValueAt(row, 0);
			TrackUserBO bo = null;
			Map<String, Collection<TrackInformation>> map = collaborative.getRecommendations(
					userId);
			ArrayList<Track> recommendations2 = collaborative2.getUserRecommendations(userId);

			Collection<TrackInformation> informations = map.get("history");
			if(informations != null && !informations.isEmpty()) {
				int totalSongs = informations.size();
				Map<String, Double> mapp = new HashMap<String, Double>();
				//Set<String> set = bo.getInfo().keySet();
				for(TrackInformation information : informations) {
					//TrackInformation information = bo.getInfo().get(se);
					if(information != null) {
						Set<String> genres = information.getGenres();
						for(String genre : genres) {
							if(mapp.containsKey(genre)) {
								mapp.put(genre, mapp.get(genre) + 1.0/totalSongs);
							} else {
								mapp.put(genre, 1.0/totalSongs);
							}
						}
					}
				}
				controller.updateGenrePreference(userId, mapp);
				BarGraph bar = new BarGraph(mapp);
				chartPanel.setChart(bar.createBarGraph());
				chartPanel.repaint();
			} else {
				chartPanel.setChart(null);
				chartPanel.repaint();
			}
			
			Collection<TrackInformation> list = map.get("suggestions");

			for(int i=0;i<recommendations2.size();i++){
                Track t = recommendations2.get(i);
                TrackInformation ele = new TrackInformation();
                ele.setTitle(t.title);
                ele.setTrackId(t.id);
                list.add(ele);
            }

			
			String[][] trackdata = new String[50][];
			int i = 0;
			for(TrackInformation information : list) {
				trackdata[i] = new String[2];
				trackdata[i][0] = information.getTrackId();
				trackdata[i][1] = information.getTitle();
				i++;
			}
			
			DefaultTableModel model = (DefaultTableModel) gridViewSong.getModel();
	        model.setDataVector(trackdata, trackNames);
	        model.fireTableDataChanged();
	        
	        Collection<TrackInformation> history = map.get("history");
	        
	        trackdata = null;
	        if(history != null && !history.isEmpty()) {
	        trackdata = new String[32][];
			i = 0;
			for(TrackInformation information : history) {
				trackdata[i] = new String[2];
				trackdata[i][0] = information.getTrackId();
				trackdata[i][1] = information.getTitle();
				i++;
			}
	        }
	        DefaultTableModel model1 = (DefaultTableModel) gridViewHistory.getModel();
	        model1.setDataVector(trackdata, trackNames);
	        model1.fireTableDataChanged();
	        btnGenRec.setEnabled(true);

		}
		// Play Button
       if(e.getSource().equals(btnPlay)){
    	   int row = gridViewSong.getSelectedRow();
			String trackId = (String)gridViewSong.getValueAt(row, 0);
			row = gridViewUserName.getSelectedRow();
			String userId = (String)gridViewUserName.getValueAt(row, 0);
			
			controller.updateCountInHasPlayed(trackId, userId);
			btnPlay.setEnabled(false);
		  }
	}


public static void main(String agr[])
{
	try
	{
	MusicUI obj=new MusicUI();	
	//obj.show();
	}
	catch(Exception e)
	{e.printStackTrace();}
	}
}


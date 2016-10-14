/*
 * 	Author Vitaly Borodin vitaly.borodin@hp.com
 * 	This file is part of HP Visitor Kiosk.
 *
 *	HP Visitor Kiosk is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *   
 *	HP Visitor Kiosk is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *   
 *	You should have received a copy of the GNU General Public License
 *	along with HP Visitor Kiosk.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class KioskSetings 
{
	public JFrame frame;
	private JDialog spframe;
	private JPanel contentPane;
	private JPanel buttonePane;
	private JPanel mainPane;
	private GridBagConstraints contentlayout;
	private FlowLayout buttonlayout;
	private JTextField textTitle 			= new JTextField();
	private JTextField textSubtitle			= new JTextField();
	private JTextField textKioskDB 			= new JTextField();
	private JTextArea  textHPIToolTip		= new JTextArea();
	private JTextArea  textOtherTooltip 	= new JTextArea();
	private JTextArea  textTempBadgeToolTip = new JTextArea();
	private JTextArea  textTempBadgeNumToolTip	= new JTextArea();
	// private JTextField textEquipmentDB 		= new JTextField();
	private JTextArea 	textCompany 		= new JTextArea(5, 20);
	private JScrollPane scrollCompany 		= new JScrollPane(textCompany);
	private JTextArea 	textSponsor 		= new JTextArea(5, 20);
	private JScrollPane scrollSponsor 		= new JScrollPane(textSponsor);
	private JTextArea 	textReasonFoVisit 	= new JTextArea(5, 20);
	private JScrollPane scrollReasonFoVisit = new JScrollPane(textReasonFoVisit);
	private JTextArea 	textCitizenship 	= new JTextArea(5, 20);
	private JScrollPane scrollCitizenship 	= new JScrollPane(textCitizenship);
	private JCheckBox chckbxTatle;
	private JCheckBox chckbxSubtitle;
	private JCheckBox chckbxCompany;
	private JCheckBox chckbxSponsor;
	private JCheckBox chckbxReasonFoVisit;
	private JCheckBox chckbxCitizenship;
	private JCheckBox chckbxRotateImage  = new JCheckBox();
	private JCheckBox chckbxPrintBadgeT  = new JCheckBox();
	private Timer t;
	private KioskData kioskdt;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem menuItem;
	
	/**
	 * Create the frame.
	 */
	public KioskSetings(KioskData data)
	{		
		int delmin = (int) (60000 * 10); // 10 min
	    
	    this.t = new Timer(delmin, new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	        Window[] arrayOfWindow;
	        int j = (arrayOfWindow = Window.getWindows()).length;
	        for (int i = 0; i < j; i++)
	        {
	          Window window = arrayOfWindow[i];
	          if ((window instanceof JDialog)) {
	            window.dispose();
	          }
	        }
	        if (spframe != null) spframe.dispose();
	      }
	    });
		
		this.kioskdt = data;
		setValue();
		setUI();
		if (spframe != null) spframe.dispose();
	}
	
	public void setFrame(JDialog dialog)
	{
	    t.start();
	    spframe = dialog;
	    spframe.addWindowListener(new WindowAdapter()
	    {
	    	@Override
	    	public void windowClosed(WindowEvent e){	}
	    	
	    	@Override 
	    	public void windowClosing(WindowEvent e){	}
	    });
	
	    try
	    {
	    	/* Inserting icon to the settings left corner  */
		    URL url = ClassLoader.getSystemResource(KioskData.icon);
			Toolkit kit = Toolkit.getDefaultToolkit();
			Image img = kit.createImage(url);
			if (spframe != null) spframe.setIconImage(img);
			if (spframe != null) spframe.setJMenuBar(menuBar);
			/* */
			
	    } catch(Exception e)
	    {
	    	System.out.println(e.getMessage());
	    }
	}
	
	private void setValue()
	{
		textTitle.setText(kioskdt.getStrTitle());
		Border b = textTitle.getBorder();
		textSubtitle.setText(kioskdt.getStrSubTitle());
		textKioskDB.setText(kioskdt.getInfo_URLOfKioskDB_());
		textHPIToolTip.setText(kioskdt.getToolTipHPD());
		textOtherTooltip.setText(kioskdt.getToolTipOtherD());
		textTempBadgeToolTip.setText(kioskdt.getToolTipTempBadgeD());
		textTempBadgeNumToolTip.setText(kioskdt.getToolTipTempBadgeNumberRow());
		textHPIToolTip.setBorder(b);
		textOtherTooltip.setBorder(b);
		textTempBadgeToolTip.setBorder(b);
		textTempBadgeNumToolTip.setBorder(b);
		// textEquipmentDB.setText(kioskdt.getInfo_URLOfEquipmentDB_());
		setTextArr(textCompany, kioskdt.getArrCompany());
		setTextArr(textSponsor, kioskdt.getArrSponsor());
		setTextArr(textReasonFoVisit, kioskdt.getArrReason());
		setTextArr(textCitizenship, kioskdt.getArrCitizenship());
		chckbxRotateImage.setSelected(kioskdt.getImageRotation());
		chckbxPrintBadgeT.setSelected(kioskdt.isPrintGastvTempBadg());
	}
	
	private void setTextArr(JTextArea arg, String[] arr)
	{
		String str = "";
		List<String> 	list = null;
		
		list =  new ArrayList<String>( Arrays.asList(arr));
		boolean first = true;
		for (int i=0; i < list.size(); i++ )
		{
			if (!list.get(i).equals("Select"))
			{
				if(first)
				{
					str += list.get(i);
					first=false;
				}
				else
					str += "\n" + list.get(i);
			}
		}
		
		arg.setColumns(30);
		arg.setRows(5);
		arg.setLineWrap(true);
		arg.setWrapStyleWord(false); 
		arg.setText(str);
		arg.setCaretPosition(0);
	}
	
	private void setUI()
	{
		menuBar = new JMenuBar();
		menu = new JMenu("Help");
		menu.getAccessibleContext().setAccessibleDescription("The menu for help and about items");
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(menu);
		menuItem = new JMenuItem("Help");
		menuItem.setFont(kioskdt.internaLlabel);
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					File pdfFile = new File(kioskdt.getManualfile());
					if (pdfFile.exists())
					{
						if (Desktop.isDesktopSupported())
							Desktop.getDesktop().open(pdfFile);
						else
						{
							JOptionPane.showMessageDialog(spframe,
								    "Awt Desktop is not supported!.\n"
									+ kioskdt.getManualfile(),
								    "kiosk error",
								    JOptionPane.ERROR_MESSAGE);
							if (KioskData.debug) System.out.println("Awt Desktop is not supported!");
						}
					}else
					{
						JOptionPane.showMessageDialog(spframe,
							    "File is not exists!.",
							    "kiosk error",
							    JOptionPane.ERROR_MESSAGE);
						if (KioskData.debug) System.out.println("File is not exists!");
					}
					if (KioskData.debug) System.out.println("Done");
				} catch (Exception e)
				{
					JOptionPane.showMessageDialog(spframe,
						    e.getMessage(),
						    "kiosk error",
						    JOptionPane.ERROR_MESSAGE);
					  e.printStackTrace();
				}
				/* *
				JOptionPane.showMessageDialog(spframe,
					      "01110011 01101111 01110010 01110010 01111001 00100000 01101110 01101111\n"
					    + "00100000 01101001 00100000 01100100 01101001 01100100 01101110 00100111\n"
					    + "01110100 00100000 01101000 01100001 01110110 01100101 00100000 01110100\n"
					    + "01101001 01101101 01100101 00100000 01110100 01101111 00100000 01110111\n"
					    + "01110010 01101001 01110100 01100101 00100000 01101000 01100101 01101100\n"
					    + "01110000 00101110 00100000 01001001 01110100 00100000 01101101 01100001\n"
					    + "01111001 00100000 01100010 01100101 00100000 01101000 01100101 01110010\n"
					    + "01100101 00100000 01101001 01101110 00100000 01101110 01100101 01111000\n"
					    + "01110100 00100000 01110110 01100101 01110010 01110011 01101001 01101111\n"
					    + "01101110 00001010\n\n"
					    + "01000111 01101111 01101111 01100100 00100000 01101100 01110101 01100011\n"
					    + "01101011 00100001");
				/* */				
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("About");
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(spframe,
					    kioskdt.getAbout());
			}
		});
		menu.add(menuItem);
		if (spframe != null) spframe.setJMenuBar(menuBar);
		Border b = textTitle.getBorder();
		contentPane = new JPanel();
		buttonePane = new JPanel();
		mainPane = new JPanel(new BorderLayout());
		contentlayout = new GridBagConstraints();
		buttonlayout = new  FlowLayout(FlowLayout.CENTER, 50, 5);
		buttonePane.setLayout(buttonlayout);
		contentlayout.fill = GridBagConstraints.HORIZONTAL;
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(100, 100, 950, 750);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridBagLayout());
		// String titlstr = "check the box if you want to be this settings local";
		JLabel TheTatile = new JLabel("");
		contentlayout.fill = GridBagConstraints.HORIZONTAL;
		contentlayout.weightx = 1.0;
		contentlayout.gridwidth = 4;
		contentlayout.gridx = 0;
		contentlayout.gridy = 0;
		TheTatile.setHorizontalAlignment(SwingConstants.CENTER);
		TheTatile.setFont(kioskdt.internaLlabel);
		contentPane.add(TheTatile, contentlayout);		
		
		int i1 = 0;
		int i2 = 1;
		JLabel lblTitle = new JLabel("Title");
		contentlayout.weightx = 0.5;
		contentlayout.weighty = 0.01;
		contentlayout.fill = GridBagConstraints.HORIZONTAL;
		contentlayout.gridwidth = 1;
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		lblTitle.setFont(kioskdt.internaLlabel);
		contentPane.add(lblTitle, contentlayout);
		
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		textTitle.setFont(kioskdt.internaLlabel);
		contentPane.add(textTitle, contentlayout);
		textTitle.setColumns(10);
		
		chckbxTatle = new JCheckBox("local / global");
		contentlayout.gridx = ++i1;
		contentlayout.gridy = i2;
		chckbxTatle.setVerticalAlignment(SwingConstants.TOP);
		chckbxTatle.setFont(kioskdt.internaLlabel);
		// contentPane.add(chckbxTatle, contentlayout);
		
		i1 = 0;
		i2++;
		JLabel lblSubtitle = new JLabel("Subtitle");
		lblSubtitle.setFont(kioskdt.internaLlabel);
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		contentPane.add(lblSubtitle, contentlayout);
		
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		textSubtitle.setFont(kioskdt.internaLlabel);
		contentPane.add(textSubtitle, contentlayout);
		textSubtitle.setColumns(10);

		chckbxSubtitle = new JCheckBox("local / global");
		contentlayout.gridx = ++i1;
		contentlayout.gridy = i2;
		chckbxSubtitle.setVerticalAlignment(SwingConstants.TOP);
		chckbxSubtitle.setFont(kioskdt.internaLlabel);
		// contentPane.add(chckbxSubtitle, contentlayout);

		//text Kiosk / Equipment DB
		i1 = 0;
		i2++;
		JLabel lblKioskDB = new JLabel("URL for Kiosk Database");
		lblSubtitle.setFont(kioskdt.internaLlabel);
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		contentPane.add(lblKioskDB, contentlayout);
		
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		textKioskDB.setFont(kioskdt.internaLlabel);
		textKioskDB.setColumns(10);
		textKioskDB.setEditable(kioskdt.editfield);
		contentPane.add(textKioskDB, contentlayout);
		
	/*	
		i1 = 0;
		i2++;
		JLabel lblEquipmentDB = new JLabel("URL for Equipment Database");
		lblSubtitle.setFont(kioskdt.internaLlabel);
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		contentPane.add(lblEquipmentDB, contentlayout);
		
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		textEquipmentDB.setFont(kioskdt.internaLlabel);
		textEquipmentDB.setEditable(kioskdt.editfield);
		textEquipmentDB.setColumns(10);
		contentPane.add(textEquipmentDB, contentlayout);

		textOtherTooltip
		textTempBadgeToolTip
		textTempBadgeNumTollTip
*/
		i1 = 0;
		i2++;
		JLabel lblHPIToolTip = new JLabel("HPI Tool Tip");
		lblHPIToolTip.setFont(kioskdt.internaLlabel);
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		contentPane.add(lblHPIToolTip, contentlayout);
		
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		textHPIToolTip.setFont(kioskdt.internaLlabel);
		textHPIToolTip.setEditable(kioskdt.editfield);
		textHPIToolTip.setColumns(10);
		contentPane.add(textHPIToolTip, contentlayout);

/**********************************************************************************************/

		i1 = 0;
		i2++;
		JLabel lblOtherToolTip = new JLabel("Other Tool Tip");
		lblOtherToolTip.setFont(kioskdt.internaLlabel);
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		contentPane.add(lblOtherToolTip, contentlayout);
		
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		textOtherTooltip.setFont(kioskdt.internaLlabel);
		textOtherTooltip.setEditable(kioskdt.editfield);
		textOtherTooltip.setColumns(10);
		contentPane.add(textOtherTooltip, contentlayout);


		i1 = 0;
		i2++;
		JLabel lblTempBadgeToolTip = new JLabel("Temp Badge Tool Tip");
		lblTempBadgeToolTip.setFont(kioskdt.internaLlabel);
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		contentPane.add(lblTempBadgeToolTip, contentlayout);
		
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		textTempBadgeToolTip.setFont(kioskdt.internaLlabel);
		textTempBadgeToolTip.setEditable(kioskdt.editfield);
		textTempBadgeToolTip.setColumns(10);
		contentPane.add(textTempBadgeToolTip, contentlayout);

	/*
		i1 = 0;
		i2++;
		JLabel lblTempBadgeNumToolTip = new JLabel("Themp Badge Number Tool Tip");
		lblTempBadgeNumToolTip.setFont(kioskdt.internaLlabel);
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		contentPane.add(lblTempBadgeNumToolTip, contentlayout);
		
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		textTempBadgeNumToolTip.setFont(kioskdt.internaLlabel);
		textTempBadgeNumToolTip.setEditable(kioskdt.editfield);
		textTempBadgeNumToolTip.setColumns(10);
		contentPane.add(textTempBadgeNumToolTip, contentlayout);


/*********************************************************************************************/
		i1 = 0;
		i2++;
		JLabel lblCompany = new JLabel("Company");
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		lblCompany.setFont(kioskdt.internaLlabel);
		contentPane.add(lblCompany, contentlayout);
		

		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		textCompany.setEditable(true);
		textCompany.setBorder(b);
		textCompany.setFont(kioskdt.internaLlabel);
		contentPane.add(scrollCompany, contentlayout);

		chckbxCompany = new JCheckBox("local / global");
		contentlayout.gridx = i1+1;
		contentlayout.gridy = i2;
		chckbxCompany.setVerticalAlignment(SwingConstants.TOP);
		chckbxCompany.setFont(kioskdt.internaLlabel);
		// contentPane.add(chckbxCompany, contentlayout);

		i1 = 0;
		i2++;
		JLabel lblSponsor = new JLabel("Sponsor");
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		lblSponsor.setFont(kioskdt.internaLlabel);
		contentPane.add(lblSponsor, contentlayout);

		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		textSponsor.setEditable(true);
		textSponsor.setFont(kioskdt.internaLlabel);
		contentPane.add(scrollSponsor, contentlayout);

		chckbxSponsor = new JCheckBox("local / global");
		contentlayout.gridx = i1+1;
		contentlayout.gridy = i2;
		chckbxSponsor.setVerticalAlignment(SwingConstants.TOP);
		chckbxSponsor.setFont(kioskdt.internaLlabel);
		// contentPane.add(chckbxSponsor, contentlayout);

		i1 = 0;
		i2++;
		JLabel lblReasonFoVisit = new JLabel("Reason fo visit");
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		lblReasonFoVisit.setFont(kioskdt.internaLlabel);
		contentPane.add(lblReasonFoVisit, contentlayout);

		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		textReasonFoVisit.setEditable(true);
		textReasonFoVisit.setFont(kioskdt.internaLlabel);
		contentPane.add(scrollReasonFoVisit, contentlayout);

		chckbxReasonFoVisit = new JCheckBox("local / global");
		contentlayout.gridx = i1+1;
		contentlayout.gridy = i2;
		chckbxReasonFoVisit.setVerticalAlignment(SwingConstants.TOP);
		chckbxReasonFoVisit.setFont(kioskdt.internaLlabel);
		// contentPane.add(chckbxReasonFoVisit, contentlayout);

		i1 = 0;
		i2++;
		JLabel lblCitizenship = new JLabel("Citizenship");
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		lblCitizenship.setFont(kioskdt.internaLlabel);
		contentPane.add(lblCitizenship, contentlayout);
		
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		textCitizenship.setEditable(true);
		textCitizenship.setFont(kioskdt.internaLlabel);
		contentPane.add(scrollCitizenship, contentlayout);
		
		chckbxCitizenship = new JCheckBox("local / global");
		contentlayout.gridx = i1+1;
		contentlayout.gridy = i2;
		chckbxCitizenship.setVerticalAlignment(SwingConstants.TOP);
		chckbxCitizenship.setFont(kioskdt.internaLlabel);
		// contentPane.add(chckbxCitizenship, contentlayout);
		
		i1 = 0;
		i2++;
		JLabel lblRotateImage = new JLabel("Rotate Print Image 180 Degrees");
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		lblRotateImage.setFont(kioskdt.internaLlabel);
		contentPane.add(lblRotateImage, contentlayout);
		
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		chckbxRotateImage.setVerticalAlignment(SwingConstants.TOP);
		chckbxRotateImage.setFont(kioskdt.internaLlabel);
		contentPane.add(chckbxRotateImage, contentlayout);
		
		i1 = 0;
		i2++;
		JLabel lblPrintBadgeforTemp = new JLabel("Print guest badge if Temp badge requested.");
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		lblPrintBadgeforTemp.setFont(kioskdt.internaLlabel);
		contentPane.add(lblPrintBadgeforTemp, contentlayout);
		
		contentlayout.gridx = i1++;
		contentlayout.gridy = i2;
		chckbxPrintBadgeT.setVerticalAlignment(SwingConstants.TOP);
		chckbxPrintBadgeT.setFont(kioskdt.internaLlabel);
		contentPane.add(chckbxPrintBadgeT, contentlayout);
		// chckbxPrintBadgeT
		
		int buttoneHight = 30,
			buttoneWidth = 150;
		
		BTNSubmit btnsubm = new BTNSubmit();
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setPreferredSize(new Dimension(buttoneWidth, buttoneHight));
		btnSubmit.addActionListener(btnsubm);
		buttonePane.add(btnSubmit, buttonlayout);
		
		BTNCancel btnc = new BTNCancel();
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(btnc);
		btnCancel.setPreferredSize(new Dimension(buttoneWidth, buttoneHight));
		buttonePane.add(btnCancel, buttonlayout);
		
		mainPane.add(contentPane, BorderLayout.CENTER );
		mainPane.add(buttonePane, BorderLayout.SOUTH );
		frame.setContentPane(mainPane);
	}

	/*
	 * Clean the window
	private void delUI()
	{
		contentPane.removeAll(); //or remove(JComponent)
		contentPane.revalidate();
		contentPane.repaint();
	}
	*/
	
	private void collectData()
	{
		kioskdt.setStrTitle(						textTitle.getText() 			);
		kioskdt.setStrSubTitle(						textSubtitle.getText()			);
		kioskdt.setArrCompany( 		splitString (	textCompany.getText() 		)	);
		kioskdt.setArrSponsor( 		splitString (	textSponsor.getText() 		)	);
		kioskdt.setArrReason(  		splitString (	textReasonFoVisit.getText()	)	);
		kioskdt.setArrCitizenship( 	splitString (	textCitizenship.getText()	)	);
		kioskdt.setInfo_URLOfKioskDB(				textKioskDB.getText()			);
		kioskdt.setToolTipHP(						textHPIToolTip.getText()		);
		kioskdt.setToolTipOther(					textOtherTooltip.getText() 		);
		kioskdt.setToolTipTempBadge(				textTempBadgeToolTip.getText() 	);
		kioskdt.setToolTipTempBadgeNumber(			textTempBadgeNumToolTip.getText());
		// kioskdt.setInfo_URLOfEquipmentDB(			textEquipmentDB.getText()		);
		kioskdt.setImageRotation(					chckbxRotateImage.isSelected()	);
		kioskdt.setPrintGastvTempBadg(				chckbxPrintBadgeT.isSelected()	);
	}
	
	private String [] splitString (String str)
	{
		ArrayList<String> lst =new  ArrayList<String>(Arrays.asList( str.split("\\r?\\n") ) );
		
		if ( (!lst.get(0).equals("Select") ) && (!lst.get(0).equals("United States")) )
			lst.add(0, "Select");
		
		String[] retval = (String[]) lst.toArray(new String[0]);
		
		return retval;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////
	class BTNSubmit implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			collectData();
			frame.dispose();
			if (spframe != null) spframe.dispose();
		}
	}
	class BTNCancel implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			frame.dispose();
			if (spframe != null) spframe.dispose();
		 }
	}	
}
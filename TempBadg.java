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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;

public class TempBadg
{
  KioskData kioskdt;
  KioskDB 	kioskDB;

  public JFrame frame;
  private JDialog spframe;
  private JTextField tF_first_name;
  private JTextField tF_last_name;
  private JLabel lblTitle;
  private JLabel lblFirstName;
  private JLabel lblLastName;
  private JLabel lblNotes;
  private JButton btnGetBadge;
  private JButton btnReturn;
  private JTextArea taNotes;
  private Font ff;
  private Font ff_title;
  private Timer t;
//  private RCFile inoutfile;
  
  public void setKioskData(KioskData data)
  {	  	  
	  this.kioskdt = data;
	  ff_title = kioskdt.internalTitle;
	  ff = kioskdt.internaLlabel;
//	  inoutfile = new RCFile(kioskdt);
	  kioskDB = new KioskDB(kioskdt);
  }
  
  public void setFrame(JDialog dialog)
  {
    t.start();
    spframe = dialog;
    spframe.addWindowListener(new WindowAdapter()
    {
    	@Override
    	public void windowClosed(WindowEvent e)
    	{
    	    
    	}
    	
    	@Override 
    	public void windowClosing(WindowEvent e)
    	{
    		clean();
    	}
    });
    
    
  }
  public TempBadg(KioskData datain)
  {
	  
	  if (KioskData.debug) System.out.println("Starting Get Temp Badge!!!");
	  setKioskData(datain);
    int delmin = 120000;
    
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
        clean();
        if (spframe != null) spframe.dispose();
      }
    });
    initialize();
    actions();
    KioskData.makelogs("new TempBadg just bean called ", 0);
  }
  
  private void clean()
  {
    tF_first_name.setText("");
    tF_last_name.setText("");
    taNotes.setText("");
    t.stop();
    kioskdt.setEquipment_tag("");
  	kioskdt.setEquipment_name("");
  	kioskdt.setEquipment_firstname("");
  	kioskdt.setEquipment_lastname("");
  	kioskdt.setEquipment_notes("");
    kioskdt.setCheckinout_firstname("");
    kioskdt.setCheckinout_lastname("");
    kioskdt.setCheckinout_notes("");
    
  }
  
  private boolean isFilled()
  {
    return (!tF_first_name.getText().equals("")) && (!tF_last_name.getText().equals(""));
  }
 
  public boolean isNum(String str)
  {
    try
    {
      Integer.parseInt(str);
    }
    catch (NumberFormatException nfe)
    {
      return false;
    }
    return true;
  }
  
  private void actions()
  {
    btnGetBadge.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        try
        {
			if (checkOutInf()) 
			{
				frame.dispose();
		        if (spframe != null) spframe.dispose();
			}
		}
        catch (SQLException e)
        {
		
        	JOptionPane.showMessageDialog(frame,
					e.getMessage(),
					"Kiosk Printing Error!!!",
					JOptionPane.ERROR_MESSAGE);
        	KioskData.makelogs(e.getMessage(), 0);
			e.printStackTrace();
		}
      }
    });
    
    btnReturn.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        try 
        {
			if (checkInInf())
			{
				frame.dispose();
		        if (spframe != null) spframe.dispose();				
			}
		}
        catch (SQLException e)
        {
        	JOptionPane.showMessageDialog(frame,
					e.getMessage(),
					"Kiosk Printing Error!!!",
					JOptionPane.ERROR_MESSAGE);
        	KioskData.makelogs(e.getMessage(), 0);
			e.printStackTrace();		
		}
      }
      
    });
  }
  
  private void initialize()
  {
    //checkfile();
    
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    int nextLabel = 140;
    int sp = 40;
    
    frame = new JFrame();
    frame.setBounds(dim.width / 2 - 225, dim.height / 2 - 225, 450, 450);
    frame.setDefaultCloseOperation(2);
    frame.getContentPane().setLayout(null);
    frame.setResizable(false);
    lblTitle = new JLabel(kioskdt.getTempBadgTitel());
    
    lblTitle.setHorizontalAlignment(0);
    lblTitle.setBounds(5, 10, 440, 96);
    lblTitle.setFont(this.ff_title);
    frame.getContentPane().add(lblTitle);
    
    lblFirstName = new JLabel("First Name");
    lblLastName = new JLabel("Last Name");
    tF_first_name = new JTextField();
    tF_last_name = new JTextField();
    lblNotes = new JLabel("Notes");
    taNotes = new JTextArea();
    
    lblFirstName.setBounds(50, nextLabel, 100, 26);
    tF_first_name.setBounds(160, nextLabel, 240, 26);
    lblLastName.setBounds(50, nextLabel += sp, 100, 26);
    tF_last_name.setBounds(160, nextLabel, 240, 26);
    lblNotes.setBounds(50, nextLabel += sp, 100, 26);
    taNotes.setBounds(160, nextLabel, 240, 108);
    
    tF_first_name.setFont(ff);
    lblFirstName.setFont(ff);
    
    lblLastName.setFont(ff);
    tF_last_name.setFont(ff);
    
    Border b = tF_first_name.getBorder();
    taNotes.setWrapStyleWord(true);
    taNotes.setLineWrap(true);
    taNotes.setFont(ff);
    taNotes.setBorder(b);
    lblNotes.setFont(ff);
    
    frame.getContentPane().add(lblFirstName);
    frame.getContentPane().add(tF_first_name);
    frame.getContentPane().add(lblLastName);
    frame.getContentPane().add(tF_last_name);
    frame.getContentPane().add(lblNotes);
    frame.getContentPane().add(taNotes);
    
    int ButtonWidth = 120;
    
    int offs = 25;
    int btnX = 225 - ButtonWidth - offs;
    int btnY = nextLabel + 150;
    
    btnGetBadge = new JButton("Get Badge");
    btnGetBadge.setBounds(btnX, btnY, ButtonWidth, 26);
    btnGetBadge.setFont(this.ff);
    frame.getContentPane().add(this.btnGetBadge);
    
    btnX = 225 + offs;
    
    btnReturn = new JButton("Return");
    btnReturn.setBounds(btnX, btnY, ButtonWidth, 26);
    btnReturn.setFont(this.ff);
    frame.getContentPane().add(this.btnReturn);
  }

/*
 *  ////////////////////////////////////////////////////////////////////////////////////////////////// 
 */
  private String[] getTempBadgeAll(String number) throws SQLException
  {
	  return kioskDB.getTempBadge(number);
  }

  /* Error */
  private boolean checkOutInf() throws SQLException
  {
	  javax.swing.UIManager.put("OptionPane.messageFont",
			  new FontUIResource( kioskdt.internalTitle )); 

	  if ( !isFilled() )
	  {
		   JOptionPane.showMessageDialog(frame,
				    kioskdt.getTempBadgMassege(),
				    kioskdt.getTheTitle(),
				    JOptionPane.ERROR_MESSAGE);
	  }
	  else
	  {
		  String s = "";
		  while (s.equals(""))
		  {
			  s = (String)JOptionPane.showInputDialog(
			                      frame,
			                      kioskdt.getTempBadgBarcodePromt()
			                      + "",
			                      "");
			  KioskData.makelogs("The entered val = \'" + s + "\' Befor cheking in DB", 0);
			  
			  if ( (s != null) && !(s.equals("")) )
			  {
				  kioskdt.setInfo_TempBadgeNumber(s);
				  if (collectDataOut(s))
				  {
					  enterDataOut();
					  clean();
					  return true;
				  }
				  else
					  return false;
			  }
			  else
			  {
				  return false;
			  }
		  }
	  }
	  return false;
  }
  
  private void enterTempBadge(String badge)
  {
	  JOptionPane.showMessageDialog(frame,
			    "The Badge number : " + badge 
			    + " was not found in the database"
			    + " please check if you entered it correctly or ask guard to add it to the database.",
			    "Kiosk",
			    JOptionPane.ERROR_MESSAGE);
  }
 
  private void enterDataOut() throws SQLException
  {
	  
	  if (KioskData.debug)
	  {
		  System.out.println("Tag: " + kioskdt.getTempbadge_tag());
		  System.out.println("Name: " + kioskdt.getTempbadge_name());
		  System.out.println("First name: " + kioskdt.getTempbadge_firstname());
		  System.out.println("Last name: " + kioskdt.getTempbadge_lastname());
		  System.out.println("Notes: " + kioskdt.getTempbadge_notes());
		  System.out.println("Entaring First name: " + kioskdt.getChecktempbadge_firstname());
		  System.out.println("Entaring Last name: " + kioskdt.getChecktempbadge_lastname());
		  System.out.println("Entaring Notes: " + kioskdt.getChecktempbadge_notes());
	  }
	  kioskDB.sendTempBadge();
  }
  
  private void enterDataIn() throws SQLException
  {
	  kioskDB.sendTempBadgeIn();
  }
  
  private boolean collectDataOut(String tagnum) throws SQLException
  {
	  if (KioskData.debug) System.out.println("Inside CollectData = " + tagnum);
	  
	  int optionPane=JOptionPane.YES_OPTION;;
	  String[] equipArr = getTempBadgeAll(tagnum);
	  
	  if (KioskData.debug)  System.out.println("The number of return arg = " + equipArr.length);
	  
	  if (KioskData.debug) 
		  for (int i=0; i< equipArr.length; i++)
			  System.out.println("\t\t argument [ "+i+" ] = " + equipArr[i]);
	  
	  if (equipArr.length < 4 )
		  {
		  	System.out.println("Some thing WRONG!!! no sich entery in the DB :"
		  			+ equipArr.length + " : " +  tagnum );
		  	enterTempBadge(tagnum);
		  	return false;
		  }
	  
	  String 	fn = tF_first_name.getText(),
	  			ln = tF_last_name.getText(),
	  			nn = taNotes.getText();
	  
	  if (equipArr.length >= 5 )
	  {
		  kioskdt.setTempbadge_tag(equipArr[0]);
		  kioskdt.setTempbadge_name(equipArr[1]);
		  kioskdt.setTempbadge_firstname(equipArr[2]);
		  kioskdt.setTempbadge_lastname(equipArr[3]);
		  kioskdt.setTempbadge_notes(equipArr[4]);
	  }
	  else 
		  KioskData.makelogs("Insufficient information!!! (Temb Dabge Data error)", 0);
	  
	  kioskdt.setChecktempbadge_firstname(fn);
	  kioskdt.setChecktempbadge_lastname(ln);
	  kioskdt.setChecktempbadge_notes(nn);
	  
	  /*  Checking if first name any thing like the first name in the DB*/ 
	  if (((fn.toLowerCase().contains(equipArr[2].toLowerCase()))||(equipArr[2].toLowerCase().contains(fn.toLowerCase())) ) &&
		 ( (ln.toLowerCase().contains(equipArr[3].toLowerCase()))||(equipArr[3].toLowerCase().contains(ln.toLowerCase())) ) )
	  {
		 KioskData.makelogs("Checking if first name any thing like the first name in the DB!!!", 0);
	  }
	  else
	  {
		  optionPane = JOptionPane.showConfirmDialog(frame,
				    "The name you enter is: " + fn + " " + ln 
				    + "\n and you are checking out " + equipArr[1] + " numbe "+ equipArr[0] + "\n"
				    + " is it correct?",
				    kioskdt.getTheTitle(),
				    JOptionPane.YES_NO_OPTION);
	  }
	  if (optionPane == JOptionPane.YES_OPTION)
		  	return true;
	  else 	return false;
	  
  }
 
  /* Error */
  private boolean checkInInf() throws SQLException
  {
	  
	  String s = (String)JOptionPane.showInputDialog(
              frame,
              kioskdt.getTempBadgBarcodePromt()
              + "",
              "");
	  if ( (s != null) && !(s.equals("")) )
	  {
		  kioskdt.setInfo_TempBadgeNumber(s);
		  if (collectDataOut(s))
		  {
			  enterDataIn();
			  return true;
		  }
		  else 
		  {
			  enterTempBadge(s);
			  return false;
		  }
	  }
	  return false;
  }
///////////////////////////////////////////////////////////////////////////////////////////////////
  class DisposeW implements ActionListener
  {
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		clean();
	}
  }
}

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
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class AddEquipment
{
	KioskDB 	kioskDB;
	KioskData	kioskdt;
	public JFrame frame;
	private JDialog spframe;

	private JTextField tF_equipment_name;
	private JTextField tF_first_name;
	private JTextField tF_last_name;
	private JTextArea ta_notes;
  
	private JLabel lblTitle;
	private JLabel lblEquipmentName;
	private JLabel lblFirstName;
	private JLabel lblLastName;
	private JLabel lblNotes;
	
	private JButton btnCancel;
	private JButton btnOK;

	private Font ff;
	private Font ff_title;

	private Timer t;
  
  public void setKioskData()
  {
	  KioskData k = new KioskData();
	  	  
	  kioskdt = k;
	  ff_title = kioskdt.internalTitle;
	  ff = kioskdt.internaLlabel;
	  kioskDB = new KioskDB(k);
  }
  
  /*
   * Just in case if I will need to change it later to call from ather frame.
   * 
  public void setFrame(JDialog dialog)
  {
    t.start();
    spframe = dialog;
  }
  */
  
  public static void main(String[] args)
  {
	  
	  AddEquipment window = new AddEquipment();
	  // window.frame.setVisible(true);
	  window.start();
  }
  
  public void start()
  {
    EventQueue.invokeLater(new Runnable()
    {
      public void run()
      {
        try
        {
          AddEquipment window = new AddEquipment();
          window.frame.setVisible(true);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    });
  } 
  public AddEquipment()
  {
	  setKioskData();
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
  }
  
  private void clean()
  {
	  tF_equipment_name.setText("");
	  tF_first_name.setText("");
	  tF_last_name.setText("");
	  ta_notes.setText("");
	  t.stop();
  }
  
  private void actions()
  {
    btnOK.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        try
        {
			if (clickADD()) 
			{
//				frame.dispose();
//		        if (spframe != null) spframe.dispose();
			}
		}
        catch (SQLException e)
        {
		
        	JOptionPane.showMessageDialog(frame,
					e.getMessage(),
					"Kiosk Printing Error!!!",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
        
        
      }
    });
    
    btnCancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        try 
        {
			if (clickCancel())
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
			e.printStackTrace();		
		}
      }
      
    });
  }
  
  private void initialize()
  {
	  Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	  
	  int nextLabel = 100;
	  int sp = 40;
    
	    frame = new JFrame();
	    frame.setBounds(dim.width / 2 - 225, dim.height / 2 - 225, 450, 450);
	    frame.setDefaultCloseOperation(2);
	    frame.getContentPane().setLayout(null);
	    frame.setResizable(false);
	    
	    lblTitle = new JLabel("Entering Equipment to the Database");
    
	    lblTitle.setHorizontalAlignment(0);
	    lblTitle.setBounds(5, 10, 440, 96);
	    lblTitle.setFont(this.ff_title);
	    frame.getContentPane().add(lblTitle);
    
	    lblEquipmentName = new JLabel("Equipment Name");
	    lblFirstName = new JLabel("First Name");
	    lblLastName = new JLabel("Last Name");
	    
	    tF_equipment_name = new JTextField();
	    tF_first_name = new JTextField();
	    tF_last_name = new JTextField();
	    lblNotes = new JLabel("Notes");
	    ta_notes = new JTextArea();
    
	    lblEquipmentName.setBounds(50, nextLabel, 120, 26);
	    tF_equipment_name.setBounds(160, nextLabel, 240, 26);
	    
	    lblFirstName.setBounds(50, nextLabel+= sp, 100, 26);
	    tF_first_name.setBounds(160, nextLabel, 240, 26);
	    lblLastName.setBounds(50, nextLabel += sp, 100, 26);
	    tF_last_name.setBounds(160, nextLabel, 240, 26);
	    lblNotes.setBounds(50, nextLabel += sp, 100, 26);
	    ta_notes.setBounds(160, nextLabel, 240, 108);
    
	    
	    tF_equipment_name.setFont(ff);
	    lblEquipmentName.setFont(ff);
	    
	    tF_first_name.setFont(ff);
	    lblFirstName.setFont(ff);
    
	    lblLastName.setFont(ff);
	    tF_last_name.setFont(ff);
    
	    Border b = tF_first_name.getBorder();
	    ta_notes.setWrapStyleWord(true);
	    ta_notes.setLineWrap(true);
	    ta_notes.setFont(ff);
	    ta_notes.setBorder(b);
	    lblNotes.setFont(ff);
    
	    frame.getContentPane().add(lblEquipmentName);
	    frame.getContentPane().add(tF_equipment_name);
	    frame.getContentPane().add(lblFirstName);
	    frame.getContentPane().add(tF_first_name);
	    frame.getContentPane().add(lblLastName);
	    frame.getContentPane().add(tF_last_name);
	    frame.getContentPane().add(lblNotes);
	    frame.getContentPane().add(ta_notes);
    
	    int ButtonWidth = 120;
    
	    int offs = 25;
	    int btnX = 225 - ButtonWidth - offs;
	    int btnY = nextLabel + 150;
    
	    btnOK = new JButton("Add");
	    btnOK.setBounds(btnX, btnY, ButtonWidth, 26);
	    btnOK.setFont(this.ff);
	    frame.getContentPane().add(this.btnOK);
    
	    btnX = 225 + offs;
    
	    btnCancel = new JButton("Cancel");
	    btnCancel.setBounds(btnX, btnY, ButtonWidth, 26);
	    btnCancel.setFont(this.ff);
	    frame.getContentPane().add(this.btnCancel);
  }

///////////////////////////////////////////////////////////////////////////////////////////////////
  
  private boolean isFilled()
  {
    return (!tF_equipment_name.getText().equals("") ) ; 
  }
  /* Error */
  private boolean clickADD() throws SQLException
  {
	  javax.swing.UIManager.put("OptionPane.messageFont",
			  new FontUIResource( kioskdt.internalTitle )); 

	  if ( !isFilled() )
	  {
		   JOptionPane.showMessageDialog(frame,
				    "Please fill in all the fields with appropriate information.",
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
			                      kioskdt.getCheckInOutBarcodePromt()
			                      + "",
			                      "");
			  
			  if ( (s != null) && !(s.equals("")) )
			  {
			  if (addToDB(s,	tF_equipment_name.getText(),
					  			tF_first_name.getText(), 
					  			tF_last_name.getText(),
					  			ta_notes.getText()
					  	 ))
				  {
					  clean();
					  return true;
				  }
				  else
				  {
					  return false;
				  }
				  
			  }
			  else
			  {
				  System.out.println("Cancel was pressed or empty string entered");
				  return false;
			  }
		  }
	  }
	  return false;
	  
  }
  private boolean addToDB(String tag, String equpment_name, String f_name, String l_name, String notes) throws SQLException
  {
	  return kioskDB.addEquipment(tag, equpment_name, f_name, l_name, notes);	  
  }
  private boolean clickCancel() throws SQLException
  {
	  return true;
  }
}

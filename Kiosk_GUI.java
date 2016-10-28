
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.KeyStroke;
import javax.swing.Timer;

///////////////////////////////////////////////////////////////////////////////////////////////////
class MyJPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private Image				img;

	MyJPanel(String strimg)
	{
		img = new ImageIcon(strimg).getImage();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(img, 0, 0, this);
	}
}

///////////////////////////////////////////////////////////////////////////////////////////////////
public class Kiosk_GUI extends JFrame
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private JFrame				that				= this;

	private MyJPanel			contentPane;
	private JTextField			textField_FirstName	= new JTextField();
	private JTextField			textField_LastName	= new JTextField();
	private JTextField			textField_BgNumber	= new JTextField();
	// private JTextField			textField_Company	= new JTextField();
	private HintTextField 		textField_Company	= new HintTextField("please fill in this field");
	private HintTextField 		textField_Reason	= new HintTextField("please fill in this field");
	private JComboBox<String>	combo_Company		= new JComboBox<String>();
	private JComboBox<String>	combo_Sponsor		= new JComboBox<String>();
	private JComboBox<String>	combo_Reason		= new JComboBox<String>();
	private JComboBox<String>	combo_Citizenship	= new JComboBox<String>();
	/* My Labels */
	private JLabel				Title				= new JLabel();
	private JLabel				lblSubTitle			= new JLabel();
	private JLabel				lblFirstName		= new JLabel();
	private JLabel				lblLastName			= new JLabel();
	private JLabel				lblCompany			= new JLabel();
	private JLabel				lblDepartment		= new JLabel();
	private JLabel				lblSponsor			= new JLabel();
	private JLabel				lblReasonForVisit	= new JLabel();
	private JLabel				lblCitizenship		= new JLabel();
	private JLabel				lblYouMustFill		= new JLabel();
	private JLabel				lblHoverOver		= new JLabel();
	private JLabel				lblStatusBar		= new JLabel();
	private JLabel				lblCheckInOut		= new JLabel();
	///////////////////////////////////////////////////////////////////////////////////////////////////
	/* Optional interface */

	private JLabel				lbl_BgNumber;
	///////////////////////////////////////////////////////////////////////////////////////////////////
	/* My Buttons */
	JButton						btnSubmit			= new JButton();
	JButton						btnCheckInOut		= new JButton();

	// the buttone to check out temp badge like equipment
	// not needed for now
	JButton						btnTempBadg			= null;

	JRadioButton				rdbtnHp				= new JRadioButton();
	JRadioButton				rdbtnOther			= new JRadioButton();
	JCheckBox					chckbxTempBadge		= new JCheckBox();
	ButtonGroup					buttonG				= new ButtonGroup();
	///////////////////////////////////////////////////////////////////////////////////////////////////
	/* My Listeners */
	ActOnCheck					othercheck			= new ActOnCheck();
	GetFocuse					reset				= new GetFocuse();
	KeyChekReq					text_checkrequired	= new KeyChekReq();
	ComboChekReq				combo_checkrequired	= new ComboChekReq();
	SubmitButton				sbmbtn				= new SubmitButton();
	SubmitInOut					checkbtn			= new SubmitInOut();
	TempBadge					tempbadgebtn		= new TempBadge();

	ExitButton					kiosk_exit			= new ExitButton();
	SetingsButton				kiosk_Setings		= new SetingsButton();

	KeyStroke					keyExit				= KeyStroke.getKeyStroke(KeyEvent.VK_Q,
			InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);
	KeyStroke					keySetings			= KeyStroke.getKeyStroke(KeyEvent.VK_S,
			InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);

	///////////////////////////////////////////////////////////////////////////////////////////////////
	private Timer				t;

	int							ggtfildX, ggnextLabel, gglabelOfS, ggtfildWidth, gglabelHeight, gglabelX, gglabelWidth,
			ggwwidth;

	KioskData					kioskData;
	KioskDB						db;
	CheckOut					checkIO;
	TempBadg					tempBadgeIO;
	KioskSetings				kioskSetings		= null;

	private Object				ipDBstarus			= new Object();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					new Kiosk_GUI();
				} catch (Exception e)
				{
					if (KioskData.debug)
						e.printStackTrace();
					else
						KioskData.makelogs(e.toString(), 0);
				}
			}
		});
	}

	Runnable updateIPandDBstatus = new Runnable()
	{
		@Override
		public void run()
		{
			String newIP = getIP();
			boolean newStatus = false;
			if ((kioskData != null) && (db != null))
				try
				{
					newStatus = db.isConnect(kioskData.getInfo_URLOfKioskDB());
				} catch (SQLException e)
				{
					KioskData.makelogs("213" + e.getMessage(), 0);
				}

			if (!newStatus)
			{
				JOptionPane.showMessageDialog(that, "Lost Connections with the Database server cher the url address.",
						kioskData.getTheTitle(), JOptionPane.ERROR_MESSAGE);
						KioskData.makelogs("Lost Connections with the Database server cher the url address.", 0);
			}

			synchronized (ipDBstarus)
			{
				if (newStatus)
					lblStatusBar.setText(newIP + "\t  OK ");
				else
					lblStatusBar.setText(newIP + "\t  ERROR!!! ");
			}
		}
	};

	/**
	 * Contractor.
	 */
	public Kiosk_GUI()
	{
		/* Geting saved data !!! */
		try
		{
			KioskData.makelogs("Starting Kiosk", 0);
			kioskData = RCKiosk.getit();

		} catch (SQLException e)
		{
			KioskData.makelogs("Geting DB Exeption !!! " + e.toString(), 0);
			JOptionPane.showMessageDialog(null, "Cannot connect to database!!! " + e.toString());
		}
		/* -------------------------------------------------------------------- */

		/* making optional interfase */
		if (kioskData.isTempbadgeUIbtn())
		{
			btnTempBadg = new JButton();
		} else
		{
			lbl_BgNumber = new JLabel();
		}
		/* --------------------------------------------------------------------- */

		db = new KioskDB(kioskData);

		int delmin = (int) (60000 * 3); // 3 min
		t = new javax.swing.Timer(delmin, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				KioskData.makelogs("Timer Starts! : " + delmin, 0);
				resetALL();
				removpanel();
				reappeare();
				textField_FirstName.requestFocusInWindow();
				t.stop();
				KioskData.makelogs("Timer Stop.", 0);
			}
		});

		ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
		exec.scheduleAtFixedRate(updateIPandDBstatus, 0, 10, TimeUnit.MINUTES);

		try
		{
			/* Icon for the kiosk main */
			URL url = ClassLoader.getSystemResource(KioskData.icon);
			Toolkit kit = Toolkit.getDefaultToolkit();
			Image img = kit.createImage(url);
			this.setIconImage(img);
			/* */

		} catch (Exception e)
		{
			KioskData.makelogs("292 -==- " + e.getMessage(), 0);
		}

		initialize();
		setVisible(true);
		textField_FirstName.requestFocusInWindow();
	}

	private void collectData()
	{
		kioskData.setInfo_FirstName(textField_FirstName.getText());
		kioskData.setInfo_LastName(textField_LastName.getText());
		if (chckbxTempBadge.isSelected())
		{
			if (!kioskData.isTempbadgeUIbtn())
				kioskData.setInfo_BgNumber(lbl_BgNumber.getText());
			else
				kioskData.setInfo_BgNumber(textField_BgNumber.getText());
		} else
			kioskData.setInfo_BgNumber("");
		if (rdbtnHp.isSelected())
			kioskData.setInfo_Company(combo_Company.getSelectedItem().toString());
		if (rdbtnOther.isSelected())
			kioskData.setInfo_Company(textField_Company.getText());
		if (combo_Reason.getSelectedItem().toString().toLowerCase().contains("other"))
			kioskData.setInfo_Reason(textField_Reason.getText());
		else
			kioskData.setInfo_Reason(combo_Reason.getSelectedItem().toString());
		kioskData.setInfo_Sponsor(combo_Sponsor.getSelectedItem().toString());
		kioskData.setInfo_Citizenship(combo_Citizenship.getSelectedItem().toString());

		KioskData.makelogs("Collected Data : " + kioskData.getCollectedData(), 0);
	}

	private void resetALL()
	{
		textField_FirstName.setText("");
		textField_LastName.setText("");
		textField_Company.setText("");
		textField_Reason.setText("");
		combo_Company.setModel(new DefaultComboBoxModel<String>(kioskData.getArrCompany()));
		combo_Sponsor.setModel(new DefaultComboBoxModel<String>(kioskData.getArrSponsor()));
		combo_Reason.setModel(new DefaultComboBoxModel<String>(kioskData.getArrReason()));
		combo_Citizenship.setModel(new DefaultComboBoxModel<String>(kioskData.getArrCitizenship()));
		combo_Company.setSelectedIndex(0);
		combo_Sponsor.setSelectedIndex(0);
		combo_Reason.setSelectedIndex(0);
		combo_Citizenship.setSelectedIndex(0);

		/* optional panel */
		textField_BgNumber.setText("");
		lbl_BgNumber.setText("");

		Title.setText(kioskData.getStrTitle());
		lblSubTitle.setText(kioskData.getStrSubTitle());
		lblFirstName.setText(kioskData.getStrFirstName());
		lblLastName.setText(kioskData.getStrLastName());
		lblCompany.setText(kioskData.getStrCompany());
		lblSponsor.setText(kioskData.getStrSponsor());
		lblReasonForVisit.setText(kioskData.getStrReasonForVisit());
		lblCitizenship.setText(kioskData.getStrCitizenship());
		lblYouMustFill.setText(kioskData.getStrYouMustFill());
		lblHoverOver.setText(kioskData.getStrHoverOver());
		btnSubmit.setText(kioskData.getStrbtnSubmit());
		rdbtnHp.setText(kioskData.getStrrdbtnHp());
		rdbtnHp.setToolTipText(kioskData.getToolTipHP());
		rdbtnOther.setText(kioskData.getStrrdbtnOther());
		rdbtnOther.setToolTipText(kioskData.getToolTipOther());
		chckbxTempBadge.setText(kioskData.getStrchckbxTempBadge());
		chckbxTempBadge.setToolTipText(kioskData.getToolTipTempBadge());
		buttonG.clearSelection();
		if (rdbtnHp.isSelected())
			rdbtnHp.setSelected(false);
		if (rdbtnOther.isSelected())
			rdbtnOther.setSelected(false);
		if (chckbxTempBadge.isSelected())
			chckbxTempBadge.setSelected(false);
		btnSubmit.setEnabled(false);
		KioskData.makelogs("Reset all UI", 0);
	}

	private void decore()
	{
		Title.setForeground(new Color(0, 150, 214));
		Title.setHorizontalAlignment(SwingConstants.CENTER);
		Title.setFont(kioskData.TitleFont);
		Title.setText(kioskData.getStrTitle());
		lblSubTitle.setFont(kioskData.subTitleFont);
		lblSubTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblSubTitle.setText(kioskData.getStrSubTitle());
		lblFirstName.setFont(kioskData.lableFont);
		lblFirstName.setHorizontalAlignment(SwingConstants.LEFT);
		lblFirstName.setText(kioskData.getStrFirstName());
		lblLastName.setFont(kioskData.lableFont);
		lblLastName.setHorizontalAlignment(SwingConstants.LEFT);
		lblLastName.setText(kioskData.getStrLastName());
		lblCompany.setFont(kioskData.lableFont);
		lblCompany.setHorizontalAlignment(SwingConstants.LEFT);
		lblCompany.setText(kioskData.getStrCompany());
		lblDepartment.setFont(kioskData.lableFont);
		lblDepartment.setHorizontalAlignment(SwingConstants.LEFT);
		lblDepartment.setText(kioskData.getStrDepartment());
		lblSponsor.setFont(kioskData.lableFont);
		lblSponsor.setHorizontalAlignment(SwingConstants.LEFT);
		lblSponsor.setText(kioskData.getStrSponsor());
		lblReasonForVisit.setFont(kioskData.lableFont);
		lblReasonForVisit.setHorizontalAlignment(SwingConstants.LEFT);
		lblReasonForVisit.setText(kioskData.getStrReasonForVisit());
		lblCitizenship.setFont(kioskData.lableFont);
		lblCitizenship.setHorizontalAlignment(SwingConstants.LEFT);
		lblCitizenship.setText(kioskData.getStrCitizenship());
		textField_FirstName.setFont(kioskData.lableFont);
		textField_FirstName.setMaximumSize(getMaximumSize());
		textField_FirstName.addFocusListener(reset);
		textField_FirstName.addKeyListener(text_checkrequired);
		textField_LastName.setFont(kioskData.lableFont);
		textField_LastName.addKeyListener(text_checkrequired);
		textField_LastName.getInputMap().put(keyExit, "exit");
		textField_LastName.getActionMap().put("exit", kiosk_exit);
		textField_LastName.getInputMap().put(keySetings, "Setings");
		textField_LastName.getActionMap().put("Setings", kiosk_Setings);
		textField_Company.setFont(kioskData.lableFont);
		textField_Company.setFont(kioskData.lableFont);
		textField_Company.addKeyListener(text_checkrequired);
		combo_Company.setFont(kioskData.lableFont);
		combo_Company.setBackground(Color.WHITE);
		combo_Company.addItemListener(combo_checkrequired);
		combo_Sponsor.setFont(kioskData.lableFont);
		combo_Sponsor.setBackground(Color.WHITE);
		combo_Sponsor.addItemListener(combo_checkrequired);
		combo_Reason.setFont(kioskData.lableFont);
		combo_Reason.setBackground(Color.WHITE);
		combo_Reason.addItemListener(combo_checkrequired);
		combo_Reason.addActionListener(othercheck);
		combo_Citizenship.setFont(kioskData.lableFont);
		combo_Citizenship.setBackground(Color.WHITE);
		combo_Company.setModel(new DefaultComboBoxModel<String>(kioskData.getArrCompany()));
		combo_Sponsor.setModel(new DefaultComboBoxModel<String>(kioskData.getArrSponsor()));
		combo_Reason.setModel(new DefaultComboBoxModel<String>(kioskData.getArrReason()));
		combo_Citizenship.setModel(new DefaultComboBoxModel<String>(kioskData.getArrCitizenship()));
		lblYouMustFill.setFont(kioskData.lableFont);
		lblYouMustFill.setHorizontalAlignment(SwingConstants.CENTER);
		lblYouMustFill.setText(kioskData.getStrYouMustFill());
		lblYouMustFill.setForeground(Color.RED);
		lblHoverOver.setFont(kioskData.sublableFont);
		lblHoverOver.setHorizontalAlignment(SwingConstants.CENTER);
		lblHoverOver.setText(kioskData.getStrHoverOver());
		rdbtnHp.setFont(kioskData.lableFont);
		rdbtnHp.setOpaque(false);
		rdbtnHp.addActionListener(othercheck);
		rdbtnHp.setText(kioskData.getStrrdbtnHp());
		rdbtnHp.setToolTipText(kioskData.getToolTipHP());
		rdbtnOther.setFont(kioskData.lableFont);
		rdbtnOther.setOpaque(false);
		rdbtnOther.addActionListener(othercheck);
		rdbtnOther.setText(kioskData.getStrrdbtnOther());
		rdbtnOther.setToolTipText(kioskData.getToolTipOther());
		buttonG.add(rdbtnHp);
		buttonG.add(rdbtnOther);
		btnSubmit.setFont(kioskData.lableFont);
		btnSubmit.setText(kioskData.getStrbtnSubmit());
		chckbxTempBadge.setFont(kioskData.lableFont);
		chckbxTempBadge.setOpaque(false);
		chckbxTempBadge.addActionListener(othercheck);
		chckbxTempBadge.setText(kioskData.getStrchckbxTempBadge());
		chckbxTempBadge.setToolTipText(kioskData.getToolTipTempBadge());

		/* optional interface */
		if (kioskData.isTempbadgeUIbtn())
		{
			textField_BgNumber.setFont(kioskData.lableFont);
			textField_BgNumber.setColumns(10);
			textField_BgNumber.addKeyListener(text_checkrequired);
			textField_BgNumber.setToolTipText(kioskData.getToolTipTempBadgeNumber());
		} else
		{
			lbl_BgNumber.setFont(kioskData.lableFont);
		}

		textField_Reason.setFont(kioskData.lableFont);
		textField_Reason.addKeyListener(text_checkrequired);
		btnSubmit.addActionListener(sbmbtn);
		btnSubmit.setEnabled(false);
		lblStatusBar.setFont(kioskData.statusbar);
		lblStatusBar.setHorizontalAlignment(SwingConstants.LEFT);

		lblCheckInOut.setText(kioskData.getStrCheckInOutLable());
		lblCheckInOut.setFont(kioskData.lableFont);
		lblCheckInOut.setHorizontalAlignment(SwingConstants.CENTER);
		lblCheckInOut.setForeground(Color.RED); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

		btnCheckInOut.setText(kioskData.getStrCheckInOutButton());
		btnCheckInOut.setFont(kioskData.lableFont);
		btnCheckInOut.addActionListener(checkbtn);

		if (btnTempBadg != null)
		{
			btnTempBadg.setText(kioskData.getStrTempBadgeButton());
			btnTempBadg.setFont(kioskData.lableFont);
			btnTempBadg.addActionListener(tempbadgebtn);
		}
	}

	private void addtopanel()
	{
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setTitle("Kiosk");
		if (!KioskData.debug)
		{
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			setUndecorated(true); 
		} else
		{
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setUndecorated(false);
			setAlwaysOnTop(false);
		}
		contentPane = new MyJPanel(kioskData.getProgFolder() + kioskData.background);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(Title);
		contentPane.add(lblSubTitle);
		contentPane.add(lblFirstName);
		contentPane.add(lblLastName);
		contentPane.add(lblCompany);
		contentPane.add(lblSponsor);
		contentPane.add(lblReasonForVisit);
		contentPane.add(lblCitizenship);
		contentPane.add(textField_FirstName);
		contentPane.add(textField_LastName);
		contentPane.add(combo_Company);
		contentPane.add(combo_Sponsor);
		contentPane.add(combo_Reason);
		contentPane.add(combo_Citizenship);
		contentPane.add(btnSubmit);
		contentPane.add(lblYouMustFill);
		contentPane.add(lblHoverOver);
		contentPane.add(rdbtnHp);
		contentPane.add(rdbtnOther);
		contentPane.add(chckbxTempBadge);

		/* Optional interface */
		if (kioskData.isTempbadgeUIbtn())
			contentPane.add(textField_BgNumber);
		else
			contentPane.add(lbl_BgNumber);

		contentPane.add(textField_Company);
		contentPane.add(textField_Reason);
		contentPane.add(lblStatusBar);
		contentPane.add(lblCheckInOut);
		contentPane.add(btnCheckInOut);
		if (btnTempBadg != null)
			contentPane.add(btnTempBadg);
		contentPane.repaint();
		revalidate();
		KioskData.makelogs("Starting Kiosk, making the UI", 0);
	}

	private void removpanel()
	{
		if (lblSponsor != null)
			contentPane.remove(lblSponsor);
		if (lblReasonForVisit != null)
			contentPane.remove(lblReasonForVisit);
		if (lblCitizenship != null)
			contentPane.remove(lblCitizenship);
		if (combo_Company != null)
			contentPane.remove(combo_Company);
		if (lblDepartment != null)
			contentPane.remove(lblDepartment);
		if (combo_Sponsor != null)
			contentPane.remove(combo_Sponsor);
		if (combo_Reason != null)
			contentPane.remove(combo_Reason);
		if (combo_Citizenship != null)
			contentPane.remove(combo_Citizenship);
		if (btnSubmit != null)
			contentPane.remove(btnSubmit);
		if (lblYouMustFill != null)
			contentPane.remove(lblYouMustFill);
		if (lblHoverOver != null)
			contentPane.remove(lblHoverOver);
		if (textField_BgNumber != null)
			contentPane.remove(textField_BgNumber);
		if (lbl_BgNumber != null)
			contentPane.remove(lbl_BgNumber);
		if (textField_Company != null)
			contentPane.remove(textField_Company);
		if (textField_Reason != null)
			contentPane.remove(textField_Reason);
		contentPane.repaint();
		revalidate();
		KioskData.makelogs("removpanel() was called ", 0);
	}

	private void reappeare()
	{
		int tfildX = ggtfildX;
		int nextLabel = ggnextLabel;
		int labelOfS = gglabelOfS;
		int tfildWidth = ggtfildWidth;
		int labelHeight = gglabelHeight;
		int labelX = gglabelX;
		int wwidth = ggwwidth;
		int labelWidth = gglabelWidth;
		if (chckbxTempBadge.isSelected())
		{
			if (kioskData.isTempbadgeUIbtn())
			{
				if ((!rdbtnHp.isSelected()) && (!rdbtnOther.isSelected()))
					rdbtnHp.setSelected(true);
				textField_BgNumber.setBounds(tfildX + 380, nextLabel, 150, labelHeight);
			} else
			{
				lbl_BgNumber.setBounds(tfildX + 410, nextLabel, 100, labelHeight);

			}
		}
		if (rdbtnHp.isSelected())
		{
			combo_Company.setBounds(tfildX + 50, nextLabel += labelOfS, tfildWidth, labelHeight);
			lblDepartment.setBounds((tfildX + 50) - (labelWidth), nextLabel, tfildWidth, labelHeight);
		}

		if (rdbtnOther.isSelected())
		{
			textField_Company.setBounds(tfildX + 50, nextLabel += labelOfS, tfildWidth, labelHeight);
		}

		lblSponsor.setBounds(labelX, nextLabel += labelOfS, labelWidth, labelHeight);
		combo_Sponsor.setBounds(tfildX, nextLabel, tfildWidth, labelHeight);
		lblReasonForVisit.setBounds(labelX, nextLabel += labelOfS, labelWidth, labelHeight);
		combo_Reason.setBounds(tfildX, nextLabel, tfildWidth, labelHeight);
		if (combo_Reason.getSelectedItem().toString().toLowerCase().contains("other"))
			textField_Reason.setBounds(tfildX + 50, nextLabel += labelOfS, tfildWidth, labelHeight);
		lblCitizenship.setBounds(labelX, nextLabel += labelOfS, labelWidth, labelHeight);
		combo_Citizenship.setBounds(tfildX, nextLabel, tfildWidth, labelHeight);
		btnSubmit.setBounds((wwidth - labelWidth) / 2, nextLabel += labelOfS, labelWidth, labelHeight);
		lblYouMustFill.setBounds(0, nextLabel += labelOfS, wwidth, labelHeight);
		// lblHoverOver.setBounds(0, nextLabel+=labelOfS, wwidth, labelHeight);
		if (contentPane != null)
		{
			contentPane.add(lblSponsor);
			contentPane.add(lblReasonForVisit);
			contentPane.add(lblCitizenship);
			if (rdbtnHp.isSelected())
			{
				contentPane.add(combo_Company);
				contentPane.add(lblDepartment);
			}
			if (rdbtnOther.isSelected())
				contentPane.add(textField_Company);
			contentPane.add(combo_Sponsor);
			contentPane.add(combo_Reason);
			contentPane.add(combo_Citizenship);
			contentPane.add(btnSubmit);
			contentPane.add(lblYouMustFill);
			contentPane.add(lblHoverOver);

			if (chckbxTempBadge.isSelected())
			{
				if (kioskData.isTempbadgeUIbtn())
					contentPane.add(textField_BgNumber);
				else
					contentPane.add(lbl_BgNumber);
			}

			if (combo_Reason.getSelectedItem().toString().toLowerCase().contains("other"))
				contentPane.add(textField_Reason);

			contentPane.revalidate();
			this.repaint();
		}
		KioskData.makelogs("reapiar() was called ", 0);
	}

	public void setlaiout()
	{
		int offsetlabel = 10;
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		/*---------------------------------------------------------*/
		// int wheight = size.height;
		int wwidth = size.width;

		FontMetrics metrics = getFontMetrics(kioskData.TitleFont);
		int titleHeight = metrics.getHeight();
		metrics = getFontMetrics(kioskData.lableFont);
		int labelHeight = metrics.getHeight() + 5;
		int labelWidth = metrics.stringWidth("Reason for Visit:") + 5;
		int tfildWidth = (int) (labelWidth * 2.5);
		int labelYStart = 50;
		int labelOfS = 50;
		int nextLabel = 0;

		nextLabel = labelYStart + titleHeight + offsetlabel + labelHeight + (offsetlabel) + 100;

		int labelX = (wwidth / 2) - ((labelWidth + tfildWidth) / 2) + 10;
		int tfildX = labelX + 10 + labelWidth;// +tfildWidth;

		lblFirstName.setBounds(labelX, nextLabel += labelOfS, labelWidth, labelHeight);
		textField_FirstName.setBounds(tfildX, nextLabel, tfildWidth, labelHeight);
		lblLastName.setBounds(labelX, nextLabel += labelOfS, labelWidth, labelHeight);
		textField_LastName.setBounds(tfildX, nextLabel, tfildWidth, labelHeight);
		lblHoverOver.setBounds(0, nextLabel += (labelOfS / 2), wwidth, labelHeight);
		lblCompany.setBounds(labelX, nextLabel += (labelOfS / 2), labelWidth, labelHeight);
		rdbtnHp.setBounds(tfildX, nextLabel, 100, labelHeight);
		rdbtnOther.setBounds(tfildX + 100, nextLabel, 100, labelHeight);
		chckbxTempBadge.setBounds(tfildX + 200, nextLabel, 250, labelHeight);

		ggtfildX = tfildX;
		ggnextLabel = nextLabel;
		gglabelOfS = labelOfS;
		ggtfildWidth = tfildWidth;
		gglabelHeight = labelHeight;
		gglabelX = labelX;
		gglabelWidth = labelWidth;
		ggwwidth = wwidth;

		reappeare();
		KioskData.makelogs("setlaiout() was called ", 0);
	}

	private void initialize()
	{
		decore();
		int offsetlabel = 10;
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		/*---------------------------------------------------------*/
		int wheight = size.height;
		int wwidth = size.width;

		FontMetrics metrics = getFontMetrics(kioskData.TitleFont);

		int titleHeight = metrics.getHeight();

		Title.setBounds(0, 50, wwidth, titleHeight + offsetlabel);
		lblSubTitle.setBounds(0, titleHeight + 100, wwidth, (titleHeight / 2) + offsetlabel * 3);
		lblStatusBar.setBounds(25, wheight - (offsetlabel * 5), wwidth, (titleHeight / 2) + offsetlabel * 3);
		lblCheckInOut.setBounds(0, wheight - (offsetlabel * 10), wwidth, (titleHeight / 2) + offsetlabel * 3);

		metrics = getFontMetrics(kioskData.lableFont);

		int btnplass = 150;
		int btnWidthE = metrics.stringWidth(kioskData.getStrCheckInOutButton()) + btnplass;
		int btnWidthT = 0;
		if (btnTempBadg != null)
			btnWidthT = metrics.stringWidth(kioskData.getStrTempBadgeButton()) + btnplass;

		int btnWidth = btnWidthE + btnWidthT;

		if (btnTempBadg != null)
			btnTempBadg.setBounds((int) ((wwidth - btnWidth) / 2), wheight - (offsetlabel * 11), btnWidthT,
					offsetlabel * 3);

		if (btnTempBadg != null)
			btnCheckInOut.setBounds((int) (((wwidth - btnWidth) / 2) + btnWidthE), wheight - (offsetlabel * 11),
					btnWidthE, offsetlabel * 3);
		else
			btnCheckInOut.setBounds((int) (((wwidth - btnWidth) / 2)), wheight - (offsetlabel * 11), btnWidthE,
					offsetlabel * 3);

		setlaiout();
		addtopanel();
		KioskData.makelogs("initialize()", 0);
	}

	private String getIP()
	{
		String ipret = "";
		try
		{
			InetAddress address = InetAddress.getLocalHost();
			ipret = address.getHostAddress();
			// to avoide trashing logs
			// KioskData.makelogs("IP Address = " + ipret, 0);
		} catch (UnknownHostException e)
		{
			if (KioskData.debug)
				e.printStackTrace();
			else
				KioskData.makelogs(e.toString(), 0);
			ipret = "ERROR retriving ip: " + e.toString();
		}
		return ipret;
	}

	private boolean checkRequired()
	{
		if (textField_FirstName.getText().trim().equals(""))
			return false;
		if (textField_LastName.getText().trim().equals(""))
			return false;
		if (rdbtnHp.isSelected())
			if (combo_Company.getSelectedIndex() == 0)
				return false;
		if (combo_Sponsor.getSelectedIndex() == 0)
			return false;
		if (combo_Reason.getSelectedIndex() == 0)
			return false;
		if (combo_Reason.getSelectedItem().toString().toLowerCase().contains("other"))
			if (textField_Reason.getText().trim().equals(""))
				return false;
		if (!rdbtnHp.isSelected() && !rdbtnOther.isSelected())
			return false;

		if (chckbxTempBadge.isSelected())
		{
			if (kioskData.isTempbadgeUIbtn())
			{
				if (textField_BgNumber.getText().trim().equals(""))
					return false;
				if (!checkTempNum())
				{
					if (KioskData.debug)
						System.out.println("Not A Number!!!");
					return false;
				}
			} else
			{
				if (lbl_BgNumber.getText().trim().equals(""))
					return false;
			}
		}

		if (rdbtnOther.isSelected())
			if (textField_Company.getText().trim().equals(""))
				return false;

		KioskData.makelogs("checkRequered() was called ", 0);
		return true;
	}

	private boolean checkRidiculous()
	{
		// Cheking for ridiculous input from user !!!
		boolean retval = false;

		if (combo_Reason.getSelectedItem().toString().toLowerCase().contains("other"))
		{
			if (textField_Reason.getText().trim().toLowerCase().equals("other")
					|| textField_Reason.getText().trim().toLowerCase().equals("temp")
					|| textField_Reason.getText().trim().toLowerCase().equals("ags")
					|| textField_Reason.getText().trim().toLowerCase().matches("-?\\d+(.\\d+)?"))
			{
				JOptionPane.showMessageDialog(that,
						"The Reason for visit can not be \"" + textField_Reason.getText().trim() + "\"!\n"
								+ "please specify your reason for visit",
						"Kiosk Printing Error!!!", JOptionPane.ERROR_MESSAGE);
				return true;

			}
			/*
			 * else if (
			 * textField_Reason.getText().trim().toLowerCase().equals("work") ||
			 * textField_Reason.getText().trim().toLowerCase().equals("w0rk") ||
			 * textField_Reason.getText().trim().toLowerCase().equals(
			 * "employee") ||
			 * textField_Reason.getText().trim().toLowerCase().equals(
			 * "work warehouse") ||
			 * textField_Reason.getText().trim().toLowerCase().equals(
			 * "warehouse") ||
			 * textField_Reason.getText().trim().toLowerCase().equals(
			 * "work/production") ||
			 * textField_Reason.getText().trim().toLowerCase().equals(
			 * "warehouse worker") ||
			 * textField_Reason.getText().trim().toLowerCase().equals(
			 * "production") ||
			 * textField_Reason.getText().trim().toLowerCase().equals("n/a") ||
			 * textField_Reason.getText().trim().toLowerCase().equals(
			 * "command center") ||
			 * textField_Reason.getText().trim().toLowerCase().equals(
			 * "randstand employee")||
			 * textField_Reason.getText().trim().toLowerCase().equals(
			 * "work here") ||
			 * textField_Reason.getText().trim().toLowerCase().equals(
			 * "conveyor work") )
			 * 
			 * { JOptionPane.showMessageDialog(that,
			 * "All employee should have badge \n" +
			 * "if you forgot or missing badge choose \"Badge Missing\" option",
			 * "Kiosk Printing Error!!!", JOptionPane.ERROR_MESSAGE); return
			 * true;
			 * 
			 * }
			 * 
			 */
			else if (textField_Reason.getText().trim().toLowerCase().equals("coffee")

			)
			{
				JOptionPane.showMessageDialog(that,
						"You do not need the badge for " + textField_Reason.getText().trim() + "\n",
						"Kiosk Printing Error!!!", JOptionPane.ERROR_MESSAGE);
				return true;
			} else if (textField_Reason.getText().trim().length() < 3)
			{
				JOptionPane.showMessageDialog(that,
						"Would you please expand upon : \"" + textField_Reason.getText().trim() + "\"\n",
						"Kiosk Printing Error!!!", JOptionPane.ERROR_MESSAGE);
				return true;
			}
		}
		KioskData.makelogs("checkRidiculous() was called ", 0);
		return retval;
	}

	public boolean checkTempNum()
	{
		try
		{
			Integer.parseInt(textField_BgNumber.getText());
			KioskData.makelogs("checkTempNum() was called ", 0);
			return true;
		} catch (Exception e)
		{
			KioskData.makelogs(e.toString(), 0);
			return false;
		}
	}

	private boolean needMassegeTempBadge()
	{
		boolean reason = false;
		boolean comp = false;
		EmployeeHolder emp = new EmployeeHolder(kioskData.getInfo_FirstName(), kioskData.getInfo_LastName());

		System.out.println("F Namme: " + kioskData.getInfo_FirstName() + " L Namme: " + kioskData.getInfo_LastName());

		if (kioskData.isEmployee(emp))
			comp = true;

		reason = kioskData.getInfo_Reason().toLowerCase().contains("missing")
				|| kioskData.getInfo_Reason().toLowerCase().contains("badge");

		KioskData.makelogs("needMassegeTempBadge() was called ", 0);
		return reason || comp;
	}

	private void scaningTempBadge()
	{
		String popupmassege = kioskData.getTempBadgBarcodePromt();
		boolean msbadgneedtemp = needMassegeTempBadge();
		if (msbadgneedtemp)
			popupmassege = kioskData.getTempBadgeMissingmass() + kioskData.getTempBadgBarcodePromt();

		String s = "";
		while (msbadgneedtemp || (s != null && (s.equals(""))))
		{
			s = (String) JOptionPane.showInputDialog(that, popupmassege);

			if (KioskData.debug)
				System.out.println("The entered val = \'" + s + "\' Befor cheking in DB");

			if ((s != null) && !(s.equals("")))
			{
				kioskData.setInfo_TempBadgeNumber(s);
				kioskData.setInfo_BgNumber(s);
				lbl_BgNumber.setText(s);
				msbadgneedtemp = false;
			}
		}
		if (s == null)
		{
			chckbxTempBadge.setSelected(false);
			textField_FirstName.requestFocus();
		}

		if (checkRequired())
		{
			btnSubmit.setEnabled(true);
			/*
			 * int n = JOptionPane.showConfirmDialog(null, setMaseg(),
			 * "Kiosk",JOptionPane.YES_NO_OPTION);
			 * 
			 * if (n == JOptionPane.YES_OPTION) {
			 * 
			 * (new Thread(new SendDataToDB(kioskData))).start();
			 * 
			 * resetALL(); removpanel(); reappeare();
			 * textField_FirstName.requestFocusInWindow(); if( t.isRunning() )
			 * t.stop(); }
			 */
		} else
			btnSubmit.setEnabled(false);
		KioskData.makelogs("scaningTempBadge() was called ", 0);
	}

	private JComponent[] setMaseg()
	{
		JLabel pup_Titel = new JLabel(), pup_Name = new JLabel(), pup_Company = new JLabel(),
				pup_Sponsor = new JLabel(), pup_Reason = new JLabel(), pup_Citizenship = new JLabel(),
				pup_Badge = new JLabel(), pup_foot = new JLabel(), pup_ = new JLabel();

		pup_Titel.setFont(kioskData.subTitleFont);
		pup_Titel.setForeground(Color.RED);
		pup_Titel.setHorizontalAlignment(JTextField.CENTER);
		pup_Name.setFont(kioskData.lableFont);
		pup_Company.setFont(kioskData.lableFont);
		pup_Sponsor.setFont(kioskData.lableFont);
		pup_Reason.setFont(kioskData.lableFont);
		pup_Citizenship.setFont(kioskData.lableFont);
		pup_Badge.setFont(kioskData.lableFont);
		pup_foot.setFont(kioskData.lableFont);
		pup_foot.setHorizontalAlignment(JTextField.CENTER);
		pup_.setFont(kioskData.lableFont);
		pup_.setHorizontalAlignment(JTextField.CENTER);
		pup_.setText("Check Out Here Upon Exit!");
		pup_Titel.setText(kioskData.getStrConfirmTitel());
		pup_Name.setText("Name: " + kioskData.getInfo_FirstName() + " " + kioskData.getInfo_LastName());
		pup_Company.setText("Company: " + kioskData.getInfo_Company());
		pup_Sponsor.setText("Sponsor: " + kioskData.getInfo_Sponsor());
		pup_Reason.setText("Reason For Visit: " + kioskData.getInfo_Reason());
		pup_Citizenship.setText("Citizenship: " + kioskData.getInfo_Citizenship());
		if (chckbxTempBadge.isSelected())
			pup_Badge.setText("Temporary Access Badge Number: " + kioskData.getInfo_BgNumber());
		else
			pup_Badge.setText("");
		pup_foot.setText(kioskData.getStrConfirmFoot());
		JComponent[] inputs = { pup_Titel, pup_, pup_Name, pup_Company, pup_Sponsor, pup_Reason, pup_Citizenship,
				pup_Badge, pup_foot };

		KioskData.makelogs("setMaseg() was called ", 0);
		return inputs;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////
	/* Action when one click the Temp Badge */
	class ActOnCheck implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// JOptionPane.showMessageDialog(null, "Works");
			if (checkRequired())
				btnSubmit.setEnabled(true);
			else
				btnSubmit.setEnabled(false);

			if (e.getSource().equals(chckbxTempBadge))
			{
				if (kioskData.isTempbadgeUIbtn())
					textField_BgNumber.requestFocus();
				else
				{
					if (chckbxTempBadge.isSelected())
					{
						scaningTempBadge();

						if (!rdbtnHp.isSelected() && !rdbtnOther.isSelected())
							// rdbtnOther.setSelected(true);
							rdbtnHp.setSelected(true);
					} else
					{
						lbl_BgNumber.setText("");
					}
				}
				KioskData.makelogs("check box Temp Badge just was clicked ", 0);
			}
			removpanel();
			reappeare();

			if (e.getSource().equals(chckbxTempBadge))
			{
				if (rdbtnHp.isSelected())
					combo_Company.requestFocus();
				// if (rdbtnOther.isSelected()) textField_Company.requestFocus();
			}

			if (e.getSource().equals(rdbtnHp))
				combo_Company.requestFocus();
			// if (e.getSource().equals(rdbtnOther)) textField_Company.requestFocus();
		}
	}

	/*
	 * Determin when user start entering data and if user will not finished in
	 * two minats reset all the filds
	 */
	class GetFocuse implements FocusListener
	{
		@Override
		public void focusGained(FocusEvent e)
		{
		}

		@Override
		public void focusLost(FocusEvent e)
		{
			KioskData.makelogs("First Name Focuse Lost!", 0);
			t.start();
		}
	}

	class KeyChekReq implements KeyListener
	{
		@Override
		public void keyPressed(KeyEvent arg0)
		{
			String str;
			if ((arg0.getKeyCode() == 10) && (arg0.getComponent() instanceof JTextField))
			{
				str = ((JTextField) arg0.getSource()).getText();
				if (str.matches("-?\\d+(.\\d+)?"))
				{
					((JTextField) arg0.getSource()).setText("");
					(new Thread(new UpdateDataToDB(str))).start();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0)
		{
			if (checkRequired())
				btnSubmit.setEnabled(true);
			else
				btnSubmit.setEnabled(false);
		}

		@Override
		public void keyTyped(KeyEvent arg0)
		{
		}
	}

	class ComboChekReq implements ItemListener
	{

		@Override
		public void itemStateChanged(ItemEvent arg0)
		{
			if (combo_Reason.getSelectedItem().toString().toLowerCase().contains("other"))
			{
				removpanel();
				reappeare();
				// textField_Reason.requestFocus();
			}
			if (checkRequired())
				btnSubmit.setEnabled(true);
			else
				btnSubmit.setEnabled(false);
		}
	}

	class SubmitButton implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			collectData();
			if (checkRidiculous())
			{
				btnSubmit.setEnabled(false);
				// combo_Reason.requestFocus();
				return;
			}

			if (!chckbxTempBadge.isSelected())
			{
				int bg = 0;
				if (needMassegeTempBadge())
					bg = JOptionPane.YES_OPTION;
				else
					bg = JOptionPane.showConfirmDialog(null, "Do you need a temporary Access badge?",
							kioskData.getTheTitle(), JOptionPane.YES_NO_OPTION);
				if (bg == JOptionPane.YES_OPTION)
				{
					chckbxTempBadge.setSelected(true);
					removpanel();
					reappeare();

					/*
					 * if (kioskData.isTempbadgeUIbtn())
					 * textField_BgNumber.requestFocus();
					 */

					if (kioskData.isTempbadgeUIbtn())
					{
						if (textField_BgNumber.getText().equals(""))
							btnSubmit.setEnabled(false);
						else
							btnSubmit.setEnabled(true);
					} else
					{
						scaningTempBadge();
						if (lbl_BgNumber.getText().equals(""))
							btnSubmit.setEnabled(false);
						else
							btnSubmit.setEnabled(true);
					}

					/*
					 * if (kioskData.isTempbadgeUIbtn())
					 * ToolTipManager.sharedInstance().mouseMoved(new
					 * MouseEvent(textField_BgNumber, 0, 0, 0, 50, -60, 0, false
					 * ) );
					 */
					// return;
				}
			}
			int n = JOptionPane.showConfirmDialog(null, setMaseg(), "Kiosk", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION)
			{
				KioskData temp = kioskData;
				(new Thread(new SendDataToDB(temp))).start();

				resetALL();
				removpanel();
				reappeare();
				textField_FirstName.requestFocusInWindow();
				if (t.isRunning())
					t.stop();
			}
		}
	}

	class SubmitInOut implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			checkIO = new CheckOut(kioskData);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			int width = dim.width;
			int height = dim.height;
			int frameHeight = 450;
			int frameWidth = 450;

			JDialog dialog = new JDialog();
			dialog.setModal(true);
			dialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
			dialog.add(checkIO.frame.getRootPane());
			checkIO.setFrame(dialog);
			dialog.setSize(frameWidth, frameHeight);
			dialog.setLocation(width / 2 - 225, height / 2 - 225);
			dialog.setResizable(false);
			dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			textField_FirstName.requestFocus();
		}
	}

	class TempBadge implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			tempBadgeIO = new TempBadg(kioskData);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			int width = dim.width;
			int height = dim.height;
			int frameHeight = 450;
			int frameWidth = 450;

			JDialog dialog = new JDialog();
			dialog.setModal(true);
			dialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
			dialog.add(tempBadgeIO.frame.getRootPane());
			tempBadgeIO.setFrame(dialog);
			dialog.setSize(frameWidth, frameHeight);
			dialog.setLocation(width / 2 - 225, height / 2 - 225);
			dialog.setResizable(false);
			dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			textField_FirstName.requestFocus();
			KioskData.makelogs("Action performed on tempBadge ", 0);
		}
	}

	class ExitButton extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			KioskData.makelogs("Q pressed !!!", 0);
			// Need to close all runing proceses !!!
			System.exit(0);
		}
	}

	class SetingsButton extends AbstractAction
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			kioskSetings = new KioskSetings(kioskData);
			KioskData.makelogs("S pressed !!!", 0);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			int width = dim.width;
			int height = dim.height;
			int frameHeight = 950;
			int frameWidth = 950;
			JDialog dialog = new JDialog();
			dialog.setModal(true);
			dialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
			dialog.add(kioskSetings.frame.getRootPane());
			kioskSetings.setFrame(dialog);
			dialog.setSize(frameWidth, frameHeight);
			dialog.setLocation(width / 2 - (frameWidth / 2), height / 2 - (frameHeight / 2));
			dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);

			resetALL();
			removpanel();
			reappeare();

			RCKiosk.save(kioskData);
			updateIPandDBstatus.run();
			textField_FirstName.requestFocus();
		}
	}

	class SendDataToDB implements Runnable
	{
		KioskData kd;

		SendDataToDB(KioskData indt)
		{
			kd = indt;
		}

		@Override
		public void run()
		{
			int auto_id = -1;
			try
			{
				db.connectDB();
				auto_id = db.writeRecord();
				db.disconnectDB();

				KioskData.makelogs("Budge number is: " + auto_id, 1);
				if (auto_id < 0)
				{
					JOptionPane.showMessageDialog(that, "Cannot retrieve budge number.", "Kiosk Printing Error!!!",
							JOptionPane.ERROR_MESSAGE);
					KioskData.makelogs("Cannot return budge number.", 1);
					return;
				}
				BadgeMaker b = new BadgeMaker();
				kd.setInfo_Barcobe(auto_id);
				b.setAllData(kd);

				if (kd.getInfo_BgNumber() != null)
					KioskData.makelogs("Corrent entery is: " + auto_id, 0);

				if ((kd.getInfo_BgNumber() == null) || kd.getInfo_BgNumber().equals("")
						|| kd.getInfo_BgNumber().equals("NULL"))
					b.printall();
				else if (kioskData.isPrintGastvTempBadg())
					b.printall();
				else
					KioskData.makelogs("Do not print", 0);
			} catch (Exception e)
			{
				JOptionPane.showMessageDialog(that, e.getMessage(), "Kiosk Printing Error!!!",
						JOptionPane.ERROR_MESSAGE);

				if (KioskData.debug)
					e.printStackTrace();
				else
					KioskData.makelogs(e.toString(), 0);
			}
		}
	}

	class UpdateDataToDB implements Runnable
	{
		String str;

		UpdateDataToDB(String indt)
		{
			str = indt;
		}

		@Override
		public void run()
		{
			try
			{
				db.connectDB();
				db.updateRecord(str);
				db.disconnectDB();
			} catch (SQLException e)
			{
				KioskData.makelogs(e.toString(), 0);
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////
	class HintTextField extends JTextField implements FocusListener
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final String	hint;
		private boolean			showingHint;

		public HintTextField(final String hint)
		{
			super(hint);
			this.hint = hint;
			this.showingHint = true;
			super.addFocusListener(this);
			// make it slightly red for the first time 
			super.setForeground(new Color(255, 153, 153));
		}

		@Override
		public void focusGained(FocusEvent e)
		{
			if (this.getText().isEmpty())
			{
				super.setText("");
				super.setForeground(Color.BLACK);
				showingHint = false;
			}
		}

		@Override
		public void focusLost(FocusEvent e)
		{
			if (this.getText().isEmpty())
			{
				super.setText(hint);
				super.setForeground(Color.RED);
				showingHint = true;
			}
		}

		@Override
		public String getText()
		{
			return showingHint ? "" : super.getText();
		}
	}
}
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
import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class KioskData
{
	
///////////////////////////////////////////////////////////////////////////////////////////////////
	/* My verebels */
	
	public static final String version 	= "2.2.6";
	
	private final String urlDOM			= "jdbc:mysql://";
	private final String urlEND			= "/kiosk";
	private String mysqlport			= "3306";
	// private String urlOfEquipmentDB		= "localhost";
	private String urlOfKioskDB			= "localhost";
	private String manualfile			= "man\\Kiosk-Manual.pdf";
	private String dbUserName			= "kioskuser";
	private String dbUserPassword		= "kioskuser";
	private String 	strTitle			= "Welcome to Bentonville Deployment Solutions Center";
	private String  strConfirmTitel		= "Bags Are Subject To Search Upon Entrance And Exit";
	private String  strConfirmFoot		= "Is The Above Information Correct?";
	private String 	strSubTitle 		= "Visitors Are Required to Sign In.";
	private String 	strFirstName 		= "First Name:";
	private String 	strLastName 		= "Last Name:";
	private String 	strCompany 			= "Company:";
	private String 	strDepartment		= "Department:";
	private String 	strSponsor 			= "Sponsor:";
	private String 	strReasonForVisit 	= "Reason for Visit:";
	private String 	strCitizenship 		= "Citizenship:";
	private String 	strYouMustFill 		= "You Must Fill in All of the Fields";
	private String 	strHoverOver 		= "* Hover over field for explanation";
	private String	strCheckInOutLable	= "For Internal Use Only";
	private String	strCheckInOutButton	= "Equipment Check In / Out";
	private String	strTempBadgeButton	= "Get Temp Access Badge";
	private String 	strbtnSubmit 		= "Submit";
	private String 	strrdbtnHp 			= "HPI";
	private String 	strrdbtnOther 		= "Other";
	private String 	strchckbxTempBadge 	= "Temp Access Badge";
	private String 	toolTipHP			= "HP Employee";
	private String 	toolTipOther		= "Randstad, Interview";
	private String	otherHint			= "Please fill in the field with your company name";
	private String 	toolTipTempBadge	= "New Employee, Forgot Badge";
	private String  toolTipTempBadgeNumber = "Enter number given by the security guard";
	
	private String CheckInOutTitel		= "<html><center>Please enter your first and last name and click the \"Check out\" button, then scan yellow tag on the equipment. <br>If you want to check in equipment click \"Check In\" buttone and scan yellow tag.";
	private String TempBadgTitel		= "<html><center>Please enter your first and last name and click the \"Get badge\" button,"
										+ " then scan yellow tag on the badge. <br>If you are returning badge then click on \"Return\" buttone and "
										+ "scan yellow tag.";
	private String TempBadgMassege	 	= "Please fill in all the fields with appropriate information.";
	private String TempBadgeMissingmass = "<html><font color=\"red\">Please do not forget to return the badge</font></html>\n"; // "<html><font color=\"red\">The ID card or $10 will be held as collateral</font></html>\n";
	private String TempBadgBarcodePromt = "Ask guard for temp badge \nand scan the barcode please:\n";
	
	private String CheckInOutfillInMassege = "Please fill in all the fields with appropriate information.";
	
	private String theTitle 			= "Kiosk";
	private String CheckInOutBarcodePromt = "Scan the barcode please:\n";
	private String About 				= "HP Visitor Kiosk \n\n"
			
										+ "Version: Release ( "+ KioskData.version  +" ) \n" 
										+ "(c) Copyright 2015\n"
										+ " Vitaly Borodin, Ryan Marlin, Dane Gerhart \n\n"
										+ "vitaly.borodin@hp.com \n"
										+ "ryan.meade.marlin@hpe.com\n"
										+ "dane.gerhardt@hp.com"
										+ "\n";
	private	String[] 	arrCompany 		= {"Select"};
	private String[] 	arrSponsor		= {"Select"};
	private String[] 	arrReason 		= {"Select","Other"};
	private String[] 	arrCitizenship 	= {"United States", "United States Minor Outlying Islands", "Afghanistan", "Aland Islands", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia, Plurinational State of", "Bonaire, Sint Eustatius and Saba", "Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia", "Cuba", "Curaçao", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guernsey", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard Island and McDonald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran, Islamic Republic of", "Iraq", "Ireland", "Isle of Man", "Israel", "Italy", "Jamaica", "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macao", "Macedonia, the former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montenegro", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Palestinian Territory, Occupied", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Réunion", "Romania", "Russian Federation", "Rwanda", "Saint Barthélemy", "Saint Helena, Ascension and Tristan da Cunha", "Saint Kitts and Nevis", "Saint Lucia", "Saint Martin (French part)", "Saint Pierre and Miquelon", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Sint Maarten (Dutch part)", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Svalbard and Jan Mayen", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Timor-Leste", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela, Bolivarian Republic of", "Viet Nam", "Virgin Islands, British", "Virgin Islands, U.S.", "Wallis and Futuna", "Western Sahara", "Yemen", "Zambia", "Zimbabwe"};
	private String[]	fontsName		= {"HP Simplified",	"HP Simplified Light","HPFutura Book","HPFutura Heavy","HPFutura Light","HPFutura Medium"};
	
	private String info_FirstName = "";
	private String info_LastName = "";
	private String info_BgNumber = "";
	private String info_Company = "";
	private String info_Reason = "";
	private String info_Sponsor = "";
	private String info_Citizenship = "";
	private int Barcode = 0;
	private String info_EquipmentNumber="";
	private String info_TempBadgeNumber="";
	
	private List <EmployeeHolder> employee = new ArrayList<EmployeeHolder>();
	
///////////////////////////////////////////////////////////////////////////////////////////////////
	/* Optional interface data */
	private boolean tempbadgeUIbtn = false;
	private boolean printGastvTempBadg = false;
/////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean imageRotation = false;
///////////////////////////////////////////////////////////////////////////////////////////////////
	/* Equipment data for check in / out */
	
	private String equipment_tag;
	private String equipment_name;
	private String equipment_firstname;
	private String equipment_lastname;
	private String equipment_notes;
	private String checkinout_firstname;
	private String checkinout_lastname;
	private String checkinout_notes;
	
	private String tempbadge_tag;
	private String tempbadge_name;
	private String tempbadge_firstname;
	private String tempbadge_lastname;
	private String tempbadge_notes;
	private String checktempbadge_firstname;
	private String checktempbadge_lastname;
	private String checktempbadge_notes;
	/* My constant */
//	private final int fontsize = 22;
	public String theFont = "HP Simplified";
	int fonti = 0;
	public Font TitleFont = new Font(getFontsName()[fonti], Font.BOLD, 70);
	public Font subTitleFont = new Font(getFontsName()[fonti], 0, 35);
	public Font lableFont = new Font(getFontsName()[fonti], 0, 22);
	public Font sublableFont = new Font(getFontsName()[fonti], 0, 15);
	public Font statusbar = new Font(getFontsName()[fonti], 0, 10);
	public Font internaLlabel = new Font(getFontsName()[fonti], 0, 15);
	public Font internalTitle = new Font(getFontsName()[fonti], 1, 16);
	
	public static String progFolder = "C:\\kioskDir\\";
	public static String icon = "icons\\icon_1.png";
	public String background = "kiosk.page.rev1.png";
	public static String checkInOutFile = "CheckInOut.csv";
	public static String logfile = "kiosklog.txt";

	public static final boolean debug = false;
	public final boolean editfield = true;
	
	Date date = new Date();
///////////////////////////////////////////////////////////////////////////////////////////////////
	public boolean isEmployee(EmployeeHolder emp)
	{
		for (int i=0; i<employee.size(); i++)
		{
			if (employee.get(i).getFName().equalsIgnoreCase(emp.getFName()) && 
					employee.get(i).getLName().equalsIgnoreCase(emp.getLName()))
				return true;
		}
		return false;
	}
	public static void makelogs(String str, int x)  
	{
		boolean infile 	 = true ;
		boolean inscreen = debug;
		
		String formattedDate = "";
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
		if (formattedDate.equals("")) formattedDate = sdf.format(date);
		
		String text = formattedDate + ":\t" + str + System.getProperty("line.separator");
		if (infile)
		{
		    try
		    {
		    	File file = new File(progFolder+logfile);
		    	FileWriter fout = new FileWriter(file, true);
		    	fout.write(text);
		    	fout.close();
		    } catch ( IOException e )
		    {
		       e.printStackTrace();
		    }	
		}
		if (inscreen)
		{
			System.out.println(text);
		}
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////
	/* Geters */
	public String getStrCheckInOutLable()
	{ return strCheckInOutLable; }
	public String getStrCheckInOutButton()
	{ return strCheckInOutButton; }
	public String getEquipment_tag()
	{ return equipment_tag; }
	public String getEquipment_name()
	{ return equipment_name; }
	public String getEquipment_firstname()
	{ return equipment_firstname; }
	public String getEquipment_lastname()
	{ return equipment_lastname; }
	public String getEquipment_notes()
	{ return equipment_notes; }
	public String getCheckinout_firstname()
	{ return checkinout_firstname; }
	public String getCheckinout_lastname()
	{ return checkinout_lastname; }
	public String getCheckinout_notes()
	{ return checkinout_notes; }
	public String getInfo_URLOfKioskDB()
	{ return urlDOM + urlOfKioskDB + ":" + mysqlport + urlEND;	}
	public String getInfo_URLOfEquipmentDB()
	{ return urlDOM + urlOfKioskDB + ":" + mysqlport + urlEND;	}
	public String getInfo_URLOfKioskDB_()
	{ return urlOfKioskDB;	}
	public String getInfo_URLOfEquipmentDB_()
	{ return urlOfKioskDB;	}
	public String getInfo_dbUserName()
	{ return dbUserName;	}
	public String getInfo_dbUserPassword()
	{ return dbUserPassword;	}
	public String getInfo_EquipmentNumber()
	{ return info_EquipmentNumber;	}
	public boolean getImageRotation()
	{ return imageRotation;	}
	public int getInfo_Barcobe()
	{ return Barcode;	}
	public String getInfo_FirstName()
	{ return info_FirstName;}
	public String getInfo_LastName()
	{ return info_LastName;	}
	public String getInfo_BgNumber()
	{ 
		if ( (info_BgNumber == null )|| (info_BgNumber.equals("")) )
			return "NULL";
		return info_BgNumber;
	}
	public String getInfo_Company()
	{ return info_Company;	}
	public String getInfo_Reason()
	{ return info_Reason;	}
	public String getInfo_Sponsor()
	{ return info_Sponsor;	}
	public String getInfo_Citizenship()
	{ return info_Citizenship;	}
	public String	getDate()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
	    String formattedDate = sdf.format(date);
	    return formattedDate;
	}
	public String 	getStrTitle()
	{ return strTitle;	}
	public String 	getStrSubTitle()
	{ return strSubTitle;	}
	public String 	getStrFirstName()
	{ return strFirstName;	}
	public String 	getStrLastName()
	{ return strLastName;	}
	public String 	getStrCompany()
	{ return strCompany;	}
	public String 	getStrSponsor()
	{ return strSponsor;	}
	public String 	getStrReasonForVisit()
	{ return strReasonForVisit;	}
	public String 	getStrCitizenship()
	{ return strCitizenship;	}
	public String 	getStrYouMustFill()
	{ return strYouMustFill;	}
	public String 	getStrHoverOver()
	{	return strHoverOver;	}
	public String 	getStrbtnSubmit()
	{ return strbtnSubmit;	}
	public String 	getStrrdbtnHp()
	{ return strrdbtnHp;	}
	public String 	getStrrdbtnOther()
	{ return strrdbtnOther;	}
	public String 	getStrchckbxTempBadge()
	{ return strchckbxTempBadge;	}
	public String[] 	getArrCompany()
	{ return arrCompany;	}
	public String[] 	getArrSponsor()
	{ return arrSponsor;	}
	public String[] 	getArrReason()
	{ return arrReason;	}
	public String[] 	getArrCitizenship()
	{ return arrCitizenship;	}
	public String 	getProgFolder()
	{ return progFolder;	}
	public String getToolTipHPRow()
	{ return toolTipHP; }
	public String getToolTipOtherRow()
	{ return  toolTipOther;	}
	public String getToolTipTempBadgeRow()
	{ return  toolTipTempBadge;	}

	public String getToolTipHPD()
	{ return toolTipHP.replace("<br>", RCFile.newLine); }
	public String getToolTipOtherD()
	{ return  toolTipOther.replace("<br>", RCFile.newLine);	}
	public String getToolTipTempBadgeD()
	{ return  toolTipTempBadge.replace("<br>", RCFile.newLine);	}

	public String getToolTipHP()
	{ return "<html><H3>i.e." + toolTipHP + "</H3></html>";	}
	
	public String getToolTipOther()
	{ return "<html><H3>i.e." + toolTipOther + "</H3></html>";	}
	public String getToolTipTempBadge()
	{ return "<html><H3>i.e." + toolTipTempBadge + "</H3></html>";	}
	public String[] getFontsName()
	{ return fontsName;	}
///////////////////////////////////////////////////////////////////////////////////////////////////
	/* Seters */
	public void setEquipment_tag(String str)
	{ 
		equipment_tag = str;
	}
	public void setEquipment_name(String str)
	{ 
		equipment_name = str;
	}
	public void setEquipment_firstname(String str)
	{ 
		equipment_firstname = str;
	}
	public void setEquipment_lastname(String str)
	{ 
		equipment_lastname = str;
	}
	public void setEquipment_notes(String str)
	{ 
		equipment_notes = str;
	}
	public void setCheckinout_firstname(String str)
	{ 
		checkinout_firstname = str;
	}
	public void setCheckinout_lastname(String str)
	{ 
		checkinout_lastname = str;
	}
	public void setCheckinout_notes(String str)
	{ 
		String tostr = str.substring(0, Math.min(str.length(), 254));
		checkinout_notes = tostr;
	}
	public void setInfo_EquipmentNumber(String in)
	{
		info_EquipmentNumber = in;
	}
	public void setImageRotation(boolean in)
	{
		imageRotation = in;
	}
	public void setImageRotation(String in)
	{
		imageRotation = in.equals("1");
	}
	public void setInfo_Barcobe(int bc)
	{
		Barcode = bc;
	}
	public void setInfo_Barcobe(String bc)
	{
		if (bc.matches("-?\\d+(.\\d+)?"))
			Barcode = Integer.parseInt(bc);
		else 
			Barcode = -1;
	}	
	public void setInfo_FirstName(String str)
	{ 
		info_FirstName = fixename(str);
	}
	public void setInfo_LastName(String str)
	{
		info_LastName = fixename(str);
	}
	public void setInfo_BgNumber(String str)
	{
		info_BgNumber = str;
	}
	public void setInfo_Company(String str)
	{
		info_Company = str;
	}
	public void setInfo_Reason(String str)
	{
		info_Reason = str;
	}
	public void setInfo_Sponsor(String str)
	{
		info_Sponsor = str;
	}
	public void setInfo_Citizenship(String str)
	{
		info_Citizenship = str;
	}
	public void 	setStrTitle(String str)
	{
		strTitle = str;
	}
	public void 	setStrSubTitle(String str)
	{
		strSubTitle = str;
	}
	public void 	setStrFirstName(String str)
	{
		strFirstName = str;
	}
	public void 	setStrLastName(String str)
	{
		strLastName = str;
	}
	public void 	setStrCompany(String str)
	{ 
		strCompany = str;
	}
	public void 	setStrSponsor(String str)
	{ 
		strSponsor = str;
	}
	public void 	setStrReasonForVisit(String str)
	{ 
		strReasonForVisit = str;
	}
	public void 	setStrCitizenship(String str)
	{ 
		strCitizenship = str;
	}
	public void 	setStrYouMustFill(String str)
	{ 
		strYouMustFill = str;
	}
	public void 	setStrHoverOver(String str)
	{	
		strHoverOver = str;
	}
	public void 	setStrbtnSubmit(String str)
	{ 
		strbtnSubmit = str;
	}
	public void 	setStrrdbtnHp(String str)
	{ 
		strrdbtnHp = str;
	}
	public void 	setStrrdbtnOther(String str)
	{ 
		strrdbtnOther = str;
	}
	public void 	setStrchckbxTempBadge(String str)
	{ 
		strchckbxTempBadge = str;
	}
	public void 	setProgFolder(String str)
	{ 
		progFolder = str;
	}
	public void setToolTipHP(String str)
	{ 
		toolTipHP = str.replace(RCFile.newLine, "<br>").replace("\r\n", "<br>").replace("\n", "<br>").replace("\r", "<br>");
	}
	public void setToolTipOther(String str)
	{ 
		toolTipOther =  str.replace(RCFile.newLine, "<br>").replace("\r\n", "<br>").replace("\n", "<br>").replace("\r", "<br>");
	}
	public void setToolTipTempBadge(String str)
	{ 
		toolTipTempBadge = str.replace(RCFile.newLine, "<br>").replace("\r\n", "<br>").replace("\n", "<br>").replace("\r", "<br>");
	}
	public void setArrCompany(String[] 	arr )
	{
		arrCompany = arr;
	}
	public void	setArrSponsor(String[] 	arr )
	{
		arrSponsor = arr;
	}
	public void	setArrReason(String[] 	arr )
	{
		arrReason = arr;
	}
	public void	setArrCitizenship(String[] 	arr )
	{ 
		arrCitizenship = arr;
	}
	public void setFontsName(String[] fonts)
	{
		this.fontsName = fonts;
	}
	public String getStrDepartment()
	{
		return strDepartment;
	}
	public void setStrDepartment(String strDepartment)
	{
		this.strDepartment = strDepartment;
	}
	public void setInfo_URLOfKioskDB(String str)
	{ 
		urlOfKioskDB = str;
	}
	public void setInfo_URLOfEquipmentDB(String str)
	{
		urlOfKioskDB = str;
	}
	public String getStrConfirmTitel()
	{
		return strConfirmTitel;
	}
	public void setStrConfirmTitel(String strConfirmTitel)
	{
		this.strConfirmTitel = strConfirmTitel;
	}
	public String getStrConfirmFoot()
	{
		return strConfirmFoot;
	}
	public void setStrConfirmFoot(String strConfirmFoot)
	{
		this.strConfirmFoot = strConfirmFoot;
	}
	public String getUrlDOM()
	{
		return urlDOM;
	}
	public String getManualfile()
	{
		return manualfile;
	}
	public void setManualfile(String manualfile)
	{
		this.manualfile = manualfile;
	}
	public String getToolTipTempBadgeNumberRow()
	{
		return  toolTipTempBadgeNumber;
	}
	public String getToolTipTempBadgeNumber()
	{
		return  "<html><H3>" + toolTipTempBadgeNumber + "</H3></html>";
	}
	public void setToolTipTempBadgeNumber(String toolTipTempBadgeNumber)
	{
		this.toolTipTempBadgeNumber = toolTipTempBadgeNumber;
	}
	public String getMysqlport()
	{
		return mysqlport;
	}
	public void setMysqlport(String mysqlport)
	{
		this.mysqlport = mysqlport;
	}
	public String getUrlEND()
	{
		return urlEND;
	}
	public String getUrlOfKioskDB()
	{
		return urlOfKioskDB;
	}
	public void setUrlOfKioskDB(String urlOfKioskDB)
	{
		this.urlOfKioskDB = urlOfKioskDB;
	}
	public String getUrlOfEquipmentDB()
	{
		return urlOfKioskDB;
	}
	public void setUrlOfEquipmentDB(String urlOfEquipmentDB)
	{
		this.urlOfKioskDB = urlOfEquipmentDB;
	}
	public String getCheckInOutTitel()
	{
		return CheckInOutTitel;
	}
	public void setCheckInOutTitel(String checkInOutTitel)
	{
		CheckInOutTitel = checkInOutTitel;
	}
	public String getCheckInOutfillInMassege()
	{
		return CheckInOutfillInMassege;
	}
	public void setCheckInOutfillInMassege(String checkInOutfillInMassege)
	{
		CheckInOutfillInMassege = checkInOutfillInMassege;
	}
	public String getTheTitle()
	{
		return theTitle;
	}
	public void setTheTitle(String theTitle)
	{
		this.theTitle = theTitle;
	}
	public String getCheckInOutBarcodePromt()
	{
		return CheckInOutBarcodePromt;
	}
	public void setCheckInOutBarcodePromt(String checkInOutBarcodePromt)
	{
		CheckInOutBarcodePromt = checkInOutBarcodePromt;
	}
	public String getAbout()
	{
		return About;
	}
	public void setAbout(String about)
	{
		About = about;
	}

	public String getStrTempBadgeButton()
	{
		return strTempBadgeButton;
	}

	public void setStrTempBadgeButton(String strTempBadgeButton)
	{
		this.strTempBadgeButton = strTempBadgeButton;
	}

	public String getTempBadgTitel()
	{
		return TempBadgTitel;
	}

	public void setTempBadgTitel(String tempBadgTitel)
	{
		TempBadgTitel = tempBadgTitel;
	}

	public String getTempBadgMassege()
	{
		return TempBadgMassege;
	}

	public void setTempBadgMassege(String tempBadgMassege)
	{
		TempBadgMassege = tempBadgMassege;
	}

	public String getTempBadgBarcodePromt()
	{
		return TempBadgBarcodePromt;
	}

	public void setTempBadgBarcodePromt(String tempBadgBarcodePromt)
	{
		TempBadgBarcodePromt = tempBadgBarcodePromt;
	}

	public String getTempbadge_tag()
	{
		return tempbadge_tag;
	}

	public void setTempbadge_tag(String tempbadge_tag)
	{
		this.tempbadge_tag = tempbadge_tag;
	}

	public String getTempbadge_name()
	{
		return tempbadge_name;
	}

	public void setTempbadge_name(String tempbadge_name)
	{
		this.tempbadge_name = tempbadge_name;
	}

	public String getTempbadge_firstname()
	{
		return tempbadge_firstname;
	}

	public void setTempbadge_firstname(String tempbadge_firstname)
	{
		this.tempbadge_firstname = tempbadge_firstname;
	}

	public String getTempbadge_lastname()
	{
		return tempbadge_lastname;
	}

	public void setTempbadge_lastname(String tempbadge_lastname)
	{
		this.tempbadge_lastname = tempbadge_lastname;
	}

	public String getTempbadge_notes()
	{
		return tempbadge_notes;
	}

	public void setTempbadge_notes(String tempbadge_notes)
	{
		this.tempbadge_notes = tempbadge_notes;
	}

	public String getChecktempbadge_firstname()
	{
		return checktempbadge_firstname;
	}

	public void setChecktempbadge_firstname(String checktempbadge_firstname)
	{
		this.checktempbadge_firstname = checktempbadge_firstname;
	}

	public String getChecktempbadge_lastname()
	{
		return checktempbadge_lastname;
	}

	public void setChecktempbadge_lastname(String checktempbadge_lastname)
	{
		this.checktempbadge_lastname = checktempbadge_lastname;
	}

	public String getChecktempbadge_notes()
	{
		return checktempbadge_notes;
	}

	public void setChecktempbadge_notes(String checktempbadge_notes)
	{
		this.checktempbadge_notes = checktempbadge_notes;
	}

	public String getInfo_TempBadgeNumber()
	{
		return info_TempBadgeNumber;
	}

	public void setInfo_TempBadgeNumber(String info_TempBadgeNumber)
	{
		this.info_TempBadgeNumber = info_TempBadgeNumber;
	}

	public boolean isTempbadgeUIbtn()
	{
		return tempbadgeUIbtn;
	}

	public void setTempbadgeUIbtn(boolean tempbadgeUIbtn)
	{
		this.tempbadgeUIbtn = tempbadgeUIbtn;
	}

	public boolean isPrintGastvTempBadg()
	{
		return printGastvTempBadg;
	}

	public void setPrintGastvTempBadg(boolean printGastvTempBadg)
	{
		this.printGastvTempBadg = printGastvTempBadg;
	}

	public String getTempBadgeMissingmass()
	{
		return TempBadgeMissingmass;
	}

	public void setTempBadgeMissingmass(String tempBadgeMissingmass)
	{
		TempBadgeMissingmass = tempBadgeMissingmass;
	}

	public List<EmployeeHolder> getEmployee()
	{
		return employee;
	}

	public void setEmployee(List<EmployeeHolder> employee)
	{
		this.employee = employee;
	}
	public void addEmployee(EmployeeHolder employee)
	{
		this.employee.add(employee);
	}
	public void resetAll()
	{
		info_FirstName = "";
		info_LastName = "";
		info_BgNumber = "";
		info_Company = "";
		info_Reason = "";
		info_Sponsor = "";
		info_Citizenship = "";
		info_EquipmentNumber="";
		info_TempBadgeNumber="";
		equipment_tag = "";
		equipment_name = "";
		equipment_firstname = "";
		equipment_lastname = "";
		equipment_notes = "";
		checkinout_firstname = "";
		checkinout_lastname = "";
		checkinout_notes = "";
		tempbadge_tag = "";
		tempbadge_name = "";
		tempbadge_firstname = "";
		tempbadge_lastname = "";
		tempbadge_notes = "";
		checktempbadge_firstname = "";
		checktempbadge_lastname = "";
		checktempbadge_notes = "";
		toolTipHP			= "";
		toolTipOther		= "";
		toolTipTempBadge	= "";
		toolTipTempBadgeNumber = "";
	}

	public String getCollectedData()
	{
		String newline = System.getProperty("line.separator");
		return 	"\tinfo_FirstName : " 			+ info_FirstName + newline +
				"\tinfo_LastName : " 			+ info_LastName + newline +
				"\tinfo_BgNumber : " 			+ info_BgNumber + newline +
				"\tinfo_Company : " 			+ info_Company + newline +
				"\tinfo_Reason : " 				+ info_Reason + newline +
				"\tinfo_Sponsor : " 			+ info_Sponsor + newline +
				"\tinfo_Citizenship : " 		+ info_Citizenship + newline +
				"\tinfo_EquipmentNumber : "  	+ info_EquipmentNumber+ newline +
				"\tinfo_TempBadgeNumber : "  	+ info_TempBadgeNumber+ newline +
				"\tequipment_tag : " 			+ equipment_tag + newline +
				"\tequipment_name : " 			+ equipment_name + newline +
				"\tequipment_firstname : " 		+ equipment_firstname + newline +
				"\tequipment_lastname : " 		+ equipment_lastname + newline +
				"\tequipment_notes : " 			+ equipment_notes + newline +
				"\tcheckinout_firstname : " 	+ checkinout_firstname + newline +
				"\tcheckinout_lastname : " 		+ checkinout_lastname + newline +
				"\tcheckinout_notes : " 		+ checkinout_notes + newline +
				"\ttempbadge_tag : " 			+ tempbadge_tag + newline +
				"\ttempbadge_name : " 			+ tempbadge_name + newline +
				"\ttempbadge_firstname : " 		+ tempbadge_firstname + newline +
				"\ttempbadge_lastname : " 		+ tempbadge_lastname + newline +
				"\ttempbadge_notes : " 			+ tempbadge_notes + newline +
				"\tchecktempbadge_firstname : "	+ checktempbadge_firstname + newline +
				"\tchecktempbadge_lastname : " 	+ checktempbadge_lastname + newline +
				"\tchecktempbadge_notes : " 	+ checktempbadge_notes + newline +
				"\ttol tip HPI : "				+ toolTipHP +newline +
				"\ttol tip Other : "			+ toolTipOther +newline +
				"\ttol tip Temp Badge : "		+ toolTipTempBadge +newline +
				"\ttol tip Temp Badge Number :"	+ toolTipTempBadgeNumber;
	}
	
	private String fixename(final String line)
	{
		return Character.toUpperCase(line.charAt(0)) + line.substring(1).toLowerCase();
		
	}
	public String getOtherHint()
	{
		return otherHint;
	}
	public void setOtherHint(String otherHint)
	{
		this.otherHint = otherHint;
	}
		
}

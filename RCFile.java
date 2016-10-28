import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.List;

public class RCFile
{
	private KioskData kiosk;
	private String fileText;
	public static String newLine = System.getProperty("line.separator");
	private String rcf;
	private String heder;
	private String Comp; 
	private String Sponsor; 
	private String Reason; 
	private String Title; 
	private String SubTitle;
	private String Rot;
	private String printGastvTempBadg;
	private String urlOfKioskDB;
	private String urlOfEquipmentDB;
	private String mysqlport;
	private String toolTipHP;
	private String toolTipOther;
	private String toolTipTempBadge;
	private String toolTipTempBadgeNumber;
	private String [] Opt = {
								"Company",
								"Sponsor",
								"Reason",
								"Title",
								"SubT",
								"RotateImage",
								"urlOfKioskDB",
								"urlOfEquipmentDB",
								"mysqlport",
								"printGastvTempBadg",
								"toolTipHP",
								"toolTipOther",
								"toolTipTempBadge",
								"toolTipTempBadgeNumber"
							};
	RCFile(KioskData k)
	{
		setFileText();
		kiosk = k;
		rcf = kiosk.getProgFolder() + "kioskrc";
		KioskData.makelogs("new RCFile just beab meked!" , 0);
	}
	public void setFileText()
	{
		heder = "###############################################################################" + newLine +
				"#  Initial settings for Kiosk" + newLine +	"# " + newLine + "#  Organization = Select,EG Pursuit,..." + newLine +
				"#" + newLine +"#  Sponsor = Select,Johann Smith,John Doe" + newLine +"#" + newLine +"#  Reason = Select,Customer Meeting,Interview,Other" + newLine +
				"#" + newLine +	"#  cityName = " + newLine +"#" + newLine +"#  First word will appear on the screen. If you do not" + newLine +
				"#  want the word \"Select\" to appear on the screen you can delete it," + newLine +
				"#  and start with first category: \"company name\" or any other word, but keep in mind that the" + newLine +
				"#  words \"Select\" and \"Other\" are keywords and used for form evaluation." + newLine +	"#" + newLine +
				"##################################################################################" + newLine +
				"#" + newLine +"#  RotateImage = 0" + newLine +"#" + newLine +"#  If you change RotateImage to 1 then the Badge Image will be printed in reverse orientation" + newLine +
				"#  " + newLine +"#  Any line that starts with a \"#\" character is a comment" + newLine + 
				"#  and will be ignored. White spaces matter. DO NOT PUT" + newLine +"#  ANY SPACE BETWEEN COMMAS AND NAMES unless you want to indent that category" + newLine +
				"#  ex. no spaces: John Doe" + newLine +"#  ex. spaces:	    John Doe" + newLine +"#" + newLine +
				"###############################################################################" + newLine + newLine;
		Comp = "Company = Select," + newLine;
		Sponsor = "Sponsor = Select," + newLine;
		Reason = "Reason = Select,Other,"+ newLine;
		Title = "Title = Welcome to Bentonville Deployment Solutions Center" + newLine;
		SubTitle = "SubT = Visitors Are Required to Sign In." + newLine;
		Rot = "RotateImage = 0" + newLine;
		printGastvTempBadg ="printGastvTempBadg = 0" + newLine;
		urlOfEquipmentDB = "urlOfEquipmentDB = localhost" + newLine;
		urlOfKioskDB = "urlOfKioskDB = localhost" + newLine;
		mysqlport = "mysqlport = 3306" + newLine;
		toolTipHP			= ""	+ newLine;
		toolTipOther		= ""	+ newLine;
		toolTipTempBadge	= ""	+ newLine;
		toolTipTempBadgeNumber = ""	+ newLine;

		fileText	= heder+Comp+Sponsor+Reason+Title+
					SubTitle+Rot+urlOfEquipmentDB+urlOfKioskDB+mysqlport+ printGastvTempBadg +
					toolTipHP+toolTipOther+toolTipTempBadge+toolTipTempBadgeNumber;
	}

	public void saveFileText()
	{
		Comp = "Company = ";
		String[] tempComp = kiosk.getArrCompany();
		for (int i=0; i < tempComp.length; i++ )
			Comp += tempComp[i] + ",";
		Comp += newLine;
		
		Sponsor = "Sponsor = ";
		String[] tempSponsor = kiosk.getArrSponsor();
		for (int i=0; i < tempSponsor.length; i++ )
			Sponsor += tempSponsor[i] + ",";
		Sponsor += newLine;
		
		Reason = "Reason = ";
		String[] tempReason = kiosk.getArrReason();
		for (int i=0; i < tempReason.length; i++ )
			Reason += tempReason[i] + ",";
		Reason += newLine;

		Title = "Title = " + kiosk.getStrTitle() + newLine;
		
		SubTitle = "SubT = " + kiosk.getStrSubTitle()  + newLine;
		
		if (kiosk.getImageRotation())
			Rot = "RotateImage = 1"  + newLine;
		else
			Rot = "RotateImage = 0"  + newLine;
		
		if (kiosk.isPrintGastvTempBadg())
			printGastvTempBadg = "printGastvTempBadg = 1" + newLine;
		else
			printGastvTempBadg = "printGastvTempBadg = 0" + newLine;
		
		urlOfEquipmentDB = "urlOfEquipmentDB = "+ kiosk.getUrlOfEquipmentDB() + newLine;
		urlOfKioskDB = "urlOfKioskDB = " + kiosk.getUrlOfKioskDB() + newLine;
		mysqlport = "mysqlport = " + kiosk.getMysqlport() + newLine;
		
		toolTipHP				= "toolTipHP = "				+ kiosk.getToolTipHPRow()				+ newLine;
		toolTipOther			= "toolTipOther = "				+ kiosk.getToolTipOtherRow() 			+ newLine;
		toolTipTempBadge		= "toolTipTempBadge = "			+ kiosk.getToolTipTempBadgeRow()		+ newLine;
		toolTipTempBadgeNumber 	= "toolTipTempBadgeNumber = "	+ kiosk.getToolTipTempBadgeNumberRow()	+ newLine;

		fileText	= heder+Comp+Sponsor+Reason+Title+
					SubTitle+Rot+urlOfEquipmentDB+urlOfKioskDB+mysqlport+ printGastvTempBadg +
					toolTipHP+toolTipOther+toolTipTempBadge+toolTipTempBadgeNumber;
		
		makefile(rcf, fileText);
	}
	
	public boolean checkFile(String strfile)
	{
		File file = new File(strfile);
		if (file.exists()) return true;
		else return false;
	}
	
	public void makefile(String strfile, String text)  
	{
        try
        {
        	File file = new File(strfile);
        	FileWriter fout = new FileWriter(file);
        	fout.write(text);
        	fout.close();
        } catch ( IOException e )
        {
        	KioskData.makelogs(e.getMessage(), 0);
           e.printStackTrace();
        }	
	}
	
	public void readfile()
	{
		if (!checkFile(rcf)) makefile(rcf, fileText);
		
		try
		{
			FileReader crfout = new FileReader(new File(rcf));
			LineNumberReader crlNum = new LineNumberReader (crfout);
			String text = null;
			//int i=0;
			// Need more work on automatization of this process
			while ((text = crlNum.readLine()) != null)
			{
				if (!text.startsWith("#"))
				{

					/*
					if (text.toLowerCase().contains(Opt[i].toLowerCase()))
					{
						String [] Nst;
						if (text.contains(" = "))
								Nst = text.split("= ");
						else 	Nst = text.split("=");
						switch (i)
						{
							case 0:	kiosk.setArrCompany(Nst[1].split(","));
									break;
							case 1:	kiosk.setArrSponsor(Nst[1].split(","));
							break;
							case 2:	kiosk.setArrReason(Nst[1].split(","));
							break;
							case 3:	kiosk.setArrCompany(Nst[1].split(","));
							break;
							case 4:	kiosk.setArrCompany(Nst[1].split(","));
							break;
							case 5:	kiosk.setArrCompany(Nst[1].split(","));
							break;
							case 6:	kiosk.setArrCompany(Nst[1].split(","));
							break;
						}
						
					}
					*/
					if (text.toLowerCase().contains(Opt[0].toLowerCase()))
					{
						String [] Nst;
						if (text.contains(" = "))
								Nst = text.split("= ");
						else 	Nst = text.split("=");
						kiosk.setArrCompany(Nst[1].split(","));
					}
					else if (text.toLowerCase().contains(Opt[1].toLowerCase()))
					{
						String [] Nst;
						if (text.contains(" = "))
								Nst = text.split("= ");
						else 	Nst = text.split("=");
						kiosk.setArrSponsor(Nst[1].split(","));
					}
					else if (text.toLowerCase().contains(Opt[2].toLowerCase()))
					{
						String [] Nst;
						if (text.contains(" = "))
								Nst = text.split("= ");
						else 	Nst = text.split("=");
						
						kiosk.setArrReason(Nst[1].split(","));
					}
					else if (text.toLowerCase().contains(Opt[3].toLowerCase()))
					{
						String [] Nst;
						if (text.contains(" = "))
								Nst = text.split("= ");
						else 	Nst = text.split("=");
						Title = Nst[1];
						kiosk.setStrTitle(Title);
					}
					else if (text.toLowerCase().contains(Opt[4].toLowerCase()))
					{
						String [] Nst;
						if (text.contains(" = "))
								Nst = text.split("= ");
						else 	Nst = text.split("=");
						SubTitle = Nst[1];
						kiosk.setStrSubTitle(SubTitle);
					}
					else if (text.toLowerCase().contains(Opt[5].toLowerCase()))
					{
						String [] Nst;
						if (text.contains(" = "))
								Nst = text.split("= ");
						else 	Nst = text.split("=");
						kiosk.setImageRotation(Nst[1]);
					}
					else if (text.toLowerCase().contains(Opt[6].toLowerCase()))
					{
						String [] Nst;
						if (text.contains(" = "))
								Nst = text.split("= ");
						else 	Nst = text.split("=");
						kiosk.setUrlOfKioskDB(Nst[1]);
					}
					else if (text.toLowerCase().contains(Opt[7].toLowerCase()))
					{
						String [] Nst;
						if (text.contains(" = "))
								Nst = text.split("= ");
						else 	Nst = text.split("=");
						kiosk.setUrlOfEquipmentDB(Nst[1]);
					}
					else if (text.toLowerCase().contains(Opt[8].toLowerCase()))
					{
						String [] Nst;
						if (text.contains(" = "))
								Nst = text.split("= ");
						else 	Nst = text.split("=");
						kiosk.setMysqlport(Nst[1]);
					}
					else if(text.toLowerCase().contains(Opt[9].toLowerCase()))
					{
						String [] Nst;
						if (text.contains(" = "))
								Nst = text.split("= ");
						else 	Nst = text.split("=");
						kiosk.setPrintGastvTempBadg(Nst[1].equals("1"));
						
					}
					else if(text.toLowerCase().contains(Opt[10].toLowerCase()))
					{
						String [] Nst;
						if (text.contains(" = "))
								Nst = text.split("= ");
						else 	Nst = text.split("=");
						kiosk.setToolTipHP(Nst[1]);
					}
					else if(text.toLowerCase().contains(Opt[11].toLowerCase()))
					{
						String [] Nst;
						if (text.contains(" = "))
								Nst = text.split("= ");
						else 	Nst = text.split("=");
						kiosk.setToolTipOther(Nst[1]);
					}
					else if(text.toLowerCase().contains(Opt[12].toLowerCase()))
					{
						String [] Nst;
						if (text.contains(" = "))
								Nst = text.split("= ");
						else 	Nst = text.split("=");
						kiosk.setToolTipTempBadge(Nst[1]);
					}
					else if(text.toLowerCase().contains(Opt[13].toLowerCase()))
					{
						String [] Nst;
						if (text.contains(" = "))
								Nst = text.split("= ");
						else 	Nst = text.split("=");
						kiosk.setToolTipTempBadgeNumber(Nst[1]);
					}
				}	
			}
			crlNum.close();
			crfout.close();
			
		} catch (Exception exp)
		{
			KioskData.makelogs("IndexOutOfBoundsException: " + exp.getMessage(), 0);
		}
	}
///////////////////////////////////////////////////////////////////////////////////////////////////
	public void print()
	{
		List<String> str = Arrays.asList(kiosk.getArrCompany());
		for (int i = 0; i < str.size(); i++)
			System.out.println(str.get(i));
		
		System.out.println("------------------------------------------------------------");
		
		str = Arrays.asList(kiosk.getArrSponsor() );
		for (int i = 0; i < str.size(); i++)
			System.out.println(str.get(i));
		
		System.out.println("------------------------------------------------------------");
		
		str = Arrays.asList(kiosk.getArrReason());
		for (int i = 0; i < str.size(); i++)
			System.out.println(str.get(i));
		
		System.out.println("------------------------------------------------------------");
		
		System.out.println(kiosk.getStrTitle());
	}

}

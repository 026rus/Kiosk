/*
 * 	Author Vitaly Borodin vitaly.borodin@hp.com
 * 	This file is part of HP Visitor Kiosk.
 */
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KioskDB
{
	private Connection con = null;
	private Statement st = null;
	private ResultSet rs = null;
///////////////////////////////////////////////////////////////////////////////////////////////////
	/* My verebels */
	private	List<String> 	Company = new ArrayList<String>();
	private List<String>  	Sponsor = new ArrayList<String>();
	private List<String> 	Reason  = new ArrayList<String>();
	private List<String> 	Equipment  = new ArrayList<String>();
	private List<String>	TempBadge = new ArrayList<String>();
	private String 		Title;
	KioskData dt;
///////////////////////////////////////////////////////////////////////////////////////////////////
	
	KioskDB (KioskData indt)
	{
		dt = indt;
	}
	
	public void setAllData(KioskData indt)
	{
		dt = indt;
	}
	
	public String[] getCompany()
	{
		String[] str = null;
		if (Company != null) str = new String[Company.size()];
		if (str == null)
			str = new String [] {"Select"};
		else
			Company.toArray(str);
		return str;
	}
	public String [] getSponsor()
	{
		String[] str = null;
		if (Sponsor != null) str = new String[Sponsor.size()];
		if (str == null)
			str = new String [] {"Select"};
		else
			Sponsor.toArray(str);
		return str;
	}
	public String [] getReason() 		
	{
		String[] str = null;
		if (Reason != null) str = new String[Reason.size()];
		if (str == null)
			str = new String [] {"Select"};
		else
			Reason.toArray(str);
		return str;
	}
	public String getTitel()
	{
		if (Title == null)
			Title = " ";
		return Title;
	}
	
	/* Checking in and out equipments like phone sets and projectors */
///////////////////////////////////////////////////////////////////////////////////////////////////
	public String[] getEquipment(String equipmentNumber) throws SQLException
	{
		Equipment.clear();
		connectDB(dt.getInfo_URLOfEquipmentDB());
		PreparedStatement ps = con.prepareStatement("SELECT * FROM equipment WHERE tag=?");
		ps.setString(1, equipmentNumber);
		
		rs = ps.executeQuery();
		
		while (rs.next())
		{
			Equipment.add(rs.getString("tag"));
			Equipment.add(rs.getString("name"));
			Equipment.add(rs.getString("firstname"));
			Equipment.add(rs.getString("lastname"));
			Equipment.add(rs.getString("notes"));
		}
		
		disconnectDB ();
		
		String[] str = null;
		if (Equipment != null) str = new String[Equipment.size()];
		if (str == null)
			str = new String [] {"Select"};
		else
			Equipment.toArray(str);
		return str;
	}
	public void sendEquipment() throws SQLException
	{
		connectDB(dt.getInfo_URLOfEquipmentDB());
		
		PreparedStatement ps = con.prepareStatement("INSERT INTO "
				+ "checkoutrecords(tag, equipmentname, firstname, lastname, notes, dataout)"
				+ " VALUES(?, ?, ?, ?, ?, NOW() )");
		ps.setString (1, dt.getEquipment_tag());
		ps.setString (2, dt.getEquipment_name());
		ps.setString (3, dt.getCheckinout_firstname());
		ps.setString (4, dt.getCheckinout_lastname());
		ps.setString (5, dt.getCheckinout_notes());
		ps.execute();

		disconnectDB ();
	}
	
	public boolean addEquipment(String tag, String equpment_name, String f_name, String l_name, String notes) throws SQLException
	{
		connectDB(dt.getInfo_URLOfEquipmentDB());
		
		PreparedStatement ps = con.prepareStatement("INSERT INTO "
				+ "equipment(tag, name, firstname, lastname, notes)"
				+ " VALUES(?, ?, ?, ?, ? )");

		ps.setString (1, tag);
		ps.setString (2, equpment_name);
		ps.setString (3, f_name);
		ps.setString (4, l_name);
		ps.setString (5, notes);
		ps.execute();

		disconnectDB ();
		
		return true;
	}
	
	public void sendEquipmentIn() throws SQLException
	{
		connectDB(dt.getInfo_URLOfEquipmentDB());
		
		PreparedStatement ps = con.prepareStatement("UPDATE checkoutrecords set datain= NOW() "
				+ "WHERE tag=? AND datain IS NULL");
		
		ps.setString(1, dt.getEquipment_tag());
		
		ps.execute();

		disconnectDB ();
	}

	/* Checking in and out Temp Badges  */
///////////////////////////////////////////////////////////////////////////////////////////////////

public String[] getTempBadge(String tempbadgeNumber) throws SQLException
{
	TempBadge.clear();
	connectDB(dt.getInfo_URLOfEquipmentDB());
	PreparedStatement ps = con.prepareStatement("SELECT * FROM tempbadge WHERE tag=?");
	ps.setString(1, tempbadgeNumber);
	
	rs = ps.executeQuery();
	
	{
		StringBuilder strb = new StringBuilder();
		String newline = System.getProperty("line.separator");
		int i = 0;
		while (rs.next())
		{
			strb.append("Data coming from DB tempbadge: " + tempbadgeNumber + newline);
			strb.append("\ttag: \t\t" + rs.getString("tag") + newline);
			strb.append("\tname: \t\t" + rs.getString("name") + newline);
			strb.append("\tfirstname: \t" + rs.getString("firstname") + newline);
			strb.append("\tlastname: \t" + rs.getString("lastname") + newline);
			strb.append("\tnotes: \t\t" + rs.getString("notes") + newline);
			i++;
		}
		KioskData.makelogs(strb.toString() + " the = " + i + " of them!", 0);
	} 
	
	while (rs.next())
	{
		TempBadge.add(rs.getString("tag"));
		TempBadge.add(rs.getString("name"));
		TempBadge.add(rs.getString("firstname"));
		TempBadge.add(rs.getString("lastname"));
		TempBadge.add(rs.getString("notes"));
	}
	
	if (KioskData.debug)
	{
		System.out.println("Insede TempBadge : ");
		for (int i=0; i < TempBadge.size(); i++)
			System.out.println("\t\t\t" + TempBadge.get(i).toString() );
	}
	
	disconnectDB ();
	
	String[] str = null;
	if (TempBadge != null) str = new String[TempBadge.size()];
	
	if (str == null)
		str = new String [] {"Select"};
	else
		TempBadge.toArray(str);
	
	return str;
}

public void sendTempBadge() throws SQLException
{
	connectDB(dt.getInfo_URLOfEquipmentDB());
	
	PreparedStatement ps = con.prepareStatement("INSERT INTO "
	+ "tempbadgerecords(tag, equipmentname, firstname, lastname, notes, dataout)"
	+ " VALUES(?, ?, ?, ?, ?, NOW() )");
	ps.setString (1, dt.getTempbadge_tag());
	ps.setString (2, dt.getTempbadge_name());
	ps.setString (3, dt.getChecktempbadge_firstname());
	ps.setString (4, dt.getChecktempbadge_lastname());
	ps.setString (5, dt.getChecktempbadge_notes());
	ps.execute();
	
	disconnectDB ();
}

public boolean addTempBadge(String tag, String tb_name, String f_name, String l_name, String notes) throws SQLException
{
	connectDB(dt.getInfo_URLOfEquipmentDB());
	
	PreparedStatement ps = con.prepareStatement("INSERT INTO "
	+ "tempbadge(tag, name, firstname, lastname, notes)"
	+ " VALUES(?, ?, ?, ?, ? )");
	
	ps.setString (1, tag);
	ps.setString (2, tb_name);
	ps.setString (3, f_name);
	ps.setString (4, l_name);
	ps.setString (5, notes);
	ps.execute();
	
	disconnectDB ();
	
	return true;
}

public void sendTempBadgeIn() throws SQLException
{
	KioskData.makelogs("Traing to return badge #: " + dt.getTempbadge_tag(), 1);
	
	connectDB(dt.getInfo_URLOfEquipmentDB());
	
	/*
	PreparedStatement ps = con.prepareStatement("UPDATE tempbadgerecords set datain= NOW() "
	+ "WHERE tag=? AND datain IS NULL");
	*/

	PreparedStatement ps = con.prepareStatement("UPDATE tempbadgerecords set datain= NOW() "
	+ "WHERE tag=? ORDER BY  dataout DESC LIMIT 1");
	
	ps.setString(1, dt.getTempbadge_tag());
	
	ps.execute();
	
	disconnectDB ();
}
///////////////////////////////////////////////////////////////////////////////////////////////////

	public void connectDB() throws SQLException
	{
		con = DriverManager.getConnection(	dt.getInfo_URLOfEquipmentDB(),
											dt.getInfo_dbUserName(),
											dt.getInfo_dbUserPassword() );
		st = con.createStatement();
	}
	public void connectDB(String url) throws SQLException
	{
		con = DriverManager.getConnection(	url,
											dt.getInfo_dbUserName(),
											dt.getInfo_dbUserPassword() );
		st = con.createStatement();
	}
	
	public void getEmployees() throws SQLException
	{
		connectDB();
		rs = st.executeQuery("SELECT * FROM employee");
		
		while(rs.next())
		{
			EmployeeHolder temp = new EmployeeHolder(rs.getString(2),rs.getString(3),rs.getString(4));
			dt.addEmployee(temp);			
		}
		disconnectDB ();		
	}
	
	
	public void readDB() throws SQLException
	{
		rs = st.executeQuery("SELECT * FROM company");
		while (rs.next())
			Company.add(rs.getString(2));
		
		rs = st.executeQuery("SELECT * FROM sponsor");
		while (rs.next())
			Sponsor.add(rs.getString(2));
        
		rs = st.executeQuery("SELECT * FROM reason");
		while (rs.next())
			Reason.add(rs.getString(2));
	}
	public void disconnectDB () throws SQLException
	{
            if (rs != null)	{	rs.close();		}
            if (st != null)	{	st.close();		}
            if (con != null){	con.close();	}
	}
	
	public boolean isConnect (String url) throws SQLException
	{
		boolean retval = false;
		connectDB(url);
		retval =  con.isValid(2);
		disconnectDB ();
		return retval;
	}
	/*
	 * testing purpuse 
	 */
	public void printing()
	{
		String[] mystr = getReason();
		
		for (int i=0; i<mystr.length; i++)
		{
			System.out.println(": " + mystr[i]);
		}
	}
	
	public void updateRecord(String str) throws SQLException
	{
		int inx = 0;
		try
		{
			inx = Integer.parseInt(str);
		}
		catch (Exception e)
		{
			KioskData.makelogs("Not number pased in to the DB : "+ e.getMessage(), 0);
		}
		
		if (inx > 100)
		st.executeUpdate("UPDATE records "
				+ "SET dateout = NOW() "
				+ "WHERE id = " + str );
		else 
		{
			PreparedStatement ps = con.prepareStatement("UPDATE records SET dateout = NOW() "
					+ "WHERE tempbadge=? ORDER BY datein DESC LIMIT 1");
					ps.setString(1, str);
					ps.execute();
		}
	}
	
	public int writeRecord() throws SQLException
	{
		int auto_id = -1;
		try
		{
		
			String sql = "INSERT INTO records "
					+ "VALUES( NULL , "
					+ "\""+ dt.getInfo_FirstName() + "\", "
					+ "\""+ dt.getInfo_LastName() + "\", "
					+ "\""+ dt.getInfo_Company() + "\", "
					+ "\""+ dt.getInfo_Citizenship() + "\", "
					+ "\""+ dt.getInfo_Reason() + "\", "
					+ "\""+ dt.getInfo_Sponsor() + "\", "
					+ "NOW(), "
					+ "NULL, "
					+ "" + dt.getInfo_BgNumber() + ")";
			
			KioskData.makelogs("inserting: " + sql , 0);
			Statement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			
			ResultSet rs = stmt.getGeneratedKeys();
		    rs.next();
		    auto_id = rs.getInt(1);
		    KioskData.makelogs("Inside the DataBase manager corrent entery is: " + auto_id, 0);
		}
		catch (Exception e)
		{
			KioskData.makelogs(e.getMessage(), 3);
		}
		return auto_id;
	}
}

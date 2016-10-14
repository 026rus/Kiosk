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
import java.sql.SQLException;


public class RCKiosk
{
	static KioskData kd = new KioskData();
	static KioskDB db = new KioskDB(kd);
	public static KioskData getit() throws SQLException
	{		
		RCFile f = new RCFile(kd);
		f.readfile();
		// db.getEmployees();
		return kd;
	}

	public static void save(KioskData data)
	{
		RCFile f = new RCFile(data);
		f.saveFileText();
	}
/* ------------------------------------------------------------------------------------------------
	public static void main(String[] args)
	{
		KioskData k = null;
		try {
			k = RCKiosk.getit();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// RCFile f = new RCFile(k);
		
		try {
			RCKiosk.getnextID();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(k.getInfo_Barcobe());
	}
-------------------------------------------------------------------------------------------------*/
}

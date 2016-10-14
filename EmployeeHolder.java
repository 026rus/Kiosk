
public class EmployeeHolder
{
	private String FName;
	private String MName;
	private String LName;
	
	public EmployeeHolder(String fName, String lName)
	{
		super();
		FName = fName;
		MName = "";
		LName = lName;
	}
	
	public EmployeeHolder(String fName, String mName, String lName)
	{
		super();
		FName = fName;
		MName = mName;
		LName = lName;
	}
	
	public String getFName()
	{
		return FName;
	}
	public void setFName(String fName)
	{
		FName = fName;
	}
	public String getLName()
	{
		return LName;
	}
	public void setLName(String lName)
	{
		LName = lName;
	}


	public String getMName()
	{
		return MName;
	}


	public void setMName(String mName)
	{
		MName = mName;
	}
}

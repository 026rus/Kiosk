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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;

import com.keepautomation.barcode.BarCode;
import com.keepautomation.barcode.IBarCode;


public class BadgeMaker
{
	
	private String progFolder;
	private int inBrCode;
	private String inName;
	private String inCompany;
	private String inSponsor;
	private String formattedDate;
	private String numBadge;
	private boolean imrot = false;
	
	BadgeMaker()
	{
		progFolder = "";
		inBrCode = 0;
		inName = "";
		inCompany = "";
		inSponsor = "";
		formattedDate = "";
		numBadge = "";
	}
	
	public void setAllData(KioskData dt)
	{
		progFolder = dt.getProgFolder();
		inBrCode = dt.getInfo_Barcobe();
		inName = dt.getInfo_FirstName();
		if (!dt.getInfo_LastName().equals("")) inName += " " + dt.getInfo_LastName(); 
		inCompany = dt.getInfo_Company();
		inSponsor = dt.getInfo_Sponsor();
		formattedDate = dt.getDate();
		numBadge = dt.getInfo_BgNumber();
		imrot = dt.getImageRotation();
		KioskData.makelogs("Seting data for badge : " + inName + " the number is: " + numBadge, 1);
	}
	
	public void setRotation(boolean in)
	{
		imrot = in;
	}
	
	public void setProgFolder(String str)
	{
		progFolder = str;
	}

	public void setNumBadge(String str)
	{
		numBadge = str;
	}
	
	public void setBrCode(int inval)
	{
		inBrCode = inval;
	}
	public void setName(String str)
	{
		inName = str;
	}
	public void setCompany(String str)
	{
		inCompany = str;
	}
	public void setSponsor(String str)
	{
		inSponsor = str;
	}
	public void setFormattedDate(String str)
	{
		formattedDate = str;
	}
	public void printall() throws IOException
	{
		bcGener();
		mkBg();
		printBg();
	}
	
	private void bcGener()
	{
		BarCode barcode = new BarCode();
		barcode.setCodeToEncode(inBrCode + "");
		barcode.setSymbology(IBarCode.CODE128);
		barcode.setX(4);
		barcode.setY(60);
		barcode.setRightMargin(0);
		barcode.setLeftMargin(0);
		barcode.setTopMargin(0);
		barcode.setBottomMargin(0);
		barcode.setChecksumEnabled(false);
		barcode.setFnc1(IBarCode.FNC1_NONE);
		try
		{
			barcode.draw(progFolder + "code128.png");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			KioskData.makelogs(e.getMessage(), 0);
		}
	}
	
	public void printBg() throws IOException
	{
		File file = new File(progFolder + "myimage.png");  
 
		InputStream is = new BufferedInputStream(new FileInputStream(file));  
		
		PrintService service = PrintServiceLookup.lookupDefaultPrintService();  
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		pras.add(new Copies(1));
		
		DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG; 
		
		DocAttributeSet das = new HashDocAttributeSet();
		das.add(new MediaPrintableArea(102,60,204,120,MediaPrintableArea.MM));
		DocPrintJob job = service.createPrintJob();  
		Doc doc = new SimpleDoc(is, flavor, das);  
		try 
		{  
			job.print(doc, pras);  
		} catch (PrintException e)
		{
			e.printStackTrace();  
			KioskData.makelogs(e.getMessage(), 0);
		}    
		is.close();  
	}  
	
	public void mkBg()
	{
		int width = 600;
		int height =336;
		int fontS = 26; 
		int fontB = 42;
		String path = progFolder + "code128.png";
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd h:mm a");
		if (formattedDate.equals("")) formattedDate = sdf.format(date);
		
		try
		{
			
			File file = new File(path);
			BufferedImage bcImage = ImageIO.read(file);
			
			// Constructs a BufferedImage of one of the predefined image types.
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			Graphics2D g2d = bufferedImage.createGraphics();
						
			// fill all the image with white
			g2d.setColor(Color.white);
			g2d.fillRect(0, 0, width, height);
			
			int bcImageH = bcImage.getHeight();
			int bcImageW = bcImage.getWidth();
			
			int shtext = 150;
			
			g2d.drawImage(bcImage, ((width-bcImageW)/2), (height-(bcImageH+5)), null);
			
			// create a string 
			g2d.setColor(Color.black);
			g2d.setFont(new Font("HP Simplified", Font.BOLD, fontB));
			g2d.drawString(inName, ((width - (inName.length()*17))/2), shtext);
			
			g2d.setFont(new Font("HP Simplified", Font.PLAIN, fontS));
			g2d.drawString("Representing: " + inCompany, 50, (shtext += 50));
			
			if (!numBadge.equals("") && (numBadge != null) && !numBadge.equals("NULL"))
			{
					g2d.drawString("#: " + numBadge, 450, shtext + 20);
			}

			g2d.drawString("Sponsor: " + inSponsor, 50, (shtext += 30));
			
			g2d.setFont(new Font("HP Simplified", Font.PLAIN, (int)(fontS/1.5)));
			g2d.drawString("" + formattedDate, 450, (shtext-55));
			
			// Disposes of this graphics context and releases any system resources that it is using.

			g2d.dispose();
			
			if (imrot)
			{
				BufferedImage returnImage = new BufferedImage( width, height, bufferedImage.getType());
				for( int x = 0; x < width; x++ )
					for( int y = 0; y < height; y++ )
							returnImage.setRGB( width-x-1, height-y-1, bufferedImage.getRGB(x,y));
				// Save as PNG
				File file1 = new File(progFolder + "myimage.png");
				ImageIO.write(returnImage, "png", file1);
			}else 
			{
				File file1 = new File(progFolder + "myimage.png");
				ImageIO.write(bufferedImage, "png", file1);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			KioskData.makelogs(e.getMessage(), 0);
		}
		
	}
}
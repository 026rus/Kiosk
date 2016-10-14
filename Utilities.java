
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Utilities
{
	public static void play(String audioFilePath)
	{
		boolean playCompleted = false;
		File audioFile = new File(audioFilePath);

		try
		{
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

			AudioFormat format = audioStream.getFormat();

			DataLine.Info info = new DataLine.Info(Clip.class, format);

			Clip audioClip = (Clip) AudioSystem.getLine(info);

			audioClip.open(audioStream);

			audioClip.start();

			while (!playCompleted)
			{
				// wait for the playback completes
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

			audioClip.close();

		} catch (UnsupportedAudioFileException e)
		{
			System.out.println("The specified audio file is not supported.");
			e.printStackTrace();
		} catch (IOException e)
		{
			System.out.println("Error playing the audio file.");
			e.printStackTrace();
		} catch (LineUnavailableException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

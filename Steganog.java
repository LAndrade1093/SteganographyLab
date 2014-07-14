import java.awt.Color;
import java.io.*;
import edu.neumont.ui.Picture;

public class Steganog 
{
	private static int BYTE_SIZE = 8;
	private static int ENCODING_BYTE_SIZE = 6;
	
	public Picture embedIntoImage(Picture cleanImage, String message) throws IOException
	{
		int numOfPixels = cleanImage.width() * cleanImage.height();
		PrimeIterator prime = new PrimeIterator(numOfPixels);
		int index = 0;
		Picture encodedImage = cleanImage;
		message = message.toUpperCase();
		
		while(index < message.length() && prime.hasNext())
		{
			char letter = message.charAt(index);
			String encodedBinary = FormEncodedBinaryString(letter);
			
			int pixelNumber = prime.next();
			Color pixelColor = encodedImage.get(pixelNumber % encodedImage.width(), pixelNumber / encodedImage.height());
			String red = Get8BitBinaryString(pixelColor.getRed());
			String green = Get8BitBinaryString(pixelColor.getGreen());
			String blue = Get8BitBinaryString(pixelColor.getBlue());	
			
			red = red.substring(0, 6) + encodedBinary.substring(0, 2);
			green = green.substring(0, 6) + encodedBinary.substring(2, 4);
			blue = blue.substring(0, 6) + encodedBinary.substring(4, 6);
			
			Color newColor = new Color(Integer.parseInt(red, 2), Integer.parseInt(green, 2), Integer.parseInt(blue, 2));
			
			encodedImage.set(pixelNumber % encodedImage.width(), pixelNumber / encodedImage.height(), newColor);
			index++;
		}
		
		return encodedImage;
	}
	
	
	private String FormEncodedBinaryString (char letterToConvert)
	{
		int encodingNumber = ((int)letterToConvert) - 32;
		String encodingBinary = Integer.toBinaryString(encodingNumber);
		while(encodingBinary.length() < ENCODING_BYTE_SIZE)
		{
			encodingBinary = "0" + encodingBinary;
		}
		
		return encodingBinary;
	}
	
	
	
	
	
	public String retrieveFromImage(Picture imageWithSecretMessage) throws IOException
	{
		int numOfPixels = imageWithSecretMessage.width() * imageWithSecretMessage.height();
		PrimeIterator prime = new PrimeIterator(numOfPixels);
		
		String embeddedMessage = "";
		while(prime.hasNext())
		{
			int pixelNumber = prime.next();
			Color pixelColor = imageWithSecretMessage.get(pixelNumber % imageWithSecretMessage.width(), pixelNumber / imageWithSecretMessage.height());
			String red = Get8BitBinaryString(pixelColor.getRed());
			String green = Get8BitBinaryString(pixelColor.getGreen());
			String blue = Get8BitBinaryString(pixelColor.getBlue());
			char asciiLetter = DecodeCharFromRGB(red, green, blue);
			embeddedMessage += asciiLetter;
		}
		
		return embeddedMessage;
	}
	
	private String Get8BitBinaryString (int number)
	{
		String binaryString = Integer.toBinaryString(number);
		while(binaryString.length() < BYTE_SIZE)
		{
			binaryString = "0" + binaryString;
		}
		return binaryString;
	}
	
	private char DecodeCharFromRGB(String r, String g, String b)
	{
		String binaryLetter = "00" + r.substring(BYTE_SIZE - 2) + g.substring(BYTE_SIZE - 2) + b.substring(BYTE_SIZE - 2);
		int asciiNumber = Integer.parseInt(binaryLetter, 2);
		char letter = (char)(asciiNumber + 32);
		return letter;
	}
}

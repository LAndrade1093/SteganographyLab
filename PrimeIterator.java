import java.util.Iterator;

public class PrimeIterator implements Iterator<Integer>
{
	private int[] primeArray;
	private int primeNumberIndex;
	
	public PrimeIterator(int max)
	{
		primeNumberIndex = 0;
		primeArray = findPrimeNumbers(max);
	}

	@Override
	public boolean hasNext() 
	{
		boolean nextIsAvailable = (primeNumberIndex + 1 == primeArray.length) ? false : true;
		return nextIsAvailable;
		
	}

	@Override
	public Integer next() {
		int number = 0;
		if(hasNext())
		{
			number = primeArray[primeNumberIndex];
			primeNumberIndex++;
		}
		else
		{
			number = -1;
		}
		return number;
	}

	@Override
	public void remove() {}
	
	private int[] findPrimeNumbers(int limit)
	{
		boolean[] divisibleNumbers = new boolean[limit];
		
		int primeCheckLimit = (int)Math.sqrt(limit);
		int primeCount = 0;
		for(int currentNumber = 2; currentNumber <= primeCheckLimit; currentNumber++)
		{
			if(!divisibleNumbers[currentNumber-1])
			{
				for(int multiple = (int)Math.pow(currentNumber, 2); multiple <= limit; multiple += currentNumber)
				{
					divisibleNumbers[multiple-1] = true;
				}
			}
		}
		
		for(int i = 0; i < divisibleNumbers.length; i++)
		{
			if(!divisibleNumbers[i])
			{
				primeCount++;
			}
		}
		
		int[] primeNumbers = new int[primeCount];
		int index = 0;
		for(int i = 1; i < divisibleNumbers.length; i++)
		{
			if(!divisibleNumbers[i])
			{
				primeNumbers[index] = i+1;
				index++;
			}
		}
		
		return primeNumbers;
	}
}

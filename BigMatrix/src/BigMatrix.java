import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.Set;

public class BigMatrix
{
	private HashMap<Integer,HashMap<Integer,Integer>> map;
	private HashMap<Integer,HashMap<Integer,Integer>> mapC;


	public BigMatrix()
	{
		map = new HashMap<Integer,HashMap<Integer,Integer>>();
		mapC = new HashMap<Integer,HashMap<Integer,Integer>>();
	}
	
	public void setValue(int row, int col, int value)
	{
		
		if(value == 0 && map.get(row)!= null && map.get(row).get(col) != null)
		{
			map.get(row).remove(col);
			if(map.get(row).size() == 0)
			{
				map.remove(row);
			}
			mapC.get(col).remove(row);
			if(mapC.get(col).size() == 0)
			{
				mapC.remove(col);
			}
		}
		else if (value!=0)
		{
			if(map.get(row) == null)
			{
				HashMap<Integer, Integer> col1 = new HashMap<Integer, Integer>();
				col1.put(col, value);
				map.put(row, col1);
			}
			else
			{
				map.get(row).put(col, value);
			}
			
			
			if(mapC.get(col) == null)
			{
				HashMap<Integer, Integer> row1 = new HashMap<Integer, Integer>();
				row1.put(row, value);
				mapC.put(col, row1);
			}
			else
			{
				mapC.get(col).put(row, value);
			}
		}
	}
	
	public int getValue(int row, int col)
	{
		if(!map.containsKey(row))
		{
			return 0;
		}
		if(!(map.get(row).containsKey(col)))
		{
			return 0;
		}
		
		return map.get(row).get(col);
	}
	
	public List<Integer> getNonEmptyRows()
	{
		List<Integer> list = new ArrayList<Integer>();
		Set<Integer> keySet = map.keySet();
		Iterator<Integer> itr = keySet.iterator();
		
		
		while(itr.hasNext())
		{
			list.add(itr.next());
		}
		return list;
	}
	
	public List<Integer> getNonEmptyRowsInColumn(int col)
	{
		if(mapC.get(col) == null)
		{
			return new ArrayList<Integer>();
		}
		
		HashMap<Integer,Integer> row = mapC.get(col);
		List<Integer> list = new ArrayList<Integer>();
		Set<Integer> keySet = row.keySet();
		Iterator<Integer> itr = keySet.iterator();
		
		while(itr.hasNext())
		{
			list.add(itr.next());
		}
		return list;
	}
	
	public List<Integer> getNonEmptyCols()
	{
		List<Integer> list = new ArrayList<Integer>();		
		Set<Integer> keySet = mapC.keySet();
		Iterator<Integer> itr = keySet.iterator();
		
		
		while(itr.hasNext())
		{
			list.add(itr.next());
		}
		return list;
	}
	
	public List<Integer> getNonEmptyColsInRow(int row)
	{
		if(map.get(row) == null)
		{
			return new ArrayList<Integer>();
		}
		
		HashMap<Integer,Integer> col = map.get(row);
		List<Integer> list = new ArrayList<Integer>();
		Set<Integer> keySet = col.keySet();
		Iterator<Integer> itr = keySet.iterator();
		
		while(itr.hasNext())
		{
			list.add(itr.next());
		}
		return list;
		
	}
	
	public int getRowSum(int row)
	{
		if(map.get(row) == null)
		{
			return 0;
		}
		HashMap<Integer,Integer> col = map.get(row);
		int sum = 0;
		List<Integer> indexes = getNonEmptyColsInRow(row);
		
		for(int index:indexes)
		{
			sum+= col.get(index);
		}
		return sum;
		
	}
	
	public int getColSum(int col)
	{
		if(mapC.get(col) == null)
		{
			return 0;
		}
		HashMap<Integer,Integer> row = mapC.get(col);
		int sum = 0;
		List<Integer> indexes = getNonEmptyRowsInColumn(col);
		
		for(int index:indexes)
		{
			sum+= row.get(index);
		}
		return sum;
		
	}
	
	public int getTotalSum()
	{
		List<Integer> rows = getNonEmptyRows();
		int sum = 0;
		
		Iterator<Integer> itr = rows.iterator();
		
		while(itr.hasNext())
		{
			sum += getRowSum(itr.next());
		}
		return sum;
	}
	
	public BigMatrix multiplyByConstant(int constant)
	{
		BigMatrix newMatrix = new BigMatrix();
		
		List<Integer> rows = getNonEmptyRows();
		Iterator<Integer> itr = rows.iterator();
		
		while(itr.hasNext())
		{
			Integer r = itr.next();
			List<Integer> cols = getNonEmptyColsInRow(r);
			Iterator<Integer> itrC = cols.iterator();
			
			while(itrC.hasNext())
			{
				Integer c = itrC.next();
				newMatrix.setValue(r,c,getValue(r,c)*constant);
			}
		}
		return newMatrix;
	}
	
	public BigMatrix addMatrix(BigMatrix other)
	{
		BigMatrix newMatrix = new BigMatrix();
		
		//Getting all the valid rows in both matrixes
		
		Set<Integer> validIndexes = new LinkedHashSet<Integer>();
		validIndexes.addAll(map.keySet());
		validIndexes.addAll(other.map.keySet());
		
		
		//Create an iterator using valid rows. Look at each row
		//Find all the valid indexes for a given row that apply to both matrixes
		//create an iterator using the valid indexes
		// Check if the row and col are in both matrixes and make a value with both values added together at the same index
		//if only my matrix has it, add the value from my matrix using the row and col
		//if only the other matrix has it, add the value from the other matrix using the row and col
		
		Iterator<Integer> itr = validIndexes.iterator();
		
		while(itr.hasNext())
		{
			int index = itr.next();
			 
			 Set<Integer> validCols = new LinkedHashSet<Integer>();
			
			 if(map.get(index) != null)
			 {
				 validCols.addAll(map.get(index).keySet());
				 if(other.map.get(index) != null)
				 {
					 validCols.addAll(other.map.get(index).keySet());
				 }
			 }
			 else
			 {
				 validCols = other.map.get(index).keySet();
			 }
			 
			 Iterator<Integer> itrC = validCols.iterator();
			 
			 
			 while(itrC.hasNext())
			 {
				 int indexC = itrC.next();
				 
				 if(getValue(index,indexC) != 0 && other.getValue(index, indexC) != 0)
				 {
					 newMatrix.setValue(index, indexC, getValue(index,indexC) + other.getValue(index, indexC));
				 }
				 else if(getValue(index,indexC) != 0 && other.getValue(index, indexC) == 0)
				 {
					 newMatrix.setValue(index, indexC, getValue(index,indexC));
				 }
				 else
				 {
					 newMatrix.setValue(index, indexC, other.getValue(index, indexC));
				 }
			 }
			  
		}
			
		return newMatrix;
	}
	
	public static void main(String[] args)
	{
		BigMatrix matrix = new BigMatrix();

	
		
		
		matrix.setValue(0, 4, 1);
		matrix.setValue(1,0,3);
		matrix.setValue(0, 2, 6);
		
		BigMatrix newMatrix = matrix.multiplyByConstant(2);
		newMatrix.setValue(4, 5, 39);

		
		BigMatrix addedMatrix = matrix.addMatrix(newMatrix);
		
		System.out.println(addedMatrix.getValue(0,4));
		System.out.println(addedMatrix.getValue(1,0));
		System.out.println(addedMatrix.getValue(0,2));
		System.out.println(addedMatrix.getValue(4,5));
		
		
		
		
	}
}
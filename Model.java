import java.awt.Color;
import java.util.*;

public class Model {
	// a model has an array of zones
	public Zone[] model;
	public static String[] file;
	public String[] names;
	
	public float max_s = - 1000;
	public float min_s = 1000;
	public float[] background = {0.0f, 0.0f, 0.0f, 0.0f};
	public int s_index = 0; // colour map variable where (x,y,z,s) => (0,1,2,3)
	public boolean orth = false;
	
	private int[] nodes;
	private int[] elements;
	private int[] start;
	private Scanner input = new Scanner(System.in);
	
	//POSITIONS
	public float  rotateX = 0.0f;
	public float  rotateY = 0.0f;
	public float  rotateZ = 0.0f;
	public float xPos = -15.0f;
	public float yPos = -3.0f;
	public float zPos = -15.0f;
	
	public Model(){
		file = ReadFile.getfile("SampleDataFile.txt");
		setArrays();
		createZones();
		
		System.out.println("Please enter a variable to colour map to: x,y,z or s");
	    
	    String temp = input.next();
	    
	    if(temp.equals("x")){
	    	s_index = 0;
	    }
	    else if(temp.equals("y")){
	    	s_index = 1;
	    }
	    else if(temp.equals("z")){
	    	s_index = 2;
	    }
	    else{
	    	s_index = 3;
	    }
	    
	    System.out.println("Please enter o for orthographic projection");
	    System.out.println("(Any other key for perspective projection)");
	    temp = input.next();
	    if(temp.equals("o")){
	    	orth = true;
	    }
	    
	    maxminColour();
		setBackgroundColor();
	}
	
	public void setBackgroundColor(){
		System.out.println("Please enter a background color");
		String temp = input.next();
		
		Color color = Color.black;
		
		if(temp.equals("green")){
			color = Color.green;
		}
		else if(temp.equals("blue")){
			color = Color.blue;
		}
		else if(temp.equals("cyan")){
			color = Color.cyan;
		}
		else if(temp.equals("magenta")){
			color = Color.magenta;
		}
		else if(temp.equals("gray")){
			color = Color.gray;
		}
		else if(temp.equals("yellow")){
			color = Color.yellow;
		}
		else if(temp.equals("orange")){
			color = Color.orange;
		}
		else if(temp.equals("pink")){
			color = Color.pink;
		}
		else if(temp.equals("red")){
			color = Color.red;
		}
		else if(temp.equals("white")){
			color = Color.white;
		}
		else if(temp.equals("darkGray")){
			color = Color.darkGray;
		}
		
		background[0] = (float)(color.getRed())/256;
		background[1] = (float)(color.getGreen())/256;
		background[2] = (float)(color.getBlue())/256;
		
	}
		
	public void maxminColour(){
		
		// gets the max and min x, y, z or s values based on s_index
		// used to map to a colour index
		for(int i = 0 ; i < model.length; i++){
			for (int j = 0; j < model[i].vertices.length; j ++){
				if(model[i].vertices[j][s_index] > max_s){
					max_s = model[i].vertices[j][s_index];
				}
				
				if(model[i].vertices[j][s_index] < min_s){
					min_s = model[i].vertices[j][s_index];
				}
				
			}
			
		}
	}
	
	public void setArrays(){
		// these arrays are used by createZones() to store the info needed to create a zone
		int count = 0;
		for(int i = 0; i < file.length; i++){
			int index_of_name = file[i].indexOf("T="); 
			
			if(index_of_name != -1){
				count ++;
			}
		}
		
		names = new String[count];
		nodes = new int[count];
		elements = new int[count];
		start = new int[count];
		model = new Zone[count];
		
		count = 0;
		for(int i = 0; i < file.length; i++){
			int index_of_name = file[i].indexOf("T="); 
			int index_of_nodes = file[i].indexOf("Nodes");
			int index_of_elements = file[i].indexOf("Elements");
			
			if(index_of_name != -1){
				start[count] = i + 1;
				if(file[i].charAt(index_of_name+3) != 'M'){
					names[count] = file[i].substring(index_of_name+3, index_of_name+7);
					
				}
				else{
					names[count] = file[i].substring(index_of_name+3, index_of_name+19);
				}
				
			}
			
			int last = 0;
			if(index_of_nodes != -1){
				for(int j = index_of_nodes+6; j < file[i].length(); j++){
					if(file[i].charAt(j) == ','){
						last = j;
						break;
					}
					
				}
				nodes[count] = Integer.parseInt(file[i].substring(index_of_nodes+6, last));
			}
			
			if(index_of_elements != -1){
				elements[count] = Integer.parseInt(file[i].substring(index_of_elements+9, file[i].length()));
				count ++;
			}
		}
		
	}

	public void createZones(){
		// creates the zones using the arrays from setArrays
		for(int i = 0; i < names.length; i++){
			Zone temp = new Zone(names[i], nodes[i], elements[i], start[i]);
			model[i] = temp;
		}
	}
}

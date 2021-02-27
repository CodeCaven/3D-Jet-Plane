
import java.util.Objects;

public class Zone {
	// zones hold the arrays for vertices and polygons 
	private int nodes;
	private int elements;
	private String name;
	private int start_index;
	
	public float[][] vertices;
	public int[][] polygons;
	
	
	public Zone(String z_name, int z_nodes, int z_elements, int z_start){
		name = z_name;
		nodes = z_nodes;
		elements = z_elements;
		start_index = z_start;
		vertices = new float[nodes][4];
		polygons = new int[elements][];
		
		createVertices();
		createPolygonList();
	}
	
	public void createVertices(){
		for(int i = start_index; i < start_index + nodes; i++){
			String[] file = Model.file;
			file[i].trim();
			
			
			int count = 0;
			for(String retval: file[i].split(" ")){
				if(!retval.isEmpty()){
					vertices[i-start_index][count] = convertSci(retval);
					count ++;
					
				}	
			}
		}
	}
	
	public float convertSci(String sci){
		// coverts from scintific notation
		int sign_index = sci.indexOf("E") + 1;
		int exp_index =  sci.indexOf("E") + 2;
		
		float number = Float.parseFloat(sci.substring(0, 7));
		String sign = sci.substring(sign_index, sign_index + 1);
		int exp = Integer.parseInt(sci.substring(exp_index));
		
		if(Objects.equals(sign, "+")){
			for(int i = 0; i < exp; i++){
				number = number * 10;
			}
		}
		else if(Objects.equals(sign, "-")){
			for(int i = 0; i < exp; i++){
				number = number / 10;
			}
			
		}
		return number;
	}
		
	public void createPolygonList(){
		for(int i = start_index + nodes; i < start_index + nodes + elements; i++){
			String[] file = Model.file;
			file[i].trim();
			int count = 0;
			for(String retval: file[i].split(" ")){
				if(!retval.isEmpty()){
					count ++;
				}	
			}
			
			polygons[i- start_index - nodes] = new int[count];
	
			count = 0;
			for(String retval: file[i].split(" ")){
				if(!retval.isEmpty()){
					polygons[i - start_index - nodes][count] = Integer.parseInt(retval);
					count ++;
				}	
			}
		
		}
			
	}	
	
}			
			
			
	

	
	


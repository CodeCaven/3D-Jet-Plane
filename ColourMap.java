
public class ColourMap {
	String[] colour_file;
	public float[][] colour_map;
	
	public ColourMap(){
		colour_file = ReadFile.getfile("CoolWarmFloat257.csv");
		createMap();
	}
	
	public void createMap(){
		int count;
		colour_map = new float[colour_file.length-1][4];
		for(int i = 1; i < colour_file.length; i++){
			count = 0;
			for (String retval: colour_file[i].split(",")) {
		         colour_map[i-1][count] =  Float.parseFloat(retval);
		         count++;
		      }
			
		}
	}
	
	public int colourIndex(float value, float min_s, float max_s){
		float range = max_s - min_s;
		int index;
		double temp;
		temp = 256 * ((value - min_s)/range);
		temp = Math.floor(temp);
		index = (int)temp;
		return index;
	}
}

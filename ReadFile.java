import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class ReadFile{

	private String path;
	
	public static String[] getfile(String input_file){
		String file_name = input_file;
		   String[] arrayLines = null;
		   
			
			try{
				ReadFile file = new ReadFile(file_name);
				arrayLines = file.openFile();
			}
			catch (IOException e){
				System.out.println(e.getMessage());
			}
		
		return arrayLines;
		
	}
	
	public ReadFile(String file_path){
		path = file_path;
	}
	
	public String[] openFile() throws IOException{
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		
		int numberOfLines = readlines();
		String textData[] = new String[numberOfLines];
		
		for(int i = 0; i < numberOfLines; i++){
			textData[i] = textReader.readLine();
		}
		
		textReader.close();
		return textData;
	}
		
	public int readlines() throws IOException{
		FileReader file_to_read = new FileReader(path);
		BufferedReader bf = new BufferedReader(file_to_read);
		
		String aLine;
		int numberOfLines = 0;
		
		while ((aLine = bf.readLine()) != null){
			numberOfLines ++;
		}
		bf.close();
		return numberOfLines;
		
	}
}

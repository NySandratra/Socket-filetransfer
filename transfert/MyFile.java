package server;
import java.util.*;
public class MyFile{
	private int id;
	private String name;
	private byte[] date;
	private String fileExtension;
	
	public MyFile(int id,String name,byte[] date,String fileExtension){
		this.id = id;
		this.name = name;
		this.date = date;
		this.fileExtension = fileExtension;
	}
	
	public void setId(int id){
		this.id = id;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setDate(byte[] date){
		this.date = date;
	}
	public void setFileExtension(String fileExtension){
		this.fileExtension = fileExtension;
	}
	
	public int getId(){
		return this.id ;
	}
	public String getName(){
		return this.name;
	}
	public byte[] getDate(){
		return this.date ;
	}
	public String getFileExtension(){
		return this.fileExtension ;
	}
}
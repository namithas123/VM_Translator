package Proj;
import java.io.*;
import java.util.Scanner;
public class Translator {
	public Translator(){
	}
	public Code code;
    public String command;
    public String type;
    public BufferedReader br;
	public static void main(String[] args)throws Exception{
		Translator vm= new Translator();
		Scanner in= new Scanner(System.in);
		System.out.println("Enter the name of file:");
		String filename=in.nextLine();
		File path=new File(filename+".vm");
	     vm.code = new Code(filename);
	     vm.parse(path); 
	     System.out.println("ASM code is build!");
	     vm.code.ending();
	     in.close();	

	}
	public void parse(File in)throws IOException{
	      br = new BufferedReader(new FileReader(in));
		while(code.morecommands(br)){
			String command = code.advance();
			String x;
			String y;
			String lab;
			String go;
			String iff;
			String ctype = code.Commandtype();
			if(ctype.equals("ARITHEMTIC")){
				code.writearithematic(command);
			}
			else if(ctype.equals("PUSH")||ctype.equals("POP")){
				x =command.split(" ")[1];
				y= command.split(" ")[2];;
			 	code.writePushpop(ctype,x,y);
				
			}
			else if(ctype.equals("LABEL")){
				lab=command.split(" ")[1];
				code.writeLabel(lab);
			}
			else if(ctype.equals("GOTO")){
				go=command.split(" ")[1];
				code.writeGoto(go);
			}
			else if(ctype.equals("IF")){
				iff=command.split(" ")[1];
				code.writeIf(iff);

			}
			
			
		}
		
	}
	 
}

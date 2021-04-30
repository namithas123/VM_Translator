package Proj;
import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
public class Code {
	public FileWriter  out;
	public String file;
	public String type;
	public String command;
    private int i = 1;
	public Code(String outFile)throws Exception{
		file = outFile;
		outFile = outFile + ".asm";
		File myob=new File(outFile);
		try{
			
			out = new FileWriter(myob);
			writeInit();
			}
		catch (IOException e) {
	            System.out.println("Error"+e);
	    }
	}
	public void ending()throws Exception{
		out.close();
		}
	public boolean morecommands(BufferedReader br){
		try{
			while((command=br.readLine())!=null){
			  if(command.isEmpty()){
				  continue;
			  }
			  type = command.trim().split(" ")[0];
			  command=command.trim().toLowerCase();
		      return true;
		      }
			br.close();
			}
		catch (IOException e) {
			System.out.println(e);
			}
		return false;
		}
	public String advance(){
		return command;
	  }
	public String Commandtype(){
		if(type.equals("push")){
			type="PUSH";
		  }
		else if(type.equals("pop")){
			  type="POP";
		  }
		else if (type.equals("label")) {
	            type = "LABEL";
	        }
		else if (type.equals("goto")) {
	            type = "GOTO";
	        }
		else if (type.equals("if-goto")) {
	            type = "IF";
	        }
		else
			type="ARITHEMTIC";
		return type;
		}
   public void writeLabel(String label)throws IOException{
	   writecomment("label"+label);
	   out.write("("  + label.toUpperCase() + ")\n");
	   }
   public void writeGoto(String lab)throws IOException{
	   writecomment("goto"+lab);
	   out.write("@"+lab.toUpperCase()+"\n");
	   out.write("0;JMP\n");
	  }
   public void writeIf(String com)throws IOException{
	   writecomment("if-goto " + com);
       out.write("@SP\n");
       out.write("M=M-1\n");
       out.write("A=M\n");
       out.write("D=M\n");
       out.write("@" + com.toUpperCase()+"\n");
       out.write("D;JNE\n");
       }
   public void writePushpop(String type, String a1,String a2){
	   String com;
	   if(type.equals("POP"))
		   com="pop";
	   else
		   com="push";
	   writecomment(com + " " + a1 + " " + a2);
	   String segm="";
	   if(a1.equals("argument"))
		   segm="ARG";
	   else if(a1.equals("local")){
		   segm="LCL";
		   
		  }
	   else if(a1.equals("temp"))
		   segm="5";
	   try {
		   if(!(a1.equals("constant") || a1.equals("static") || a1.equals("pointer"))){
			   if(a1.equals("temp"))
				   out.write("@0\n");
			   else
				   out.write("@" + segm+"\n");
			   if (a1.equals("temp")) 
	                    out.write("D=A\n");
			   else
				   out.write("D=M\n");
			   if(a1.equals("temp"))
				   out.write("@" + segm+"\n");
			   else
				   out.write("@" + a2+"\n");
			   out.write("D=D+A\n");
			   if(type.equals("POP")){
				   out.write("@R13\n");
				   out.write("M=D\n");
				   }
			   else
				   out.write("A=D\n");
			   }
		   if (type.equals("POP")) {
			   if (a1.equals("static")){
				   out.write("@" + file + "." + a2+"\n");
				   out.write("D=A\n@R13\nM=D\n@SP\nAM=M-1\nD=M\n@R13\nA=M\n");
				   }
			   else{
				   out.write("@SP\n");
	               out.write("M=M-1\n");
	               out.write("A=M\n");
	               out.write("D=M\n");
	               if(a1.equals("pointer")) {
	            	   if (a2.equals("0")) {
	            		   out.write("@THIS\n");
	            		   }
	            	   else {
	                        out.write("@THAT\n");
	                        }
	            	   }
	               else{
	            	   out.write("@R13\n");
	                   out.write("A=M\n");
	                   }
	               }
			   out.write("M=D\n");
			   }
		   else {
			   if (a1.equals("pointer")) {
				   if (a2.equals("0")) {
					   out.write("@THIS\n");
					   }
				   else 
					   out.write("@THAT\n");
				   out.write("D=M\n");
	                }
			   else if (a1.equals("static")) {
				   out.write("@" +file + "." + a2+"\n");
				   out.write("D=M\n");
				   }
			   else if (a1.equals("constant")) {
				   out.write("@" + a2+"\n");
				   out.write("D=A\n");
				   }
			   else{
				   out.write("D=M\n");
				   }
			   out.write("@SP\n");
			   out.write("A=M\n");
	           out.write("M=D\n");
	           out.write("@SP\n");      
	           out.write("M=M+1\n");
	           }
		   }
	   catch (IOException e) {
		   System.out.println(e);
		   }
	   }
   public void writeInit() {
       try {
           // SP = 256
           out.write("@256\n");
           out.write("D = A\n");
           out.write("@SP\n");
           out.write("M = D\n");
       }
       catch (IOException e) {
           System.out.println(e);
       }
   }
   public void writearithematic(String arg) throws IOException{
	   writecomment(arg);
	   if(!(arg.equals("not")||arg.equals("neg"))){
		   out.write("@SP\n");
		   out.write("AM=M-1\n");
		   out.write("D=M\n");
           out.write("A=A-1\n");}
           if(arg.equals("not")||arg.equals("neg")){
        	   if(arg.equals("neg"))
        		   out.write("D=0\n");
        	   out.write("@SP\n");
        	   out.write("A=M-1\n");
          }
          if(arg.equals("add")){
        	  out.write("M=D+M\n");
          }
          else if(arg.equals("sub")){
        	  out.write("M=M-D\n");     
          }
          else if(arg.equals("and")){
        	  out.write("M=M&D\n");
          }
          else if(arg.equals("or")){
        	  out.write("M=M|D\n");
          }
          else if (arg.equals("neg")) {
              out.write("M=D-M\n");
          }
          else if (arg.equals("not")) {
              out.write("M=!M\n");
          }
          else if (arg.equals("eq") || arg.equals("gt") || arg.equals("lt")) {
              out.write("D=M-D\n");
              out.write("@IFFALSE" + Integer.toString(i)+"\n");
              if (arg.equals("eq")) {
                  out.write("D;JNE\n");
              }
              else if (arg.equals("gt")) {
                  out.write("D;JLE\n");
              }
              else if (arg.equals("lt")) {
                  out.write("D;JGE\n");
              }
              out.write("@SP\n");
              out.write("A=M-1\n");
              out.write("M=-1\n");
              out.write("@END" + Integer.toString(i)+"\n");
              out.write("0;JMP\n");
              out.write("(IFFALSE" + Integer.toString(i) + ")\n");
              out.write("@SP\n");
              out.write("A=M-1\n");
              out.write("M=0\n");
              out.write("(END" + Integer.toString(i) + ")\n");
              i++;
              }
          }
   public void writecomment(String line){
		  try{
			  out.write("//"+command+"\n");
		  }
		  catch (IOException e) {
	            System.out.println(e);
	        }
	  }
}

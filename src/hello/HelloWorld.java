package hello;

public class HelloWorld {

	public int intValue(){
		return 1;
	}
	
	public void print(){
		System.out.println(intValue());
	}
	
	public static void main(String... args){
		new HelloWorld().print();
	}
	
}

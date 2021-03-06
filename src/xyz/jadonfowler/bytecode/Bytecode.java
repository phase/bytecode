/**
 * bytecode was made by Jadon Fowler.
 * This file is licensed under the MIT License.
 */
package xyz.jadonfowler.bytecode;

import java.lang.reflect.Method;

import jdk.internal.org.objectweb.asm.util.ASMifier;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author Jadon "Phase" Fowler on Apr 6, 2015
 */
public class Bytecode implements Opcodes{

	static ClassWriter cw = new ClassWriter(0);
	static MethodVisitor mv;
	
	public static byte[] compile(String name){
		
		
		/*
		 * Type    | Descriptor
		 * void    | V
		 * boolean | Z
		 * char    | C
		 * byte    | B
		 * short   | S
		 * int     | I
		 * float   | F
		 * long    | J
		 * double  | D
		 * type[]  | [<type>
		 * class   | L<class>;
		 */
		
		/*
		 * Method Discriptors
		 *  (arguments) return
		 *  
		 *  public static void main(String[] a){}
		 *  ([Ljava/lang/String;)V
		 *  
		 *  public String test(int i, boolean b){}
		 *  (IZ)Ljava/lang/String; - No separation between types
		 *  
		 */
		
		cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, "java/lang/Integer", null, "java/lang/Number", new String[] {"java/lang/Comparable"});
		
//		{ //Constructor?
//			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
//			mv.visitCode();
//			mv.visitVarInsn(ALOAD, 0);//Load something.. maybe loading constructor?
//			//                 How to invoke | Super class    | Method name | (args)returnType
//			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V"); 
//			mv.visitInsn(RETURN);
//			mv.visitMaxs(1, 1);
//			mv.visitEnd();
//		}
//		{
//			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
//			mv.visitCode();
//			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//			mv.visitLdcInsn("main: " + name);
//			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
//			mv.visitInsn(RETURN);
//			mv.visitMaxs(2, 1);
//			mv.visitEnd();
//		}
//		
//		{
//			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "test", "()V", null, null);
//			mv.visitCode();
//			mv.visitInsn(ICONST_0);
//			mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
//			mv.visitMethodInsn(INVOKESTATIC, "hello/HelloWorld", "main", "([Ljava/lang/String;)V");
//			mv.visitInsn(RETURN);
//			mv.visitMaxs(1, 0);
//			mv.visitEnd();
//		}
		
		{
			mv = cw.visitMethod(ACC_PUBLIC, "print", "()V", null, null);
			mv.visitCode();
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL , "java/lang/Integer", "intValue", "()I");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V");
			mv.visitInsn(RETURN);
			mv.visitMaxs(2, 1);
			mv.visitEnd();
		}
		
		cw.visitEnd();
		
		return cw.toByteArray();
	}
	
	public static void createMethod(int modifiers, String name, String args, String returnType){
		if(args == null) args = "";
		if(returnType == null) returnType = "V";
		mv = cw.visitMethod(modifiers, name, "("+args+")"+returnType, null, null);
	}
	
	public static class DynamicClassLoader extends ClassLoader{
		public Class<?> define(String className, byte[] bytecode){
			return super.defineClass(className, bytecode, 0, bytecode.length);
		}
	}
	
	public static void main(String[] args) throws Exception {
//		DynamicClassLoader loader = new DynamicClassLoader();
//		Class<?> helloWorldClass = loader.define("hello.HelloWorld", compile("Test"));
//		Method m = helloWorldClass.getMethod("main", String[].class);
//		m.invoke(null, (Object) new String[] {});
//		Method m2 = helloWorldClass.getMethod("test");
//		m2.invoke(null);
		DynamicClassLoader loader = new DynamicClassLoader();
		loader.define("java.lang.Integer", compile("Test"));
	}

}

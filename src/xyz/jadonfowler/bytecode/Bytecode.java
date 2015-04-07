/**
 * bytecode was made by Jadon Fowler.
 * This file is licensed under the MIT License.
 */
package xyz.jadonfowler.bytecode;

import java.lang.reflect.Method;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author Jadon "Phase" Fowler on Apr 6, 2015
 */
public class Bytecode implements Opcodes{

	public static byte[] compile(String name){
		ClassWriter cw = new ClassWriter(0);
		MethodVisitor mv;
		
		cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "hello/HelloWorld", null, "java/lang/Object", null);
		
		cw.visitSource("HelloWorld.java", null);
		
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(4, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", true);
			mv.visitInsn(RETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lhello/HelloWorld;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(7, l0);
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			mv.visitLdcInsn(String.format("Hello, %s!", name));
			mv.visitMethodInsn(INVOKESPECIAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", true);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(8, l1);
			mv.visitInsn(RETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("args", "[Ljava/lang/String;", null, l0, l2, 0);
			mv.visitMaxs(2, 1);
			mv.visitEnd();
		}
		
		cw.visitEnd();
		
		return cw.toByteArray();
	}
	
	public static class DynamicClassLoader extends ClassLoader{
		public Class<?> define(String className, byte[] bytecode){
			return super.defineClass(className, bytecode, 0, bytecode.length);
		}
	}
	
	public static void main(String[] args) throws Exception {
		DynamicClassLoader loader = new DynamicClassLoader();
		Class<?> helloWorldClass = loader.define("hello.HelloWorld", compile("Test"));
		Method m = helloWorldClass.getMethod("main", String[].class);
		m.invoke(null, (Object) new String[] {});
	}

}

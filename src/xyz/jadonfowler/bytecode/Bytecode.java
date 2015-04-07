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
		
		cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, "hello/HelloWorld", null, "java/lang/Object", null);
		
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
			mv.visitCode();
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			mv.visitLdcInsn("main: " + name);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
			mv.visitInsn(RETURN);
			mv.visitMaxs(2, 1);
			mv.visitEnd();
		}
		
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "test", "()V", null, null);
			mv.visitCode();
			mv.visitInsn(ICONST_0);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
			mv.visitMethodInsn(INVOKESTATIC, "hello/HelloWorld", "main", "([Ljava/lang/String;)V");
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 0);
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
		Method m2 = helloWorldClass.getMethod("test");
		m2.invoke(null);
	}

}

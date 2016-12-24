/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sruds;

/**
 *
 * @author UDAY
 */
import java.io.*;
import javax.swing.JTextArea;
 
	class TextAreaOutputStream extends OutputStream {

	private final JTextArea textArea;
	private final StringBuilder sb = new StringBuilder();

	/*TextAreaOutputStream(){
        textArea=null;}*/
        TextAreaOutputStream(final JTextArea textArea) {
		this.textArea = textArea;
	}

	@Override
	public void flush(){ 
		System.out.println("FLUSHED!");
	}
	
	@Override
	public void close(){ }

	@Override
	public void write(int b) throws IOException {
                
		/*if (b == '\r')
			return;*/
		
		if (b == '\n') {
			textArea.append(sb.toString());
			sb.setLength(0);
		}
		
		sb.append((char)b);
                //textArea.append(sb.toString());
	}
}

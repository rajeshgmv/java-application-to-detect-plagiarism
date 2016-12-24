/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sruds;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author UDAY
 */
public final class Text {
  public String []text;
  public String []tag;  
  public int strlength;
    Text(String file) throws IOException, Exception
    {
        format(file);
         strlength=text.length;
        tag=new String[strlength];
         tagger();    
    }
    Text(){this.strlength=0;}
    void getSize()
    {
         
        strlength=text.length;
       //System.out.println();
        //System.out.println(strlength);
    }
    void format(String file) throws IOException, Exception
    {
         BufferedReader br = new BufferedReader(new FileReader(file));
    int i;
         try {
        StringBuilder sb = new StringBuilder();
        String []words;
        String line = br.readLine();
        while(line!=null)
        {
        //String regex="( an | a | the )";
       // line=line.replaceAll(regex," ");
       // regex="\\W+";                                                 // "//W+" stands for multiple whitespaces
        //line=line.replaceAll(regex," ");
         
            line=POSTagger.main(line);
            words=line.split(" ");
        
        for( i=0;i<words.length;i++)
         {
            
            sb.append(words[i]);
            sb.append(" ");
         }
        line = br.readLine();
        }
        String temp=sb.toString();
    text=temp.split(" ");
     

/*   for( i=0;i<text.length;i++)
       System.out.println(text[i]+" ");*/
    }
        finally {
        br.close();
    }
    
    }
    void tagger()
    {
    
    for(int i=0;i<text.length;i++)
        {
        tag[i]=text[i].substring(text[i].indexOf("/")+1);
        text[i]=text[i].replaceAll(text[i].substring(text[i].indexOf("/")),"");
     //   System.out.println(text[i]);
        }
    }
}

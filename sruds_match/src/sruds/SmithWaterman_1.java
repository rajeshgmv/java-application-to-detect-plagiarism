
package sruds;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;


public final class SmithWaterman_1 {

    private Text query, template;
    //
    static final int MATCH = 1;
    static final int MISMATCH = -1;
    static final int GAP = -1;
    //
    static final byte LEFT = 1;
    static final byte UP = 2;
    static final byte DIAG = 4;
    static final byte START = 0;
    //
    private int[][] score, M;
    private byte[][] pointer;
    static final int threshold = 5;
    private int count;
    private double similarity;
    //
    //ArrayList<Point> getPoints =new ArrayList<Point>();
    LinkedList<Point> getPoints = new LinkedList<Point>();
    StringBuilder query_match = new StringBuilder();
    StringBuilder template_match = new StringBuilder();
    Text query_result = new Text();
    Text template_result = new Text();
    
    public SmithWaterman_1(String q, String t) throws IOException, Exception {
        this.query = new Text(q);
        this.template = new Text(t);
        score = new int[query.strlength + 1][template.strlength + 1];
        pointer = new byte[query.strlength + 1][template.strlength + 1];
        M = new int[query.strlength + 1][template.strlength + 1];
        
        populate();
    }

    void populate() {
        int i, j;

        for (i = 0; i <= query.strlength; i++) {
            score[i][0] = 0;
            pointer[i][0] = START;
        }
        for (j = 0; j <= template.strlength; j++) {
            score[0][j] = 0;
            pointer[0][j] = START;
        }

        for (i = 1; i <= query.strlength; i++) {
            for (j = 1; j <= template.strlength; j++) {

                if (query.text[i - 1].equals(template.text[j - 1])) {
                    score[i][j] = score[i - 1][j - 1] + MATCH;
                    pointer[i][j] = DIAG;
                } else {
                    int diagScore = score[i - 1][j - 1] + MISMATCH;
                    int upScore = score[i - 1][j] + GAP;
                    int leftScore = score[i][j - 1] + GAP;

                    score[i][j] = Math.max(diagScore, Math.max(upScore,
                            Math.max(leftScore, 0)));
                    pointer[i][j] = 0;

                    // find the directions that give the maximum scores.
                    // the bitwise OR operator is used to record multiple
                    // directions.
                    if (diagScore == score[i][j]) {
                        pointer[i][j] = DIAG;
                    }
                    if (leftScore == score[i][j]) {
                        pointer[i][j] = LEFT;
                    }
                    if (upScore == score[i][j]) {
                        pointer[i][j] = UP;
                    }
                    if (0 == score[i][j]) {
                        pointer[i][j] = START;
                    }

                }
                /*irving cutoff*/
                if (score[i][j] == 0) {
                    M[i][j] = 0;
                } else if (query.text[i - 1].equals(template.text[j - 1])) {
                    M[i][j] = Math.max(M[i - 1][j - 1], score[i - 1][j - 1]);
                } else {
                    M[i][j] = Math.max(Math.max(score[i][j - 1], M[i][j - 1]), Math.max(Math.max(score[i - 1][j - 1], M[i - 1][j - 1]), Math.max(score[i - 1][j], M[i - 1][j])));
                }
                if ((M[i][j] - score[i][j]) >= threshold) {
                    score[i][j] = 0;
                    M[i][j] = 0;
                }

            }
        }
        /* for ( i = 1; i <= query.strlength; i++) {
        System.out.print(i + "-");
        for ( j = 1; j <= template.strlength; j++) {
        System.out.print("-" + (j - 1));
        System.out.print("  (" + score[i][j] + ")");
        }
        System.out.println();
        }*/
        //irving_cutoff();
        traceback();
    }

  
    void traceback() {
        for (int i = 1; i<=query.strlength; i++) {
            for (int j = 1; j <=template.strlength; j++) {
                if (score[i][j] > threshold && score[i][j] > score[i - 1][j - 1]
                        && score[i][j] > score[i - 1][j] && score[i][j] > score[i][j - 1] && score[i][j] > M[i][j]) {
                    if (i == query.strlength || j == template.strlength || score[i][j] > score[i + 1][j + 1]) {
                        // should be lesser than prev maxScore
                        
                        getPoints.add(new Point(i, j));

                    }
                }
            }
        }

        /*  for (int i = 1; i <= query.strlength; i++) {
        System.out.print(i + "-");
        for (int j = 1; j <= template.strlength; j++) {
        System.out.print("-" + (j - 1));
        System.out.print("  (" + score[i][j] + ")");
        }
        System.out.println();
        }*/
        getAlignments();
    }

    void getAlignments() {

        Point temp = new Point();
        while (!(getPoints.isEmpty())) {
            temp = getPoints.pop();
           // System.out.println(temp.i+"   "+temp.j);
            count = 0;
            getAlignments(temp.i, temp.j, "", "");
            //System.out.println("hi");
        }

    }

    void getAlignments(int i, int j, String s1, String s2) {
        
        int k;
        if ((score[i][j] == 0)&&(count > threshold)) {
            	    
            query_match.append(s1);
            query_match.append("\t");
            template_match.append(s2);
            template_match.append("\t");
             return;
        } 
        else {
            if ((pointer[i][j] == LEFT)) {
               score[i][j]=0;
              
                count++;
                getAlignments(i, j - 1, (" " + "_" + s1), (" " + template.text[j - 1] + s2));

            }
            if ((pointer[i][j] == UP)) {
                score[i][j]=0;
              
                count++;
                getAlignments(i - 1, j, " " + query.text[i - 1] + s1, " " + "_" + s2);

            }
            if ((pointer[i][j] == DIAG)) {
                score[i][j]=0;
              
                count++;
                getAlignments(i - 1, j - 1, " " + query.text[i - 1] + s1, " " + template.text[j - 1] + s2);

            }
            return;
        }  
    }

    void printAlignments() {
        /*for (int i = 1; i <= query.strlength; i++) {
        System.out.print(i + "-");
        for (int j = 1; j <= template.strlength; j++) {
        System.out.print("-" + (j - 1));
        System.out.print("  (" + score[i][j] + ")");
        }
        System.out.println();
        }*/

        String query_temp = query_match.toString();
        String template_temp = template_match.toString();
               
        query_result.text = query_temp.split(" ");
        template_result.text = template_temp.split(" ");
        query_result.getSize();
        template_result.getSize();
        for (int i = 0; i < query_result.strlength; i++) {

            if (query_result.text[i].length() < template_result.text[i].length()) {
                for (int j = 0; j < (template_result.text[i].length() - query_result.text[i].length()); j++) {
                    query_result.text[i] += " ";
                }

            } else {
                for (int j = 0; j < (query_result.text[i].length() - template_result.text[i].length()); j++) {
                    template_result.text[i] += " ";
                }

            }

        }
        for (int i = 0; i < query_result.strlength; i++) {
            System.out.print(query_result.text[i] + "\t");

        }
        System.out.println();
        for (int i = 0; i < template_result.strlength; i++) {
            System.out.print(template_result.text[i] + "\t");
         
          }
            query_result.getSize();
            similarity=((double)query_result.strlength/query.strlength)*100;
            System.out.println();
            System.out.println();
            System.out.printf("The Percentage similarity is %.2f",similarity);
            System.out.println();
    }

    public static void main(String[] args) throws IOException, Exception {
        // TODO  code application logic here
        Scanner s = new Scanner(System.in);
        String query=null, template=null;
      //  query=args[0];
      //  template=args[1];
       System.out.println("Enter the query file name : ");
        query = s.next();
        System.out.println("Enter the template file name : ");
        template = s.next();
       SmithWaterman sw = new SmithWaterman(query, template);
        sw.printAlignments();
    }
}

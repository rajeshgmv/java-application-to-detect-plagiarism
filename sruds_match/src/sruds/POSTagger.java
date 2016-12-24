package sruds;


/*
 * POSTagger.java
 * * 
 */

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * This class is a wrapper, calling the University Tokyo or the Standford
 * POS tagger. The taggers however are not the part of this software and 
 * should be downloaded seperately from the following hyperlink:-
 * 
 * http://www-tsujii.is.s.u-tokyo.ac.jp/~tsuruoka/postagger/
 * 
 * The class works as a proxy agent by making a Runtime execution call to the 
 * executable binary of the Taggers. While the Tokyo tagger is a native 
 * executable, the Stanford tagger is a java based archive offering direct api
 * call. The Iization takes some time as the taggers loads their models. 
 * Further on the output, input and the eroor streams are extracted. The tokyo
 * tagger writes the tagged version to its output stream, of any sentence 
 * printed to its input stream. Stanford engine, as said earlier has a tagger 
 * object where a model file can be passed directly and a tagging method can be
 * called. The class is finalised as 
 * 
 * The following is the Penn Treebank tag set for English, given in 
 * [Brill, 1992] 
 *	1.	CC	Coordinating conjunction
 *	2.	CD	Cardinal number
 *	3.	DT	Determiner
 *	4.	EX	Existential there
 *	5.	FW	Foreign word
 *	6.	IN	Preposition or subordinating conjunction
 *	7.	JJ	Adjective
 *	8.	JJR	Adjective, comparative
 *	9.	JJS	Adjective, superlative
 *	10.	LS	List item marker
 *	11.	MD	Modal
 *	12.	NN	Noun, singular or mass
 *	13.	NNS	Noun, plural
 *	14.	NP	Proper noun, singular
 *	15.	NPS	Proper noun, plural
 *	16.	PDT	Predeterminer
 *	17.	POS	Possessive ending
 *	18.	PP	Personal pronoun
 *	19.	PP$	Possessive pronoun
 *	20.	RB	Adverb
 *	21.	RBR	Adverb, comparative
 *	22.	RBS	Adverb, superlative
 *	23.	RP	Particle
 *	24.	SYM	Symbol
 *	25.	TO	to
 *	26.	UH	Interjection
 *	27.	VB	Verb, base form
 *	28.	VBD	Verb, past tense
 *	29.	VBG	Verb, gerund or present participle
 *	30.	VBN	Verb, past participle
 *	31.	VBP	Verb, non-3rd person singular present
 *	32.	VBZ	Verb, 3rd person singular present
 *	33.	WDT	Wh-determiner
 *	34.	WP	Wh-pronoun
 * 	35.	WP$	Possessive wh-pronoun
 *	36.	WRB	Wh-adverb    
 */
public final class POSTagger {
    
    private static POSTagger singletonTagger = null;// Singleton reference
    private MaxentTagger stanfordTagger = null;     // Standford tagger object    
   
    
    /**
     * The protected Constructor helps to implement the singleton pattern here.
     * The real tagger program will be spawed in a seperate process, and 
     * multiple instances will make duplicate processes while one can be used by 
     * all the querying agents. Thus the only way to instantiate this calss is 
     * throught the getInstance method. This mechanism makes sure a strict 
     * singleton pattern in followed.
     * 
     * @param tagger
     * 
     */
    protected POSTagger() throws Exception  {
        
        // Initialise the Standford tagger engine......
        stanfordTagger =
                new MaxentTagger("bidirectional-wsj-0-18.tagger");            
    }
    
    /**
     * Returns an Instance of the POSTagger with a specified tagging engine.
     * 
     * @param tagger
     * @return
     */
    public static POSTagger getInstance() {
        if(singletonTagger == null) try {
            singletonTagger = new POSTagger();
        } catch (Exception ex) {
            ex.printStackTrace();
        }        
        return singletonTagger;          
    }
        
    
    /**
     * The method tags a Sentence using the Stanford Tagger...
     * 
     * @param sentence
     * @return Tagged Sentence
     * @throws java.lang.Exception
     */ 
    //@SuppressWarnings("static-access")
    protected String tagSentence(String sentence) {
        String taggedSentence = null;
       
        try {
            taggedSentence = MaxentTagger.tagString(sentence);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return taggedSentence;
    }
    
    
    
    /**
     * Sample main method to demonstarte the proxy Postagger and to compare 
     * various POS Tagging Engines, Currently it uses the Tokyo University and 
     * the Standford University taggers.
     * 
     * @param args
     * @throws java.lang.Exception
     */
    public static String main(String args) throws Exception {

        POSTagger tagger = POSTagger.getInstance();
        String t=tagger.tagSentence(args);
        return t;
        //System.out.println(t);
    }
}

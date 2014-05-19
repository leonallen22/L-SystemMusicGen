/**
 * Generate a production with a context-free L-system.
 * @author		Harry Allen
 */

import java.util.ArrayList;

public class LSystem {

	protected	ArrayList<String>	m_alphabet;
	protected	String				m_axiom;
	protected	ArrayList<String>	m_rule;
	
	protected	ArrayList<String[]> defaultAlphabets;
	protected	ArrayList<String> defaultAxioms;
	protected	ArrayList<String[]> defaultRules;
	protected	String []	m_defAlphabet1	= {"A", "B"};
	protected	String []	m_defAlphabet2	= {"A", "B"};
	protected	String []	m_defAlphabet3	= {"A", "B"};
	protected	String []	m_defAlphabet4	= {"A"};
	protected	String []	m_defAlphabet5	= {"A", "B", "C", "D"};
	protected	String		m_defAxiom1		= "A";
	protected	String		m_defAxiom2		= "A";
	protected	String		m_defAxiom3		= "A";
	protected	String		m_defAxiom4		= "A";
	protected	String		m_defAxiom5		= "A C A";
	protected	String []	m_defRule1		= {"g + g - g B", "+ g - g B"};
	protected	String []	m_defRule2		= {"- B g + A g A + g B -", "+ A g - B g B - g A +"};
	protected 	String [] 	m_defRule3 		= {"A + g - g B - g + g + g -", "- g + g A - g +"};
	protected 	String [] 	m_defRule4 		= {"g + g g - g + g g - g + g g g - g - g g g + g - g g + g - g g + g"};
	protected	String []	m_defRule5		= {"g - g B g + A g A + g B g - g", "+ A g - B g B - g A +", "+ C g + g - g - C g C - g - g + g C +", "- g + g A - g +"};
	protected	String		m_tree;
	
	/** Constructor. */
	public LSystem()
	{	
		defaultAlphabets = new ArrayList<String[]>();
		defaultAxioms 	 = new ArrayList<String>();
		defaultRules 	 = new ArrayList<String[]>();
		m_alphabet		 = new ArrayList<String>();
		m_rule	   		 = new ArrayList<String>();
		
		defaultAlphabets.add(new String[]{"A", "B"});
		defaultAlphabets.add(new String[]{"A", "B"});
		defaultAlphabets.add(new String[]{"A", "B"});
		defaultAlphabets.add(new String[]{"A"});
		defaultAlphabets.add(new String[]{"A", "B", "C", "D"});
		defaultAxioms.add("A");
		defaultAxioms.add("A");
		defaultAxioms.add("A");
		defaultAxioms.add("A");
		defaultAxioms.add("A C A");
		defaultRules.add(new String[]{"g + g - g B", "+ g - g B"});
		defaultRules.add(new String[]{"- B g + A g A + g B -", "+ A g - B g B - g A +"});
		defaultRules.add(new String[]{"A + g - g B - g + g + g -", "- g + g A - g +"});
		defaultRules.add(new String[]{"g + g g - g + g g - g + g g g - g - g g g + g - g g + g - g g + g"});
		defaultRules.add(new String[]{"g - g B g + A g A + g B g - g", "+ A g - B g B - g A +", "+ C g + g - g - C g C - g - g + g C +", "- g + g A - g +"});
		
		for(String x : m_defAlphabet2)
			m_alphabet.add(x);
		
		m_axiom	= m_defAxiom2;
		
		for(String x : m_defRule2)
			m_rule.add(x);
		
		m_tree = "";
	}
	
	/**
	 * Generate the tree by iterating through the
	 * specified number of times, expanding symbols using
	 *  the rules where applicable.
	 */
	public void iterate(int maxLength)
	{		
		m_tree = new String(m_axiom);
		
		for(int k=0 ; k < maxLength ; ++k)
		{
			String[] Tree = m_tree.split("");
			StringBuffer newTree = new StringBuffer();
			
			for(int i=0 ; i < Tree.length ; ++i)
			{
				for(int j=0 ; j < m_alphabet.size() ; ++j)
				{
					String alpha = m_alphabet.get(j);
					
					if(Tree[i].equals(alpha))
					{
						Tree[i] = m_rule.get(j);
					}
				}
			}
			
			for(String str : Tree)
			{
				newTree.append(str);
			}
			
			m_tree = newTree.toString();
		}
	}
	
	/** Return the current state of the L-System	 */
	public String getTree()
	{
		return m_tree;
	}
	
	/** Return the set of production rules of the L-System */
	public String getRules()
	{
		StringBuffer prodrules = new StringBuffer();
		
		for(int i=0 ; i < m_alphabet.size() ; ++i)
		{
			prodrules.append("\t\t" + m_alphabet.get(i) + ": " + m_rule.get(i) + "\r\n");
		}
		
		return prodrules.toString();
	}
	
	/** Return the alphabet of the L-System */
	public String getAlphabet()
	{
		StringBuffer alpha = new StringBuffer();
		
		for(String c : m_alphabet)
		{
			alpha.append(String.valueOf(c) + " ");
		}
		
		return alpha.toString();
	}
	
	/** Return the axiom for the L-System */
	public String getAxiom()
	{
		return m_axiom;
	}
	
	public ArrayList<String[]> getDefaultAlphabets()
	{
		return defaultAlphabets;
	}
	
	public ArrayList<String> getDefaultAxioms()
	{
		return defaultAxioms;
	}
	
	public ArrayList<String[]> getDefaultRules()
	{
		return defaultRules;
	}
	
	public void setAxiom(String axiom)
	{
		m_axiom = axiom;
	}
	
	public void setAlphabet(ArrayList<String> newalphabet)
	{
		m_alphabet = newalphabet;
	}
	
	public void setRules(ArrayList<String> newrules)
	{
		m_rule = newrules;
	}
	
	public void setAlphabetDef(int defnum)
	{
		m_alphabet.clear();
		
		for(String x : defaultAlphabets.get(defnum))
		{
			m_alphabet.add(x);
		}
	}
	
	public void setAxiomDef(int defnum)
	{
		m_axiom = defaultAxioms.get(defnum);
	}
	
	public void setRulesDef(int defnum)
	{
		m_rule.clear();
		
		for(String x : defaultRules.get(defnum))
		{
			m_rule.add(x);
		}
	}
}
import java.util.ArrayList;

/**
 * Generate a production with a context-free L-system.
 * @author		Harry Allen
 */
public class LSystem {

	private	ArrayList<String>	m_alphabet;
	private	String				m_axiom;
	private	ArrayList<String>	m_rule;
	private	ArrayList<String[]> defaultAlphabets;
	private	ArrayList<String> 	defaultAxioms;
	private	ArrayList<String[]> defaultRules;
	private	String		m_tree;
	
	/**
	 * Default Constructor. Defaults to Preset L-System #1.
	 */
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
		defaultAlphabets.add(new String[]{"A", "B", "C", "D"});
		defaultAlphabets.add(new String[]{"B", "L", "R", "T"});
		defaultAlphabets.add(new String[]{"A", "B"});
		defaultAlphabets.add(new String[]{"A", "B", "g"});
		defaultAlphabets.add(new String[]{"A", "B", "g"});
		defaultAxioms.add("A");
		defaultAxioms.add("A B");
		defaultAxioms.add("A");
		defaultAxioms.add("A C A");
		defaultAxioms.add("R @ @ B");
		defaultAxioms.add("A");
		defaultAxioms.add("A");
		defaultAxioms.add("A");
		defaultRules.add(new String[]{"g + g - g B", "+ g - g B"});
		defaultRules.add(new String[]{"- B g + A g A + g B -", "+ A g - B g B - g A +"});
		defaultRules.add(new String[]{"A + g - g B - g + g + g -", "- g + g A - g +"});
		defaultRules.add(new String[]{"g - g B g + A g A + g B g - g", "+ A g - B g B - g A +", "+ C g + g - g - @ @ D g D - g - g + g C +", "- g + g @ A - g +"});
		defaultRules.add(new String[]{"@ @ T L - B + + B", "- g + g - g", "@ @ R", "T g"});
		defaultRules.add(new String[]{"- g g g g g + g g f g g B", " - g g g g + g + g g - g + g g g - g A"});
		defaultRules.add(new String[]{"- g g g g g + g g f g g B", " - g g g g + g + g g - g + g g g - g A", "- g"});
		defaultRules.add(new String[]{"g g - g + g + g - g - g + g g - g g + B", "g g - g + g g + g - g g f g + g - g g A", "- g"});
		
		for(String x : defaultAlphabets.get(0))
			m_alphabet.add(x);
		
		m_axiom	= defaultAxioms.get(0);
		
		for(String x : defaultRules.get(0))
			m_rule.add(x);
		
		m_tree = "";
	}
	
	/**
	 * Generate the tree by iterating through the specified number of times, expanding symbols using the rules where applicable.
	 * @param maxLength maximum number of iterations
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
						Tree[i] = m_rule.get(j);
				}
			}
			
			for(String str : Tree)
				newTree.append(str);
			
			m_tree = newTree.toString();
		}
	}
	
	/**
	 * @return The current state of the L-System.
	 */
	public String getTree()
	{
		return m_tree;
	}
	
	/**
	 * @return The set of production rules of the L-System as an ArrayList of Strings.
	 */
	public ArrayList<String> getRules()
	{
		return m_rule;
	}
	
	/**
	 * @return The alphabet of the L-System as an ArrayList of Strings.
	 */
	public ArrayList<String> getAlphabet()
	{
		return m_alphabet;
	}
	
	/**
	 * @return The axiom for the L-System.
	 */
	public String getAxiom()
	{
		return m_axiom;
	}
	
	/**
	 * @return An ArrayList of all preset L-System alphabets as arrays of Strings
	 */
	public ArrayList<String[]> getDefaultAlphabets()
	{
		return defaultAlphabets;
	}
	
	/**
	 * @return An ArrayList of all preset L-System axioms as Strings.
	 */
	public ArrayList<String> getDefaultAxioms()
	{
		return defaultAxioms;
	}
	
	/**
	 * @return An ArrayList of all preset L-System production rules as arrays of Strings.
	 */
	public ArrayList<String[]> getDefaultRules()
	{
		return defaultRules;
	}
	
	/**
	 * Set the accepted String as the L-System's axiom.
	 * @param axiom new axiom
	 */
	public void setAxiom(String axiom)
	{
		m_axiom = axiom;
	}
	
	/**
	 * Set the accepted ArrayList of Strings as the L-System's alphabet.
	 * @param newalphabet new alphabet
	 */
	public void setAlphabet(ArrayList<String> newalphabet)
	{
		m_alphabet = newalphabet;
	}
	
	/**
	 * Set the accepted ArrayList of Strings as the L-System's production rules.
	 * @param newrules new set of rules
	 */
	public void setRules(ArrayList<String> newrules)
	{
		m_rule = newrules;
	}
	
	/**
	 * Set the L-System's alphabet to one of the preset L-Systems' based on the parameter.
	 * @param defnum the number of the default to be set
	 */
	public void setAlphabetDef(int defnum)
	{
		m_alphabet.clear();
		
		for(String x : defaultAlphabets.get(defnum))
			m_alphabet.add(x);
	}
	
	/**
	 * Set the L-System's axiom to one of the preset L-Systems' based on the parameter.
	 * @param defnum the number of the default to be set
	 */
	public void setAxiomDef(int defnum)
	{
		m_axiom = defaultAxioms.get(defnum);
	}
	
	/**
	 * Set the L-System's production rules to one of the preset L-Systems' based on the parameter.
	 * @param defnum the number of the default to be set
	 */
	public void setRulesDef(int defnum)
	{
		m_rule.clear();
		
		for(String x : defaultRules.get(defnum))
			m_rule.add(x);
	}
}
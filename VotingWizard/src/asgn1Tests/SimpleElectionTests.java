/**
 * 
 */
package asgn1Tests;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import asgn1Election.Candidate;
import asgn1Election.CandidateIndex;
import asgn1Election.Election;
import asgn1Election.ElectionException;
import asgn1Election.SimpleElection;
import asgn1Election.Vote;
import asgn1Election.VoteCollection;
import asgn1Election.VoteList;

/**
 * @author Ian
 *
 */
public class SimpleElectionTests {

	private Election simpleElect;
	private Vote testVote;
	private VoteCollection vc;
	private TreeMap<CandidateIndex, Candidate> cds;
	
	int numberOfTestCandidates = 15;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		simpleElect = new SimpleElection("test");
		vc = new VoteCollection(numberOfTestCandidates);
		
		buildCandidateList();
		buildVoteCollection();
		setNumberOfCandidates(numberOfTestCandidates);
	}

	
	/*
	 *    Test Section for SimpleElection Constructor
	 */
	/**
	 * Test method for {@link asgn1Election.SimpleElection#SimpleElection(java.lang.String)}.
	 */
	@Test
	public void testSimpleElectionNormal() {
		assertNotNull(simpleElect);
	}
	
	@Test
	public void testSimpleElectionIsInstanceOf() {
		assertTrue(simpleElect instanceof Election);
	}
	
	@Test
	public void testSimpleElectionNameSaved() {
		assertEquals("test", simpleElect.getName());
	}
	
	@Test
	public void testSimpleElectionTwoObjectsDiffName() {
		Election testSimple = new SimpleElection("other");
		
		assertNotEquals(testSimple.getName(), simpleElect.getName());
	}
	

	/*
	 *    Test Section for SimpleElection.FindWinner()
	 */
	/**
	 * Test method for {@link asgn1Election.SimpleElection#findWinner()}.
	 */
	@Test
	public void testFindWinner() {
		String returnedString;
		
		returnedString = simpleElect.findWinner();
		
		assertEquals(returnAssertedFindWinnerText(), returnedString);
	}

	
	/*
	 *    Test Section for SimpleElection.IsFormal()
	 */
	/**
	 * Test method for {@link asgn1Election.SimpleElection#isFormal(asgn1Election.Vote)}.
	 */
	@Test
	public void testIsFormalNormal() {
		testVote = new VoteList(numberOfTestCandidates);
		
		for (int i = 1; i <= numberOfTestCandidates; i++){
			testVote.addPref(i);
		}
		
		assertTrue(simpleElect.isFormal(testVote));
	}
	
	@Test
	public void testIsFormalAllOnesFail() {
		//Fails because more than one '1'
		testVote = new VoteList(numberOfTestCandidates);
		
		for (int i = 1; i <= numberOfTestCandidates; i++){
			testVote.addPref(1);
		}
		
		assertFalse(simpleElect.isFormal(testVote));
	}

	@Test
	public void testIsFormalAllFifteensFail() {
		//Fails because no '1'
		testVote = new VoteList(numberOfTestCandidates);
		
		for (int i = 1; i <= numberOfTestCandidates; i++){
			testVote.addPref(15);
		}
		
		assertFalse(simpleElect.isFormal(testVote));
	}
	
	@Test
	public void testIsFormalVotesReversePass() {
		testVote = new VoteList(numberOfTestCandidates);
		
		for (int i = numberOfTestCandidates; i >= 1; i--){
			testVote.addPref(i);
		}
		
		assertTrue(simpleElect.isFormal(testVote));
	}
	
	@Test
	public void testIsFormalDuplicateNumberFourPass() {
		//Passes because there is only one '1'
		testVote = new VoteList(numberOfTestCandidates);
		
		for (int i = 1; i <= 4; i++){
			testVote.addPref(i);
		}
		testVote.addPref(4);
		
		for (int i = 6; i <= 10; i++){
			testVote.addPref(i);
		}
		testVote.addPref(4);
		
		for (int i = 12; i <= 15; i++){
			testVote.addPref(i);
		}
		
		assertTrue(simpleElect.isFormal(testVote));
	}
	
	@Test
	public void testIsFormalDuplicateOnBounderyFail() {
		//Fails because there is no '1'
		testVote = new VoteList(numberOfTestCandidates);
		
		testVote.addPref(15);
		for (int i = 2; i <= 14; i++){
			testVote.addPref(i);
		}
		testVote.addPref(15);

		assertFalse(simpleElect.isFormal(testVote));
	}
	
	@Test
	public void testIsFormalDuplicatePrimaries() {
		//Fails because there is two '1'
		testVote = new VoteList(numberOfTestCandidates);
		
		for (int i = 2; i <= 5; i++){
			testVote.addPref(i);
		}
		testVote.addPref(1);
		
		for (int i = 6; i <= 10; i++){
			testVote.addPref(i);
		}
		testVote.addPref(1);
		
		for (int i = 11; i <= 14; i++){
			testVote.addPref(i);
		}

		assertFalse(simpleElect.isFormal(testVote));
	}
	
	@Test
	public void testIsFormalBoundaryZeroFail() {
		//Fail because of Zero
		testVote = new VoteList(numberOfTestCandidates);
		
		testVote.addPref(0);
		testVote.addPref(1);

		assertFalse(simpleElect.isFormal(testVote));
	}
	
	@Test
	public void testIsFormalBoundarySixteenFail() {
		//Fail because exceeds numberOfTestCandidates(15)
		testVote = new VoteList(numberOfTestCandidates);
		
		testVote.addPref(16);
		testVote.addPref(1);

		assertFalse(simpleElect.isFormal(testVote));
	}
	
	@Test
	public void testIsFormalBoundaryThreeFail() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		//Fail because exceeds max 2
		int numCandiForThisTest = 2;
		
		setNumberOfCandidates(numCandiForThisTest);
		testVote = new VoteList(numCandiForThisTest);
		
		testVote.addPref(3);
		testVote.addPref(1);

		assertFalse(simpleElect.isFormal(testVote));
	}
	
	
	
	/**
	 * Test method for {@link asgn1Election.SimpleElection#clearWinner(int)}.
	 */
	@Test
	public void testClearWinner() {
		fail("Not yet implemented");
	}



	/**
	 * Test method for {@link asgn1Election.SimpleElection#toString()}.
	 */
	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

	
	
	/*
	 *    Private Helper methods
	 */
	private void setNumberOfCandidates(int numCandi) throws NoSuchFieldException, SecurityException, 
			IllegalArgumentException, IllegalAccessException{
		//Push into the instance of simpleElect
		Field field = Election.class.getDeclaredField("numCandidates");
		field.setAccessible(true);
		field.set(simpleElect, numCandi);
		field.setAccessible(false);
	}
	
	private void buildCandidateList() throws 
	        ElectionException, NoSuchFieldException, SecurityException, 
	        IllegalArgumentException, IllegalAccessException{

		this.cds = new TreeMap<CandidateIndex, Candidate>();
		Candidate[] candidateArray = new Candidate[numberOfTestCandidates];
			
		candidateArray[0] = new Candidate("Candidate1","Candidate1 Party","CP1", 0);
		candidateArray[1] = new Candidate("Candidate2","Candidate2 Party","CP2", 0);
		candidateArray[2] = new Candidate("Candidate3","Candidate3 Party","CP3", 0);
		candidateArray[3] = new Candidate("Candidate4","Candidate4 Party","CP4", 0);
		candidateArray[4] = new Candidate("Candidate5","Candidate5 Party","CP5", 0);
		candidateArray[5] = new Candidate("Candidate6","Candidate6 Party","CP6", 0);
		candidateArray[6] = new Candidate("Candidate7","Candidate7 Party","CP7", 0);
		candidateArray[7] = new Candidate("Candidate8","Candidate8 Party","CP8", 0);
		candidateArray[8] = new Candidate("Candidate9","Candidate9 Party","CP9", 0);
		candidateArray[9] = new Candidate("Candidate10","Candidate10 Party","CP10", 0);
		candidateArray[10] = new Candidate("Candidate11","Candidate11 Party","CP11", 0);
		candidateArray[11] = new Candidate("Candidate12","Candidate12 Party","CP12", 0);
		candidateArray[12] = new Candidate("Candidate13","Candidate13 Party","CP13", 0);
		candidateArray[13] = new Candidate("Candidate14","Candidate14 Party","CP14", 0);
		candidateArray[14] = new Candidate("Candidate15","Candidate15 Party","CP15", 0);
		
		for (int i = 0; i < candidateArray.length; i++){
			this.cds.put(new CandidateIndex(i+1), candidateArray[i]);
		}
		
		//Push into the instance of simpleElect
		Field field = Election.class.getDeclaredField("cds");
		field.setAccessible(true);
		field.set(simpleElect, cds);
		field.setAccessible(false);
	}
	
	private void buildVoteCollection() throws NoSuchFieldException, 
			SecurityException, IllegalArgumentException, IllegalAccessException{
		addVoteToList(buildVote("15,10,1,13,9,8,4,6,7,11,1,12,3,14,2"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,14,14,13,12,9,4,11"));
		addVoteToList(buildVote("10,6,4,1,14,9,11,13,3,15,7,2,12,5,8"));
		addVoteToList(buildVote("1,7,4,8,5,2,13,3,11,12,6,14,15,9,10"));
		addVoteToList(buildVote("10,15,4,3,7,14,13,5,1,9,11,12,6,8,2"));
		addVoteToList(buildVote("3,4,14,1,6,13,9,2,12,5,10,8,15,11,7"));
		addVoteToList(buildVote("4,14,10,1,15,13,11,2,3,7,6,9,12,5,8"));
		addVoteToList(buildVote("15,13,2,11,6,12,5,4,8,1,7,9,14,10,3"));
		addVoteToList(buildVote("14,11,9,10,15,2,13,4,3,7,8,12,5,6,1"));
		addVoteToList(buildVote("9,3,5,13,14,2,6,4,10,15,11,1,8,12,7"));
		addVoteToList(buildVote("10,7,11,6,15,9,5,12,8,1,2,4,14,3,13"));
		addVoteToList(buildVote("10,12,6,1,11,15,13,5,8,3,7,4,14,2,9"));
		addVoteToList(buildVote("13,6,8,15,2,3,14,9,11,12,5,1,7,4,10"));
		addVoteToList(buildVote("5,13,11,2,10,3,9,14,12,6,1,15,4,8,7"));
		addVoteToList(buildVote("7,5,10,13,11,14,4,9,15,8,12,2,6,1,3"));
		addVoteToList(buildVote("14,7,9,1,5,3,8,12,10,4,11,13,6,2,15"));
		addVoteToList(buildVote("6,9,4,5,1,14,15,12,11,8,3,13,2,7,10"));
		addVoteToList(buildVote("9,11,13,1,4,5,15,7,10,3,6,2,14,12,8"));
		addVoteToList(buildVote("1,15,2,8,11,9,12,5,14,4,6,7,10,3,13"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("9,4,7,8,11,14,2,10,1,3,13,5,6,12,15"));
		addVoteToList(buildVote("6,14,8,15,2,5,3,7,10,1,12,9,4,13,11"));
		addVoteToList(buildVote("3,2,5,12,1,4,15,13,11,6,8,7,10,9,14"));
		addVoteToList(buildVote("15,4,10,9,14,8,13,7,3,6,2,1,5,12,11"));
		addVoteToList(buildVote("3,4,11,15,9,2,7,13,5,14,12,8,6,1,10"));
		addVoteToList(buildVote("7,6,3,15,1,11,9,13,10,14,2,8,5,4,12"));
		addVoteToList(buildVote("14,1,9,4,13,2,15,6,12,5,7,11,8,10,3"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("3,2,5,10,11,12,1,4,7,8,6,15,14,13,9"));
		addVoteToList(buildVote("6,9,5,1,11,3,7,8,15,4,2,10,14,13,12"));
		addVoteToList(buildVote("4,2,8,6,7,10,13,1,9,15,14,12,11,5,3"));
		addVoteToList(buildVote("10,6,4,1,14,9,11,13,3,15,7,2,12,5,8"));
		addVoteToList(buildVote("6,4,15,1,13,12,10,14,3,8,5,9,2,7,11"));
		addVoteToList(buildVote("11,6,10,3,14,4,9,13,1,2,5,15,12,7,8"));
		addVoteToList(buildVote("11,6,3,3,14,4,9,13,1,2,15,15,12,7,8"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("4,13,11,14,3,10,2,1,9,5,6,8,12,7,15"));
		addVoteToList(buildVote("9,3,5,13,14,2,6,4,10,15,11,1,8,12,7"));
		addVoteToList(buildVote("2,4,6,10,15,7,5,1,9,3,8,14,11,12,13"));
		addVoteToList(buildVote("3,9,5,15,4,10,1,7,8,14,2,13,11,6,12"));
		addVoteToList(buildVote("9,10,8,2,5,6,12,3,7,11,4,13,15,1,14"));
		addVoteToList(buildVote("15,6,1,12,7,4,2,10,8,13,5,3,9,11,14"));
		addVoteToList(buildVote("5,2,9,14,13,11,15,6,8,12,7,3,4,10,1"));
		addVoteToList(buildVote("2,15,10,12,1,14,8,13,3,6,7,4,9,5,11"));
		addVoteToList(buildVote("1,9,13,6,14,12,7,5,3,10,11,4,8,2,15"));
		addVoteToList(buildVote("2,15,4,6,13,11,12,14,9,10,5,1,8,3,7"));
		addVoteToList(buildVote("13,3,12,1,7,14,11,10,8,5,15,9,4,6,2"));
		addVoteToList(buildVote("10,11,3,4,13,6,14,8,7,9,5,1,12,15,2"));
		addVoteToList(buildVote("7,4,1,11,15,8,6,13,2,5,10,3,9,12,14"));
		addVoteToList(buildVote("2,14,9,5,7,3,1,15,8,13,10,12,6,4,11"));
		addVoteToList(buildVote("6,9,15,13,7,11,5,10,4,12,14,8,2,3,1"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("1,9,13,4,2,5,15,3,7,8,11,6,10,12,14"));
		addVoteToList(buildVote("13,3,14,12,6,2,15,8,1,9,4,7,11,5,10"));
		addVoteToList(buildVote("14,11,2,6,5,1,13,3,15,12,7,4,9,10,8"));
		addVoteToList(buildVote("5,9,1,2,8,6,14,13,15,11,10,4,7,3,12"));
		addVoteToList(buildVote("7,8,15,14,1,12,10,3,4,6,9,11,5,2,13"));
		addVoteToList(buildVote("15,9,12,14,2,5,8,3,4,13,7,6,1,11,10"));
		addVoteToList(buildVote("3,9,10,6,4,2,8,15,1,11,5,14,12,13,7"));
		addVoteToList(buildVote("4,14,8,15,7,11,10,9,1,5,3,2,13,12,6"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("15,3,9,13,7,6,8,4,10,14,12,5,2,1,11"));
		addVoteToList(buildVote("12,9,1,11,3,13,7,6,4,2,8,14,5,10,15"));
		addVoteToList(buildVote("15,1,10,9,12,6,2,7,5,11,3,8,4,14,13"));
		addVoteToList(buildVote("1,12,5,4,11,6,8,13,15,1,10,3,7,2,14"));
		addVoteToList(buildVote("6,4,1,14,11,13,9,10,5,15,7,2,8,3,12"));
		addVoteToList(buildVote("13,1,2,11,12,4,15,14,9,6,8,5,10,7,3"));
		addVoteToList(buildVote("6,12,3,7,14,4,5,9,10,2,15,8,13,1,11"));
		addVoteToList(buildVote("11,12,7,9,5,10,6,3,4,1,14,2,8,15,13"));
		addVoteToList(buildVote("8,14,3,5,7,6,13,9,1,12,4,10,15,11,2"));
		addVoteToList(buildVote("11,4,6,1,10,14,12,2,9,8,7,3,13,5,15"));
		addVoteToList(buildVote("15,8,13,1,4,14,6,12,7,3,5,9,10,11,2"));
		addVoteToList(buildVote("11,6,10,13,5,4,3,1,8,12,15,2,9,14,7"));
		addVoteToList(buildVote("8,3,10,14,7,13,5,12,4,2,6,11,9,1,15"));
		addVoteToList(buildVote("4,11,9,5,1,3,15,6,8,2,14,7,12,13,10"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("14,9,4,10,15,12,5,2,6,8,13,3,7,1,11"));
		addVoteToList(buildVote("1,1,1,1,1,1,1,1,1,1,1,1,1,1,1"));
		addVoteToList(buildVote("13,15,5,9,14,1,4,2,12,3,10,8,11,6,7"));
		addVoteToList(buildVote("14,6,11,8,13,7,1,3,12,5,2,10,15,9,4"));
		addVoteToList(buildVote("6,10,11,12,15,13,5,3,1,8,4,9,2,7,14"));
		addVoteToList(buildVote("3,10,15,1,12,11,9,8,14,5,6,13,4,2,7"));
		addVoteToList(buildVote("4,9,1,2,3,7,10,11,14,13,6,8,15,12,5"));
		addVoteToList(buildVote("4,5,7,9,15,6,11,14,12,13,3,1,2,10,8"));
		addVoteToList(buildVote("1,14,3,11,4,7,9,12,5,8,10,6,13,2,15"));
		addVoteToList(buildVote("4,7,9,10,11,15,5,13,6,8,1,3,2,12,14"));
		addVoteToList(buildVote("5,15,13,7,12,4,10,14,3,1,6,11,2,8,9"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("2,1,13,4,11,15,10,12,7,6,9,14,8,5,3"));
		addVoteToList(buildVote("6,12,1,8,11,10,3,4,2,5,14,7,9,13,15"));
		addVoteToList(buildVote("15,11,12,3,6,7,8,5,14,2,9,1,13,10,4"));
		addVoteToList(buildVote("2,13,7,8,9,4,3,11,15,12,5,14,1,6,10"));
		addVoteToList(buildVote("15,1,11,9,5,8,12,3,4,7,2,10,14,13,6"));
		addVoteToList(buildVote("15,10,1,5,12,3,7,14,4,2,8,11,9,6,13"));
		addVoteToList(buildVote("4,7,1,6,13,2,10,5,14,9,3,8,11,12,15"));
		addVoteToList(buildVote("12,2,9,5,11,4,1,6,7,14,8,3,13,15,10"));
		addVoteToList(buildVote("1,9,10,12,11,14,8,5,6,2,7,3,4,13,15"));
		addVoteToList(buildVote("3,10,8,1,15,11,4,14,9,7,12,6,13,5,2"));
		addVoteToList(buildVote("4,13,8,14,6,1,10,9,5,12,15,11,7,3,2"));
		addVoteToList(buildVote("11,8,3,15,5,14,12,9,4,10,6,7,1,2,13"));
		addVoteToList(buildVote("3,4,10,2,1,7,15,9,11,13,14,6,12,8,5"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("7,3,9,10,1,5,12,15,8,2,14,4,11,13,6"));
		addVoteToList(buildVote("12,5,13,7,9,10,3,14,11,4,8,15,1,2,6"));
		addVoteToList(buildVote("8,7,15,5,1,6,14,12,2,3,4,13,10,9,11"));
		addVoteToList(buildVote("7,14,12,8,13,11,1,15,4,9,5,3,10,6,2"));
		addVoteToList(buildVote("13,2,3,5,4,14,10,6,8,12,1,11,9,15,7"));
		addVoteToList(buildVote("15,14,3,6,11,8,9,13,5,12,4,2,10,7,1"));
		addVoteToList(buildVote("7,11,3,13,2,4,10,8,9,5,6,1,14,15,12"));
		addVoteToList(buildVote("4,11,3,13,8,9,6,5,1,2,14,7,10,15,12"));
		addVoteToList(buildVote("10,6,4,1,14,9,11,13,3,15,7,2,12,5,8"));
		addVoteToList(buildVote("7,2,14,9,4,6,1,8,3,15,5,11,12,13,10"));
		addVoteToList(buildVote("4,2,10,1,15,8,3,11,5,13,12,6,9,7,14"));
		addVoteToList(buildVote("12,10,5,3,2,1,7,4,6,13,15,14,8,11,9"));
		addVoteToList(buildVote("13,10,11,4,8,5,7,12,1,3,2,14,9,6,15"));
		addVoteToList(buildVote("5,12,8,11,7,2,3,15,13,6,1,4,9,14,10"));
		addVoteToList(buildVote("15,6,8,2,11,1,5,3,7,12,13,14,9,10,4"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("9,2,11,4,10,6,1,13,3,5,7,8,14,12,15"));
		addVoteToList(buildVote("14,11,3,7,15,10,13,8,2,1,12,4,9,6,5"));
		addVoteToList(buildVote("11,15,4,10,5,6,3,13,14,7,8,2,12,9,1"));
		addVoteToList(buildVote("10,6,11,12,5,14,9,15,7,3,1,4,2,8,13"));
		addVoteToList(buildVote("6,7,10,14,1,13,3,5,12,11,9,2,15,4,8"));
		addVoteToList(buildVote("10,8,11,12,14,2,9,6,15,1,4,3,13,5,7"));
		addVoteToList(buildVote("5,6,4,12,9,14,7,3,15,8,1,10,13,2,11"));
		addVoteToList(buildVote("2,2,2,2,2,2,2,2,2,2,2,2,2,2,2"));
		addVoteToList(buildVote("15,8,14,10,2,13,11,3,12,1,7,6,9,5,4"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("14,3,8,9,4,13,10,6,2,12,5,11,7,15,1"));
		addVoteToList(buildVote("2,8,15,6,3,12,9,13,7,5,1,4,11,14,10"));
		addVoteToList(buildVote("10,11,15,2,5,12,4,9,6,1,7,3,13,14,8"));
		addVoteToList(buildVote("1,3,12,13,9,6,4,7,10,14,8,11,15,5,2"));
		addVoteToList(buildVote("3,11,13,5,1,4,2,7,14,8,9,6,15,12,10"));
		addVoteToList(buildVote("10,7,12,2,3,1,9,13,15,6,11,8,4,14,5"));
		addVoteToList(buildVote("15,5,10,11,1,13,8,2,4,3,12,9,6,14,7"));
		addVoteToList(buildVote("9,1,7,5,11,2,14,4,12,6,3,15,13,8,10"));
		addVoteToList(buildVote("6,14,13,4,3,15,12,2,9,10,7,1,5,11,8"));
		addVoteToList(buildVote("11,9,6,7,4,13,1,14,10,12,5,2,3,8,15"));
		addVoteToList(buildVote("8,13,12,10,14,3,1,7,9,15,4,5,11,6,2"));
		addVoteToList(buildVote("14,15,6,4,10,13,7,8,5,9,12,3,2,11,1"));
		addVoteToList(buildVote("11,12,1,8,10,7,4,9,5,2,6,14,3,15,13"));
		addVoteToList(buildVote("9,14,12,1,11,2,3,5,15,10,8,6,4,7,13"));
		addVoteToList(buildVote("13,11,8,10,6,5,4,3,2,1,14,12,7,15,9"));
		addVoteToList(buildVote("9,5,1,4,11,8,14,10,7,15,13,12,3,6,2"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("1,5,10,4,2,3,15,6,7,14,9,8,12,11,13"));
		addVoteToList(buildVote("9,12,11,7,15,5,6,3,13,10,4,1,8,2,14"));
		addVoteToList(buildVote("2,4,10,6,7,9,8,5,3,15,14,12,1,11,13"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("12,13,9,4,1,10,15,7,2,14,3,5,11,6,8"));
		addVoteToList(buildVote("10,6,5,1,9,7,12,15,13,8,14,4,2,3,11"));
		addVoteToList(buildVote("7,4,11,6,10,9,12,5,15,8,14,1,2,13,3"));
		addVoteToList(buildVote("12,7,10,8,4,5,6,13,15,2,14,1,3,11,9"));
		addVoteToList(buildVote("2,3,6,1,14,9,7,11,10,4,8,15,12,5,13"));
		addVoteToList(buildVote("8,14,11,12,7,15,3,1,10,4,9,5,6,2,13"));
		addVoteToList(buildVote("4,9,11,8,6,15,5,7,3,10,12,2,13,1,14"));
		addVoteToList(buildVote("12,6,5,2,9,3,11,14,8,10,13,7,4,15,1"));
		addVoteToList(buildVote("8,4,2,14,7,6,13,3,5,11,10,9,15,12,1"));
		addVoteToList(buildVote("1,11,12,15,14,2,7,9,8,4,13,6,5,10,3"));
		addVoteToList(buildVote("2,3,5,6,14,7,1,13,4,9,15,8,12,10,11"));
		addVoteToList(buildVote("6,8,10,15,12,2,4,11,3,13,9,14,7,5,1"));
		addVoteToList(buildVote("6,7,3,10,8,15,2,1,9,4,14,11,13,12,5"));
		addVoteToList(buildVote("2,14,11,4,1,3,15,7,10,6,5,13,9,8,12"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("9,14,10,5,6,13,3,11,4,8,12,1,2,7,15"));
		addVoteToList(buildVote("7,13,12,4,2,15,1,11,5,3,6,8,9,14,10"));
		addVoteToList(buildVote("3,3,3,3,3,3,3,3,3,3,3,3,3,3,3"));
		addVoteToList(buildVote("13,14,1,4,6,15,12,11,5,3,8,2,9,7,10"));
		addVoteToList(buildVote("1,2,4,13,12,14,9,5,11,15,6,3,7,10,8"));
		addVoteToList(buildVote("14,3,10,13,8,5,7,11,6,12,9,2,4,1,15"));
		addVoteToList(buildVote("3,9,14,4,7,6,1,12,15,11,8,13,10,5,2"));
		addVoteToList(buildVote("12,14,6,7,5,10,11,8,9,4,2,13,15,3,1"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("13,8,9,3,14,15,11,7,1,6,10,5,4,2,12"));
		addVoteToList(buildVote("13,6,5,2,9,1,8,7,14,15,4,10,3,11,12"));
		addVoteToList(buildVote("1,2,13,7,14,3,10,12,4,8,15,9,6,11,5"));
		addVoteToList(buildVote("14,8,1,12,15,7,6,13,4,10,5,11,2,3,9"));
		addVoteToList(buildVote("12,9,7,4,1,11,13,6,8,14,15,10,3,2,5"));
		addVoteToList(buildVote("10,8,2,5,6,9,7,4,15,11,3,12,13,14,1"));
		addVoteToList(buildVote("9,8,11,12,13,5,14,4,7,1,3,2,15,6,10"));
		addVoteToList(buildVote("3,6,13,4,2,11,5,8,9,15,1,14,7,10,12"));
		addVoteToList(buildVote("8,2,11,12,6,5,9,14,15,4,1,3,7,13,10"));
		addVoteToList(buildVote("13,11,6,8,1,4,10,9,12,15,2,14,7,3,5"));
		addVoteToList(buildVote("9,3,1,4,12,5,10,6,14,7,11,2,13,8,15"));
		addVoteToList(buildVote("6,11,7,13,14,10,3,5,2,12,9,8,1,4,15"));
		addVoteToList(buildVote("7,2,14,9,13,4,6,8,3,1,11,10,5,15,12"));
		addVoteToList(buildVote("9,1,13,11,7,5,6,8,15,14,12,10,2,3,4"));
		addVoteToList(buildVote("3,12,13,8,1,14,11,10,6,7,9,2,15,4,5"));
		addVoteToList(buildVote("7,1,8,9,3,15,13,11,4,5,10,2,6,14,12"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("7,9,8,2,3,10,15,5,4,1,11,14,12,6,13"));
		addVoteToList(buildVote("10,6,4,1,14,9,11,13,3,15,7,2,12,5,8"));
		addVoteToList(buildVote("4,10,1,7,5,9,8,3,11,13,2,15,6,14,12"));
		addVoteToList(buildVote("2,1,5,15,8,3,12,10,14,4,6,9,7,13,11"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("15,13,2,9,10,4,3,7,12,8,1,11,5,14,6"));
		addVoteToList(buildVote("6,11,12,15,14,13,3,5,9,8,4,10,2,7,1"));
		addVoteToList(buildVote("4,6,12,7,5,10,8,13,9,15,11,2,1,3,14"));
		addVoteToList(buildVote("2,10,6,9,14,15,3,7,5,4,8,13,1,11,12"));
		addVoteToList(buildVote("1,12,5,14,2,9,8,13,7,6,15,3,11,10,4"));
		addVoteToList(buildVote("2,12,7,14,10,6,8,11,1,4,5,13,3,15,9"));
		addVoteToList(buildVote("9,7,5,8,10,14,13,4,3,15,1,6,11,2,12"));
		addVoteToList(buildVote("5,15,6,13,7,3,11,9,12,14,4,10,1,2,8"));
		addVoteToList(buildVote("3,14,10,15,1,8,7,9,12,5,6,11,13,2,4"));
		addVoteToList(buildVote("10,3,6,8,12,7,1,15,14,11,5,13,9,2,4"));
		addVoteToList(buildVote("3,11,5,12,4,7,15,10,9,6,8,1,2,14,13"));
		addVoteToList(buildVote("5,7,2,12,8,10,4,15,13,6,11,3,1,14,9"));
		addVoteToList(buildVote("12,9,4,8,7,13,15,14,2,10,11,3,6,1,5"));
		addVoteToList(buildVote("13,5,7,12,10,14,11,9,3,4,2,6,1,15,8"));
		addVoteToList(buildVote("1,8,6,3,5,13,11,4,2,14,10,7,15,9,12"));
		addVoteToList(buildVote("11,15,14,8,10,1,9,3,6,2,4,12,13,7,5"));
		addVoteToList(buildVote("15,12,11,4,5,13,6,2,1,7,9,8,14,3,10"));
		addVoteToList(buildVote("14,15,12,9,7,10,8,1,6,11,5,2,3,4,13"));
		addVoteToList(buildVote("10,9,3,2,8,13,14,12,11,7,6,15,4,1,5"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("10,3,4,6,8,14,12,9,15,11,2,1,7,5,13"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("14,2,3,10,5,7,1,6,11,8,12,4,13,15,9"));
		addVoteToList(buildVote("9,7,12,6,11,3,8,13,2,10,5,4,14,1,15"));
		addVoteToList(buildVote("6,1,10,2,8,5,3,13,14,7,15,4,11,12,9"));
		addVoteToList(buildVote("4,2,8,15,1,11,5,13,14,3,12,10,7,9,6"));
		addVoteToList(buildVote("6,9,8,2,15,7,1,14,13,3,5,11,12,10,4"));
		addVoteToList(buildVote("1,13,2,12,9,6,11,10,4,7,14,5,8,3,15"));
		addVoteToList(buildVote("7,12,5,6,11,10,14,8,3,4,2,1,9,13,15"));
		addVoteToList(buildVote("14,7,13,3,9,11,12,5,1,2,10,15,6,4,8"));
		addVoteToList(buildVote("11,15,1,5,6,12,9,4,2,8,7,3,14,13,10"));
		addVoteToList(buildVote("2,7,14,8,4,10,15,11,6,12,3,9,1,13,5"));
		addVoteToList(buildVote("8,11,12,1,4,7,6,13,14,10,2,9,15,3,5"));
		addVoteToList(buildVote("13,6,3,14,15,2,5,8,9,12,4,7,11,10,1"));
		addVoteToList(buildVote("15,2,10,3,9,5,4,8,12,13,14,11,7,1,6"));
		addVoteToList(buildVote("7,13,6,10,14,4,11,2,15,12,1,9,8,3,5"));
		addVoteToList(buildVote("5,4,2,3,15,1,6,7,11,14,9,10,8,13,12"));
		addVoteToList(buildVote("2,15,12,14,7,13,9,6,10,11,4,3,1,8,5"));
		addVoteToList(buildVote("15,13,2,10,5,4,3,6,14,7,11,12,8,9,1"));
		addVoteToList(buildVote("5,3,10,8,9,15,12,2,13,6,7,1,14,4,11"));
		addVoteToList(buildVote("10,3,14,6,15,9,7,5,4,11,8,2,12,1,13"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("15,9,5,11,7,12,6,10,1,14,3,8,4,2,13"));
		addVoteToList(buildVote("3,13,8,15,10,14,4,11,9,12,2,7,5,1,6"));
		addVoteToList(buildVote("4,4,4,4,4,4,4,4,4,4,4,4,4,4,4"));
		addVoteToList(buildVote("8,9,12,7,13,1,15,2,4,14,6,10,11,3,5"));
		addVoteToList(buildVote("11,6,4,8,13,1,12,3,7,5,2,10,9,14,15"));
		addVoteToList(buildVote("7,4,11,6,14,15,5,13,1,3,12,8,2,10,9"));
		addVoteToList(buildVote("1,5,15,11,8,3,6,14,9,10,4,2,7,13,12"));
		addVoteToList(buildVote("2,11,6,12,14,8,4,13,7,1,9,10,3,15,5"));
		addVoteToList(buildVote("3,6,8,7,11,5,10,12,13,1,15,9,4,14,2"));
		addVoteToList(buildVote("4,11,3,12,10,6,15,13,5,8,2,7,14,1,9"));
		addVoteToList(buildVote("1,11,4,5,14,15,9,12,13,8,2,7,6,10,3"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("5,11,6,8,3,2,13,12,9,15,7,10,14,1,4"));
		addVoteToList(buildVote("4,6,15,2,14,12,11,7,5,9,13,1,3,8,10"));
		addVoteToList(buildVote("3,11,4,9,12,6,1,14,2,15,7,5,10,13,8"));
		addVoteToList(buildVote("15,12,8,2,14,11,3,6,13,7,4,5,9,10,1"));
		addVoteToList(buildVote("5,15,9,1,6,10,12,2,7,14,4,13,8,11,3"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("6,15,13,7,4,8,14,2,3,1,10,12,5,11,9"));
		addVoteToList(buildVote("12,13,14,1,4,2,11,9,5,15,10,6,3,7,8"));
		addVoteToList(buildVote("9,3,5,15,2,4,11,14,10,8,13,6,7,12,1"));
		addVoteToList(buildVote("5,13,14,9,4,6,15,2,1,7,12,3,11,10,8"));
		addVoteToList(buildVote("2,3,7,9,4,6,12,5,8,14,13,1,11,10,15"));
		addVoteToList(buildVote("12,3,6,15,14,2,11,13,9,4,8,5,10,7,1"));
		addVoteToList(buildVote("5,9,6,7,13,15,4,12,14,10,8,1,11,2,3"));
		addVoteToList(buildVote("6,15,1,5,11,8,13,10,7,12,9,2,4,3,14"));
		addVoteToList(buildVote("3,13,2,9,15,14,4,8,10,11,5,7,6,12,1"));
		addVoteToList(buildVote("13,8,5,9,7,4,14,6,1,2,3,12,11,10,15"));
		addVoteToList(buildVote("10,1,2,15,12,6,4,9,14,13,8,5,7,3,11"));
		addVoteToList(buildVote("10,4,2,3,6,5,11,12,14,13,15,8,1,9,7"));
		addVoteToList(buildVote("13,2,10,8,9,11,6,3,1,14,15,12,5,7,4"));
		addVoteToList(buildVote("8,6,10,7,1,5,2,11,12,15,9,14,3,13,4"));
		addVoteToList(buildVote("10,3,2,11,15,6,8,14,9,12,13,7,4,1,5"));
		addVoteToList(buildVote("15,6,9,4,8,1,5,2,12,13,7,11,14,3,10"));
		addVoteToList(buildVote("10,9,1,11,14,2,4,3,13,5,15,7,8,6,12"));
		addVoteToList(buildVote("10,6,4,1,14,9,11,13,3,15,7,2,12,5,8"));
		addVoteToList(buildVote("7,4,8,12,3,11,14,15,9,2,10,6,13,5,1"));
		addVoteToList(buildVote("10,14,2,12,3,15,9,1,7,5,8,6,4,11,13"));
		addVoteToList(buildVote("4,11,13,3,9,15,12,8,5,1,6,2,7,10,14"));
		addVoteToList(buildVote("10,6,4,1,14,9,11,13,3,15,7,2,12,5,8"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("13,1,3,14,6,7,11,9,10,2,12,8,4,15,5"));
		addVoteToList(buildVote("12,15,2,4,13,6,10,8,5,9,1,7,11,3,14"));
		addVoteToList(buildVote("4,11,12,5,6,10,13,9,2,1,14,8,7,15,3"));
		addVoteToList(buildVote("13,8,3,7,5,1,2,10,6,15,4,14,9,12,11"));
		addVoteToList(buildVote("14,6,11,4,13,12,5,3,7,8,15,2,10,9,1"));
		addVoteToList(buildVote("6,7,13,9,5,3,2,12,11,4,14,8,15,10,1"));
		addVoteToList(buildVote("11,14,12,4,13,10,8,3,15,5,1,9,7,6,2"));
		addVoteToList(buildVote("8,4,13,1,3,12,10,5,7,14,2,6,9,11,15"));
		addVoteToList(buildVote("8,2,15,11,6,10,14,3,1,4,13,12,5,9,7"));
		addVoteToList(buildVote("3,13,8,5,11,14,4,9,10,1,12,15,6,7,2"));
		addVoteToList(buildVote("8,9,7,2,3,5,11,12,13,6,1,4,15,14,10"));
		addVoteToList(buildVote("9,7,4,10,5,14,12,8,1,6,13,15,3,11,2"));
		addVoteToList(buildVote("2,6,1,8,12,9,13,4,10,3,11,14,7,5,15"));
		addVoteToList(buildVote("1,8,2,13,14,7,12,15,4,11,5,10,9,6,3"));
		addVoteToList(buildVote("14,10,12,6,8,2,13,7,1,9,5,15,3,11,4"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("7,4,13,10,1,15,11,12,14,3,6,2,8,9,5"));
		addVoteToList(buildVote("15,3,4,8,2,1,13,12,6,11,14,7,9,5,10"));
		addVoteToList(buildVote("12,10,15,3,4,9,5,2,13,14,7,6,1,11,8"));
		addVoteToList(buildVote("7,3,9,10,4,11,12,1,14,13,8,2,15,5,6"));
		addVoteToList(buildVote("1,7,3,15,6,12,10,2,4,11,5,8,9,13,14"));
		addVoteToList(buildVote("1,12,15,11,10,3,5,13,2,9,7,8,14,6,4"));
		addVoteToList(buildVote("13,4,10,8,9,11,7,12,6,2,15,5,14,1,3"));
		addVoteToList(buildVote("10,4,3,8,15,7,13,9,1,14,2,12,6,11,5"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("12,5,9,6,7,15,4,1,8,13,14,3,2,10,11"));
		addVoteToList(buildVote("1,11,4,9,7,2,15,6,13,14,10,3,12,5,8"));
		addVoteToList(buildVote("6,13,8,7,11,2,4,15,5,1,10,14,9,3,12"));
		addVoteToList(buildVote("7,10,8,5,11,3,9,1,4,14,6,12,2,13,15"));
		addVoteToList(buildVote("9,3,15,13,14,11,10,7,5,4,8,6,2,12,1"));
		addVoteToList(buildVote("12,8,15,1,3,9,2,13,14,6,7,5,11,10,4"));
		addVoteToList(buildVote("6,3,11,8,5,14,7,2,12,10,4,9,15,1,13"));
		addVoteToList(buildVote("13,9,15,10,14,2,1,4,7,5,11,3,6,12,8"));
		addVoteToList(buildVote("10,11,9,5,4,12,1,8,6,13,15,2,14,3,7"));
		addVoteToList(buildVote("11,13,3,2,15,6,1,8,4,10,5,7,12,14,9"));
		addVoteToList(buildVote("13,9,14,1,2,12,4,15,6,7,11,8,3,10,5"));
		addVoteToList(buildVote("4,10,14,11,13,12,15,9,8,1,5,6,3,2,7"));
		addVoteToList(buildVote("14,9,2,13,7,4,5,3,6,1,8,11,12,15,10"));
		addVoteToList(buildVote("10,2,14,7,5,13,9,3,6,4,8,1,12,11,15"));
		addVoteToList(buildVote("14,15,12,3,4,6,2,9,5,8,10,13,7,1,11"));
		addVoteToList(buildVote("6,10,7,5,4,3,8,11,2,12,14,1,15,9,13"));
		addVoteToList(buildVote("1,11,4,3,9,5,6,13,7,12,10,2,15,8,14"));
		addVoteToList(buildVote("6,2,14,4,1,13,5,15,10,7,9,8,3,11,12"));
		addVoteToList(buildVote("10,13,7,6,12,3,2,5,1,4,8,15,14,9,11"));
		addVoteToList(buildVote("14,5,2,13,3,15,11,9,10,7,12,8,4,1,6"));
		addVoteToList(buildVote("1,5,14,11,9,3,10,8,15,12,7,4,13,6,2"));
		addVoteToList(buildVote("11,10,8,1,6,9,5,4,13,2,15,3,14,12,7"));
		addVoteToList(buildVote("3,7,10,14,13,11,6,2,8,12,4,5,9,15,1"));
		addVoteToList(buildVote("7,6,8,11,14,3,4,12,9,13,5,1,2,10,15"));
		addVoteToList(buildVote("8,11,4,2,7,15,12,10,1,6,13,3,5,14,9"));
		addVoteToList(buildVote("6,12,5,15,7,10,4,3,14,2,1,11,9,8,13"));
		addVoteToList(buildVote("4,6,11,8,10,14,12,1,7,5,3,15,13,2,9"));
		addVoteToList(buildVote("10,13,1,3,12,8,4,9,6,7,5,15,14,11,2"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("4,8,13,7,5,1,6,2,12,9,15,14,3,11,10"));
		addVoteToList(buildVote("9,2,14,3,8,10,7,6,5,4,11,12,13,1,15"));
		addVoteToList(buildVote("5,1,3,10,11,14,7,2,15,13,4,6,12,8,9"));
		addVoteToList(buildVote("7,11,10,4,9,12,14,5,1,3,8,6,13,2,15"));
		addVoteToList(buildVote("9,10,11,1,15,13,14,6,5,8,2,12,7,4,3"));
		addVoteToList(buildVote("4,10,2,1,8,3,7,14,15,9,6,13,12,11,5"));
		addVoteToList(buildVote("2,4,11,6,1,8,10,14,15,9,7,12,5,13,3"));
		addVoteToList(buildVote("4,7,11,6,8,13,14,2,5,1,10,3,15,9,12"));
		addVoteToList(buildVote("7,14,13,8,4,1,5,2,9,6,10,12,11,3,15"));
		addVoteToList(buildVote("7,3,12,14,9,6,15,8,5,1,10,13,2,11,4"));
		addVoteToList(buildVote("7,1,15,13,9,4,6,8,3,10,2,12,5,11,14"));
		addVoteToList(buildVote("10,8,2,15,9,3,5,6,11,7,12,14,13,4,1"));
		addVoteToList(buildVote("11,15,1,4,13,14,9,3,7,10,6,8,2,12,5"));
		addVoteToList(buildVote("8,14,11,9,12,15,3,2,7,10,13,6,5,1,4"));
		addVoteToList(buildVote("4,2,7,6,3,12,11,14,10,9,1,15,5,13,8"));
		addVoteToList(buildVote("9,15,4,13,8,12,10,1,14,11,2,3,6,5,7"));
		addVoteToList(buildVote("2,7,4,14,1,6,12,8,10,3,15,5,13,9,11"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("15,10,5,13,9,8,4,6,7,11,1,12,3,14,2"));
		addVoteToList(buildVote("1,7,4,8,5,2,13,3,11,12,6,14,15,9,10"));
		addVoteToList(buildVote("10,15,4,3,7,14,13,5,1,9,11,12,6,8,2"));
		addVoteToList(buildVote("10,6,4,1,14,9,11,13,3,15,7,2,12,5,8"));
		addVoteToList(buildVote("3,4,14,1,6,13,9,2,12,5,10,8,15,11,7"));
		addVoteToList(buildVote("4,14,10,1,15,13,11,2,3,7,6,9,12,5,8"));
		addVoteToList(buildVote("15,13,2,11,6,12,5,4,8,1,7,9,14,10,3"));
		addVoteToList(buildVote("14,11,9,10,15,2,13,4,3,7,8,12,5,6,1"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,14,14,13,12,9,4,11"));
		addVoteToList(buildVote("10,7,11,6,15,9,5,12,8,1,2,4,14,3,13"));
		addVoteToList(buildVote("10,12,6,1,11,15,13,5,8,3,7,4,14,2,9"));
		addVoteToList(buildVote("13,6,8,15,2,3,14,9,11,12,5,1,7,4,10"));
		addVoteToList(buildVote("5,13,11,2,10,3,9,14,12,6,1,15,4,8,7"));
		addVoteToList(buildVote("7,5,10,13,11,14,4,9,15,8,12,2,6,1,3"));
		addVoteToList(buildVote("14,7,9,1,5,3,8,12,10,4,11,13,6,2,15"));
		addVoteToList(buildVote("6,9,4,5,1,14,15,12,11,8,3,13,2,7,10"));
		addVoteToList(buildVote("9,11,13,1,4,5,15,7,10,3,6,2,14,12,8"));
		addVoteToList(buildVote("1,15,2,8,11,9,12,5,14,4,6,7,10,3,13"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("9,4,7,8,11,14,2,10,1,3,13,5,6,12,15"));
		addVoteToList(buildVote("6,14,8,15,2,5,3,7,10,1,12,9,4,13,11"));
		addVoteToList(buildVote("3,2,5,12,1,4,15,13,11,6,8,7,10,9,14"));
		addVoteToList(buildVote("15,4,10,9,14,8,13,7,3,6,2,1,5,12,11"));
		addVoteToList(buildVote("3,4,11,15,9,2,7,13,5,14,12,8,6,1,10"));
		addVoteToList(buildVote("7,6,3,15,1,11,9,13,10,14,2,8,5,4,12"));
		addVoteToList(buildVote("14,1,9,4,13,2,15,6,12,5,7,11,8,10,3"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("3,2,5,10,11,12,1,4,7,8,6,15,14,13,9"));
		addVoteToList(buildVote("6,9,5,1,11,3,7,8,15,4,2,10,14,13,12"));
		addVoteToList(buildVote("4,2,8,6,7,10,13,1,9,15,14,12,11,5,3"));
		addVoteToList(buildVote("6,4,15,1,13,12,10,14,3,8,5,9,2,7,11"));
		addVoteToList(buildVote("11,6,10,3,14,4,9,13,1,2,5,15,12,7,8"));
		addVoteToList(buildVote("11,6,3,3,14,4,9,13,1,2,15,15,12,7,8"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("4,13,11,14,3,10,2,1,9,5,6,8,12,7,15"));
		addVoteToList(buildVote("9,3,5,13,14,2,6,4,10,15,11,1,8,12,7"));
		addVoteToList(buildVote("2,4,6,10,15,7,5,1,9,3,8,14,11,12,13"));
		addVoteToList(buildVote("3,9,5,15,4,10,1,7,8,14,2,13,11,6,12"));
		addVoteToList(buildVote("9,10,8,2,5,6,12,3,7,11,4,13,15,1,14"));
		addVoteToList(buildVote("15,6,1,12,7,4,2,10,8,13,5,3,9,11,14"));
		addVoteToList(buildVote("5,2,9,14,13,11,15,6,8,12,7,3,4,10,1"));
		addVoteToList(buildVote("2,15,10,12,1,14,8,13,3,6,7,4,9,5,11"));
		addVoteToList(buildVote("1,9,13,6,14,12,7,5,3,10,11,4,8,2,15"));
		addVoteToList(buildVote("2,15,4,6,13,11,12,14,9,10,5,1,8,3,7"));
		addVoteToList(buildVote("13,3,12,1,7,14,11,10,8,5,15,9,4,6,2"));
		addVoteToList(buildVote("10,11,3,4,13,6,14,8,7,9,5,1,12,15,2"));
		addVoteToList(buildVote("7,4,1,11,15,8,6,13,2,5,10,3,9,12,14"));
		addVoteToList(buildVote("2,14,9,5,7,3,1,15,8,13,10,12,6,4,11"));
		addVoteToList(buildVote("6,9,15,13,7,11,5,10,4,12,14,8,2,3,1"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("1,9,13,4,2,5,15,3,7,8,11,6,10,12,14"));
		addVoteToList(buildVote("13,3,14,12,6,2,15,8,1,9,4,7,11,5,10"));
		addVoteToList(buildVote("14,11,2,6,5,1,13,3,15,12,7,4,9,10,8"));
		addVoteToList(buildVote("5,9,1,2,8,6,14,13,15,11,10,4,7,3,12"));
		addVoteToList(buildVote("7,8,15,14,1,12,10,3,4,6,9,11,5,2,13"));
		addVoteToList(buildVote("15,9,12,14,2,5,8,3,4,13,7,6,1,11,10"));
		addVoteToList(buildVote("3,9,10,6,4,2,8,15,1,11,5,14,12,13,7"));
		addVoteToList(buildVote("4,14,8,15,7,11,10,9,1,5,3,2,13,12,6"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("15,3,9,13,7,6,8,4,10,14,12,5,2,1,11"));
		addVoteToList(buildVote("12,9,1,11,3,13,7,6,4,2,8,14,5,10,15"));
		addVoteToList(buildVote("15,1,10,9,12,6,2,7,5,11,3,8,4,14,13"));
		addVoteToList(buildVote("1,12,5,4,11,6,8,13,15,9,10,3,7,2,14"));
		addVoteToList(buildVote("6,4,1,14,11,13,9,10,5,15,7,2,8,3,12"));
		addVoteToList(buildVote("13,1,2,11,12,4,15,14,9,6,8,5,10,7,3"));
		addVoteToList(buildVote("6,12,3,7,14,4,5,9,10,2,15,8,13,1,11"));
		addVoteToList(buildVote("11,12,7,9,5,10,6,3,4,1,14,2,8,15,13"));
		addVoteToList(buildVote("8,14,3,5,7,6,13,9,1,12,4,10,15,11,2"));
		addVoteToList(buildVote("11,4,6,1,10,14,12,2,9,8,7,3,13,5,15"));
		addVoteToList(buildVote("15,8,13,1,4,14,6,12,7,3,5,9,10,11,2"));
		addVoteToList(buildVote("11,6,10,13,5,4,3,1,8,12,15,2,9,14,7"));
		addVoteToList(buildVote("8,3,10,14,7,13,5,12,4,2,6,11,9,1,15"));
		addVoteToList(buildVote("4,11,9,5,1,3,15,6,8,2,14,7,12,13,10"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("14,9,4,10,15,12,5,2,6,8,13,3,7,1,11"));
		addVoteToList(buildVote("1,1,1,1,1,1,1,1,1,1,1,1,1,1,1"));
		addVoteToList(buildVote("13,15,5,9,14,1,4,2,12,3,10,8,11,6,7"));
		addVoteToList(buildVote("14,6,11,8,13,7,1,3,12,5,2,10,15,9,4"));
		addVoteToList(buildVote("6,10,11,12,15,13,5,3,1,8,4,9,2,7,14"));
		addVoteToList(buildVote("3,10,15,1,12,11,9,8,14,5,6,13,4,2,7"));
		addVoteToList(buildVote("4,9,1,2,3,7,10,11,14,13,6,8,15,12,5"));
		addVoteToList(buildVote("4,5,7,9,15,6,11,14,12,13,3,1,2,10,8"));
		addVoteToList(buildVote("1,14,3,11,4,7,9,12,5,8,10,6,13,2,15"));
		addVoteToList(buildVote("4,7,9,10,11,15,5,13,6,8,1,3,2,12,14"));
		addVoteToList(buildVote("10,6,4,1,14,9,11,13,3,15,7,2,12,5,8"));
		addVoteToList(buildVote("5,15,13,7,12,4,10,14,3,1,6,11,2,8,9"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("2,1,13,4,11,15,10,12,7,6,9,14,8,5,3"));
		addVoteToList(buildVote("6,12,1,8,11,10,3,4,2,5,14,7,9,13,15"));
		addVoteToList(buildVote("15,11,12,3,6,7,8,5,14,2,9,1,13,10,4"));
		addVoteToList(buildVote("2,13,7,8,9,4,3,11,15,12,5,14,1,6,10"));
		addVoteToList(buildVote("15,1,11,9,5,8,12,3,4,7,2,10,14,13,6"));
		addVoteToList(buildVote("15,10,1,5,12,3,7,14,4,2,8,11,9,6,13"));
		addVoteToList(buildVote("4,7,1,6,13,2,10,5,14,9,3,8,11,12,15"));
		addVoteToList(buildVote("12,2,9,5,11,4,1,6,7,14,8,3,13,15,10"));
		addVoteToList(buildVote("1,9,10,12,11,14,8,5,6,2,7,3,4,13,15"));
		addVoteToList(buildVote("3,10,8,1,15,11,4,14,9,7,12,6,13,5,2"));
		addVoteToList(buildVote("4,13,8,14,6,1,10,9,5,12,15,11,7,3,2"));
		addVoteToList(buildVote("11,8,3,15,5,14,12,9,4,10,6,7,1,2,13"));
		addVoteToList(buildVote("3,4,10,2,1,7,15,9,11,13,14,6,12,8,5"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("7,3,9,10,1,5,12,15,8,2,14,4,11,13,6"));
		addVoteToList(buildVote("12,5,13,7,9,10,3,14,11,4,8,15,1,2,6"));
		addVoteToList(buildVote("8,7,15,5,1,6,14,12,2,3,4,13,10,9,11"));
		addVoteToList(buildVote("7,14,12,8,13,11,1,15,4,9,5,3,10,6,2"));
		addVoteToList(buildVote("13,2,3,5,4,14,10,6,8,12,1,11,9,15,7"));
		addVoteToList(buildVote("15,14,3,6,11,8,9,13,5,12,4,2,10,7,1"));
		addVoteToList(buildVote("7,11,3,13,2,4,10,8,9,5,6,1,14,15,12"));
		addVoteToList(buildVote("4,11,3,13,8,9,6,5,1,2,14,7,10,15,12"));
		addVoteToList(buildVote("7,2,14,9,4,6,1,8,3,15,5,11,12,13,10"));
		addVoteToList(buildVote("4,2,10,1,15,8,3,11,5,13,12,6,9,7,14"));
		addVoteToList(buildVote("12,10,5,3,2,1,7,4,6,13,15,14,8,11,9"));
		addVoteToList(buildVote("13,10,11,4,8,5,7,12,1,3,2,14,9,6,15"));
		addVoteToList(buildVote("5,12,8,11,7,2,3,15,13,6,1,4,9,14,10"));
		addVoteToList(buildVote("15,6,8,2,11,1,5,3,7,12,13,14,9,10,4"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("9,2,11,4,10,6,1,13,3,5,7,8,14,12,15"));
		addVoteToList(buildVote("14,11,3,7,15,10,13,8,2,1,12,4,9,6,5"));
		addVoteToList(buildVote("11,15,4,10,5,6,3,13,14,7,8,2,12,9,1"));
		addVoteToList(buildVote("10,6,11,12,5,14,9,15,7,3,1,4,2,8,13"));
		addVoteToList(buildVote("6,7,10,14,1,13,3,5,12,11,9,2,15,4,8"));
		addVoteToList(buildVote("10,8,11,12,14,2,9,6,15,1,4,3,13,5,7"));
		addVoteToList(buildVote("5,6,4,12,9,14,7,3,15,8,1,10,13,2,11"));
		addVoteToList(buildVote("2,2,2,2,2,2,2,2,2,2,2,2,2,2,2"));
		addVoteToList(buildVote("15,8,14,10,2,13,11,3,12,1,7,6,9,5,4"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("14,3,8,9,4,13,10,6,2,12,5,11,7,15,1"));
		addVoteToList(buildVote("2,8,15,6,3,12,9,13,7,5,1,4,11,14,10"));
		addVoteToList(buildVote("10,11,15,2,5,12,4,9,6,1,7,3,13,14,8"));
		addVoteToList(buildVote("1,3,12,13,9,6,4,7,10,14,8,11,15,5,2"));
		addVoteToList(buildVote("3,11,13,5,1,4,2,7,14,8,9,6,15,12,10"));
		addVoteToList(buildVote("10,7,12,2,3,1,9,13,15,6,11,8,4,14,5"));
		addVoteToList(buildVote("15,5,10,11,1,13,8,2,4,3,12,9,6,14,7"));
		addVoteToList(buildVote("9,1,7,5,11,2,14,4,12,6,3,15,13,8,10"));
		addVoteToList(buildVote("6,14,13,4,3,15,12,2,9,10,7,1,5,11,8"));
		addVoteToList(buildVote("11,9,6,7,4,13,1,14,10,12,5,2,3,8,15"));
		addVoteToList(buildVote("8,13,12,10,14,3,1,7,9,15,4,5,11,6,2"));
		addVoteToList(buildVote("14,15,6,4,10,13,7,8,5,9,12,3,2,11,1"));
		addVoteToList(buildVote("11,12,1,8,10,7,4,9,5,2,6,14,3,15,13"));
		addVoteToList(buildVote("9,14,12,1,11,2,3,5,15,10,8,6,4,7,13"));
		addVoteToList(buildVote("13,11,8,10,6,5,4,3,2,1,14,12,7,15,9"));
		addVoteToList(buildVote("9,5,1,4,11,8,14,10,7,15,13,12,3,6,2"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("1,5,10,4,2,3,15,6,7,14,9,8,12,11,13"));
		addVoteToList(buildVote("9,12,11,7,15,5,6,3,13,10,4,1,8,2,14"));
		addVoteToList(buildVote("2,4,10,6,7,9,8,5,3,15,14,12,1,11,13"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("12,13,9,4,1,10,15,7,2,14,3,5,11,6,8"));
		addVoteToList(buildVote("10,6,5,1,9,7,12,15,13,8,14,4,2,3,11"));
		addVoteToList(buildVote("7,4,11,6,10,9,12,5,15,8,14,1,2,13,3"));
		addVoteToList(buildVote("12,7,10,8,4,5,6,13,15,2,14,1,3,11,9"));
		addVoteToList(buildVote("2,3,6,1,14,9,7,11,10,4,8,15,12,5,13"));
		addVoteToList(buildVote("8,14,11,12,7,15,3,1,10,4,9,5,6,2,13"));
		addVoteToList(buildVote("4,9,11,8,6,15,5,7,3,10,12,2,13,1,14"));
		addVoteToList(buildVote("12,6,5,2,9,3,11,14,8,10,13,7,4,15,1"));
		addVoteToList(buildVote("8,4,2,14,7,6,13,3,5,11,10,9,15,12,1"));
		addVoteToList(buildVote("1,11,12,15,14,2,7,9,8,4,13,6,5,10,3"));
		addVoteToList(buildVote("2,3,5,6,14,7,1,13,4,9,15,8,12,10,11"));
		addVoteToList(buildVote("6,8,10,15,12,2,4,11,3,13,9,14,7,5,1"));
		addVoteToList(buildVote("6,7,3,10,8,15,2,1,9,4,14,11,13,12,5"));
		addVoteToList(buildVote("2,14,11,4,1,3,15,7,10,6,5,13,9,8,12"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("9,14,10,5,6,13,3,11,4,8,12,1,2,7,15"));
		addVoteToList(buildVote("10,6,4,1,14,9,11,13,3,15,7,2,12,5,8"));
		addVoteToList(buildVote("7,13,12,4,2,15,1,11,5,3,6,8,9,14,10"));
		addVoteToList(buildVote("3,3,3,3,3,3,3,3,3,3,3,3,3,3,3"));
		addVoteToList(buildVote("13,14,1,4,6,15,12,11,5,3,8,2,9,7,10"));
		addVoteToList(buildVote("1,2,4,13,12,14,9,5,11,15,6,3,7,10,8"));
		addVoteToList(buildVote("14,3,10,13,8,5,7,11,6,12,9,2,4,1,15"));
		addVoteToList(buildVote("3,9,14,4,7,6,1,12,15,11,8,13,10,5,2"));
		addVoteToList(buildVote("12,14,6,7,5,10,11,8,9,4,2,13,15,3,1"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("13,8,9,3,14,15,11,7,1,6,10,5,4,2,12"));
		addVoteToList(buildVote("13,6,5,2,9,1,8,7,14,15,4,10,3,11,12"));
		addVoteToList(buildVote("1,2,13,7,14,3,10,12,4,8,15,9,6,11,5"));
		addVoteToList(buildVote("14,8,1,12,15,7,6,13,4,10,5,11,2,3,9"));
		addVoteToList(buildVote("12,9,7,4,1,11,13,6,8,14,15,10,3,2,5"));
		addVoteToList(buildVote("10,8,2,5,6,9,7,4,15,11,3,12,13,14,1"));
		addVoteToList(buildVote("9,8,11,12,13,5,14,4,7,1,3,2,15,6,10"));
		addVoteToList(buildVote("3,6,13,4,2,11,5,8,9,15,1,14,7,10,12"));
		addVoteToList(buildVote("8,2,11,12,6,5,9,14,15,4,1,3,7,13,10"));
		addVoteToList(buildVote("13,11,6,8,1,4,10,9,12,15,2,14,7,3,5"));
		addVoteToList(buildVote("9,3,1,4,12,5,10,6,14,7,11,2,13,8,15"));
		addVoteToList(buildVote("6,11,7,13,14,10,3,5,2,12,9,8,1,4,15"));
		addVoteToList(buildVote("7,2,14,9,13,4,6,8,3,1,11,10,5,15,12"));
		addVoteToList(buildVote("9,1,13,11,7,5,6,8,15,14,12,10,2,3,4"));
		addVoteToList(buildVote("3,12,13,8,1,14,11,10,6,7,9,2,15,4,5"));
		addVoteToList(buildVote("7,1,8,9,3,15,13,11,4,5,10,2,6,14,12"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("7,9,8,2,3,10,15,5,4,1,11,14,12,6,13"));
		addVoteToList(buildVote("4,10,1,7,5,9,8,3,11,13,2,15,6,14,12"));
		addVoteToList(buildVote("2,1,5,15,8,3,12,10,14,4,6,9,7,13,11"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("15,13,2,9,10,4,3,7,12,8,1,11,5,14,6"));
		addVoteToList(buildVote("6,11,12,15,14,13,3,5,9,8,4,10,2,7,1"));
		addVoteToList(buildVote("4,6,12,7,5,10,8,13,9,15,11,2,1,3,14"));
		addVoteToList(buildVote("2,10,6,9,14,15,3,7,5,4,8,13,1,11,12"));
		addVoteToList(buildVote("1,12,5,14,2,9,8,13,7,6,15,3,11,10,4"));
		addVoteToList(buildVote("2,12,7,14,10,6,8,11,1,4,5,13,3,15,9"));
		addVoteToList(buildVote("9,7,5,8,10,14,13,4,3,15,1,6,11,2,12"));
		addVoteToList(buildVote("5,15,6,13,7,3,11,9,12,14,4,10,1,2,8"));
		addVoteToList(buildVote("3,14,10,15,1,8,7,9,12,5,6,11,13,2,4"));
		addVoteToList(buildVote("10,3,6,8,12,7,1,15,14,11,5,13,9,2,4"));
		addVoteToList(buildVote("3,11,5,12,4,7,15,10,9,6,8,1,2,14,13"));
		addVoteToList(buildVote("5,7,2,12,8,10,4,15,13,6,11,3,1,14,9"));
		addVoteToList(buildVote("12,9,4,8,7,13,15,14,2,10,11,3,6,1,5"));
		addVoteToList(buildVote("13,5,7,12,10,14,11,9,3,4,2,6,1,15,8"));
		addVoteToList(buildVote("1,8,6,3,5,13,11,4,2,14,10,7,15,9,12"));
		addVoteToList(buildVote("11,15,14,8,10,1,9,3,6,2,4,12,13,7,5"));
		addVoteToList(buildVote("15,12,11,4,5,13,6,2,1,7,9,8,14,3,10"));
		addVoteToList(buildVote("14,15,12,9,7,10,8,1,6,11,5,2,3,4,13"));
		addVoteToList(buildVote("10,9,3,2,8,13,14,12,11,7,6,15,4,1,5"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("10,3,4,6,8,14,12,9,15,11,2,1,7,5,13"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("14,2,3,10,5,7,1,6,11,8,12,4,13,15,9"));
		addVoteToList(buildVote("9,7,12,6,11,3,8,13,2,10,5,4,14,1,15"));
		addVoteToList(buildVote("6,1,10,2,8,5,3,13,14,7,15,4,11,12,9"));
		addVoteToList(buildVote("4,2,8,15,1,11,5,13,14,3,12,10,7,9,6"));
		addVoteToList(buildVote("6,9,8,2,15,7,1,14,13,3,5,11,12,10,4"));
		addVoteToList(buildVote("1,13,2,12,9,6,11,10,4,7,14,5,8,3,15"));
		addVoteToList(buildVote("7,12,5,6,11,10,14,8,3,4,2,1,9,13,15"));
		addVoteToList(buildVote("14,7,13,3,9,11,12,5,1,2,10,15,6,4,8"));
		addVoteToList(buildVote("11,15,1,5,6,12,9,4,2,8,7,3,14,13,10"));
		addVoteToList(buildVote("2,7,14,8,4,10,15,11,6,12,3,9,1,13,5"));
		addVoteToList(buildVote("8,11,12,1,4,7,6,13,14,10,2,9,15,3,5"));
		addVoteToList(buildVote("13,6,3,14,15,2,5,8,9,12,4,7,11,10,1"));
		addVoteToList(buildVote("15,2,10,3,9,5,4,8,12,13,14,11,7,1,6"));
		addVoteToList(buildVote("7,13,6,10,14,4,11,2,15,12,1,9,8,3,5"));
		addVoteToList(buildVote("5,4,2,3,15,1,6,7,11,14,9,10,8,13,12"));
		addVoteToList(buildVote("2,15,12,14,7,13,9,6,10,11,4,3,1,8,5"));
		addVoteToList(buildVote("15,13,2,10,5,4,3,6,14,7,11,12,8,9,1"));
		addVoteToList(buildVote("5,3,10,8,9,15,12,2,13,6,7,1,14,4,11"));
		addVoteToList(buildVote("10,3,14,6,15,9,7,5,4,11,8,2,12,1,13"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("15,9,5,11,7,12,6,10,1,14,3,8,4,2,13"));
		addVoteToList(buildVote("3,13,8,15,10,14,4,11,9,12,2,7,5,1,6"));
		addVoteToList(buildVote("4,4,4,4,4,4,4,4,4,4,4,4,4,4,4"));
		addVoteToList(buildVote("8,9,12,7,13,1,15,2,4,14,6,10,11,3,5"));
		addVoteToList(buildVote("11,6,4,8,13,1,12,3,7,5,2,10,9,14,15"));
		addVoteToList(buildVote("7,4,11,6,14,15,5,13,1,3,12,8,2,10,9"));
		addVoteToList(buildVote("1,5,15,11,8,3,6,14,9,10,4,2,7,13,12"));
		addVoteToList(buildVote("2,11,6,12,14,8,4,13,7,1,9,10,3,15,5"));
		addVoteToList(buildVote("3,6,8,7,11,5,10,12,13,1,15,9,4,14,2"));
		addVoteToList(buildVote("4,11,3,12,10,6,15,13,5,8,2,7,14,1,9"));
		addVoteToList(buildVote("1,11,4,5,14,15,9,12,13,8,2,7,6,10,3"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("5,11,6,8,3,2,13,12,9,15,7,10,14,1,4"));
		addVoteToList(buildVote("4,6,15,2,14,12,11,7,5,9,13,1,3,8,10"));
		addVoteToList(buildVote("3,11,4,9,12,6,1,14,2,15,7,5,10,13,8"));
		addVoteToList(buildVote("15,12,8,2,14,11,3,6,13,7,4,5,9,10,1"));
		addVoteToList(buildVote("5,15,9,1,6,10,12,2,7,14,4,13,8,11,3"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("6,15,13,7,4,8,14,2,3,1,10,12,5,11,9"));
		addVoteToList(buildVote("12,13,14,1,4,2,11,9,5,15,10,6,3,7,8"));
		addVoteToList(buildVote("9,3,5,15,2,4,11,14,10,8,13,6,7,12,1"));
		addVoteToList(buildVote("5,13,14,9,4,6,15,2,1,7,12,3,11,10,8"));
		addVoteToList(buildVote("2,3,7,9,4,6,12,5,8,14,13,1,11,10,15"));
		addVoteToList(buildVote("12,3,6,15,14,2,11,13,9,4,8,5,10,7,1"));
		addVoteToList(buildVote("5,9,6,7,13,15,4,12,14,10,8,1,11,2,3"));
		addVoteToList(buildVote("6,15,1,5,11,8,13,10,7,12,9,2,4,3,14"));
		addVoteToList(buildVote("3,13,2,9,15,14,4,8,10,11,5,7,6,12,1"));
		addVoteToList(buildVote("13,8,5,9,7,4,14,6,1,2,3,12,11,10,15"));
		addVoteToList(buildVote("10,1,2,15,12,6,4,9,14,13,8,5,7,3,11"));
		addVoteToList(buildVote("10,4,2,3,6,5,11,12,14,13,15,8,1,9,7"));
		addVoteToList(buildVote("13,2,10,8,9,11,6,3,1,14,15,12,5,7,4"));
		addVoteToList(buildVote("8,6,10,7,1,5,2,11,12,15,9,14,3,13,4"));
		addVoteToList(buildVote("10,3,2,11,15,6,8,14,9,12,13,7,4,1,5"));
		addVoteToList(buildVote("15,6,9,4,8,1,5,2,12,13,7,11,14,3,10"));
		addVoteToList(buildVote("10,9,1,11,14,2,4,3,13,5,15,7,8,6,12"));
		addVoteToList(buildVote("7,4,8,12,3,11,14,15,9,2,10,6,13,5,1"));
		addVoteToList(buildVote("10,14,2,12,3,15,9,1,7,5,8,6,4,11,13"));
		addVoteToList(buildVote("4,11,13,3,9,15,12,8,5,1,6,2,7,10,14"));
		addVoteToList(buildVote("10,6,4,1,14,9,11,13,3,15,7,2,12,5,8"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("13,1,3,14,6,7,11,9,10,2,12,8,4,15,5"));
		addVoteToList(buildVote("12,15,2,4,13,6,10,8,5,9,1,7,11,3,14"));
		addVoteToList(buildVote("4,11,12,5,6,10,13,9,2,1,14,8,7,15,3"));
		addVoteToList(buildVote("13,8,3,7,5,1,2,10,6,15,4,14,9,12,11"));
		addVoteToList(buildVote("14,6,11,4,13,12,5,3,7,8,15,2,10,9,1"));
		addVoteToList(buildVote("6,7,13,9,5,3,2,12,11,4,14,8,15,10,1"));
		addVoteToList(buildVote("11,14,12,4,13,10,8,3,15,5,1,9,7,6,2"));
		addVoteToList(buildVote("8,4,13,1,3,12,10,5,7,14,2,6,9,11,15"));
		addVoteToList(buildVote("8,2,15,11,6,10,14,3,1,4,13,12,5,9,7"));
		addVoteToList(buildVote("3,13,8,5,11,14,4,9,10,1,12,15,6,7,2"));
		addVoteToList(buildVote("8,9,7,2,3,5,11,12,13,6,1,4,15,14,10"));
		addVoteToList(buildVote("9,7,4,10,5,14,12,8,1,6,13,15,3,11,2"));
		addVoteToList(buildVote("2,6,1,8,12,9,13,4,10,3,11,14,7,5,15"));
		addVoteToList(buildVote("10,6,4,1,14,9,11,13,3,15,7,2,12,5,8"));
		addVoteToList(buildVote("1,8,2,13,14,7,12,15,4,11,5,10,9,6,3"));
		addVoteToList(buildVote("14,10,12,6,8,2,13,7,1,9,5,15,3,11,4"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("7,4,13,10,1,15,11,12,14,3,6,2,8,9,5"));
		addVoteToList(buildVote("15,3,4,8,2,1,13,12,6,11,14,7,9,5,10"));
		addVoteToList(buildVote("12,10,15,3,4,9,5,2,13,14,7,6,1,11,8"));
		addVoteToList(buildVote("7,3,9,10,4,11,12,1,14,13,8,2,15,5,6"));
		addVoteToList(buildVote("1,7,3,15,6,12,10,2,4,11,5,8,9,13,14"));
		addVoteToList(buildVote("1,12,15,11,10,3,5,13,2,9,7,8,14,6,4"));
		addVoteToList(buildVote("13,4,10,8,9,11,7,12,6,2,15,5,14,1,3"));
		addVoteToList(buildVote("10,4,3,8,15,7,13,9,1,14,2,12,6,11,5"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("12,5,9,6,7,15,4,1,8,13,14,3,2,10,11"));
		addVoteToList(buildVote("1,11,4,9,7,2,15,6,13,14,10,3,12,5,8"));
		addVoteToList(buildVote("6,13,8,7,11,2,4,15,5,1,10,14,9,3,12"));
		addVoteToList(buildVote("7,10,8,5,11,3,9,1,4,14,6,12,2,13,15"));
		addVoteToList(buildVote("9,3,15,13,14,11,10,7,5,4,8,6,2,12,1"));
		addVoteToList(buildVote("12,8,15,1,3,9,2,13,14,6,7,5,11,10,4"));
		addVoteToList(buildVote("6,3,11,8,5,14,7,2,12,10,4,9,15,1,13"));
		addVoteToList(buildVote("13,9,15,10,14,2,1,4,7,5,11,3,6,12,8"));
		addVoteToList(buildVote("10,11,9,5,4,12,1,8,6,13,15,2,14,3,7"));
		addVoteToList(buildVote("11,13,3,2,15,6,1,8,4,10,5,7,12,14,9"));
		addVoteToList(buildVote("13,9,14,1,2,12,4,15,6,7,11,8,3,10,5"));
		addVoteToList(buildVote("4,10,14,11,13,12,15,9,8,1,5,6,3,2,7"));
		addVoteToList(buildVote("14,9,2,13,7,4,5,3,6,1,8,11,12,15,10"));
		addVoteToList(buildVote("10,2,14,7,5,13,9,3,6,4,8,1,12,11,15"));
		addVoteToList(buildVote("14,15,12,3,4,6,2,9,5,8,10,13,7,1,11"));
		addVoteToList(buildVote("6,10,7,5,4,3,8,11,2,12,14,1,15,9,13"));
		addVoteToList(buildVote("1,11,4,3,9,5,6,13,7,12,10,2,15,8,14"));
		addVoteToList(buildVote("6,2,14,4,1,13,5,15,10,7,9,8,3,11,12"));
		addVoteToList(buildVote("10,13,7,6,12,3,2,5,1,4,8,15,14,9,11"));
		addVoteToList(buildVote("14,5,2,13,3,15,11,9,10,7,12,8,4,1,6"));
		addVoteToList(buildVote("1,5,14,11,9,3,10,8,15,12,7,4,13,6,2"));
		addVoteToList(buildVote("11,10,8,1,6,9,5,4,13,2,15,3,14,12,7"));
		addVoteToList(buildVote("3,7,10,14,13,11,6,2,8,12,4,5,9,15,1"));
		addVoteToList(buildVote("7,6,8,11,14,3,4,12,9,13,5,1,2,10,15"));
		addVoteToList(buildVote("10,6,4,1,14,9,11,13,3,15,7,2,12,5,8"));
		addVoteToList(buildVote("8,11,4,2,7,15,12,10,1,6,13,3,5,14,9"));
		addVoteToList(buildVote("6,12,5,15,7,10,4,3,14,2,1,11,9,8,13"));
		addVoteToList(buildVote("4,6,11,8,10,14,12,1,7,5,3,15,13,2,9"));
		addVoteToList(buildVote("10,13,1,3,12,8,4,9,6,7,5,15,14,11,2"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		addVoteToList(buildVote("4,8,13,7,5,1,6,2,12,9,15,14,3,11,10"));
		addVoteToList(buildVote("9,2,14,3,8,10,7,6,5,4,11,12,13,1,15"));
		addVoteToList(buildVote("5,1,3,10,11,14,7,2,15,13,4,6,12,8,9"));
		addVoteToList(buildVote("7,11,10,4,9,12,14,5,1,3,8,6,13,2,15"));
		addVoteToList(buildVote("9,10,11,1,15,13,14,6,5,8,2,12,7,4,3"));
		addVoteToList(buildVote("4,10,2,1,8,3,7,14,15,9,6,13,12,11,5"));
		addVoteToList(buildVote("2,4,11,6,1,8,10,14,15,9,7,12,5,13,3"));
		addVoteToList(buildVote("4,7,11,6,8,13,14,2,5,1,10,3,15,9,12"));
		addVoteToList(buildVote("7,14,13,8,4,1,5,2,9,6,10,12,11,3,15"));
		addVoteToList(buildVote("7,3,12,14,9,6,15,8,5,1,10,13,2,11,4"));
		addVoteToList(buildVote("7,1,15,13,9,4,6,8,3,10,2,12,5,11,14"));
		addVoteToList(buildVote("10,8,2,15,9,3,5,6,11,7,12,14,13,4,1"));
		addVoteToList(buildVote("11,15,1,4,13,14,9,3,7,10,6,8,2,12,5"));
		addVoteToList(buildVote("8,14,11,9,12,15,3,2,7,10,13,6,5,1,4"));
		addVoteToList(buildVote("4,2,7,6,3,12,11,14,10,9,1,15,5,13,8"));
		addVoteToList(buildVote("9,15,4,13,8,12,10,1,14,11,2,3,6,5,7"));
		addVoteToList(buildVote("2,7,4,14,1,6,12,8,10,3,15,5,13,9,11"));
		addVoteToList(buildVote("7,15,8,6,2,5,3,10,1,14,13,12,9,4,11"));
		
		//Push into the instance of simpleElect
		Field field = Election.class.getDeclaredField("vc");
		field.setAccessible(true);
		field.set(simpleElect, vc);
		field.setAccessible(false);
	}
	
	private void addVoteToList(Vote vote){
		if ((vote == null) || (!isFormal(vote))) {
			this.vc.updateInformalCount();
		} else {
			this.vc.includeFormalVote(vote);
		}
	}
	
	private Vote buildVote(String voteString){
		Vote vote = new VoteList(numberOfTestCandidates);
		
		String[] votes = voteString.trim().split(",");
		int singleVote;
		
		for (String singleString : votes){
			singleVote = Integer.parseInt(singleString);
			vote.addPref(singleVote);
		}
		
		return vote;
	}
	
	private boolean isFormal(Vote v) {
		int voteValue, innerLoopCounter, outerLoopCounter;
		Iterator<Integer> iter;
		Object voteObject;
		
		outerLoopCounter = 0;
		iter = v.iterator();
		
		
		while(iter.hasNext()){
			voteObject = iter.next();
			voteValue = (int) voteObject;
			outerLoopCounter++;
			innerLoopCounter = 0;			
			
			// Test to see if there is a duplicate vote
			// Skipping the current vote being tested
			for (Object obj : v){
				innerLoopCounter++;
				if (obj.equals(voteObject) && outerLoopCounter != innerLoopCounter){
					return false;
				}
			}
			
			//Test to see if the voted number is higher than the available candidates
			if (voteValue > this.numberOfTestCandidates){
				return false;
			}
		}
		return true;
	}
	
	private String returnAssertedFindWinnerText(){
		String returnString;
		
		returnString = "Results for election: test\n";
		returnString += "Enrolment: 0\n\n";
		returnString += "Candidate1          Candidate1 Party              (CP1)\n";
		returnString += "Candidate2          Candidate2 Party              (CP2)\n";
		returnString += "Candidate3          Candidate3 Party              (CP3)\n";
		returnString += "Candidate4          Candidate4 Party              (CP4)\n";
		returnString += "Candidate5          Candidate5 Party              (CP5)\n";
		returnString += "Candidate6          Candidate6 Party              (CP6)\n";
		returnString += "Candidate7          Candidate7 Party              (CP7)\n";
		returnString += "Candidate8          Candidate8 Party              (CP8)\n";
		returnString += "Candidate9          Candidate9 Party              (CP9)\n";
		returnString += "Candidate10         Candidate10 Party             (CP10)\n";
		returnString += "Candidate11         Candidate11 Party             (CP11)\n";
		returnString += "Candidate12         Candidate12 Party             (CP12)\n";
		returnString += "Candidate13         Candidate13 Party             (CP13)\n";
		returnString += "Candidate14         Candidate14 Party             (CP14)\n";
		returnString += "Candidate15         Candidate15 Party             (CP15)\n\n\n";
		returnString += "Counting primary votes;\n\n";
		returnString += "Simple election: test\n\n";
		returnString += "Candidate1 (CP1)            90\n";
		returnString += "Candidate2 (CP2)            56\n";
		returnString += "Candidate3 (CP3)            84\n";
		returnString += "Candidate4 (CP4)           124\n";
		returnString += "Candidate5 (CP5)            96\n";
		returnString += "Candidate6 (CP6)            64\n";
		returnString += "Candidate7 (CP7)            80\n";
		returnString += "Candidate8 (CP8)            52\n";
		returnString += "Candidate9 (CP9)           216\n";
		returnString += "Candidate10 (CP10)          96\n";
		returnString += "Candidate11 (CP11)          70\n";
		returnString += "Candidate12 (CP12)          94\n";
		returnString += "Candidate13 (CP13)          60\n";
		returnString += "Candidate14 (CP14)          96\n";
		returnString += "Candidate15 (CP15)         100\n\n";
		returnString += "Informal                    14\n\n";
		returnString += "Votes Cast                   0\n\n\n";
		returnString += "Candidate Candidate1 (Candidate1 Party) is the winner with 90 votes...\n";
		
		return returnString;
	}
}

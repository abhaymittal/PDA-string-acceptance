import java.util.Scanner;
import java.util.ArrayList;
import java.util.Stack;
import java.nio.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PDAImplement {
	public static void main(String[] args) {
		PDA automaton=new PDA();
		while(true) {
			clearScreen();
			System.out.println(" *----------------------------------------------------------------------------*");
			System.out.println(" |                                PDA SIMULATOR                               |");
			System.out.println(" *----------------------------------------------------------------------------*");
			System.out.println("Enter your choice:");
			System.out.println("1. Enter a PDA\n2. Enter a string\n3. Exit");
			Scanner scan=new Scanner(System.in);
			int choice=Integer.parseInt(scan.nextLine());
			if(choice==3)
				break;
			else if(choice>3) {
				System.out.println("Invalid choice entered, press enter to continue");
				scan.nextLine();
				continue;
			}
			else if (choice==1) {
				clearScreen();
				System.out.println("Enter the name of the input file");
				String fName=scan.nextLine();
				automaton=new PDA();
				String[] tokens,stokens;
				try(BufferedReader reader=new BufferedReader(new FileReader(fName))) {
					String line = null;
					while((line=reader.readLine())!=null) {
						line=line.replaceAll("\\s+"," ");
						stokens=line.replaceFirst("^ ","").split(" ");
						
						if(stokens[0].equals("Q")) {
							line=reader.readLine();
							line=line.replaceAll("\\s+"," ");
							tokens=line.replaceFirst("^ ","").split(" ");
							automaton.setState(tokens);
						}
						else if (stokens[0].equals("S")) {
							line=reader.readLine();
							line=line.replaceAll("\\s+"," ");
							tokens=line.replaceFirst("^ ","").split(" ");
							automaton.setAlphabet(tokens);
						}
						else if (stokens[0].equals("SS")) {
							line=reader.readLine();
							line=line.replaceAll("\\s+"," ");
							tokens=line.replaceFirst("^ ","").split(" ");
							automaton.setStackAlphabet(tokens);
						}
						else if(stokens[0].equals("qo")) {
							//automata.setInitial(stokens[1]);
							line=reader.readLine();
							line=line.replaceAll("\\s+"," ");
							tokens=line.replaceFirst("^ ","").split(" ");
							automaton.setInitialState(tokens[0]);
							//System.out.println(automaton.getInitial());
						}
						else if (stokens[0].equals("zo")) {
							line=reader.readLine();
							line=line.replaceAll("\\s+"," ");
							tokens=line.replaceFirst("^ ","").split(" ");
							automaton.setInitStackSym(tokens[0]);
						}
						else if(stokens[0].equals("F")) {
							line=reader.readLine();
							line=line.replaceAll("\\s+"," ");
							tokens=line.replaceFirst("^ ","").split(" ");
							automaton.setFinalState(tokens);
						}
						else if(stokens[0].equals("T")) {
							while((line=reader.readLine())!=null) {
								tokens=line.split("=");
								if(tokens.length == 2) {
									tokens[0]=tokens[0].replaceAll("\\s+", " ");
									String[] temp=tokens[0].replaceFirst("^ ","").split(" ");
									PDATransition trans=new PDATransition();
									trans.setInState(temp[0]);
									trans.setInSymbol(temp[1]);
									trans.setInStackTop(temp[2]);
									String[] outTrans=tokens[1].split(";");
									for(int i=0;i<outTrans.length;i++) {
										outTrans[i]=outTrans[i].replaceAll("\\s+", " ");
										temp=outTrans[i].replaceFirst("^ ","").split(" ");
										trans.setTransOut(temp[0],temp[1]);
									}
									automaton.setTransition(trans);
								}
							}
						}				
					}
					
				}
				catch(IOException x) {
					System.err.format("IOException occured "+x.getMessage());
					System.out.println("\n\nPress enter to continue");
					scan.nextLine();
					continue;
				}
			}
			if((choice==2)&&(automaton.getInitialState()==null)) {
				System.out.println("Enter a PDA first.");
				System.out.println("Invalid choice entered, press enter to continue");
				scan.nextLine();
				continue;
			}
			automaton.display();
			AtomicBoolean outp=new AtomicBoolean(false);
			AtomicBoolean flag=new AtomicBoolean(true);
			PDAProcessor root=new PDAProcessor(flag,automaton,outp);
			System.out.println("Enter a string");
			String str=scan.nextLine();
			root.setInputString(str);
			root.setCurrentState(automaton.getInitialState());
			Stack<String> st=new Stack<String>();
			st.push(automaton.getInitStackSym());
			root.setStack(st);
			root.start();
			//while(root.getFlag());
			root.join();
			//System.out.println("Exiting");
			if(root.getOutput()) {
				System.out.println("String is accepted");
			}
			else
				System.out.println("String is rejected");
			System.out.println("Press enter to continue");
			scan.nextLine();
		}
	}
	
	public static void clearScreen() {
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}
}

class PDA {
	private String[] state;
	private String[] alphabet;
	private String[] stackAlphabet;
	private String qo;
	private String zo;
	private String[] finalState;
	private ArrayList<PDATransition> transitions;
	
	public PDA() {
		transitions=new ArrayList<PDATransition>(5);
	}
	
	public void setState(String[] states) {
		state=new String[states.length];
		for(int i=0;i<states.length;i++) {
			state[i]=new String(states[i]);
		}
	}
	
	public void setAlphabet(String[] alpha) {
		alphabet=new String[alpha.length];
		for(int i=0;i<alpha.length;i++) {
			alphabet[i]=new String(alpha[i]);
		}
	}
	
	public void setStackAlphabet(String[] sAlpha) {
		stackAlphabet=new String[sAlpha.length];
		for(int i=0;i<sAlpha.length;i++) {
			stackAlphabet[i]=new String(sAlpha[i]);
		}
	}
	
	public void setInitialState(String initS) {
		qo=initS;
	}
	
	public void setInitStackSym(String stackSym) {
		zo=stackSym;
	}
	public void setFinalState(String[] st) {
		finalState=new String[st.length];
		for(int i=0;i<st.length;i++) {
			finalState[i]=new String(st[i]);
		}
	}
	public void setTransition(PDATransition trans) {
		transitions.add(trans);
	}
	public String getInitStackSym() {
		return zo;
	}
	public String getInitialState() {
		return qo;
	}
	
	public int getTransitionSize() {
		return transitions.size();
	}
	public PDATransition getTransition(int index) {
		return transitions.get(index);
	}
	public boolean checkFinal(String st) {
		for(int i=0;i<finalState.length;i++) {
			if(finalState[i].equals(st))
				return true;
		}
		return false;
	}
	
	public void display() {
		//System.out.println("The following is the five tuple for the DFA");
		System.out.println("Q");
		for(int i=0;i<state.length;i++) {
			System.out.print(state[i]+"   ");
		}
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println();
		System.out.println("S");
		for(int i=0;i<alphabet.length;i++) {
			System.out.print(alphabet[i]+"   ");
		}
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println();
		System.out.println("SS");
		for(int i=0;i<stackAlphabet.length;i++) {
			System.out.print(stackAlphabet[i]+"   ");
		}
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println();
		System.out.println("qo");
		System.out.println(qo);
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println();
		System.out.println("zo");
		System.out.println(zo);
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println();
		System.out.println("F");
		//for(int i=0;i<finalState.length;i++) {
			//System.out.print(finalState[i]+"("+finalState[i].length()+")"+"   ");
		//}
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println();
		System.out.println("T");
		int largestLen=0;
		for(int i=0;i<state.length;i++) {
			if(state[i].length()>largestLen)
				largestLen=state[i].length();
		}
		for(int i=0;i<transitions.size();i++) {
			//System.out.print(transitions.get(i).getInState()+" "+transitions.get(i).getInSymbol()+" "+transitions.get(i).getInStackTop() + " = ");
			System.out.format("%"+largestLen+"s "+"%"+largestLen+"s "+"%"+largestLen+"s = ",transitions.get(i).getInState(),transitions.get(i).getInSymbol(),transitions.get(i).getInStackTop());
			//int nout=transitions.get(i).getOutTransSize();
			for(int j=0;j<transitions.get(i).getOutTransSize();j++) {
				String[] out=transitions.get(i).getOutTrans(j);
				if(j!=0)
					System.out.print("; ");
				//System.out.print(out[0]+" "+out[1]+" ");
				System.out.format("%"+largestLen+"s %"+largestLen+"s",out[0],out[1]);
			}
			System.out.println();
		}
	}
}

class PDATransition {
	private String inState;
	private String inSymbol;
	private String inStackTop;
	private ArrayList<OutTransition> output;
	private class OutTransition {
		private String outState;
		private String outStackString;
		public void setOutState(String st) {
			outState=st;
		}
		public String getOutState() {
			return outState;
		}
		public void setOutStackString(String outStack) {
			outStackString=outStack;
		}
		public String getOutStackString() {
			return outStackString;
		}
	}
	
	public PDATransition() {
		output=new ArrayList<OutTransition>();
	}
	public void setInState(String st) {
		inState=st;
	}
	public String getInState() {
		return inState;
	}
	public void setInSymbol(String sym) {
		inSymbol=sym;
	}
	public String getInSymbol() {
		return inSymbol;
	}
	public void setInStackTop(String stop) {
		inStackTop=stop;
	}
	public String getInStackTop() {
		return inStackTop;
	}
	public void setTransOut(String outState, String outStack) {
		OutTransition out=new OutTransition();
		out.setOutState(outState);
		out.setOutStackString(outStack);
		output.add(out);
	}
	public int getOutTransSize() {
		return output.size();
	}
	public String[] getOutTrans(int index) {
		String[] str=new String[2];
		str[0]=new String(output.get(index).getOutState());
		str[1]=new String(output.get(index).getOutStackString());
		return str;
	}
}

class PDAProcessor implements Runnable {

	private Stack<String> stack;
	private String currentState;
	private String input;
	private Thread t;
	private PDA automaton;
	private static AtomicBoolean flag;
	private static AtomicBoolean output;
	private ArrayList<PDAProcessor> childList;
	
	public PDAProcessor(AtomicBoolean f,PDA automata,AtomicBoolean out) {
		stack=new Stack<String>();
		flag=f;
		output=out;
		automaton=automata;
		childList=new ArrayList<PDAProcessor>(2);
	}
	public void run() {
	//System.out.println("input length is "+input.length());
		if(input.length()==0) {
			if(automaton.checkFinal(currentState)||(stack.size()==0)) {
				//System.out.println("String is accepted");
				output.set(true);
				flag.set(false);
			}
			
		}
		if(flag.get()) {
			//System.out.println("Checking for "+currentState+","+input);
			for(int i=0;i<automaton.getTransitionSize();i++) {
				//System.out.println("Checking with "+automaton.getTransition(i).getInState()+" , "+automaton.getTransition(i).getInSymbol()+" , "+automaton.getTransition(i).getInStackTop());
				if(automaton.getTransition(i).getInState().equals(currentState)&&((input.length()>0&&automaton.getTransition(i).getInSymbol().equals(input.substring(0,1)))||automaton.getTransition(i).getInSymbol().equals("@"))&&((stack.size()>0&&automaton.getTransition(i).getInStackTop().equals(stack.peek()))||automaton.getTransition(i).getInStackTop().equals("@"))) {
					for(int j=0;j<automaton.getTransition(i).getOutTransSize();j++) {
						PDAProcessor pth=new PDAProcessor(flag,automaton,output);
						String[] out = automaton.getTransition(i).getOutTrans(j);
						pth.setCurrentState(out[0]);
						//System.out.println("Moving to "+out[0]+" get stack symbol "+out[1]);
						if(!automaton.getTransition(i).getInSymbol().equals("@"))
							pth.setInputString(input.substring(1));
						else
							pth.setInputString(input);
						String[] tokens=out[1].split("\\.");
						/*System.out.println("Printing tokens");
						for(int k=0;k<tokens.length;k++) {
							System.out.print(tokens[k]+" ");
						}
						System.out.println();*/
						pth.setStack(stack);
						if(!automaton.getTransition(i).getInStackTop().equals("@")) {
							//System.out.println("Popping "+pth.peekSym());
							pth.popSym();
						}
						for(int k=tokens.length-1;k>=0;k--) {
							//System.out.println("Pushing "+tokens[k]+" for "+out[0] +" "+tokens[k].equals("@"));
							if(!tokens[k].equals("@"))
								pth.pushSym(tokens[k]);
						}
						pth.start();
						childList.add(pth);
					}
				}
				//else System.out.println("Condition fail for "+currentState+","+input);
			}
		}
		for(int i=0;i<childList.size();i++) {
			
			childList.get(i).join();
			//System.out.println("Finished child "+i);
		}
	}
	public void start() {
	
		if (t==null) {
		//System.out.println("Starting new Thread");
			t=new Thread(this);
			t.start();
		}
	}
	public void join() {
		while(true) {
			try {
				t.join();
				break;
			}
			catch(InterruptedException e) {
			System.out.println("Trying to join");
			}
		}
	}
	public void setAutomata(PDA automata) {
		automaton=automata;
	}
	
	public void setCurrentState(String st) {
		currentState=st;
	}
	
	public void setInputString(String in) {
		input=in;
	}
	
	public String getCurrentState() {
		return currentState;
	}
	
	public String getInputString() {
		return input;
	}
	public void pushSym(String sym) {
		stack.push(sym);
	}
	public String popSym() {
		return stack.pop();
	}
	public String peekSym() {
		return stack.peek();
	}
	public void setStack(Stack<String> s) {
		stack.addAll(s);
		//System.out.println("Setting the stack "+stack.size());
	}
	public boolean getFlag() {
		return flag.get();
	}
	public boolean getOutput() {
		return output.get();
	}
}

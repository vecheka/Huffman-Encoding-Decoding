import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/*
 * TCSS 342 - HW 3 
 */

/**
 * @author Vecheka Chhourn
 * @version 1.0
 * A class that handles the Huffman's Encoding and Decoding of a text file, and 
 * writing it to a compressed and uncompressed files.
 * ********************************Note******************************** :
 * I have included a decoding method for the program, and I have also implemented
 * my own Low Priority Queue to use in encoding the message.
 */
public class CodingTree {
	
	/** Character codes file.*/
	private static final String CODE_FILE = "codes.txt";
	/** Compressed file.*/
	private static final String COMPRESSED = "compressed.txt";
	/** Uncompressed file.*/
	private static final String UNCOMPRESSED = "uncompressed.txt";
	
	/** Map to store characters and their codes.*/
	private Map<Character, String> codes;
	/** Encoded message in binary.*/
	private String bits;
	/** Map to store characters and their frequency.*/
	private Map<Character, Integer> myCharCount;
	/** Priority Queue to store trees of characters and their weights.*/
	private MyPriorityQueue<HuffmanTree> myQueue;
	
	
	
	/**
	 * Constructor that takes the message to be encoded.
	 * @param message to be encoded
	 */
	public CodingTree(final String message) {
		codes = new HashMap<>();
		myCharCount = new HashMap<>();
		myQueue = new MyPriorityQueue<>();
		bits = "";
		countCharactersFrequency(message);
		buildHuffmanTree();
		encodeMessage(message);
		writeCharacterCodesToFile();
	}
	
	/**
	 * Empty Constructor.
	 */
	public CodingTree(){}
	
	
	/**
	 * Write characters' codes to a file.
	 */
	private void writeCharacterCodesToFile() {
		try {
			PrintStream output = new PrintStream(new File(CODE_FILE));
			for (final Character ch: codes.keySet()) {
				output.println(ch + "=" + codes.get(ch));
			}
			output.close();
			
		} catch (final FileNotFoundException theE) {
			theE.printStackTrace();
		}
		
	}

	/**
	 * Encode the message to binary codes, write as bytes to a file.
	 * @param theMessage to be encoded
	 */
	private void encodeMessage(final String theMessage) {
		final StringBuilder temp = new StringBuilder();
		final int num_bits = 8;	
		final int radix = 2;
		final FileOutputStream output;
		try {
			output = new FileOutputStream(new File(COMPRESSED));

			for (int i = 0; i < theMessage.length(); i++) {
				temp.append(codes.get(theMessage.charAt(i)));
			}

			final byte[] b = new byte[(temp.length() / num_bits)];
			
			for (int i = 0; i < b.length; i++) {
				
				b[i] = (byte) Integer.parseUnsignedInt(temp.substring(i * num_bits, (i * num_bits) + num_bits), radix);				
				
			}
			
			// write the remaining bits to the file
			final byte[] extraByte = new byte[2];
			if (temp.length() % num_bits != 0) {
				final String extraBit = temp.substring(temp.length() - temp.length() % num_bits);
				
				// index 0 informs that there is a remaining bits
				extraByte[0] = (byte) Integer.parseUnsignedInt("0", radix);
				extraByte[1] = (byte) Integer.parseUnsignedInt(extraBit, radix);
			}
			
			
			// write encoded message to the file
			try {
				output.write(b);
				output.write(extraByte);
				output.close();
			} catch (final IOException theE) {
				theE.printStackTrace();
			}
		} catch (final FileNotFoundException theE1) {
			
			theE1.printStackTrace();
		}
		
		// not necessary
		bits = temp.toString();
	}


	/**
	 * Count each characters frequency.
	 * @param theMessage to be read.
	 */
	private void countCharactersFrequency(final String theMessage) {
		for (int i = 0; i < theMessage.length(); i++) {
			final Character temp = theMessage.charAt(i);
			if (myCharCount.containsKey(temp)) {
				myCharCount.put(temp, myCharCount.get(temp) + 1);
			} else {
				myCharCount.put(temp, 1);
			}
		}
	}
	
	
	/**
	 * Build Huffman's Tree from Characters frequency, and encode
	 * each characters to binary codes.
	 */
	private void buildHuffmanTree() {
		// build trees and add each characters and their frequency to the queue
		for (final Character ch: myCharCount.keySet()) {
			final HuffmanTree tree = new HuffmanTree();
			tree.buildTree(myCharCount.get(ch), ch);
			myQueue.offer(tree);
		}
		
		// combine each minimum trees till one tree is remained
		while (myQueue.size() > 1) {
			final HuffmanTree newTree = myQueue.poll().combineTrees(myQueue.poll());
			myQueue.offer(newTree);
		}
		
		// encode the characters to binary codes
		final HuffmanTree finalTree = myQueue.peek();
		finalTree.encode();
		codes = finalTree.getCharacterCodesMap();
		
	}
	
	
	// added method
	/**
	 * Decode binary message to the original message.
	 * @param bits binary message
	 * @param codes each characters' codes in binary form
	 * @return the original message
	 */
	public void decode(final String bits, final Map<Character, String> codes) {
		final StringBuilder originalMessage = new StringBuilder();
		// put map in reverse order string->character
		// intention: better run time
		final Map<String, Character> reverseCodes = new HashMap<>();
		for (final Character ch: codes.keySet()) {
			reverseCodes.put(codes.get(ch), ch);
		}
		
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < bits.length(); i++) {
			temp.append(bits.charAt(i));
			if (reverseCodes.containsKey(temp.toString())) {
				originalMessage.append(reverseCodes.get(temp.toString()));
				temp = new StringBuilder();
			}
		}
		
		final PrintStream output;
		try {
			output = new PrintStream(new File(UNCOMPRESSED));
			output.print(originalMessage.toString());
			output.close();
		} catch (final FileNotFoundException theE) {
			theE.printStackTrace();
		}
	}
	
	
	/** 
	 * Display the encoded message.
	 * @return encoded message in binary form.
	 */
	@Override
	public String toString() {
		return bits;
	}
}

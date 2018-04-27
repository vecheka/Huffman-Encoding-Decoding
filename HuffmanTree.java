import java.util.HashMap;
import java.util.Map;

/*
 * TCSS 342 - HW 3 
 */

/**
 * @author Vecheka Chhourn
 * @version 1.0
 * A class that implements a tree structure to build Huffman's Tree for encoding with
 * characters as children, and their frequencies as parent or root.
 *
 */
public class HuffmanTree implements Comparable<HuffmanTree> {
	
	/** Overall root in the tree.*/
	private TreeNode<Object> root;
	/** Map to store characters and their binary codes.*/
	private final Map<Character, String> myCodes;
	
	
	/**
	 * Constructor to initialize the tree.
	 */
	public HuffmanTree() {
		root = null;
		myCodes = new HashMap<>();
	}
	
	/**
	 * Build a tree with a root initialized to the given value, 
	 * and with one child to the left
	 * @param theRootValue root's data
	 * @param theLeftChild left child
	 */
	public void buildTree(final Integer theRootValue, final Character theLeftChild ) {
		root = buildTree(root, theRootValue, theLeftChild);	
	}
	
	/**
	 * Build a tree with a root initialized to the given value, 
	 * and with one child to the left
	 * @param theRoot overall root of a tree
	 * @param theRootValue root's data
	 * @param theLeftChild left child
	 * @return new tree structure
	 */
	private TreeNode<Object> buildTree(final TreeNode<Object> theRoot, final Integer theRootValue, 
			final Character theLeftChild) {
		return new TreeNode<Object>(theRootValue, new TreeNode<Object>(theLeftChild), null); 
	}

	
	/**
	 * Combine two trees to make one tree.
	 * @param theOther other tree to combine with
	 */
	public HuffmanTree combineTrees(final HuffmanTree theOther) {
		final HuffmanTree newTree = new HuffmanTree();
		newTree.root = combineTrees(root, theOther.root);	
		return newTree;
	}
	
	
	/**
	 * Combine two trees to make one tree.
	 * @param theRoot overall root of the tree
	 * @param theOtherRoot overall root of the second tree
	 * @return new tree structure
	 */
	private TreeNode<Object> combineTrees(final TreeNode<Object> theRoot, final TreeNode<Object> theOtherRoot) {
		// this is hard coded, there is a better way, we'll fix it
//		if ((theOtherRoot.right == null && theOtherRoot.left != null) 
//				&& (theRoot.right == null && theOtherRoot.left != null)) {
//			theRoot.right = theOtherRoot.left;
//		} else if ((theOtherRoot.right != null && theOtherRoot.left != null) 
//				&& (theRoot.left != null && theRoot.right != null)) {
//			root.left = new TreeNode<Object>(theRoot.data, theRoot.left, theRoot.right);
//			root.right = new TreeNode<Object>(theOtherRoot.data, theOtherRoot.left, theOtherRoot.right);
//		} else if ((theOtherRoot.right == null && theOtherRoot.left != null) 
//				&& (theRoot.left != null && theRoot.right != null)) {
//			root.left = new TreeNode<Object>(theRoot.data, theRoot.left, theRoot.right);
//			root.right = new TreeNode<Object>(theOtherRoot.data, theOtherRoot.left, null);
//		} else if ((theOtherRoot.right != null && theOtherRoot.left != null) 
//				&& (theRoot.left != null && theRoot.right == null)) {
//			root.left = new TreeNode<Object>(theRoot.data, theRoot.left, null);
//			root.right = new TreeNode<Object>(theOtherRoot.data, theOtherRoot.left, theOtherRoot.right);
//		}
		
		// fixed it!
		final int tempNode = (Integer) root.data + (Integer) theOtherRoot.data;
		
		if (theRoot.left != null && theRoot.right == null) {
			theRoot.data = theRoot.left.data;
			theRoot.left = null;
		}
		if (theOtherRoot.left != null && theOtherRoot.right == null) {
			theOtherRoot.data = theOtherRoot.left.data;
			theOtherRoot.left = null;
		}
		
//		if ((theRoot.left != null && theRoot.right == null) 
//				&& (theOtherRoot.left != null && theOtherRoot.right == null)) {
//			theRoot.data = theRoot.left.data;
//			theRoot.left = null;
//			theOtherRoot.data = theOtherRoot.left.data;
//			theOtherRoot.left = null;
//			tree = new TreeNode<Object>(tempNode, theRoot, theOtherRoot);
//		} else if (theRoot.left != null && theRoot.right == null) {
//			theRoot.data = theRoot.left.data;
//			theRoot.left = null;
//			tree = new TreeNode<Object>(tempNode, theRoot, theOtherRoot);
//		} else if (theOtherRoot.left != null && theOtherRoot.right == null) {
//			theOtherRoot.data = theOtherRoot.left.data;
//			theOtherRoot.left = null;
//			tree = new TreeNode<Object>(tempNode, theRoot, theOtherRoot);
//		} else {
//			tree = new TreeNode<Object>(tempNode, theRoot, theOtherRoot);
//		}
		
		
		return  new TreeNode<Object>(tempNode, theRoot, theOtherRoot);
	}

	/**
	 * To encode a character to binary form.
	 * @param theCharacter to find in the tree.
	 * @return binary form of the character
	 */
	public void encode() {
		encode(root, "");
	}
	
	// helper method to recursively find the character in the tree
	/**
	 * To encode a character to binary form.
	 * @param theCharacter to find in the tree.
	 * @param theRoot overall root of the tree
	 * @return binary form of the character
	 */
	private void encode(final TreeNode<Object> theRoot, 
			final String theBinary) {
		
		
		if (theRoot.left == null && theRoot.right == null) {
			myCodes.put((char) theRoot.data, theBinary);
			return;
		} 
		encode(theRoot.left, theBinary + "0");
		encode(theRoot.right, theBinary + "1");
		
	}

//	/** 
//	 * Check if it is a character.
//	 * @param theChar character to check.
//	 */
//	private boolean isCharacter(final Character theChar) {
//		return Character.isLetter(theChar) || theChar == ' ' ||
//				(theChar.toString().matches("[^A-Za-z ]"));
//	}
	
	/**
	 * Get Map of characters' codes in binary.
	 * @return map of characters' codes
	 */
	public Map<Character, String>getCharacterCodesMap() {
		return myCodes;
	}
	
	
	/**
	 * Compare the root of two trees.
	 * @param theOther another tree to compare with
	 * @return 1 if bigger, 0 if the same, -1 if smaller
	 */
	@Override
	public int compareTo(final HuffmanTree theOther) {
		int result = 0;
		if ((Integer) this.root.data > (Integer) theOther.root.data) {
			result = 1;
		} else if ((Integer) this.root.data < (Integer) theOther.root.data) {
			result  = -1;
		}
		return result;
	}
	
	
	// For visualizing tree structure (use for testing purposes!)
	/**
	 * Print tree in order traversal
	 */
	public void printInOrder() {
		System.out.print("In order: ");
		printInOrder(root);
		System.out.println();
	}
	
	/**
	 * Helper method to print tree in order traversal. 
	 * @param theRoot overall root of the tree
	 */
	private void printInOrder(final TreeNode<Object> theRoot) {
		if(theRoot != null) {
			printInOrder(theRoot.left);
			System.out.print(theRoot.data+" ");
			printInOrder(theRoot.right);
		}

	}
		
}



/**
 * A class to construct a node in the tree.
 * @param <E> Generic type
 */
class TreeNode<E> {
	
	/** Value of a node.*/
	protected E data;
	/** Left child.*/
	protected TreeNode<E> left;
	/** Right child.*/
	protected TreeNode<E> right;
	
	
	/** 
	 * Constructor that takes an object, left root, and right root.
	 * @param theData to add to the tree
	 * @param theLeft left root
	 * @param theRight right root
	 */
	public TreeNode(final E theData, final TreeNode<E> theLeft, 
			final TreeNode<E> theRight) {
		data = theData;
		left = theLeft;
		right = theRight;
	}
	
	/**
	 * Copy constructor that takes an object and add to the tree, 
	 * and initialize both children to null.
	 * @param theData to add to the tree
	 */
	public TreeNode(final E theData) {
		this(theData, null, null);
	}
}

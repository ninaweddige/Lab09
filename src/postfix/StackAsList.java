package postfix;

public class StackAsList<E> implements Stack<E> {

	private int size = 0;
	private Node first;
	
	//constuctor
	public StackAsList() {
		this.first = null;
	}
	
	//inner class 
	class Node {
		public E data;
		public Node next;
	}	

	@Override
	public void push(E element) {
		//add an element at first position
		Node newNode = new Node();
		newNode.data = element;
		
		if (first != null) {
			newNode.next = first;
		}
		first = newNode;
		size++;
	}

	@Override
	public E pop() throws StackUnderflowException{
		if (!empty()) {
			//get the data field of the first element
			E tmp = first.data;
			//update first element
			first = first.next;
			size--;
			return tmp;
		}else{
			throw new StackUnderflowException("Nothing on the stack to pop.");
		} 
	}

	@Override
	public void clear() { //is it ok to just set first to null instead?
		if (empty()) {return;}
		while (first.next != null) {
			first = first.next;
		}
		//remove last element
		first = null;
		size = 0;
	}

	@Override
	public E peek() throws StackUnderflowException {
		if(!empty()) {
			return first.data;
		}else{
			throw new StackUnderflowException("Stack is empty.");
		}
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean empty() {
		if (first != null) {return false;}
		else {return true;}
	}
	
	@Override
	public String toString() {
		String r = "";
		Node position;
		
		if (this.empty()) {
			return r;
		}else{
			position = first;
		}
		
		while (position.next != null) {
			r += position.data;
			r += ", ";
			position = position.next;
		}
		
		//add the last node's data
		r += position.data;
		return r;
	}
	
}

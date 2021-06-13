package postfix;

public interface Stack<E> {
	
	void push(E element);
	
	E pop() throws StackUnderflowException;
	
	void clear();
	
	E peek() throws StackUnderflowException;
	
	int size();
	
	boolean empty();
	
	String toString();
}

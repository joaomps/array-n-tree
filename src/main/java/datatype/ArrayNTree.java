package main.java.datatype;

import java.util.*;
import java.lang.reflect.Array;

/**
 * Projeto AED do grupo nº 43
 * 
 * @author 47169 Cláudia Miranda
 * @author 48782 João Silva
 * @author Gonçalo Lobo
 * 
 */
public class ArrayNTree<T extends Comparable<T>> implements NTree<T> {

	private T data;
	private ArrayNTree<T>[] children;

	/**
	 * Creates an empty tree
	 * 
	 * @best-case O(1)
	 * @worst-case O(1)
	 * 
	 * @param capacity
	 *            The capacity of each node, ie, the maximum number of direct
	 *            successors
	 */
	@SuppressWarnings("unchecked")
	public ArrayNTree(int capacity) {
		data = null;
		this.children = (ArrayNTree<T>[]) Array.newInstance(ArrayNTree.class,
				capacity);
	}

	/**
	 * Create a tree with one element
	 * 
	 * @best-case O(1)
	 * @worst-case O(1)
	 * 
	 * @param elem
	 *            The element value
	 * @param capacity
	 *            The capacity of each node, ie, the maximum number of direct
	 *            successors
	 */
	@SuppressWarnings("unchecked")
	public ArrayNTree(T elem, int capacity) {
		data = elem;
		this.children = (ArrayNTree<T>[]) Array.newInstance(ArrayNTree.class,
				capacity);
	}

	/**
	 * Creates a tree with the elements inside the given list
	 * 
	 * @best-case O(1)
	 * @worst-case O(n)
	 * 
	 * @param elem
	 *            The list with all the elements to insert
	 * @param capacity
	 *            The capacity of each node, ie, the maximum number of direct
	 *            successors
	 */
	@SuppressWarnings("unchecked")
	public ArrayNTree(List<T> list, int capacity) {
		data = null;
		this.children = (ArrayNTree<T>[]) Array.newInstance(ArrayNTree.class,
				capacity);

		for (int i = 0; i < list.size(); i++) {
			insert(list.get(i));
		}
	}

	/**
	 * Verifies if tree is empty
	 * 
	 * @best-case O(1)
	 * @worst-case O(1)
	 * 
	 * @return true iff tree is empty
	 */
	public boolean isEmpty() {
		return this.data == null;
	}

	/**
	 * Verifies if tree is a leaf, ie, only has one element
	 * 
	 * @best-case O(1)
	 * @worst-case O(n)
	 * 
	 * @return true iff tree is a leaf
	 */
	public boolean isLeaf() {
		boolean allChildrenAreNull = true;

		if (children == null || children.length == 0)
			return true;

		for (int i = 0; i < children.length; i++) {
			if (children[i] != null)
				allChildrenAreNull = false;
		}

		return this.data != null && allChildrenAreNull;
	}

	/**
	 * The number of elements of a tree. An empty tree has zero elements
	 * 
	 * @best-case O(1)
	 * @worst-case O(n)
	 * 
	 * @return the number of elements
	 */
	public int size() {
		if (isEmpty())
			return 0;

		int size = 1;
		for (int i = 0; i < children.length; i++) {
			if (children[i] != null)
				size += children[i].size();
		}

		return size;
	}

	/**
	 * Count the number of leaves. An empty tree has zero leaves
	 * 
	 * @best-case O(1)
	 * @worst-case O(n)
	 * 
	 * @return the number of leaves
	 */
	public int countLeaves() {
		if (isEmpty())
			return 0;

		if (isLeaf())
			return 1;

		int leaves = 0;
		for (int i = 0; i < children.length; i++) {
			if (children[i] != null) {
				if (children[i].isLeaf())
					leaves++;
				else
					leaves += children[i].countLeaves();
			}
		}

		return leaves;
	}

	/**
	 * The tree's height. An empty tree has height zero, a leaf has height one
	 * 
	 * @best-case O(1)
	 * @worst-case O(n)
	 * 
	 * @return the tree's height
	 */
	public int height() {
		if (isEmpty())
			return 0;

		if (isLeaf())
			return 1;

		int[] heights = new int[children.length];

		for (int i = 0; i < children.length; i++) {
			if (children[i] != null) {
				heights[i] = 1 + children[i].height();
			}
		}

		Arrays.sort(heights);

		return heights[heights.length - 1];
	}

	/**
	 * The minimum value of the tree
	 * 
	 * @best-case O(1)
	 * @worst-case O(1)
	 * 
	 * @requires !isEmpty()
	 * @return the minimum value
	 */
	public T min() {
		List<T> result = toList();
		return result.get(0);
	}

	/**
	 * The maximum value of the tree
	 * 
	 * @best-case O(1)
	 * @worst-case O(1)
	 * 
	 * @requires !isEmpty()
	 * @return the maximum value
	 */
	public T max() {
		List<T> result = toList();
		return result.get(result.size() - 1);
	}

	/**
	 * Verifies is an element in in the tree
	 * 
	 * @best-case O(1)
	 * @worst-case O(n)
	 * 
	 * @param elem
	 *            the element to be searched
	 * @return true iff elem belongs to tree
	 */
	public boolean contains(T elem) {
		if (isEmpty())
			return false;

		if (isLeaf()) {
			return data.equals(elem) ? true : false;
		} else {
			if (data.equals(elem))
				return true;

			for (int i = 0; i < children.length; i++) {
				if (children[i] != null && children[i].contains(elem)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Insert element into tree keeping the invariant If an element already
	 * exists, the tree does not change
	 * 
	 * @best-case O(1)
	 * @worst-case O(n^2)
	 * 
	 * @param elem
	 *            the element to be inserted
	 */
	@SuppressWarnings("unchecked")
	public void insert(T elem) {
		if (!contains(elem)) {
			if (isEmpty()) {
				data = elem;
			} else {
				if (isLeaf()) {
					children[0] = new ArrayNTree<>(elem, children.length);
				} else {
					if (elem.compareTo(data) < 0) {
						List<T> listAux = toList();
						data = elem;
						this.children = (ArrayNTree<T>[]) Array
								.newInstance(ArrayNTree.class, children.length);
						for (int i = 0; i < listAux.size(); i++) {
							insert(listAux.get(i));
						}
					} else {
						// O elemento E a guardar num array com espaco livre:
						// este tera de se colocar
						// ou (a) no inicio ou meio do vetor (tendo-se de
						// empurrar os restantes) ou
						// (b) no fim do vetor. De notar que so se pode colocar
						// E no vetor da raiz
						// se o maior dos filhos do elemento anterior for menor
						// que E (cf. o segundo
						// exemplo dos seguintes).
						boolean inseriu = false;
						if (existsSpaceInTheArrayOfChildren()) {
							for (int i = 1; i < children.length
									&& !inseriu; i++) {
								T elementGreatter = greaterElementOfTheSon(
										children[i - 1]);

								if (elementGreatter == null) {
									if (children[i] == null) {
										inseriu = true;
										children[i] = new ArrayNTree<>(elem,
												children.length);
									}
								} else {
									if (elem.compareTo(greaterElementOfTheSon(
											children[i])) > 0) {
										inseriu = true;
										children[i].data = elem;
									}
								}
							}

							// Ordena os filhos
							if (inseriu) {
								bubbleSort(children);
							}
						}

						if (!existsSpaceInTheArrayOfChildren() || !inseriu) {
							for (int i = 0; i < children.length - 1; i++) {
								if (elem.compareTo(children[i].data) > 0 && elem
										.compareTo(children[i + 1].data) < 0) {
									children[i].insert(elem);
									inseriu = true;
									break;
								}
							}
							if (!inseriu) {
								children[children.length - 1].insert(elem);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Sorts the children in in the tree
	 * 
	 * @best-case O(n)
	 * @worst-case O(n^2)
	 * 
	 * @param children2
	 */
	private void bubbleSort(ArrayNTree<T>[] children2) {
		boolean swapped = true;
		int j = 0;
		T tmp;
		while (swapped) {
			swapped = false;
			j++;
			for (int i = 0; i < children2.length - j; i++) {
				if (children2[i] != null && children2[i + 1] != null
						&& children2[i].data
								.compareTo(children2[i + 1].data) > 0) {
					tmp = children2[i].data;
					children2[i].data = children2[i + 1].data;
					children2[i + 1].data = tmp;
					swapped = true;
				}
			}
		}
	}

	/**
	 * Returns the biggest element in our children array
	 * 
	 * @best-case O(n)
	 * @worst-case O(n)
	 * 
	 * @param children2
	 * @return
	 */
	private T greaterElementOfTheSon(ArrayNTree<T> children2) {
		T element = null;

		if (children2 == null || children2.children == null)
			return element;

		for (int i = 0; i < children2.children.length; i++) {
			if (element == null) {
				if (children2.children[i] != null) {
					element = children2.children[i].data;
				}
			} else {
				if (children2.children[i].data.compareTo(element) > 0) {
					element = children2.children[i].data;
				}
			}
		}

		return element;
	}

	/**
	 * Verifies if there is space to insert a new element
	 * 
	 * @best-case O(n)
	 * @worst-case O(n)
	 * 
	 * @return
	 */
	private boolean existsSpaceInTheArrayOfChildren() {
		for (int i = 0; i < children.length; i++) {
			if (children[i] == null) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Delete element from tree keeping the invariant If an element does not
	 * exist, the tree does not change
	 * 
	 * @best-case O(1)
	 * @worst-case O(n^2)
	 * 
	 * @param elem
	 *            the element to be deleted
	 */
	@SuppressWarnings("unchecked")
	public void delete(T elem) {
		if (!isEmpty()) {
			if (isLeaf()) {
				if (data.equals(elem)) {
					data = null;
					children = (ArrayNTree<T>[]) Array
							.newInstance(ArrayNTree.class, children.length);
				}
			} else {
				if (contains(elem)) {
					if (data.equals(elem)) {
						for (int i = 0; i < children.length; i++) {
							if (children[i] != null) {
								data = children[i].data;
								children[i].delete(data);
								children[i] = null;
								break;
							}
						}
					} else {
						boolean removed = false;
						for (int i = 0; i < children.length; i++) {
							if (children[i] != null
									&& elem.equals(children[i].data)) {
								removed = true;
								if (children[i].isLeaf()) {
									children[i] = null;
								} else {
									if (children[i].children[0] != null) {
										children[i].data = children[i].children[0].data;
										for (int j = 1; j < children[i].children.length; j++) {
											children[i].children[j
													- 1] = children[i].children[j];
										}
										children[i].children[children[i].children.length
												- 1] = null;
									} else {
										children[i].data = null;
									}
								}
							}
						}

						if (!removed) {
							for (int i = 0; i < children.length; i++) {
								if (children[i] != null) {
									children[i].delete(elem);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Is this tree equal to another object? Two NTrees are equal iff they have
	 * the same values
	 * 
	 * @best-case O(1)
	 * @worst-case O(n)
	 * 
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (other instanceof ArrayNTree) {
			return equalTree((ArrayNTree<T>) other);
		}
		return false;
	}

	private boolean equalTree(ArrayNTree<T> other) {

		if (this.size() != other.size() || this.data != other.data) {
			return false;
		}

		List<T> tree1 = this.toList();
		List<T> tree2 = other.toList();

		return tree1.equals(tree2);
	}

	/**
	 * Convert tree into list. The list has the elements accordingly to the
	 * tree's prefix traversal, ie, the elements will be sequenced by increasing
	 * order
	 * 
	 * @best-case O(1)
	 * @worst-case O(n)
	 * 
	 * @returns the list with the tree's elements
	 */
	public List<T> toList() {
		List<T> list = new ArrayList<>();

		if (!isEmpty()) {
			list.add(data);
			if (!isLeaf()) {
				for (int i = 0; i < children.length; i++) {
					if (children[i] != null) {
						list.addAll(children[i].toList());
					}
				}
			}
		}
		Collections.sort(list, (a, b) -> a.compareTo(b));
		return list;
	}

	/**
	 * Clones a new tree with the same elements of this
	 * 
	 * @best-case O(1)
	 * @worst-case O(1)
	 * 
	 * @returns a new tree with the same elements of this
	 */
	public ArrayNTree<T> clone() {
		ArrayNTree<T> result = new ArrayNTree<>(children.length);

		result.data = data;
		result.children = children;

		return result;
	}

	public String toString() {
		if (isEmpty())
			return "[]";

		if (isLeaf())
			return "[" + data + "]";

		StringBuilder sb = new StringBuilder();
		sb.append("[" + data + ":");

		for (NTree<T> brt : children)
			if (brt != null)
				sb.append(brt.toString());

		return sb.append("]").toString();
	}

	// more detailed information about tree structure
	public String info() {
		return this + ", size: " + size() + ", height: " + height()
				+ ", nLeaves: " + countLeaves();
	}

	/**
	 * @returns an iterator traversing elements in a increasing order
	 */
	public Iterator<T> iterator() {
		return new ArrayNTreeIterator(this);
	}

	private class ArrayNTreeIterator implements Iterator<T> {

		private List<T> list;
		private int index;

		public ArrayNTreeIterator(ArrayNTree<T> arrayNTree) {
			list = arrayNTree.toList();

			index = 0;
		}

		@Override
		public boolean hasNext() {
			return index < list.size();
		}

		@Override
		public T next() {
			if (this.hasNext()) {
				T element = list.get(index);
				index++;
				return element;
			}

			throw new NoSuchElementException();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}

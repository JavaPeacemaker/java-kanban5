package managers.history;


class Node<E> {

    public Node<E> prev;
    public E element;
    public Node<E> next;

    public Node(Node<E> prev, E element, Node<E> next) {
        this.prev = prev;
        this.element = element;
        this.next = next;
    }

}
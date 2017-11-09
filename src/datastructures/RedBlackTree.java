/*
 * Data Structures and Algorithms.
 * Copyright (C) 2016 Rafael Guterres Jeffman
 *
 * See the LICENSE file accompanying this source code, for
 * licensing restrictions that might apply.
 *
 */

package datastructures;

class RedBlackNode<T extends Comparable<T>> {

	private T value;
	private RedBlackNode<T> left;
	private RedBlackNode<T> right;
	private RedBlackNode<T> parent;
	private boolean red;
	
	public RedBlackNode(T value) {
		this.value = value;
		this.red = true;
	}

	public RedBlackNode<T> insert(T value) throws DuplicateKeyException {
		int cmp = value.compareTo(this.value);
		if (cmp < 0)
			return insertLeft(value);
		else if (cmp > 0)
			return insertRight(value);
		else
			throw new DuplicateKeyException("Already inserted: "+value);
	}
	
	
	private RedBlackNode<T> insertLeft(T value) throws DuplicateKeyException {
		if (left == null) {
			left = new RedBlackNode<>(value);
			left.parent = this;
			return left;
		} else
			return left.insert(value);
	}

	private RedBlackNode<T> insertRight(T value) throws DuplicateKeyException {
		if (right == null) {
			right = new RedBlackNode<>(value);
			right.parent = this;
			return right;
		} else
			return right.insert(value);
	}
	
	public RedBlackNode<T> getParent() {
		return parent;
	}
	
	public void setBlack() {
		this.red = false;
	}

	public void setRed() {
		this.red = true;
	}

	public boolean isRed() {
		return red;
	}
	
	public void print() {
		String r = red ? "*" : ""; 
		System.out.print("(" + value + r +" ");
		if (left != null)
			left.print();
		else
			System.out.print("_");
		if (right != null)
			right.print();
		else
			System.out.print(" _");
		System.out.print(")");
	}

	public RedBlackNode<T> getUncle() {
		if (parent == null)
			return null;
		RedBlackNode<T> G = parent.parent;
		if (G == null)
			return null;
		if (G.left == parent)
			return G.right;
		return G.left;
	}

	public boolean isRightSon() {
		if (parent == null)
			return false;
		return parent.right == this;
	}

	public boolean isLeftSon() {
		if (parent == null)
			return false;
		return parent.left == this;
	}

	public void rotateLeft() {
		if (right == null) return;
		RedBlackNode<T> N = this;
		RedBlackNode<T> P = this.parent;
		RedBlackNode<T> R = this.right;
		RedBlackNode<T> S = R.left;
		//
		RedBlackNode<T> B = S;
		R.left = N;
		N.right = B;
		// parents
		N.parent = R;
		R.parent = P;
		if (B != null)
			B.parent = N;
	}

	public void rotateRight() {
		if (left == null) return;
		RedBlackNode<T> N = this;
		RedBlackNode<T> P = this.parent;
		RedBlackNode<T> L = this.left;
		RedBlackNode<T> S = L.right;
		//
		RedBlackNode<T> B = S;
		L.right = N;
		N.left = B;
		// parents
		N.parent = L;
		L.parent = P;
		if (B != null)
			B.parent = N;
	}

	public void setRight(RedBlackNode<T> node) {
		right = node;
	}

	public void setLeft(RedBlackNode<T> node) {
		left = node;
	}
	
}

public class RedBlackTree<T extends Comparable<T>>
{
	private RedBlackNode<T> root;
	
	public void insert(T data) throws DuplicateKeyException
	{
		RedBlackNode<T> node;
		if (root == null)
			node = root = new RedBlackNode<>(data);
		else
			node = root.insert(data);
		insert_case1(node);
	}
	
	private void insert_case1(RedBlackNode<T> node) {
		if (node.getParent() == null) {
			node.setBlack();
			return;
		}
		insert_case2(node);
	}
	
	private void insert_case2(RedBlackNode<T> node) {
		RedBlackNode<T> P = node.getParent();
		if (!P.isRed()) return;
		insert_case3(node);
	}
	
	private void insert_case3(RedBlackNode<T> node) {
		RedBlackNode<T> P = node.getParent();
		RedBlackNode<T> U = node.getUncle();
		RedBlackNode<T> G = P.getParent();
		if (P.isRed() && (U != null && U.isRed())) {
			P.setBlack();
			U.setBlack();
			G.setRed();
			insert_case1(G);
		} else
			insert_case4(node);
	}
	
	private void insert_case4(RedBlackNode<T> node) { // P is R, U is B 
		RedBlackNode<T> P = node.getParent();
		RedBlackNode<T> G = P.getParent();
		RedBlackNode<T> N = node;

		if (P.isRightSon() && !node.isRightSon()) {
			P.rotateRight();
			G.setRight(node);
			N = P;
		}
		else if (P.isLeftSon() && !node.isLeftSon()) {
			P.rotateLeft();
			G.setLeft(node);
			N = P;
		}
			
		insert_case5(N);
	}
	
	private void insert_case5(RedBlackNode<T> node) { // P is R, U is B 
		RedBlackNode<T> P = (RedBlackNode<T>)(node.getParent());
		RedBlackNode<T> G = (RedBlackNode<T>)P.getParent();
		RedBlackNode<T> GG = null;
		boolean gl = G.isLeftSon(); 
		if (G.getParent() != null)
			GG = (RedBlackNode<T>)(G.getParent());
		
		P.setBlack();
		G.setRed();
		if (P.isRightSon()) {
			G.rotateLeft();
		} else {
			G.rotateRight();
		}
		if (GG != null)
			if (gl) GG.setLeft(P);
			else GG.setRight(P);
		else
			root = P;
	}
	
	public void print() {
		if (root == null)
			System.out.println("Empty tree.");
		else
			root.print();
	}


//Os cases foram feitos baseados em leituras feitas sobre exclusão em árvore redblack. Confesso que achei bem complicada a implementação, porém, tentei entender como funciona o algorítmo
//e tentei explicar criando os cases a baixo com comentários sobre o que acontece em cada case.



        public void excluiFilho ( Node nova ) {
            Node son;
                if (Node.Right != null)
                son = Node.Right;
                else 
                son = Node.Left;
            SubstituiNo(Node, son)
                if(Node.cor == Black){
                if(son.cor == Red)
                son.cor = Black;
            else
                ExclusaoCaso1(son);

// no caso 1 vamos setar que "nova" seja a nova raiz se nova for diferente de null quer dizer que estamos na raiz então pulamos para o caso 2

        public void excluiCaso1( Node nova){
        if ( nova.parent != null)
        excluiCaso2(nova);
}
// No caso 2 se o nó irmão for vermelho seta vermelho no pai e preto no irmão; Depois rotacionamos o nó para a esquerda do pai se o pai estiver para a esquerda, se não, rotacionamos para a direita.
        public void excluiCaso2(Node nova){
        Node i = irmao(nova);
        if (i.cor == Red){
        nova.parent.cor = Red;
        i.cor Black;
        if (nova == nova.parent.left)
        rotateLeft(nova.parent);
        else
        rotateRight(nova.parent);
}
        exclusaoCaso3(nova);

}
// No caso 3 Se o pai, irmao e filho forem pretos pintamos de vermelho o irmão, se não, chamamos o caso de exclusão 4.
        public void excluiCaso3(Node nova){
        Node i = irmao(nova);
        if((nova.parent.cor == Black) && (i.cor == Black) && (i.left.cor == Black) && (i.Right.cor == Black)) {
        i.cor = Red;
        excluiCaso1(nova.parent);
        }else
        excluiCaso4(nova);

}
//No caso 4 se pai é vermelho mas o irmao e filhos são pretos. Pintamos de vermelho o irmão e o pai de preto. Se não, chamamos o caso 5
        public void excluiCaso4(Node nova){
        Node i = irmao(nova);
        if ((nova.parent.cor == Red) && (i.cor == Black) && ( i.left.cor == Black) && (i.right.cor == Black)) {
        i.cor = Red;
        nova.parent.cor = Black;
        }else
        excluiCaso5(nova);
}
// No caso 5 se o irmao for preto acessa o primeiro if e a raiz for igual o pai a esquerda e a cor do irmao a direita for preto e o irmao a esquerda vermelho,
// o irmao recebe vermelho e o irmao a esquerda preto e é rotacionado para a direita. Se não, se o nó pai estiver a direita e o irmao a esquerda for preto e irmao a direita for vermelho
// irmao a esquerda recebe vermelho e o irmao a direita recebe preto e toraciona o irmao para a esquerda.
        public void excluiCaso5(Node nova){
        Node i = irmao(nova);
        if (i.cor == Black){
        if((nova == nova.parent.left) && (i.right.cor == Black) && (i.left.cor == Red)) {
        i.cor = Red;
        i.left.cor = Black;
        rotateRight(i);
        }else if ((nova == nova.parent.right) && (i.left.cor == Black) && (i.right.cor == Red)){
        i.cor = Red;
        i.right.cor = Black;
        rotateLeft(i);
}
}
        excluiCaso6(nova);  
}
// No caso 6 o nó irmão copia a cor do pai, se o nó pai estiver a esquerda o nó irmao recebe a cor preta. e rotaciona o nó pai para esquerda
// Se não, o irmão a esquerda recebe preto e rotaciona o pai para a direita.      
  public void excluiCaso6(Node nova){
Node i = irmao(nova);
i.cor = nova.parent.cor;
nova.parent.cor = Black;
if ( nova == nova.parent.left){
i.right.cor = Black;
rotateLeft(nova.parent);
}else{
i.left.cor = Black;
rotateRight(nova.parent);
}
}

}

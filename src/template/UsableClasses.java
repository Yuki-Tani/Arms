package template;

import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;

public class UsableClasses {
	public static void main(String[] args){
		ForTest.test();
	}
}

// Point //
////////////////////////////////////////////////////////

class Point implements Comparable<Point>{
	int x;
	int y;
	// equals method must be changed !! 
	
	public Point(int x,int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int compareTo(Point o) {
		return Integer.compare(this.x,o.x);
	}
	@Override
	public boolean equals(Object o){
		return x == ((Point)o).x &&
			   y == ((Point)o).y;
	}
	@Override
	public int hashCode(){
		return Integer.hashCode(x*y);
	}
}
///////////////////////////////////////////////////////////

// BIT //
/////////////////////////////////////////////////////////////
class BIT {
	int[] bit;
	
	public BIT(int[] array){ // 0 is empty. 1,2,3... data
		bit = new int[array.length];
		for(int i=1;i<array.length;i++){ // deep copy
			bit[i] = array[i];	
		}
		for(int i=1;i<array.length-1;i++){ // make bit
			if(i+(i&-i)<array.length){
				bit[i+(i&-i)] += bit[i];
			}
		}
	}
	public long getSum1to(int i){
		long sum = 0;
		while(i>0){
			sum += bit[i];
			i -= (i&-i);
		}
		return sum;
	}
	
	public long getSum(int s,int g){
		return getSum1to(g) - getSum1to(s-1);
	}
	
	public void add(int i,int num){
		while(i<bit.length){
			bit[i] += num;
			i += (i&-i);
		}
	}
}
////////////////////////////////////////////////////////////

// Segment Tree//
///////////////////////////////////////////////////////////
class SegmentTree{
	int leaf_num;
	int[] tree;
	
	public SegmentTree(int[] array){
		leaf_num = 1;
		while(leaf_num < array.length) leaf_num *= 2;
		tree = new int[2*leaf_num-1];
		
		Arrays.fill(tree,Integer.MAX_VALUE); // default : for min
		
		for(int i=0;i<array.length;i++){
			update(i,array[i]);
		}
	}
	
	public void update(int i,int num){
		i += leaf_num -1;
		tree[i] = num;
		while(i>0){
			i = (i-1)/2;
			tree[i] = judge(tree[i*2+1],tree[i*2+2]);
		}
	}
	
	// get [left,right]
	public int getBetween(int left,int right){
		return query(left,right+1,0,0,leaf_num);
	}
	
	
	private int judge(int a,int b){ // default: min
		return Math.min(a, b);
	}
	
	// [a b) from [l^root^r)
	private int query(int a,int b,int root,int l,int r){
		if(r<=a || b<=l) return Integer.MAX_VALUE; // default : for max
		if(a<=l && r<=b) return tree[root];
		
		int vl = query(a,b,root*2+1,l,(l+r)/2);
		int vr = query(a,b,root*2+2,(l+r)/2,r);
		return judge(vl,vr);
	}
}


///////////////////////////////////////////////////////////

// Union-Find Tree//
///////////////////////////////////////////////////////////
class UnionFind{
	int[] parent;
	int[] depth;
	
	public UnionFind(int eNum){
		parent = new int[eNum];
		depth = new int[eNum];
		for(int i=0;i<eNum;i++){
			parent[i] = i;
		}
	}
	
	public int getRoot(int x){
		if(parent[x]==x) return x;
		return parent[x] = getRoot(parent[x]); // press
	}
	
	public void unit(int x,int y){
		x = getRoot(x);
		y = getRoot(y);
		if(x==y) return;
		if(depth[x]<depth[y]){
			parent[x] = y;
		}else{
			parent[y] = x;
			if(depth[x]==depth[y]) depth[x]++;
		}
	}
	
	public boolean same(int x,int y){
		return getRoot(x)==getRoot(y);
	}
}
////////////////////////////////////////////////////////////

class ForTest{
	public static void test(){
		System.out.println("Point(compareTo)");
		PriorityQueue<Point> que = new PriorityQueue<>();
		Point p1 = new Point(1,3);
		Point p2 = new Point(4,1);
		Point p3 = new Point(2,-1);
		Point p4 = new Point(-2,8);
		que.add(p1);
		que.add(p2);
		que.add(p3);
		que.add(p4);
		System.out.println(que.poll().x);
		System.out.println(que.poll().x);
		System.out.println(que.poll().x);
		System.out.println(que.poll().x);
		System.out.println("Point(hashCode)");
		HashMap<Point,Integer> map = new HashMap<>();
		map.put(p1,1);
		map.put(p2,2);
		map.put(p3,3);
		map.put(p4,4);
		System.out.println(map.get(p4));
		System.out.println(map.get(p3));
		System.out.println(map.get(p2));
		System.out.println(map.get(p1));
		System.out.println("BIT");
		int[] base = {0,1,2,3,4,5,6,7,8,9,10};
		BIT bit = new BIT(base);
		System.out.println(" (sum)1-10:"+bit.getSum(1, 10));
		System.out.println("      4-6:"+bit.getSum(4, 6));
		bit.add(5, 3);
		System.out.println(" (add 5->3)4-6:"+bit.getSum(4, 5));
	}
}

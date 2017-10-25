package template;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class UsableMethods {

	int[][] wv = { { 2, 3 }, { 1, 2 }, { 3, 4 }, { 2, 2 } };
	int[][] edge = { { 0, 9, 2, 1 }, { 9, 0, 3, 1 }, { 2, 3, 0, 4 }, { 1, 1, 4, 0 } };
	int MAX_I = 4;
	int MAX_C = 5;

	public void test() {
		System.out.println(solveMemoRec(1, 5));
		// int[] dis = solveDijkstra(edge,0);
		int[] dis = solveBF(edge, 0);
		for (int i : dis) {
			System.out.print(i + " ");
		}
		// boolean negative = solveBF(edge,0);
		// System.out.println(negative);

		System.out.println("\n");
		solveWF(edge);
		for (int i = 0; i < edge.length; i++) {
			for (int j = 0; j < edge.length; j++) {
				System.out.print(edge[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("#BS test");
		int[] list = { 1, 2, 2, 4, 6, 10, 10, 10, 12, 15 };
		System.out.println(lowerBound(list, 10, -1, 10));
		System.out.println(upperBound(list, 10, -1, 10));
		System.out.println(upperBound(list, 2, -1, 10) - lowerBound(list, 2, -1, 10));
		System.out.println(lowerBound(list, 0, -1, 10));
		System.out.println(lowerBound(list, 100, -1, 10));
	}

	// BinarySearch //
	//////////////////////////////////////////////////////////////
	// OKとなる最大のindexを求める
	// 常に checkNext(left) -> true, checkNext(right) -> false
	// checkNext(right),checkNext(left)がアクセスされることは無い
	private int maxBinarySerch(int target, int left, int right) {// left-OK
																	// right-notOK
		// bottom
		if (right - left <= 1) {
			return left;
		}
		// recursion
		int next = left + (right - left) / 2;
		if (checkNext(target, next)) {// OK -> right
			return maxBinarySerch(target, next, right);
		} else { // notOK -> left
			return maxBinarySerch(target, left, next);
		}
	}

	// OKとなる最小のindexを求める
	// 常に checkNext(left) -> false, checkNext(right) -> true
	// checkNext(left),checkNext(right)がアクセスされることは無い
	private int minBinarySerch(int target, int left, int right) {
		if (right - left <= 1) {
			return right;
		}
		int next = left + (right - left + 1) / 2;
		if (checkNext(target, next)) {
			return minBinarySerch(target, left, next);
		} else {
			return minBinarySerch(target, next, right);
		}
	}

	private boolean checkNext(int target, int next) {
		return next < target;
	}

	//////////////////////////////////////////////////////////////

	// lowerBound, upperBound
	// target >= (>) list[i] となる最小のi
	//////////////////////////////////////////////////////////////
	private int lowerBound(int[] list, int target, int left, int right) {
		if (right - left <= 1)
			return right;
		int index = left + (right - left + 1) / 2;
		if (list[index] >= target) {
			return lowerBound(list, target, left, index);
		} else {
			return lowerBound(list, target, index, right);
		}
	}

	private int upperBound(int[] list, int target, int left, int right) {
		if (right - left <= 1)
			return right;
		int index = left + (right - left + 1) / 2;
		if (list[index] > target) {
			return upperBound(list, target, left, index);
		} else {
			return upperBound(list, target, index, right);
		}
	}

	//////////////////////////////////////////////////////////////

	/// DP //
	//////////////////////////////////////////////////////////////
	private int solveDP(int iMax, int jMax) {
		// initialize
		int[][] dp = new int[iMax + 1][jMax + 1];

		// DP
		for (int i = 1; i <= iMax; i++) {
			// i-th cost & value
			int cost = getCost(i);
			int value = getValue(i);
			// cost loop
			for (int j = 0; j <= jMax; j++) {
				if (j - cost < 0) {
					dp[i][j] = dp[i - 1][j];
				} else {
					dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - cost] + value);
				}
			}
		}
		return dp[iMax][jMax];
	}

	private int getCost(int id) {
		// id is 1.2.3....
		return wv[id - 1][0];
	}

	private int getValue(int id) {
		// id is 1.2.3....
		return wv[id - 1][1];
	}

	///////////////////////////////////////////////////////////////

	// Memo Recursion //
	//////////////////////////////////////////////////////////////

	int memo[][] = new int[MAX_I + 2][MAX_C + 2]; // for 0 & bottom

	private int solveMemoRec(int startI, int startJ) {
		// initialize memo
		for (int i = 0; i <= MAX_I; i++) {
			for (int j = 0; j <= MAX_C; j++) {
				memo[i][j] = -1;
			}
		}
		// solve
		return memoRec(startI, startJ);
	}

	private int memoRec(int i, int j) {
		// refer memo
		int m = getMemo(i, j);
		if (m >= 0) {
			return m;
		}

		int ans;
		// bottom
		if (i > MAX_I) {
			ans = 0;
		} else {
			// recursion
			int cost = getCost(i);
			int value = getValue(i);
			if (j < cost) {
				ans = memoRec(i + 1, j);
			} else {
				ans = Math.max(memoRec(i + 1, j), memoRec(i + 1, j - cost) + value);
			}
		}
		// record
		recordMemo(i, j, ans);
		return ans;
	}

	private int getMemo(int i, int j) {
		return memo[i][j];
	}

	private void recordMemo(int i, int j, int result) {
		memo[i][j] = result;
	}
	/*
	 * private int getCost(int id){ // id is 1.2.3.... return wv[id-1][0]; }
	 * 
	 * private int getValue(int id){ // id is 1.2.3.... return wv[id-1][1]; }
	 */

	/////////////////////////////////////////////////////////////

	// Bellman-Ford O(VE)//
	////////////////////////////////////////////////////////////
	private int[] solveBF(int[][] edge, int start) {
		int INF = Integer.MAX_VALUE / 10;
		int d[] = new int[edge.length];
		Arrays.fill(d, INF); // when get negative-loop,fill 0
		d[start] = 0;
		boolean update = true;
		for (int k = 0; update && k < edge.length; k++) {
			for (int i = 0; i < edge.length; i++) { // for all edges
				for (int j = 0; j < edge[i].length; j++) {
					if (d[i] + edge[i][j] < d[j]) {
						d[j] = d[i] + edge[i][j];
						update = true;
						// if(k==edge.length-1) return true;
					}
				}
			}
		}
		return d;
		// return false;
	}

	/////////////////////////////////////////////////////////////

	// Dijkstra O(ElogV) //
	/////////////////////////////////////////////////////////////
	private int[] solveDijkstra(int[][] edge, int start) {// none - INF
		int INF = Integer.MAX_VALUE / 10;
		int[] d = new int[edge.length];
		Arrays.fill(d, INF);
		d[start] = 0;

		// [index,distance]
		PriorityQueue<Integer[]> que = new PriorityQueue<>((x1, x2) -> Integer.compare(x1[1], x2[1]));
		que.add(new Integer[] { start, 0 });

		while (!que.isEmpty()) {
			Integer[] v = que.poll();
			int index = v[0];
			int dist = v[1];
			if (dist != d[index])
				continue;
			for (int i = 0; i < edge.length; i++) {
				if (dist + edge[index][i] < d[i]) {
					d[i] = dist + edge[index][i];
					que.add(new Integer[] { i, d[i] });
				}
			}
		}
		return d;
	}

	////////////////////////////////////////////////////////////

	// Warshall-Floyd O(V^3)//
	/////////////////////////////////////////////////////////////
	private void solveWF(int[][] edge) { // none - INF
		for (int k = 0; k < edge.length; k++) {
			for (int i = 0; i < edge.length; i++) {
				for (int j = 0; j < edge.length; j++) {
					edge[i][j] = Math.min(edge[i][j], edge[i][k] + edge[k][j]);
				}
			}
		}
	}

	/////////////////////////////////////////////////////////////

	// LIS O(nlogn) 最長増加部分列
	/////////////////////////////////////////////////////////////
	private int lis(int[] list) {
		int[] dp = new int[list.length];
		Arrays.fill(dp, Integer.MAX_VALUE);
		for (int h : list) {
			int index = -Arrays.binarySearch(dp, h) - 1;
			dp[index] = h;
		}
		return -Arrays.binarySearch(dp, Integer.MAX_VALUE - 1) - 1;
	}
	/////////////////////////////////////////////////////////////

	// 二部グラフ判定
	////////////////////////////////////////////////////////////
	private boolean isBipartite(List<List<Integer>> edge, int sizeV) {
		boolean bip = true;

		int[] color = new int[sizeV];
		LinkedList<Integer> stack = new LinkedList<>();
		color[0] = 1;
		stack.push(0);

		while (!stack.isEmpty()) {
			int node = stack.pop();
			int size = edge.get(node).size();
			for (int i = 0; i < size; i++) {
				int next = edge.get(node).get(i);
				if (color[next] == 0) {
					color[next] = (-1) * color[node];
					stack.push(next);
				} else {
					if (color[next] == color[node]) {
						bip = false;
					}
				}
			}
		}
		return bip;
	}

	public static void main(String[] args) {
		new UsableMethods().test();
	}
}

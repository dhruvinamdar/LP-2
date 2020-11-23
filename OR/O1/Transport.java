public class Transport {
	
	public int[][] NWC(int[][] _cost, int[] _route, int[] _dest) {
		
		int[][] cost = new int[_cost.length][_cost[0].length];
		for(int i = 0; i<_cost.length; i++) 
			cost[i] = _cost[i].clone();
		int[] route = _route.clone();
		int[] dest = _dest.clone();
		
		int i = 0, j = 0;
		int alloc[][] = new int[cost.length][cost[0].length];
		while(i<route.length && j<dest.length) {
			int min = route[i]>dest[j]?dest[j]:route[i];
			alloc[i][j] = min;
			route[i] -= min;
			dest[j] -= min;
			if(route[i]==0)
				i++;
			if(dest[j]==0)
				j++;
		}
		return alloc;
	}
	
	public int[][] LCM(int[][] _cost, int[] _route, int[] _dest) {
		
		int[][] cost = new int[_cost.length][_cost[0].length];
		for(int i = 0; i<_cost.length; i++) 
			cost[i] = _cost[i].clone();
		int[] route = _route.clone();
		int[] dest = _dest.clone();
		
		int alloc[][] = new int[cost.length][cost[0].length];
		int max = Integer.MAX_VALUE;
		while(true) {
			int i = 0, j = 0;
			for(int x = 0; x<cost.length; x++) {
				for(int y = 0; y<cost[0].length; y++) {
					if(cost[x][y] < cost[i][j]) {
						i = x;
						j = y;
					}
				}
			}
			if(cost[i][j] == max) 
				break;
			int min = route[i]>dest[j]?dest[j]:route[i];
			alloc[i][j] = min;
			route[i] -= min;
			dest[j] -= min;
			if(route[i]==0) {
				for(int x = 0; x<dest.length; x++)
					cost[i][x] = max;
			}
			if(dest[j]==0) {
				for(int x = 0; x<route.length; x++)
					cost[x][j] = max;
			}
		}
		return alloc;
	}
	
	public int[] penaltyUpdate(int[][] cost) {
		int penal[] = new int[cost.length + cost[0].length];
		int max = Integer.MAX_VALUE;
		// row penalties
		for(int i = 0; i<cost.length; i++) {
			int min, second;
			min = second = max;
			for(int j = 0; j<cost[0].length; j++) {
				if(cost[i][j] < min) {
					second = min;
					min = cost[i][j];
				}
				else if(cost[i][j] < second && cost[i][j] != min)
					second = cost[i][j];
			}
			penal[i] = second == max ? (min == max ? 0 : min) : (second - min);
		}
		
		// column penalties
		for(int i = 0; i<cost[0].length; i++) {
			int min, second;
			min = second = max;
			for(int j = 0; j<cost.length; j++) {
				if(cost[j][i] < min) {
					second = min;
					min = cost[j][i];
				}
				else if(cost[j][i] < second && cost[j][i] != min)
					second = cost[j][i];
			}
			penal[i+cost.length] = second == max ? (min == max ? 0 : min) : (second - min);
		}
		
		for(int i = 0; i<penal.length; i++)
			System.out.print(penal[i] + " ");
		System.out.println();
		return penal;
	}
	
	public int[][] VAM(int[][] _cost, int[] _route, int[] _dest) {
		
		int[][] cost = new int[_cost.length][_cost[0].length];
		for(int i = 0; i<_cost.length; i++) 
			cost[i] = _cost[i].clone();
		int[] route = _route.clone();
		int[] dest = _dest.clone();
		
		int alloc[][] = new int[cost.length][cost[0].length];
		int penal[] = new int[cost.length + cost[0].length];
		
		int max = Integer.MAX_VALUE;
		while(true) {
			int i = 0, j = 0;
			
			penal = penaltyUpdate(cost);
			int maxpenal = Integer.MIN_VALUE;
			int maxk = 0;
			for(int k = 0; k<penal.length; k++) {
				if(maxpenal < penal[k]) {
					maxpenal = penal[k];
					maxk = k;
				}
			}
			
			if(maxk>=cost.length) {
				j = maxk - cost.length;
				int mincost = max;
				for(int x = 0; x<cost.length; x++) {
					if(cost[x][j] < mincost) {
						mincost = cost[x][j];
						i = x;
					}
				}
			}
			else {
				i = maxk;
				int mincost = max;
				for(int x = 0; x<cost[0].length; x++) {
					if(cost[i][x] < mincost) {
						mincost = cost[i][x];
						j = x;
					}
				}
			}
			
			if(cost[i][j] == max) 
				break;
			int min = route[i]>dest[j]?dest[j]:route[i];
			alloc[i][j] = min;
			route[i] -= min;
			dest[j] -= min;
			if(route[i]==0) {
				for(int x = 0; x<dest.length; x++)
					cost[i][x] = max;
			}
			if(dest[j]==0) {
				for(int x = 0; x<route.length; x++)
					cost[x][j] = max;
			}

			for(int x = 0; x<alloc.length; x++) {
				for(int y = 0; y<alloc[0].length; y++) {
					System.out.print(alloc[x][y] + "\t");
				}
				System.out.print("\n");
			}
			System.out.print("\n");
		}
		return alloc;
	}
	
	public int sumCosts(int[][] alloc, int[][] cost) {
		int sum = 0;
		for(int i = 0; i<alloc.length; i++) {
			for(int j = 0; j<alloc[0].length; j++) {
				sum = sum + alloc[i][j]*cost[i][j];
			}
		}
		return sum;
	}
	
	public static void main(String[] args) {
//		Scanner sc = new Scanner(System.in);
//		int routecnt = 0, destcnt = 0;
//		System.out.print("Enter the number of routes : ");
//		routecnt = sc.nextInt();
//		System.out.print("Enter the number of destinations : ");
//		destcnt = sc.nextInt();
//		int[] supply = new int[routecnt];
//		System.out.print("Enter the supply for each route : ");
//		for(int i = 0; i<routecnt; i++) {
//            supply[i] = sc.nextInt();
//        }
//		int[] demand = new int[destcnt];
//		System.out.print("Enter the demand for each destination : ");
//		for(int i = 0; i<destcnt; i++) {
//            demand[i] = sc.nextInt();
//        }
//		int[][] costs = new int[routecnt][destcnt];
//		System.out.print("Enter the costs : ");
//		for(int i = 0; i<routecnt; i++) {
//            for(int j = 0; j<destcnt; j++){
//                costs[i][j] = sc.nextInt();
//            }
//        }
		int[] supply = {150, 160, 90};
		int[] demand = {140, 120, 90, 50};
		int[][] costs = 
			{{16, 18, 21, 12}, 
			 {17, 19, 14, 13}, 
			 {32, 11, 15, 10}};
		
		Transport obj = new Transport();
		
        int result1 = obj.sumCosts(obj.NWC(costs, supply, demand), costs);
        
        int result2 = obj.sumCosts(obj.LCM(costs, supply, demand), costs);
        
        int result3 = obj.sumCosts(obj.VAM(costs, supply, demand), costs);

		System.out.println("From NorthWest Corner method, total cost = " + result1);
		System.out.println("From Least Cost method, total cost = " + result2);
		System.out.println("From Vogel's Approximation method, total cost = " + result3);
		
	}
	
}

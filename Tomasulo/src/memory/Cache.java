package memory;

public class Cache {
	static int level;
	static L1Cache l1Cache;
	static L2Cache l2Cache;
	static L3Cache l3Cache;
	

	public Cache(int lev, L1Cache l1, L2Cache l2, L3Cache l3) {
		level = lev;
		l1Cache = l1;
		l2Cache = l2;
		l3Cache = l3;
	}


	public static int read(int address, long currentTime, Instruction instruction) throws Exception{
		instruction.setCacheStartTime(currentTime);
		int value;
		
		
		/**
		 * If level 1 cache only
		 * if(found) return the value and set the end time to current time + access time of l1 cache
		 * else call read miss and set the end time to current time + access time of l1 cache + mem access time
		 */
		if(level == 1){
			try{
				value = l1Cache.readFromCache(address, currentTime);
				instruction.setCacheEndTime(currentTime + l1Cache.getCycles());
			} catch(Exception e) {
				//Not found in the cache
				if(e.getMessage().equals(L1Cache.NOT_FOUND)){
					value = l1Cache.readMiss(address, currentTime);
					instruction.setCacheEndTime(currentTime + l1Cache.getCycles() + Memory.getAccessTime());
				} else {
					throw e;
				}
			}
			return value;
		} else 
			/**
			 * If two level caches, try in L1
			 * if(found) return value in L1 and set end time of the reading from cache
			 * else
			 * try to find it in L2
			 * if(found) return the value
			 * else
			 * read miss from L2
			 */
			if(level == 2) {
			try{
				value = l1Cache.readFromCache(address, currentTime);
				instruction.setCacheEndTime(currentTime + l1Cache.getCycles());
			} catch(Exception e) {
				/**
				 * Not found in L1 cache, try L2 cache.
				 */
				if(e.getMessage().equals(L1Cache.NOT_FOUND)){
					try{
						value = l2Cache.readFromCache(address, currentTime);
						instruction.setCacheEndTime(currentTime + l1Cache.getCycles() + l2Cache.getCycles());
					} catch(Exception e1){
						/**
						 * Not found in L2, read miss from L2.
						 */
						if(e1.getMessage().equals(L2Cache.NOT_FOUND)){
							value = l2Cache.readMiss(address, currentTime);
							instruction.setCacheEndTime(currentTime + l1Cache.getCycles() + l2Cache.getCycles() + Memory.getAccessTime());
						} else {
							throw e1;
						}
					}
				} else {
					throw e;
				}
			}
			return value;
		} 
		/**
		 * If three level caches, try in L1
		 * if(found) return value in L1 and set end time of the reading from cache
		 * else
		 * try to find it in L2
		 * if(found) return the value
		 * else
		 * try to find it in L3
		 * if(found) return the value
		 * else
		 * Read miss from L3
		 */
			
			else	{
			try{
				value = l1Cache.readFromCache(address, currentTime);
				instruction.setCacheEndTime(currentTime + l1Cache.getCycles());
			} catch(Exception e) {
				/**
				 * Not found in L1 cache, try L2 cache.
				 */
				if(e.getMessage().equals(L1Cache.NOT_FOUND)){
					try{
						value = l2Cache.readFromCache(address, currentTime);
						instruction.setCacheEndTime(currentTime + l1Cache.getCycles() + l2Cache.getCycles());
					} catch(Exception e1){
						/**
						 * Not found in L2, try in L3.
						 */
						if(e1.getMessage().equals(L2Cache.NOT_FOUND)){
							try{
								value = l3Cache.readFromCache(address, currentTime);
								instruction.setCacheEndTime(currentTime + l1Cache.getCycles() + l2Cache.getCycles() + l3Cache.getCycles());
							} catch(Exception e2){
								/**
								 * Not found in L3, read miss from L3.
								 */
								if(e2.getMessage().equals(L3Cache.NOT_FOUND)){
									value = l3Cache.readMiss(address, currentTime);
									instruction.setCacheEndTime(currentTime + l1Cache.getCycles() + l2Cache.getCycles() + l3Cache.getCycles() + Memory.getAccessTime());
								} else {
									throw e2;
								}
							}
						} else {
							throw e1;
						}
					}
				} else {
					throw e;
				}
			}
			return value;
		}
	}
	
	
	public static void write(int address, int value,  long currentTime, Instruction instruction) throws Exception{
		l1Cache.writeToCache(address, value, currentTime, instruction);
		//System.out.println(Processor.mem.toString());
	}
    /*public static void main(String[] args) throws Exception {

    	L1Cache nc = new L1Cache(L1Cache.WRITE_BACK, 10, 256, 32, 2);
    	L2Cache nc2 = new L2Cache(L1Cache.WRITE_BACK, 10, 256, 64, 2);
    	L3Cache nc3 = new L3Cache(L1Cache.WRITE_BACK, 10, 512, 128, 2);
    	Memory mem = new Memory(1024, 100);
    	nc3.setL1(nc);
    	nc3.setL2(nc2);
    	nc2.setL3(nc3);
    	nc2.setL1(nc);
    	nc.setL2(nc2);
    	nc.setL3(nc3);
    	Cache c = new Cache(3, nc, nc2, nc3);
		Memory.store(0, 4);
		Memory.store(1, 6);
		Memory.store(2, 8);
		Memory.store(3, 9);
		Memory.store(4, 0);
		Memory.store(5, 3);
		Memory.store(6, 7);
		Memory.store(7, 6);
		Instruction s = new Instruction();
		//c.write(0, 1, 0, s);
		//c.write(0, 10, currentTime, instruction)
		//System.out.println(c.read(22, 5, s));
		//System.out.println(c.read(7, 7, s));
		
		c.write(6, 19999, 1, s);
		System.out.println(nc3);
		System.out.println(nc2);
		System.out.println(nc);
		System.out.println(mem);
		
		System.out.println(read(22, 11, s));
		System.out.println(read(14, 8, s));
		
		System.out.println("*****************");
		System.out.println(nc3);
		System.out.println(nc2);
		System.out.println(nc);
		System.out.println(mem);
    }*/
}

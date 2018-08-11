package threadlock1;

public class DieLock extends Thread {
	
	private boolean flag;
	
	public DieLock(boolean flag) {
		this.flag = flag;
	}
	
	@Override
	public void run() {
		if (flag) {
			synchronized (Mylock.objA){
				System.out.println("if objA");
				synchronized (Mylock.objB) {
					System.out.println("if objB");
				}
			}
		}else {
			synchronized (Mylock.objB){
				System.out.println("else objB");
				synchronized (Mylock.objB) {
					System.out.println("else objA");
				}
			}
		}
	}

}

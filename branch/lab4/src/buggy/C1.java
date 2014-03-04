package buggy;

public class C1 extends C0{

	C1(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	@Override
	public int compute() {
		return x+y;
	}

}

package buggy;

public class C2 extends C0{

	C2(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	@Override
	public int compute() {
		return x-y;
	}
}

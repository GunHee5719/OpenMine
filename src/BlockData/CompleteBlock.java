package BlockData;

public class CompleteBlock extends Block{
    public CompleteBlock(int i, int j, int number) {
        super(i, j, number);
    }

    @Override
    public void MouseClick(int i, int j) {
        System.out.println("CompleteBlock can not be clicked!");
    }
}
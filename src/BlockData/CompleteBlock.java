package BlockData;

public class CompleteBlock extends Block{
    public CompleteBlock(int i, int j) {
        super(i, j);
    }

    @Override
    public void MouseClick() {
        // 완료된 블록은 애초에 이 함수가 호출될 일이 없긴 함.
        System.out.println("CompleteBlock can not be clicked!");
    }
}
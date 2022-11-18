package bridge;

import java.util.ArrayList;
import java.util.List;
/**
 * 다리 건너기 게임을 관리하는 클래스
 */
public class BridgeGame {
    OutputView output = new OutputView();
    InputView input = new InputView();
    CheckError check = new CheckError();
    BridgeRandomNumberGenerator Maker = new BridgeRandomNumberGenerator();
    BridgeMaker Bridge = new BridgeMaker(Maker);
    int size;
    List<String> bridge_answer;
    List<String> bridge_input = new ArrayList<>();
    int count = 0;
    String[] Bridge_out;

    /**
     * 사용자가 칸을 이동할 때 사용하는 메서드
     * <p>
     * 이동을 위해 필요한 메서드의 반환 타입(return type), 인자(parameter)는 자유롭게 추가하거나 변경할 수 있다.
     */
    public boolean move(String choice) {
        bridge_input.add(choice);
        int num = bridge_input.size() - 1;
        boolean same =  bridge_input.get(num).equals(bridge_answer.get(num));
        Bridge_out =  output.printMap(bridge_input,choice,same);
        return same;
    }

    /**
     * 사용자가 게임을 다시 시도할 때 사용하는 메서드
     * <p>
     * 재시작을 위해 필요한 메서드의 반환 타입(return type), 인자(parameter)는 자유롭게 추가하거나 변경할 수 있다.
     */
    public void retry() {
        count++;
        String retry_choice =  thirdStep();
        if(retry_choice.equals("R")) clear_global();
        if(retry_choice.equals("Q")){
            output.printResult(Bridge_out,false,count);
        }
        //retry_choice 가 Q일경우 종료
        //retry_choice 가 R인 경우 다시시작
    }
    public void clear_global(){
        bridge_input.clear();
        repeat_input();
    }
    public void start(){
        output.printStart();
        firstStep();
        bridge_answer =  Bridge.makeBridge(size);
        System.out.println(bridge_answer);
        repeat_input();
    }

    public void firstStep(){
        output.printFirst();
        try {
            size = input.readBridgeSize();
            check.checkBridgeSize(size);
        }catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
            firstStep();
        }
    }

    public String secondStep(){
        String input_temp = "";
        output.printSecond();
        try {
            input_temp = input.readMoving();
            check.checkChoice(input_temp);
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return secondStep();
        }
        return input_temp;
    }

    public void repeat_input(){
        boolean answer = true;
        for(int i = 0 ; i < size ; i++) {
            String choice = secondStep();
            answer =  move(choice);
            if(answer == false) break;
        }
        //if(answer == true) 최종결과 출력
        if(answer == false) retry();
    }
    public String thirdStep(){
        String input_temp = "";
        output.printRetry();
        try {
            input_temp = input.readGameCommand();
            check.checkRetry(input_temp);
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return thirdStep();
        }
        return input_temp;
    }
}

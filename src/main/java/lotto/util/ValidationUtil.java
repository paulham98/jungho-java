package lotto.util;

import static lotto.enums.LottoNumber.FIRST;
import static lotto.enums.LottoNumber.LAST;
import static lotto.enums.LottoNumber.LEAST_PURCHASE_AMOUNT;
import static lotto.enums.LottoNumber.LENGTH;
import static lotto.enums.message.ErrorMessage.BUY_REMAINDER;
import static lotto.enums.message.ErrorMessage.BUY_TYPE;
import static lotto.enums.message.ErrorMessage.NUMBER_BLANK;
import static lotto.enums.message.ErrorMessage.NUMBER_COUNT;
import static lotto.enums.message.ErrorMessage.NUMBER_DUPLICATE;
import static lotto.enums.message.ErrorMessage.NUMBER_RANGE;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ValidationUtil {

    /*
    구매 금액에 대한 검증
    - 숫자가 맞는지
    - 1000원 단위 인지
    */

    public int validateBuy(String amount) {
        validateNumberType(amount);
        int buyAmount = Integer.parseInt(amount);

        if (buyAmount % LEAST_PURCHASE_AMOUNT.getNumber() != 0) {
            throw new IllegalArgumentException(BUY_REMAINDER);
        }
        return buyAmount;
    }

    private void validateNumberType(String input) {
        String regex = "[0-9]+";

        if (!Pattern.matches(regex, input)) {
            throw new IllegalArgumentException(BUY_TYPE);
        }
    }
    /*
    당첨 번호에 관한 검즘
    - 숫자가 맞는지
    - 1~45가 맞는지
    - 중복은 없는지
     */
    public List<Integer> validateWinningNumber(String input){
        String[] inputSplit = input.split(",");
        validationWinningNumbers(inputSplit);
        InputUtil inputUtil = new InputUtil();
        List<Integer> winningNumbers = inputUtil.strToIntegerList(inputSplit);
        validateWinningNumbersRange(winningNumbers);
        return winningNumbers;
    }
    private void validationWinningNumbers(String[] input){
        validateNumbersLength(input);
        validateBlank(input);
        validateNumbers(input);
        validateDuplicate(input);
    }
    private void validateNumbersLength(String[] input){
        if(input.length != LENGTH.getNumber()){
            throw new IllegalArgumentException(NUMBER_COUNT);
        }
    }

    private void validateBlank(String[] input){
        boolean isBlank = Arrays.stream(input)
                .noneMatch(str -> str.equals(" ") || str.equals(""));

        if(!isBlank) {
            throw new IllegalArgumentException(NUMBER_BLANK);
        }
    }

    private void validateNumbers(String[] input){
        for(String num : input){
            validateNumberType(num);
        }
    }
    private void validateWinningNumbersRange(List<Integer> input){
        for(int num : input){
            if(isNotInRange(num)){
                throw new IllegalArgumentException(NUMBER_RANGE);
            }
        }
    }
    public void validateBonusNumberRange(int number){
        if(isNotInRange(number)){
            throw new IllegalArgumentException(NUMBER_RANGE);
        }
    }
    public void validateBonusNumberDuplicate(int number, List<Integer> playerNums){
        for(Integer num : playerNums){
            if(number == num){
                throw new IllegalArgumentException(NUMBER_DUPLICATE);
            }
        }
    }
    private boolean isNotInRange(int number){
        return number < FIRST.getNumber() || number > LAST.getNumber();
    }
    private void validateDuplicate(String[] input){
        if(Arrays.stream(input).distinct().count() != LENGTH.getNumber()) {
            throw new IllegalArgumentException(NUMBER_DUPLICATE);
        }
    }

}

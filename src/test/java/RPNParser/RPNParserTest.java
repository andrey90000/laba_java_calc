package RPNParser;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static java.lang.Math.pow;

public class RPNParserTest extends TestCase {
    protected String input1;
    protected RPNParser p;
    protected String input2;
    protected String input3;
    protected double checkAnswer;

    protected double checkNumeric;

    @Before
    public void setUp() throws Exception {
        p = new RPNParser();
        input1 = "-15*(7-mod10(58)+sqrt(12*98+(9+5*6)))/5+(12.2-(9+8*(7+8/4)))";
        input2 = "2+2*2+";
        input3 = "2*(2+9/9)";
    }

    @Test
    public void testIsNumeric() {
        String a = "23.87";
        String b = "34a.9";
        RPNParser p = new RPNParser();
        Assert.assertEquals(true, p.isNumeric(a));
        Assert.assertEquals(false, p.isNumeric(b));
    }

    @Test
    public void testIsOperator() {
        String operand = "*";
        String notOperand = ")";
        String notOperand2 = "*&";
        Assert.assertEquals(true, p.isOperator(operand));
        Assert.assertEquals(false, p.isOperator(notOperand));
        Assert.assertEquals(false, p.isOperator(notOperand2));
    }

    @Test
    public void testIsInputCorrect() {
        Assert.assertEquals(true, p.isInputCorrect(input1));
        Assert.assertEquals(false, p.isInputCorrect(input2));
    }

    @Test
    public void testIsStrToRPN() {
        String expected1 = " 15 u- 7 58 mod10 - 12 98 * 9 5 6 * + + sqrt + * 5 / 12.2 9 8 7 8 4 / + * + - +";
        String expected3 = " 2 2 9 9 / + *";
        Assert.assertEquals(expected1, p.StrToRPN(input1));
        Assert.assertEquals(expected3, p.StrToRPN(input3));


    }

}
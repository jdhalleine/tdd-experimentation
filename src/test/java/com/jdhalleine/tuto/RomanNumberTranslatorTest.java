package com.jdhalleine.tuto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RomanNumberTranslatorTest {

    @BeforeEach
    public void init(){
        RomanNumberTranslator.ret=0;
    }

    @Test
    public void emptyStringShouldReturnZero(){
       Assertions.assertEquals(0, RomanNumberTranslator.translate(""));
    }

    @Test
    public void IShouldReturnOneRecursiveStyle(){
        Assertions.assertEquals(1, RomanNumberTranslator.translateV2("I"));
    }

    @Test
    public void IIShouldReturnTwoRecursiveStyle(){
        Assertions.assertEquals(2, RomanNumberTranslator.translateV2("II"));
    }

    @Test
    public void XIShouldReturnElevenRecursiveStyle(){
        Assertions.assertEquals(11, RomanNumberTranslator.translateV2("XI"));
    }

    @Test
    public void iiShouldReturnTwo(){
       Assertions.assertEquals(2, RomanNumberTranslator.translate("II"));
    }

    @Test
    public void iiiShouldReturnThree(){
        Assertions.assertEquals(3, RomanNumberTranslator.translate("III"));
    }

    @Test
    public void xxShouldReturnTwenty(){
        Assertions.assertEquals(20, RomanNumberTranslator.translate("XX"));
    }

    @Test
    public void xiShouldReturnEleven(){
        Assertions.assertEquals(11, RomanNumberTranslator.translate("XI"));
    }

    @Test
    public void ixShouldReturnNine(){
        Assertions.assertEquals(9, RomanNumberTranslator.translate("IX"));
    }

    @Test
    public void ivShouldReturn4(){
        Assertions.assertEquals(4, RomanNumberTranslator.translate("IV"));
    }

    @Test
    public void ivShouldReturn4RecursiveStyle(){
        Assertions.assertEquals(4, RomanNumberTranslator.translateV2("IV"));
    }

    @Test
    public void XLShouldReturn40(){
        Assertions.assertEquals(40, RomanNumberTranslator.translate("XL"));
    }

    @Test
    public void xviShouldReturnSixteen(){
        Assertions.assertEquals(16, RomanNumberTranslator.translate("XVI"));
    }

    @Test
    public void xviShouldReturnSixteenRecursiveStyle(){
        Assertions.assertEquals(16, RomanNumberTranslator.translateV2("XVI"));
    }


    @Test
    public void romanFigureStringShouldReturnTheGoodNumber(){
        Assertions.assertEquals(10, RomanNumberTranslator.translate("X"));
        Assertions.assertEquals(1, RomanNumberTranslator.translate("I"));
        Assertions.assertEquals(1000, RomanNumberTranslator.translate("M"));
        Assertions.assertEquals(50, RomanNumberTranslator.translate("L"));
        Assertions.assertEquals(100, RomanNumberTranslator.translate("C"));
        Assertions.assertEquals(500, RomanNumberTranslator.translate("D"));
        Assertions.assertEquals(5, RomanNumberTranslator.translate("V"));
    }


}

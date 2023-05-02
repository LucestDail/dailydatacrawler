package com.dailydatahub.dailydatacrawler.test.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dailydatahub.dailydatacrawler.selenium.service.SeleniumService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/test")
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TestController {

    @Autowired
    private SeleniumService seleniumService;

    /**
     * get can be present server data
     * like select in sql
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/process", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject selectData(@PathVariable("page") String strUrl) throws Exception {
        return seleniumService.getUrlPage(strUrl);
    }

    /**
     * post can change server data status
     * like merge, update in sql
     * merge seems like to be better
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/process", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject postMethod(JSONObject jsonRequestObject) throws Exception {
        return seleniumService.selectData(jsonRequestObject);
    }

    /**
     * put should be add server data
     * like insert in sql
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/process", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject insertData(JSONObject jsonRequestObject) throws Exception {
        return seleniumService.insertData(jsonRequestObject);
    }

    /**
     * delete can be delete server data
     * like delete in sql
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/process", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject deleteMethod(JSONObject jsonRequestObject) throws Exception {
        return seleniumService.deleteData(jsonRequestObject);
    }

    /**
     * hangul test request from my childhood friend, jang
     */
    @RequestMapping(value = "/jang", method = RequestMethod.GET)
    @ResponseBody
    public String hangulTest() throws Exception {

        List<String> jasoList = new ArrayList<String>();
        jasoList.add("");
        jasoList.add("ㅏ");
        jasoList.add("ㄴ");
        jasoList.add("ㄱ");
        jasoList.add("ㅡ");
        jasoList.add("ㄹ");
        return assemble(jasoList);
    }

    // First '가' : 0xAC00(44032), 끝 '힟' : 0xD79F(55199)
    private final int FIRST_HANGUL = 44032;

    // 19 initial consonants
    private final char[] CHOSUNG_LIST = {
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
            'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    private int JUNGSUNG_COUNT = 21;

    // 21 vowels
    private final char[] JUNGSUNG_LIST = {
            'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ',
            'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ',
            'ㅣ'
    };

    private int JONGSUNG_COUNT = 28;

    // 28 consonants placed under a vowel(plus one empty character)
    private final char[] JONGSUNG_LIST = {
            ' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ',
            'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ',
            'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    public List<String> disassemble(char hangul) {
        List<String> jasoList = new ArrayList<>();

        String hangulStr = String.valueOf(hangul);

        if (hangulStr.matches(".*[가-힣]+.*")) {
            int baseCode = hangulStr.charAt(0) - FIRST_HANGUL;

            final int chosungIndex = baseCode / (JONGSUNG_COUNT * JUNGSUNG_COUNT);
            jasoList.add(Character.toString(CHOSUNG_LIST[chosungIndex]));

            final int jungsungIndex = (baseCode - ((JONGSUNG_COUNT * JUNGSUNG_COUNT) * chosungIndex)) / JONGSUNG_COUNT;
            jasoList.add(Character.toString(JUNGSUNG_LIST[jungsungIndex]));

            final int jongsungIndex = (baseCode - ((JONGSUNG_COUNT * JUNGSUNG_COUNT) * chosungIndex)
                    - (JONGSUNG_COUNT * jungsungIndex));
            if (jongsungIndex > 0) {
                jasoList.add(Character.toString(JONGSUNG_LIST[jongsungIndex]));
            }
        }
        return jasoList;
    }

    public List<String> disassemble(String hangul) {
        List<String> jasoList = new ArrayList<String>();

        for (int i = 0, li = hangul.length(); i < li; i++) {
            jasoList.addAll(disassemble(hangul.charAt(i)));
        }

        return jasoList;
    }

    public String assemble(List<String> jasoList) {
        String result = "";
        int startIdx = 0;
        while (true) {
            if (startIdx < jasoList.size()) {
                final int assembleSize = getNextAssembleSize(jasoList, startIdx);
                result += assemble(jasoList, startIdx, assembleSize);
                startIdx += assembleSize;
            } else {
                break;
            }
        }
        return result;
    }

    private String assemble(List<String> jasoList, final int startIdx, final int assembleSize) {
        int unicode = FIRST_HANGUL;

        final int chosungIndex = new String(CHOSUNG_LIST).indexOf(jasoList.get(startIdx));

        if (chosungIndex >= 0) {
            unicode += JONGSUNG_COUNT * JUNGSUNG_COUNT * chosungIndex;
        } else {
            System.out.println((startIdx + 1) + "번째 자소가 한글 종성이 아닙니다");
        }

        final int jungsungIndex = new String(JUNGSUNG_LIST).indexOf(jasoList.get(startIdx + 1));

        if (jungsungIndex >= 0) {
            unicode += JONGSUNG_COUNT * jungsungIndex;
        } else {
            System.out.println((startIdx + 2) + "번째 자소가 한글 종성이 아닙니다");
        }

        if (assembleSize > 2) {
            final int jongsungIndex = new String(JONGSUNG_LIST).indexOf(jasoList.get(startIdx + 2));

            if (jongsungIndex >= 0) {
                unicode += jongsungIndex;
            } else {
                System.out.println((startIdx + 3) + "번째 자소가 한글 종성이 아닙니다");
            }
        }

        return Character.toString((char) unicode);
    }

    private int getNextAssembleSize(List<String> jasoList, final int startIdx) {
        int remainJasoLength = jasoList.size() - startIdx;
        int assembleSize = 0;

        if (remainJasoLength > 3) {
            if (new String(JUNGSUNG_LIST).contains(jasoList.get(startIdx + 3))) {
                assembleSize = 2;
            } else {
                assembleSize = 3;
            }
        } else if (remainJasoLength == 3 || remainJasoLength == 2) {
            assembleSize = remainJasoLength;
        } else {
            System.out.println("한글을 구성할 자소가 부족하거나 한글이 아닌 문자가 있습니다");
        }
        return assembleSize;
    }

}

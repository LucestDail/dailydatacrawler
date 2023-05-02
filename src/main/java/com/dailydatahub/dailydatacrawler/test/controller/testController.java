package com.dailydatahub.dailydatacrawler.test.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dailydatahub.dailydatacrawler.selenium.service.SeleniumService;

@RequestMapping("/test")
public class testController {

    @Autowired
    private SeleniumService seleniumService;
    /**
     * get can be present server data
     * like select in sql
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
    public String hangulTest() throws Exception{
        return "";
    }
    
}

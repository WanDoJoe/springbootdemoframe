package com.wdqsoft.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wdqsoft.common.lang.Result;
import com.wdqsoft.common.utils.aesutils.AESCoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class _BaseController {

    //是否需要加密
    private boolean isEncodeASE=false;
    //是否需要解密
    private boolean isDecodeASE=false;
    //ASE秘钥
    private String ASECode_Key="8aa978c8929f153cc446f84d604672c2";

//    @Autowired
//    ProtocolsFunction protocolsFunction;

    public JSONObject paramsToJO(String paramsStrJson){
        String decodeStr=paramsStrJson;
        if(isDecodeASE){
         //解密方法
            try {
                decodeStr=AESCoder.getData(ASECode_Key,paramsStrJson);
                log.info("解密后数据");
                log.info(decodeStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JSONObject parseObject= JSON.parseObject(decodeStr);
        JSONObject jsondata=parseObject.getJSONObject("jsondata");
            return jsondata;
        }

    public JSONObject paramsToJA(String paramsStrJson){
        String decodeStr=paramsStrJson;
        if(isDecodeASE){
            //解密方法
            try {
                decodeStr=AESCoder.getData(ASECode_Key,paramsStrJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JSONObject parseObject= JSON.parseObject(decodeStr);
        JSONObject jsondata=parseObject.getJSONObject("jsondata");
        jsondata.put("rows",parseObject.getString("rows")) ;
        jsondata.put("page",parseObject.getString("page"));

        return jsondata;
        }

    /**
     * 加密数据 返回给 前端
     * @param result
     * @return
     */
    public String decodeResponseDate(Result result){
        String json =JSON.toJSONString(result);

        try {
            if(isEncodeASE) {
                return AESCoder.setData(ASECode_Key, json);
            }else {
                return json;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  "";
    }

    public JSONObject getWxJSONob(String body){
        log.info(body);
        JSONObject object = JSON.parseObject(body);
        return object;
    }

}

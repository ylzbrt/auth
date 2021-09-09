package com.ylzinfo.brt.utils;

import cn.hutool.json.JSONUtil;
import com.ylzinfo.brt.entity.AuthReturnEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {
    public static void writeDenied(HttpServletResponse response, String flag, String message) throws IOException {
        AuthReturnEntity returnEntity=new AuthReturnEntity();
        returnEntity.setCode(flag);
        returnEntity.setMessage(message);
        String result = JSONUtil.toJsonStr(returnEntity);
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(result);
    }
}

import cn.hutool.core.util.ReUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
public class Swagger {
    @Test
    public void test(){
      log.info("{}",get("ReturnEntity«IPage«RepoProjectMap对象»»"));
      log.info("{}",get("ReturnEntity«List«RepoProjectMap对象»»"));
      log.info("{}",get("ReturnEntity«Set«RepoProjectMap对象»»"));
      log.info("{}",get("ReturnEntity«RepoProjectMap对象»"));
    }
    public String get(String t){
        final String s = ReUtil.get("«(.*)»", t, 1);
        if(StringUtils.isEmpty(s)){
            return "";
        }
        if(s.contains("«")){
            return get(s);
        }
        return s;
    }


}

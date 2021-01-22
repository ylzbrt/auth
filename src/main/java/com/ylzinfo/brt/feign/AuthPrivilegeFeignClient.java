package com.ylzinfo.brt.feign;



import com.ylzinfo.brt.entity.AuthReturnEntity;
import com.ylzinfo.brt.feign.dto.RegisterApiDTO;
import com.ylzinfo.brt.feign.vo.RegisterApiVO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("privilege")
public interface AuthPrivilegeFeignClient {



    @PostMapping("/resource/registerApi")
    AuthReturnEntity<RegisterApiVO> registerApi(@RequestBody RegisterApiDTO dto);

}

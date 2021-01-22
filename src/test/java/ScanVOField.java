import engine.RepoBO;
import engine.RepoRestrictBO;
import engine.entity.RepoRestrict;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.lang.reflect.*;
import java.util.*;

@Slf4j
public class ScanVOField {
    @Test
    public void getReturnType() throws NoSuchMethodException, ClassNotFoundException {
        final Method method = ScanVOField.class.getMethod("getBos");
        final AnnotatedType annotatedReturnType = method.getAnnotatedReturnType();
        final Type returnType = annotatedReturnType.getType();
        if (returnType instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) returnType)
                    .getActualTypeArguments();// 泛型类型列表
            for (Type type : types) {
                log.info("inner type={}",type);
            }
        }else{
            log.info("  type={}",returnType);
        }


    }

    @Test
    public void scanField() {
        Set<ApiField> apiFields = new HashSet<>();

        scanFieldItem(apiFields, RepoBO.class);
        for (ApiField apiField : apiFields) {
            log.info("apiField={}", apiField);
        }
        log.info("size={}", apiFields.size());
    }

    private void scanFieldItem(Set<ApiField> apiFields, Class aclass) {


        for (Field field : FieldUtils.getAllFields(aclass)) {
            field.setAccessible(true);
            if (field.getType().isAssignableFrom(List.class)) {
                Type gt = field.getGenericType();    //得到泛型类型
                ParameterizedType pt = (ParameterizedType) gt;
                Class lll = (Class) pt.getActualTypeArguments()[0];
                scanFieldItem(apiFields, lll);
                continue;
            }
            final ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            final ApiField apiField = new ApiField();
            apiField.setEnName(field.getName());

            if (annotation != null) {
                final String value = annotation.value();
                if (!StringUtils.isEmpty(value)) {
                    apiField.setCnName(value);
                }
            } else {
                apiField.setCnName(field.getName());
            }
            apiFields.add(apiField);
        }


    }

@Data
@AllArgsConstructor
@NoArgsConstructor
public static class ApiField {
    private String enName;
    private String cnName;
}

    public RepoBO getBo() {
        return null;
    }public List<RepoBO> getBos() {
        return null;
    }


}

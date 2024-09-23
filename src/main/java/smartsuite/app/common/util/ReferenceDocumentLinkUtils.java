package smartsuite.app.common.util;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReferenceDocumentLinkUtils {

    public static Map<String, List<Object>> findListReferenceDocIds(List<Map<String, Object>> searchList){
        if(!CollectionUtils.isEmpty(searchList)){
           Map<String,List<Object>> searchIds = searchList.stream()
                    .filter(Objects::nonNull)   //method ref
                    .flatMap(map -> map.entrySet().stream())
                    .collect(Collectors.groupingBy(
                            entry -> entry.getKey()+"s",
                            Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                    ));
           return searchIds;
        }else{
            return null;
        }
     }
}

package io.riggo.web.integration.parser;

import io.riggo.data.domain.EquipmentType;
import io.riggo.data.domain.Load;
import io.riggo.data.domain.LoadStop;
import io.riggo.data.domain.Shipper;

import java.util.Map;

public interface RequestBodyParserPostPutLoad {

    LoadStop resolveFirstStop(Map<String, Object> dataHashMap);

    LoadStop resolveLastStop(Map<String, Object> dataHashMap);

    Load resolveLoad(Map<String, Object> dataHashMap);

    Shipper resolveShipper(Map<String, Object> dataHashMap, Integer siteId);

    EquipmentType resolveEquipmentType(Map<String, Object> dataHashMap);
}

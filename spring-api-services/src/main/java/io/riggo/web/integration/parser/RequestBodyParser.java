package io.riggo.web.integration.parser;

import io.riggo.data.domain.EquipmentType;
import io.riggo.data.domain.Load;
import io.riggo.data.domain.LoadStop;
import io.riggo.data.domain.Shipper;

public interface RequestBodyParser {

    LoadStop resolveFirstStop();

    LoadStop resolveLastStop();

    Load resolveLoad();

    Shipper resolveShipper();

    EquipmentType resolveEquipmentType();
}

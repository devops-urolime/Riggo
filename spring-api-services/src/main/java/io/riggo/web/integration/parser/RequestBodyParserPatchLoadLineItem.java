package io.riggo.web.integration.parser;

import io.riggo.data.domain.LoadLineItem;

import java.util.List;
import java.util.Map;

public interface RequestBodyParserPatchLoadLineItem {

    List<LoadLineItem> resolveLoadLineItemsList(Map<String, Object> dataHashMap);
}

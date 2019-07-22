package io.riggo.web.integration.parser;

import io.riggo.data.domain.LoadLineItem;

import java.util.List;

public interface RequestBodyParserPatchLoadLineItem {

    List<LoadLineItem> resolveLoadLineItemsList();
}

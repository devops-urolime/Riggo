package io.riggo.web.integration.parser;

import io.riggo.data.domain.LoadStop;

import java.util.List;
import java.util.Map;

public interface RequestBodyParserPatchLoadStop {

    List<LoadStop> resolveLoadStopsList(Map<String, Object> dataHashMap);
}
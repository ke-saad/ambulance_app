package com.example.routingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "traffic-api-client", url = "${traffic.api.url}")
public interface TrafficApiClient {



    // TomTom Traffic Flow Segment Data
    @GetMapping("/traffic/services/{versionNumber}/flowSegmentData/{style}/{zoom}/{format}")
    Map<String, Object> getFlowSegmentData(
            @PathVariable("versionNumber") int versionNumber,
            @PathVariable("style") String style,
            @PathVariable("zoom") int zoom,
            @PathVariable("format") String format,
            @RequestParam("point") String point,
            @RequestParam(value = "unit", required = false) String unit,
            @RequestParam(value = "openLr", required = false) Boolean openLr,
            @RequestParam("key") String key
    );

    // TomTom Traffic Incident Details
    @GetMapping("/traffic/services/{versionNumber}/incidentDetails")
    Map<String, Object> getIncidentDetails(
            @PathVariable("versionNumber") int versionNumber,
            @RequestParam(value = "bbox", required = false) String bbox,
            @RequestParam(value = "ids", required = false) String ids,
            @RequestParam(value = "fields", required = false) String fields,
            @RequestParam(value = "language", required = false) String language,
            @RequestParam(value = "t", required = false) String t,
            @RequestParam(value = "categoryFilter", required = false) String categoryFilter,
            @RequestParam(value = "timeValidityFilter", required = false) String timeValidityFilter,
            @RequestParam("key") String key
    );

    @GetMapping("/calculateRoute/{format}")
    Map<String, Object> calculateRoute(
            @RequestParam("key") String key,
            @RequestParam("points") String points,
            @RequestParam(value = "instructionsType", required = false) String instructionsType,
            @RequestParam(value = "avoid", required = false) String avoid,
            @RequestParam(value = "travelMode", required = false) String travelMode,
            @RequestParam(value = "routeRepresentation", required = false) String routeRepresentation,
            @RequestParam(value = "computeBestOrder", required = false) String computeBestOrder
    );
}

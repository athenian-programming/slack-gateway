package com.sudothought.gateway;

import spark.Route;

public interface RouteSource {

  Route getRoute(final String name, final String command);
}

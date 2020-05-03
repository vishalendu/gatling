package com.yantriks.gatling.util.http

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicCheckExample extends Simulation {

  val httpconf = http.baseUrl("https://api.openbrewerydb.org")
  val scn = scenario("basicCheckScenario").exec(http("basicCheckRequest").get("/breweries")
    .check(status.is(200))
    .check(regex("\"name\":\".*Pizza.*\"")))
  setUp(scn.inject(atOnceUsers(1 ))).protocols(httpconf)


}
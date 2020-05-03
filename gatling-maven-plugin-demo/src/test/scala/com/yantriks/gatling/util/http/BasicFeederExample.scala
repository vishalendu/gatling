package com.yantriks.gatling.util.http

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.{Level,LoggerContext}

class BasicFeederExample extends Simulation {

  val states = csv("data/states.csv").circular

  val ctx: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
  ctx.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))

  val httpconf = http.baseUrl("https://api.openbrewerydb.org")
  val scn = scenario("basicGetScenario").feed(states)
    .exec(http("basicGetRequest").get("/breweries?by_state=${state}"))
  setUp(scn.inject(atOnceUsers(3 ))).protocols(httpconf)


}
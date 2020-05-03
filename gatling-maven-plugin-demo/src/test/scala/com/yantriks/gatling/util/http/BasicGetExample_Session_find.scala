package com.yantriks.gatling.util.http

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jsonpath.JsonPath

class BasicGetExample_Session_find extends Simulation {

//  val ctx: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
//  ctx.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))

  val httpconf = http.baseUrl("https://api.openbrewerydb.org")
  val scn = scenario("basicGetScenario").exec(http("basicGetRequest").get("/breweries")
    .check(jsonPath("$..website_url").find.saveAs("searchwebsite")))
      .exec {session =>
        val testattr = session("searchwebsite")
        println(s"<Session> ********************************************* \n ${testattr.validate[String]}")
        println(s"</Session> *********************************************")
      session}
  setUp(scn.inject(atOnceUsers(1 ))).protocols(httpconf)


}
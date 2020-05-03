package com.yantriks.gatling.util.http

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicGetExample_Session extends Simulation {

//  val ctx: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
//  ctx.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))

  val httpconf = http.baseUrl("https://api.openbrewerydb.org")
  val scn = scenario("basicGetScenario").exec(http("basicGetRequest").get("/breweries")
    .check(bodyString.saveAs("resp")))
      .exec {session =>
      val res = session("resp").as[String]
      println(s"<Response body> ********************************************* \n $res")
        println(s"</Response body> *********************************************")
      session}
  setUp(scn.inject(atOnceUsers(1 ))).protocols(httpconf)


}
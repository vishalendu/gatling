package com.yantriks.gatling.util.http

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicGetExample_MultiRequest extends Simulation {

//  val ctx: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
//  ctx.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))

  val httpconf = http.baseUrl("https://api.openbrewerydb.org")
  val scn = scenario("basicGetScenario").exec(http("basicGetRequest").get("/breweries")
    .check(jsonPath("$..postal_code").find.saveAs("searchpin")))
    .exec(session =>{
      println(s"<basicGetScenario: Session> ********************************************* \n Validate first Pin: ${session("searchpin").validate[String]}")
      println(s"</basicGetScenario: Session> *********************************************")
      session
      })
      .exec(http("getDetailsFromPin").get("/breweries?by_postal=${searchpin}").check(jsonPath("$..phone").find.saveAs("phone")))
      .exec(session =>{
        println(s"<getDetailsFromPin: Session> ********************************************* \n Validate Phone Number: ${session("phone").validate[Int]}")
        println(s"</getDetailsFromPin: Session> *********************************************")
        session
      })

  setUp(scn.inject(atOnceUsers(1 ))).protocols(httpconf)


}
package com.yantriks.gatling.util.http

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicGetExample_MultiRequest2 extends Simulation {

//  val ctx: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
//  ctx.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))

  var pin1 = ""
  var pin2 = ""

  val httpconf = http.baseUrl("https://api.openbrewerydb.org")
  val scn = scenario("basicGetScenario").exec(http("basicGetRequest").get("/breweries")
    .check(jsonPath("$..postal_code").findAll.saveAs("searchpin")))
    .exec(session =>{

      val pinvect = session("searchpin").as[Vector[String]]
      pin1 = pinvect(0)
      pin2 = pinvect(1)
      println(s"<basicGetScenario: Session> *********************************************")
      println(s"Validate Pin1,Pin2: ${pin1.asInstanceOf[String]},${pin2.asInstanceOf[String]}")
      println(s"</basicGetScenario: Session> *********************************************\n")
      session.set("pin1",pin1).set("pin2",pin2)
      })
      .exec(http("getDetailsFromPin1").get("/breweries?by_postal=${pin1}").check(jsonPath("$..phone").find.saveAs("phone")))
      .exec(session =>{
        println(s"<getDetailsFromPin1: Session> *********************************************")
        println(s"Validate For Pin: ${pin1} Phone Number: ${session("phone").validate[Int]}")
        println(s"</getDetailsFromPin1: Session> *********************************************\n")
        session
      })
    .exec(http("getDetailsFromPin2").get("/breweries?by_postal=${pin2}").check(jsonPath("$..phone").find.saveAs("phone")))
    .exec(session =>{
      println(s"<getDetailsFromPin2: Session> *********************************************")
      println(s"Validate For Pin: ${pin2} Phone Number: ${session("phone").validate[Int]}")
      println(s"</getDetailsFromPin2: Session> *********************************************\n")
      session
    })

  setUp(scn.inject(atOnceUsers(1 ))).protocols(httpconf)


}
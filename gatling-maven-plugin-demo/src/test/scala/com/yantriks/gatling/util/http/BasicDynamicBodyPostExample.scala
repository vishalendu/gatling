package com.yantriks.gatling.util.http

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicDynamicBodyPostExample extends Simulation {

//  val ctx: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
//  ctx.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))

  /*
  This class uses a feeder and updates the dynamic variable in the dynamicBody.json before executing the post
   */

  val messages = csv("data/message.csv").circular

  val body = """{"message":"This String will be returned by echo"}"""
  val httpconf = http.baseUrl("https://postman-echo.com").header("Content-Type","application/json")
  val scn = scenario("basicRawBodyPostScenario").feed(messages)
    .exec(http("basicPostRequest").post("/post").body(ElFileBody("data/dynamicBody.json")).asJson
      .check(bodyString.saveAs("ResponseBody")))
      .exec(session =>{
        println("<basicPostScenario: Session> *********************************************")
        println(session("ResponseBody").as[String])
        println("</basicPostScenario: Session> *********************************************")
        session
      })
  setUp(scn.inject(atOnceUsers(2 ))).protocols(httpconf)


}
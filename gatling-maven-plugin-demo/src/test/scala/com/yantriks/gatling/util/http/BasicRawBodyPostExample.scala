package com.yantriks.gatling.util.http

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicRawBodyPostExample extends Simulation {

//  val ctx: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
//  ctx.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))

  val httpconf = http.baseUrl("https://postman-echo.com").header("Content-Type","application/json")
  val scn = scenario("basicRawBodyPostScenario")
    .exec(http("basicPostRequest").post("/post").body(RawFileBody("data/PostReqBody.json")).asJson
      .check(bodyString.saveAs("ResponseBody")))
      .exec(session =>{
        println("<basicPostScenario: Session> *********************************************")
        println(session("ResponseBody").as[String])
        println("</basicPostScenario: Session> *********************************************")
        session
      })
  setUp(scn.inject(atOnceUsers(1 ))).protocols(httpconf)


}
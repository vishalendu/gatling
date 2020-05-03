package com.yantriks.gatling.util.http

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random

class BasicDynamicStringBodyPostExample extends Simulation {

//  val ctx: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
//  ctx.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))

  /*
  This class can create a string with feeder and local variables and use it for post request
  also added random string in the mix
   */

  val messages = csv("data/message.csv").circular
  val action = "hello"
  var dynamicString = ""
  var user = ""
  var msgbody = ""

  val httpconf = http.baseUrl("https://postman-echo.com").header("Content-Type","application/json")
  val scn = scenario("basicRawBodyPostScenario").feed(messages)
    .exec(session =>{
      dynamicString = s"""{"message":"${session("MessageBody").as[String]}", "action":"$action", "user":"user${Random.alphanumeric.take(4).mkString}"}"""
      session.set("dynamicString",dynamicString)
    })
    .exec(http("basicPostRequest").post("/post").body(StringBody("${dynamicString}")).asJson
      .check(bodyString.saveAs("ResponseBody")))
      .exec(session =>{
        println("<basicDyStringPostScenario: Session> *********************************************")
        println(session("ResponseBody").as[String])
        println("</basicDyStringPostScenario: Session> *********************************************")
        session
      })
  setUp(scn.inject(atOnceUsers(2 ))).protocols(httpconf)


}
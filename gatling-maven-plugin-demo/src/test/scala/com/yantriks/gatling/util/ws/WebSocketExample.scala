package com.yantriks.gatling.util.ws

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.{Level, LoggerContext}

class WebSocketExample extends Simulation {

  //  val ctx: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
  //  ctx.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))

  val httpproto = http.baseUrl("https://www.websocket.org").wsBaseUrl("wss://echo.websocket.org")

  val scn = scenario("websockerscn")
    .exec(http("FirstHttpReq").get("/echo.html"))
    .pause(2)
    .exec(ws("opensocket").connect("/?encoding=text")
      .onConnected(exec(
        ws("sendtext").sendText("Hello World")
      ).pause(2)
        .exec(
          ws("sendtext1").sendText("Goodbye World")
            .await(30)(ws.checkTextMessage("checkTextResp").check(regex(".*Goodbye.*").saveAs("myString")))
        )))
    .exec(session=>{
      println("***************************************")
      println(session("myString").as[String])
      println("***************************************")
      session
    })
    .exec(ws("closesocket").close)

  setUp(scn.inject(atOnceUsers(1))).protocols(httpproto)


}
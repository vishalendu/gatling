package com.yantriks.gatling.util.sse

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class SSEExample extends Simulation{

   val checkid = sse.checkMessage("SSECheckID").check(jsonPath("$.id").saveAs("SSEID"))
   val checkdata = sse.checkMessage("SSECheckData").check(jsonPath("$.data").saveAs("SSEDATA"))

    val httpConf = http.baseUrl("http://demo.howopensource.com")
      .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\nA")
      .inferHtmlResources()
      .acceptLanguageHeader("en-US,en;q=0.9")
      .acceptEncodingHeader("gzip, deflate")
      .userAgentHeader("Gatling")
      .upgradeInsecureRequestsHeader("1")
      .doNotTrackHeader("1")


  val scn = scenario("SSESenarioExample")
    .exec(sse("SSEConnect").connect("/sse/stocks.php")
      .await(30)(sse.checkMessage("SSECheck").check(regex(".*").saveAs("SSEResponse"))))
    .exec(session=>{
      println(s"SSEResponse: \n ${session("SSEResponse").as[String]}")
      session
    })
    .pause(5)
    .repeat(4) {
      exec(sse("SSECheck").setCheck.await(30)(checkdata, checkid))
        .exec(session => {
          println("***********************************************")
          println(s"SSEID From Check: ${session("SSEID").as[String]}")
          println(s"SSEData From Check: ${session("SSEDATA").as[String]}")
          println("***********************************************")
          session
        })
    }
    .exec(sse("SSEClose").close())


  setUp(scn.inject((atOnceUsers(1)))).protocols(httpConf)

}

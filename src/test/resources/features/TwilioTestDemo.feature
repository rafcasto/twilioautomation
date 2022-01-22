Feature: In order to automate twilio scenarios
  As a twilio tester
  I need an Automation framework

  Background: setup twilio client
    Given Twilio client is started

  Scenario: testing an inbound call
    Given customer makes a call
    |twiml|
    |  <Response><Dial><Number>+6468806272</Number></Dial><Play digits="4"></Play><Pause length="10"/></Response>   |
    When Twilio client picks up the call
    Then A new service log is created


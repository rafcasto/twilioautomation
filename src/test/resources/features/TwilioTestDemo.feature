Feature: In order to automate twilio scenarios
  As a twilio tester
  I need an Automation framework

  Background: setup twilio client
    Given Twilio client is started

  Scenario: testing an inbound call
    Given customer makes a call
      | twiml                                                                             |
      | <Response><Dial><Number sendDigits='wwwwww4'>$toNumber</Number></Dial></Response> |
    When Twilio client picks up the call
    And Wait for call to finish
    Then A new service log is created
      | say                                                                                                            |
      | Hello, how can we direct your call? Press 1 for sales, or say sales. To reach support, press 2 or say support. |
      | Sorry but that's not a valid response. Please try again.                                                       |
      | Hello, how can we direct your call? Press 1 for sales, or say sales. To reach support, press 2 or say support. |
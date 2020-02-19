Feature: Clients are politely greeted

  Scenario: Sunday isn't Friday
    Given Ahmad is the greeter today
    When "John Doe" enters the shop
    Then The client should be greeted with "Hello John Doe, welcome to our shop, my name is Ahmad."

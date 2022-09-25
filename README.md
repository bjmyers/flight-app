## Flight App

A project for SWENG 861, hits the Tequila Kiwi API to query about flight prices. JavaX backend for API calls with a Swing frontend

TODO:
Replace ISO-8601 String formatting in flight display
Let user enter airport using code rather than from combobox
Allow for multiple currency types
Improve look and feel
Unit Tests!
Refactor!


DONE:
Refactor error reporting logic
Store initial airports so API call not needed on startup
Figure out why second combobox isn't editable in the same way as the first
Call API if initial airports is unable to load on startup and warn user
Add Logging
Improve display for final flight
Input validation on date fields
Test out using all destinations
Let the user select any destination
## Flight App

A project for SWENG 861, hits the Tequila Kiwi API to query about flight prices. JavaX backend for API calls with a Swing frontend

TODO:
Defect: Entering invalid code doesn't seem to matter
Allow for multiple currency types
Let user select number of adults and number of children
Allow for layovers, find way of displaying multi-leg flight
Improve look and feel


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
Add Method documentation
Replace ISO-8601 String formatting in flight display
Have airport combobox include both name and code in selection "Los Angeles (LAX)"
Refactor locations loader into its own class
Let user enter airport using code rather than from combobox
## Flight App

A project for SWENG 861, hits the Tequila Kiwi API to query about flight prices. JavaX backend for API calls with a Swing frontend

TODO:

Improve display for final flight
Let user enter airport using code rather than from combobox
Test out using all destinations
	If using all destinations is decently performant, let the user select any destination
Allow for multiple currency types


DONE:
Refactor error reporting logic
Store initial airports so API call not needed on startup
Figure out why second combobox isn't editable in the same way as the first
Call API if initial airports is unable to load on startup and warn user
Add Logging
# Event Planner
Copyright 2016 Arnell Christoper D. Dalid  

Event Planner is a simple and comprehensive application that saves data into a  
database. This application is purely written in Java and uses [Apache Derby Database](https://db.apache.org/derby/)  
as a database backend.  

Event Planner is licensed under the **Apache License, Version 2.0**. For usage,  
distribution and reproduction of this software, please see [LICENSE](http://www.apache.org/licenses/LICENSE-2.0)  

![Event Planner](https://s5.postimg.org/45jrtkkjr/Event_Planner.jpg)

## Changelogs

### [\[0.1.0\]](https://github.com/rockyroadshub/event-planner/releases/tag/0.1.0)  
  
**New Features:**  

- New panel: Properties (can now change the color of the current day (foreground)  
and the color of the day with a registered event (background) and save it in the  
_planner.properties_)  
- Creates the properties file '_planner.properties_' if it doesn't exist  
- Can now delete events in a month and date (e.g. delete all events in August 2016)  
- Separated button icon source files outside jar file for easier customization  
- Separated [database libraries](https://github.com/rockyroadshub/planner-core) for accessing (preparation for plugin support)  
- Added border per panel (for easier identification)  

**Modifications**  

- Implemented Data Mapper Pattern to database system  
- Updated java docs from database libraries  
- Improved systematic loading of GUI panels   

**To do's:**  

- Plugin support  
- Smart notifications  
- Real time alarm (for registered schedules/events)  
- Improve GUI (Look and Feel support)  
- Improve loading of source files (e.g. icons,background images, etc..)  
- System tray (Windows)  
- Error Handling  
- Update Java docs  
- Holiday memory database  
- Contacts memory database  


### [\[0.0.0\]](https://github.com/rockyroadshub/event-planner/releases/tag/0.0.0)  
  
**To do's:**  

- Plugin support  
- Smart notifications  
- Alarm  
- Settings for easier customization  
- Improve GUI (Look and Feel support)  
- Systematic loading of source files  
- Overview panel  
- System tray (Windows)  
- Error handling  
- Java Docs  

[Please visit Rocky Roads Hub!](https://rockyroadshub.wordpress.com)
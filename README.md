# TwinColGrid Add-on for Vaadin

TwinColGrid Add-on is a UI component add-on for Vaadin Framework versions 7 and 8.

## Features

TwinColGrid is built upon the same idea of TwinColSelect component, but using grid instead of select. 
It provides a multiple selection component that shows two lists side by side, with the left column containing unselected items and the right column the selected items. The user can select items from the list on the left and click on the ">" button to move them to the list on the right. Items can be deselected by selecting them in the right list and clicking on the "<" button.

This add-on is a server-side-only component, so there's no need to recompile the widgetset.

## Online demo

Not available yet.
<Try the add-on demo at>

## Download release

Official releases of this add-on will be available at Vaadin Directory soon. 

## Building and running demo

git clone https://github.com/FlowingCode/TwinColGrid
mvn clean install
cd demo
mvn jetty:run

To see the demo, navigate to http://localhost:8080/

## Release notes

### Version 1.0.0-SNAPSHOT
- First Version

## Roadmap

This component is developed as a hobby with no public roadmap or any guarantees of upcoming releases. 

## Issue tracking

The issues for this add-on are tracked on its github.com page. All bug reports and feature requests are appreciated. 

## Contributions

Contributions are welcome, but there are no guarantees that they are accepted as such. Process for contributing is the following:
- Fork this project
- Create an issue to this project about the contribution (bug or feature) if there is no such issue about it already. Try to keep the scope minimal.
- Develop and test the fix or functionality carefully. Only include minimum amount of code needed to fix the issue.
- Refer to the fixed issue in commit
- Send a pull request for the original project
- Comment on the original issue that you have implemented a fix for it

## License & Author

Add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.

TwinColGrid Add-on is written by 
- Felipe Lang





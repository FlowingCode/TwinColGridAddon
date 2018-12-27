[![Build Status](https://jenkins.flowingcode.com/job/TwinColGrid-addon/badge/icon)](https://jenkins.flowingcode.com/job/TwinColGrid-addon)

# TwinColGrid Add-on for Vaadin

TwinColGrid Add-on is a UI component add-on for Vaadin Framework version 8.x

## Features

TwinColGrid is built upon the same idea of TwinColSelect component, but using grid instead of select. 
It provides a multiple selection component that shows two grids side by side. Left grid contains unselected items and the right grid the selected items.
The user can select items from the list on the left and click on the ">" button to move them to the list on the right. 
Items can be deselected by selecting them in the right list and clicking on the "<" button.
Component also supports drag and drop between grids.

This add-on is a server-side-only component, so there's no need to recompile the widgetset.

## Online demo

Try the add-on demo at http://addons.flowingcode.com/twincolgrid

## Download release

Official releases are available at Vaadin Directory https://vaadin.com/directory/component/twincolgrid-add-on 

## Building and running demo
```
git clone https://github.com/FlowingCode/TwinColGrid
mvn clean install
cd twincolgrid-demo
mvn jetty:run
```

To see the demo, navigate to http://localhost:8080/

## Release notes

- **Version 1.0.0** First release of the component. This version has dependencies that are not available in the public repositories.
- **Version 1.0.1** First stable release. Compatible with Vaadin 8.1/8.2.
- **Version 1.0.2** Compatible with Vaadin 8.3 and later. Support for Vaadin Designer. Read-only mode.

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





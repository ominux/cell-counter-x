# cell-counter-x
Extended version of the Cell Counter ImageJ plugin, available from http://rsbweb.nih.gov/ij/plugins/cell-counter.html.
The `cell-counter-x` ImageJ plugin maintains all features of the original plugin while adding new features:
* Which kind of marker is that number? Hovering markers shows the type in a tooltip-like overlay.
* Custom mapping: changing the mapping of a number to a meaningful literal in an editor let's you keep track of the semantics of each marker


![Cell Counter X v1.2](/doc/screenshots/ccx_screen.png)

##Build and Install
###Build using Maven
```bash
git clone https://github.com/pkainz/cell-counter-x
cd cell-counter-x
mvn clean package
```

Find the JAR file in `target/cell-counter-x-<version>.jar` and move it to the plugins folder, or subfolder, restart ImageJ, and there will be a new `Cell Counter X` command in the Plugins menu or submenu.


##Develop
Create an Eclipse project using maven-eclipse-plugin with the following commands:
```bash
mvn clean eclipse:clean eclipse:eclipse
```

Then import the existing project via the Eclipse "Import..." dialog.

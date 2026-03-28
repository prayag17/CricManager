## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## How to Run the Application

This project uses an SQLite database `cricmanager.db`. To run the application from the command line, you need to ensure the SQLite JDBC driver is available in your `lib` folder.

### 1. Download SQLite JDBC Driver
Download the `sqlite-jdbc` JAR file (e.g., `sqlite-jdbc-3.x.x.jar`) from [Maven Central](https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc) and place it inside the `lib/` directory.

### 2. Compile the Project
Open your terminal (PowerShell or Command Prompt) and run the following command from the root of the project to compile the Java files into the `bin/` directory:

```powershell
javac -d bin src/*.java
```

### 3. Run the Application
After compiling, you need to include both the `bin` directory and the `lib` directory in your classpath. Run the following command from the root of the project:

```powershell
# On Windows (using semicolon as a classpath separator)
java --enable-native-access=ALL-UNNAMED -cp "bin;lib/*" App

# On macOS/Linux (using colon as a classpath separator)
java --enable-native-access=ALL-UNNAMED -cp "bin:lib/*" App 
```

If everything is set up correctly, you should see the database initialize and the "Application ready." message.

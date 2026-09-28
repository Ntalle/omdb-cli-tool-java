# OMDb CLI Tool

A Java command-line application that uses the OMDb API to retrieve and display movie information.

## Setup

Set the `OMDB_API_KEY` environment variable to your OMDb API key. In PowerShell:

```powershell
$env:OMDB_API_KEY = "your_api_key"
```

Keep the key private; do not commit it to the repository.

## Build and Run

From the project directory, run:

```powershell
mvn test
mvn exec:java "-Dexec.mainClass=Main"
```

The application will prompt you to enter a movie title. Enter `Q` to quit.
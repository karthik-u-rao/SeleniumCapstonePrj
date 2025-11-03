# Selenium Capstone Project

Automated test suite covering three areas:

1. **OrangeHRM UI flows** – Selenium + TestNG regression around login, leave management, and recruitment.
2. **ReqRes API flows** – Rest Assured tests backed by an in-memory mock that reproduces the public API responses without external calls.
3. **BMI Calculator BDD** – Cucumber scenarios that exercise a lightweight BMI calculator model for both metric and US units.

The repo only contains source, configuration, and helper scripts needed to run these suites locally.

## Tech stack

- Java 11
- Maven
- Selenium WebDriver 4.15.0 with WebDriverManager
- TestNG 7.8.0
- Cucumber JVM 7.14.0
- Rest Assured 5.3.2

## Layout

```
src/test/
├── java/com/
│   ├── orangehrm/         # Page objects + TestNG suite
│   ├── reqres/            # Mock-backed Rest Assured tests
│   ├── bmicalculator/     # Cucumber steps + TestNG runner
│   └── utils/             # Shared Selenium base tests
└── resources/features/    # BMI feature file

pom.xml                    # Maven build definition
testng.xml                 # Aggregated TestNG suite
run-tests.bat              # Optional Windows helper to run mvn test
```

`target/` and other build artefacts are ignored.

## Getting started

1. Install Java 11 (or higher) and Maven.
2. Install the Chrome browser (latest stable). WebDriverManager will download a matching driver automatically.
3. Clone the repo and install dependencies:

```powershell
git clone <your-fork-or-origin>
cd SeleniumCapstonPrj
mvn -q dependency:go-offline
```

## Running tests

Run the full suite (UI + API + Cucumber):

```cmd
mvn clean test
```

Run individual suites (Option B – recommended):

```cmd
:: ReqRes API (fast)
mvn test -Dtest=ReqResAPITests

:: BMI Cucumber (fast)
mvn test -Dtest=BMICalculatorRunner

:: OrangeHRM UI (headed by default)
mvn test -Dtest=OrangeHRMTests
```

Optional: Run by TestNG suite XML (use quotes if needed in PowerShell)

```cmd
mvn test -Dsurefire.suiteXmlFiles=testng-reqres.xml
mvn test -Dsurefire.suiteXmlFiles=testng-bmi.xml
mvn test -Dsurefire.suiteXmlFiles=testng-orangehrm.xml
```

PowerShell users: quote the -D argument, e.g.

```powershell
mvn test "-Dsurefire.suiteXmlFiles=testng-reqres.xml"
```

If you see "Unknown lifecycle phase '.suiteXmlFiles=…'", your shell parsed -D incorrectly. Prefer -Dtest=... or quote as shown above.

Reports are under `target/surefire-reports/` and `target/cucumber-reports/`.

### Headless mode (opt-in)

UI tests are headed by default. Enable headless explicitly:

```cmd
mvn test -Dtest=OrangeHRMTests -Dheadless=true
```

## Notes on OrangeHRM tests

- The tests now force Chrome to use English (en-US) via a dedicated base class `EnglishBaseTest`, ensuring the OrangeHRM demo always loads in English.
- Smart waiting: interactions use `SmartWait` for overlay-aware clicks (handles `.oxd-form-loader` / `.oxd-loading-spinner`) without globally increasing waits.
- If you still see rare click interceptions, re-run or slightly increase the per-action timeouts in `SmartWait` methods only.

## What the tests cover

### OrangeHRM UI

- Login with demo credentials (`Admin` / `admin123`).
- Apply Leave workflow and View Leave List.
- Recruitment module: add candidate and view candidates.

### ReqRes API

- CRUD-style endpoints with assertions on status and payload.
- All responses are provided by an in-memory filter (`ReqResMockFilter`) so tests work offline deterministically.

### BMI Calculator (Cucumber)

- Metric and US unit BMI calculations with scenario outlines and validations.

## Running in Eclipse (TestNG)

1) Install TestNG plugin
- Help > Eclipse Marketplace…
- Search for "TestNG" and install "TestNG for Eclipse"

2) Import the Maven project
- File > Import… > Maven > Existing Maven Projects
- Root Directory: your project folder
- Finish, then Right‑click project > Maven > Update Project…

3) Ensure Java 11+ is configured
- Window > Preferences > Java > Installed JREs: select a JDK 11+
- Project > Properties > Java Compiler: set compliance to 11 (or higher)

4) Run suites
- Right‑click `testng-reqres.xml` > Run As > TestNG Suite
- Right‑click `testng-bmi.xml` > Run As > TestNG Suite
- Right‑click `testng-orangehrm.xml` > Run As > TestNG Suite (headed by default)

5) Run classes
- Right‑click `src/test/java/com/reqres/ReqResAPITests.java` > Run As > TestNG Test
- Right‑click `src/test/java/com/bmicalculator/BMICalculatorRunner.java` > Run As > TestNG Test
- Right‑click `src/test/java/com/orangehrm/OrangeHRMTests.java` > Run As > TestNG Test

6) Optional headless
- In Run Configurations… > TestNG > (your config) > Arguments > VM arguments: `-Dheadless=true`

7) View reports
- TestNG Results view and `target/surefire-reports/index.html`
- Cucumber HTML: `target/cucumber-reports/cucumber.html`

## Troubleshooting

- Chrome DevTools warnings about CDP versions are benign unless you use DevTools APIs.
- If WebDriverManager has a stale driver after Chrome updates, clear `%USERPROFILE%\.cache\selenium`.
- If your Maven `~/.m2/settings.xml` contains `<repositories>` at top level, move it under a `<profile>` to avoid warnings.

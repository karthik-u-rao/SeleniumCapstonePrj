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
│   └── utils/             # Shared Selenium base test
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
git clone https://github.com/karthikurao/SeleniumCapstonePrj.git
cd SeleniumCapstonePrj
mvn -q dependency:go-offline
```

## Running tests

Run the full suite (UI + API + Cucumber):

```powershell
mvn clean test
```

Run individual suites (Option B – recommended):

Use class-based selection to avoid shell quoting issues.

```cmd
:: ReqRes API (fast)
mvn test -Dtest=ReqResAPITests

:: BMI Cucumber (fast)
mvn test -Dtest=BMICalculatorRunner

:: OrangeHRM UI (headed)
mvn test -Dtest=OrangeHRMTests
```

Optional: Run by TestNG suite XML (ensure proper quoting in PowerShell)

```cmd
:: ReqRes API only
mvn test -Dsurefire.suiteXmlFiles=testng-reqres.xml

:: BMI only
mvn test -Dsurefire.suiteXmlFiles=testng-bmi.xml

:: OrangeHRM UI only (headed)
mvn test -Dsurefire.suiteXmlFiles=testng-orangehrm.xml
```

PowerShell users: quote the -D argument, e.g.

```powershell
mvn test "-Dsurefire.suiteXmlFiles=testng-reqres.xml"
```

Note: If you see "Unknown lifecycle phase '.suiteXmlFiles=…'", your shell parsed the -D incorrectly. Prefer Option B (-Dtest=...) or quote as shown above.

Reports are generated under `target/surefire-reports/` and `target/cucumber-reports/` after a run.

## Run specific suites (fast paths)

You can also use the interactive helper script:

```cmd
run-tests.bat
```

- 2) ReqRes API only
- 3) BMI only
- 4) OrangeHRM UI only (headed)
- 1) All (headed)

Headless mode is only offered for the fast suites via the menu. You can always force headless explicitly with `-Dheadless=true` if desired.

### Headless mode

All UI tests support headless execution. Enable it with either approach:

```cmd
mvn -Dheadless=true -Dsurefire.suiteXmlFiles=testng-orangehrm.xml test
```

Or set an environment variable (Windows PowerShell example):

```powershell
$env:HEADLESS='true'
mvn -Dsurefire.suiteXmlFiles=testng-orangehrm.xml test
```

## Troubleshooting OrangeHRM runs

- Element click intercepted / loader overlays: The OrangeHRM pages display a transient loader (`.oxd-form-loader`). Tests now wait for the loader to disappear around key clicks, but on slow networks you may still see intermittent “element click intercepted”. Re-run; if persistent, increase waits a bit.
- Chrome DevTools Protocol (CDP) warnings: You may see messages like “Unable to find CDP implementation matching 141”. These are benign unless you use devtools APIs. If you do, add a matching devtools artifact, e.g.:
  - `org.seleniumhq.selenium:selenium-devtools-v141:4.15.0`
- Headless stability: On some environments headless is more reliable. Use `-Dheadless=true` as shown above.
- Driver mismatch: WebDriverManager resolves drivers automatically. If Chrome just updated, clear the cache at `%USERPROFILE%\.cache\selenium` or re-run to refresh.

## What the tests cover

### OrangeHRM UI

- **Login** with default demo credentials (`Admin` / `admin123`).
- **Apply Leave** workflow with date selection and comment entry.
- **View Leave List** filtering to confirm newly applied leave visibility.
- **Recruitment** module coverage for adding and listing candidates.

### ReqRes API

- Exercises the CRUD login and registration endpoints exposed by ReqRes.
- Assertions cover status codes and key payload fields.
- Responses are supplied by `ReqResMockFilter` so the suite works offline and avoids third-party rate limits.

### BMI Calculator (Cucumber)

- Metric and US unit calculations with validation of BMI value and category.
- Scenario outline with multiple data sets.
- “Clear” workflow to ensure state resets between calculations.

## Notes & tips

- `BaseTest` spins up Chrome in headed mode. If you need headless execution, add `options.addArguments("--headless=new")` in that class.
- WebDriverManager caches downloaded drivers in the user profile; clear `%USERPROFILE%/.cache/selenium` if you need a fresh binary.
- The API mock intentionally delays the `/users?delay=3` response (~2.6s) to keep the timing assertion meaningful.

## Running in Eclipse (TestNG)

Follow these steps to run the tests from Eclipse with a visible browser for OrangeHRM:

1) Install TestNG plugin
- Help > Eclipse Marketplace…
- Search for "TestNG" and install "TestNG for Eclipse"
- Restart Eclipse if prompted

2) Import the Maven project
- File > Import… > Maven > Existing Maven Projects
- Root Directory: `C:\Users\P12C4F0\IdeaProjects\SeleniumCapstonPrj`
- Finish
- Right‑click the project > Maven > Update Project… (select the project) > OK

3) Ensure Java 11+ is configured
- Window > Preferences > Java > Installed JREs: add/select a JDK 11+
- Project-specific: Right‑click project > Properties > Java Compiler: set compliance to 11 (or higher)

4) Run as a TestNG Suite (grouped runs)
- ReqRes only: right‑click `testng-reqres.xml` > Run As > TestNG Suite
- BMI only: right‑click `testng-bmi.xml` > Run As > TestNG Suite
- OrangeHRM headed: right‑click `testng-orangehrm.xml` > Run As > TestNG Suite (launches Chrome visibly)

5) Run individual TestNG classes (fast)
- ReqRes API: right‑click `src/test/java/com/reqres/ReqResAPITests.java` > Run As > TestNG Test
- BMI (Cucumber via TestNG): right‑click `src/test/java/com/bmicalculator/BMICalculatorRunner.java` > Run As > TestNG Test
- OrangeHRM (headed): right‑click `src/test/java/com/orangehrm/OrangeHRMTests.java` > Run As > TestNG Test

6) Optional: Create persistent Run Configurations
- Run > Run Configurations… > TestNG
- Suite-based: set Project = SeleniumCapstonPrj, Suite = (e.g.) `testng-orangehrm.xml`
- Class-based: set Project and Test class (e.g., `com.orangehrm.OrangeHRMTests`)
- VM arguments (optional headless for non-UI runs): `-Dheadless=true`
  - Note: Only the `-Dheadless` JVM property controls headless; UI is visible by default.

7) View reports
- TestNG results view: Window > Show View > Other… > TestNG > Results
- HTML reports:
  - TestNG: `target/surefire-reports/index.html`
  - Cucumber (BMI): `target/cucumber-reports/cucumber.html`

Troubleshooting (Eclipse)
- WebDriver versions: WebDriverManager fetches drivers automatically. If Chrome updated, re-run or clear `%USERPROFILE%\.cache\selenium`.
- CDP warnings for Chrome: benign unless you use DevTools APIs.
- OrangeHRM loader overlay: tests include waits for `.oxd-form-loader`. If a rare click interception occurs, re-run; if persistent, increase waits slightly.
- Maven settings warning: If you see "Unrecognised tag: 'repositories'" in `~/.m2/settings.xml`, remove the top-level `<repositories>` or move it under a `<profile>`.

Feature: BMI Calculator
  As a user
  I want to calculate my BMI
  So that I can know my body mass index

  Scenario: Calculate BMI using metric system
    Given I navigate to the BMI calculator website
    When I select metric units
    And I enter age as "25"
    And I select gender as "male"
    And I enter height as "180" cm
    And I enter weight as "75" kg
    And I click calculate button
    Then I should see BMI result displayed
    And BMI category should be shown

  Scenario: Calculate BMI using US units
    Given I navigate to the BMI calculator website
    When I select US units
    And I enter age as "30"
    And I select gender as "female"
    And I enter height feet as "5" and inches as "6"
    And I enter weight as "140" pounds
    And I click calculate button
    Then I should see BMI result displayed
    And BMI category should be shown

  Scenario: Clear BMI calculation
    Given I navigate to the BMI calculator website
    When I select metric units
    And I enter age as "25"
    And I select gender as "male"
    And I enter height as "180" cm
    And I enter weight as "75" kg
    And I click calculate button
    And I click clear button
    Then All fields should be cleared

  Scenario Outline: Calculate BMI for different body types
    Given I navigate to the BMI calculator website
    When I select metric units
    And I enter age as "<age>"
    And I select gender as "<gender>"
    And I enter height as "<height>" cm
    And I enter weight as "<weight>" kg
    And I click calculate button
    Then I should see BMI result displayed

    Examples:
      | age | gender | height | weight |
      | 20  | male   | 170    | 60     |
      | 35  | female | 165    | 55     |
      | 40  | male   | 175    | 85     |
      | 28  | female | 160    | 70     |

  Scenario Outline: Metric BMI category classification
    Given I navigate to the BMI calculator website
    When I select metric units
    And I enter age as "<age>"
    And I select gender as "<gender>"
    And I enter height as "<height>" cm
    And I enter weight as "<weight>" kg
    And I click calculate button
    Then BMI category should be "<category>"

    Examples:
      | age | gender | height | weight | category        |
      | 22  | female | 180    | 52     | Underweight     |
      | 31  | male   | 172    | 50     | Underweight     |
      | 29  | female | 168    | 64     | Normal weight   |
      | 37  | male   | 175    | 72     | Normal weight   |
    | 45  | male   | 165    | 78     | Overweight      |
      | 34  | female | 158    | 78     | Obesity         |

  Scenario Outline: US BMI category classification
    Given I navigate to the BMI calculator website
    When I select US units
    And I enter age as "<age>"
    And I select gender as "<gender>"
    And I enter height feet as "<feet>" and inches as "<inches>"
    And I enter weight as "<weight>" pounds
    And I click calculate button
    Then BMI category should be "<category>"

    Examples:
      | age | gender | feet | inches | weight | category        |
      | 32  | male   | 5    | 10     | 120    | Underweight     |
      | 27  | female | 5    | 6      | 140    | Normal weight   |
      | 45  | male   | 5    | 9      | 185    | Overweight      |
      | 50  | female | 5    | 4      | 190    | Obesity         |
    | 38  | male   | 5    | 11     | 125    | Underweight     |
      | 28  | female | 6    | 0      | 180    | Normal weight   |

  Scenario Outline: Metric BMI result precision
    Given I navigate to the BMI calculator website
    When I select metric units
    And I enter age as "<age>"
    And I select gender as "<gender>"
    And I enter height as "<height>" cm
    And I enter weight as "<weight>" kg
    And I click calculate button
    Then BMI result should be approximately <result>

    Examples:
      | age | gender | height | weight | result |
      | 26  | female | 170    | 65     | 22.5   |
      | 33  | male   | 182    | 74     | 22.3   |
      | 41  | male   | 155    | 50     | 20.8   |
      | 38  | female | 168    | 82     | 29.1   |
      | 47  | male   | 190    | 120    | 33.2   |

  Scenario Outline: US BMI result precision
    Given I navigate to the BMI calculator website
    When I select US units
    And I enter age as "<age>"
    And I select gender as "<gender>"
    And I enter height feet as "<feet>" and inches as "<inches>"
    And I enter weight as "<weight>" pounds
    And I click calculate button
    Then BMI result should be approximately <result>

    Examples:
      | age | gender | feet | inches | weight | result |
      | 36  | female | 5    | 3      | 110    | 19.5   |
      | 52  | male   | 6    | 1      | 210    | 27.7   |
      | 41  | female | 5    | 7      | 165    | 25.8   |
      | 47  | male   | 5    | 9      | 240    | 35.4   |
      | 28  | female | 6    | 0      | 135    | 18.3   |

  Scenario Outline: BMI boundary validation for metric units
    Given I navigate to the BMI calculator website
    When I select metric units
    And I enter age as "<age>"
    And I select gender as "<gender>"
    And I enter height as "<height>" cm
    And I enter weight as "<weight>" kg
    And I click calculate button
    Then BMI result should be between <lowerBound> and <upperBound>
    And BMI category should be "<category>"

    Examples:
      | age | gender | height | weight | lowerBound | upperBound | category      |
      | 30  | female | 168    | 52     | 18.3       | 18.5       | Underweight   |
      | 42  | male   | 170    | 72     | 24.8       | 25.0       | Normal weight |
      | 37  | female | 162    | 66     | 25.1       | 25.3       | Overweight    |
      | 45  | male   | 165    | 90     | 33.0       | 33.2       | Obesity       |
      | 33  | male   | 175    | 77     | 25.0       | 25.2       | Overweight    |

  Scenario: Consecutive BMI calculations update result
    Given I navigate to the BMI calculator website
    When I select metric units
    And I enter age as "32"
    And I select gender as "female"
    And I enter height as "170" cm
    And I enter weight as "60" kg
    And I click calculate button
    Then BMI result should be approximately 20.8
    And BMI category should be "Normal weight"
    When I enter height as "165" cm
    And I enter weight as "80" kg
    And I click calculate button
    Then BMI result should be approximately 29.4
    And BMI category should be "Overweight"


Feature: searching for cars

  @Smoke
  Scenario Outline: Finding new Cars "<carBrand>"
    Given user navigates to carlwale website
    Given user mouseHovers on newcars
    Given user clicks on findnewcars link
    Given user clicks on "<carBrand>"
    And User validates car title as "<carTitle>"
    Examples:
      | carBrand       | carTitle            |
      |Toyota          |Toyota Cars          |
      |Kia             |Kia Cars             |
      |Honda           |Honda Cars           |
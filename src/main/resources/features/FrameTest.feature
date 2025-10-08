Feature: FrameTesting


  @frame_test
  Scenario: Finding new Cars "<carBrand>"
    Given I navigate to url with frames
    And I validate an elementText in Frame
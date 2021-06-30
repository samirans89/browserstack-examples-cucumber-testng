Feature: Login Feature

  @login @prod @smoke
  Scenario Outline: Login to store app with incorrect creds
    Given I download app from Google Play Store
    And I start the app
    And I enter app credentials <username> <password>
    And I press Log In Button
    Then I should see login error <message>
    Examples:
      | username    | password    | message                                                                                                      |
      | 'fav_user'  | 'testing99' | 'Please upgrade your app to the newest version. This version of the Wells Fargo App is no longer supported.' |
      | 'fav_user2' | 'testing99' | 'Please upgrade your app to the newest version. This version of the Wells Fargo App is no longer supported.' |

  @login @qa @regression
  Scenario Outline: Login to app with incorrect creds
    Given I start the app
    And I enter app credentials <username> <password>
    And I press Log In Button
    Then I should see login error <message>
    Examples:
      | username    | password    | message                                                                                                      |
      | 'fav_user'  | 'testing99' | 'Please upgrade your app to the newest version. This version of the Wells Fargo App is no longer supported.' |
      | 'fav_user2' | 'testing99' | 'Please upgrade your app to the newest version. This version of the Wells Fargo App is no longer supported.' |

  @login @qa @regression
  Scenario Outline: Login to app with incorrect creds temp duplicate
    Given I start the app
    And I enter app credentials <username> <password>
    And I press Log In Button
    Then I should see login error <message>
    Examples:
      | username    | password    | message                                                                                                      |
      | 'fav_user'  | 'testing99' | 'Please upgrade your app to the newest version. This version of the Wells Fargo App is no longer supported.' |